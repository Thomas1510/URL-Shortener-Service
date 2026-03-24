package com.codec.urlshortener.service;

import com.codec.urlshortener.model.Url;
import com.codec.urlshortener.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    public Url shortenUrl(String originalUrl) {
        String shortCode;
        do {
            shortCode = UUID.randomUUID().toString().substring(0, 6);
        } while (urlRepository.findByShortCode(shortCode).isPresent());

        Url url = new Url();
        url.setOriginalUrl(originalUrl);
        url.setShortCode(shortCode);

        return urlRepository.save(url);
    }


    public Url getOriginalUrl(String shortCode) {
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() -> new RuntimeException("Short URL not found: " + shortCode));

        url.setClickCount(url.getClickCount() + 1);
        return urlRepository.save(url);
    }

    public List<Url> getAllUrls() {
        return urlRepository.findAll();
    }
}