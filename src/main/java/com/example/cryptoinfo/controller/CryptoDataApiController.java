package com.example.cryptoinfo.controller;

import com.example.cryptoinfo.dto.CryptoDataDTO;
import com.example.cryptoinfo.service.CryptoDataService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/crypto")
public class CryptoDataApiController {
    private final CryptoDataService cryptoDataService;

    @Value("${coincap.api.key}")
    private String apiKey;

    public CryptoDataApiController(CryptoDataService cryptoDataService) {
        this.cryptoDataService = cryptoDataService;
    }

    @GetMapping
    public List<CryptoDataDTO> updateCryptoData() {
        return cryptoDataService.fetchAndSaveCryptoData();
    }
}
