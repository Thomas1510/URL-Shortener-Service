package com.codec.urlshortener.controller;

import com.codec.urlshortener.model.Url;
import com.codec.urlshortener.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UrlController {

    @Autowired
    private UrlService urlService;

    @PostMapping("/api/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody Map<String, String> body) {
        String originalUrl = body.get("url");
        if (originalUrl == null || originalUrl.isBlank()) {
            return ResponseEntity.badRequest().body("URL is required");
        }
        if (!originalUrl.startsWith("http://") && !originalUrl.startsWith("https://")) {
            return ResponseEntity.badRequest().body("URL must start with http:// or https://");
        }
        Url saved = urlService.shortenUrl(originalUrl);
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable("shortCode") String shortCode) {
        try {
            Url url = urlService.getOriginalUrl(shortCode);
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", url.getOriginalUrl());
            return new ResponseEntity<>(headers, HttpStatus.FOUND);
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/api/analytics")
    public ResponseEntity<List<Url>> analytics() {
        return ResponseEntity.ok(urlService.getAllUrls());
    }
}