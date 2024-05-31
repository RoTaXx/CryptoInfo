package com.example.cryptoinfo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CryptoDataServiceScheduler {

    private static final Logger logger = LoggerFactory.getLogger(CryptoDataServiceScheduler.class);
    private final CryptoDataService cryptoDataService;

    @Value("${coincap.api.key}")
    private String apiKey;

    public CryptoDataServiceScheduler(CryptoDataService cryptoDataService) {
        this.cryptoDataService = cryptoDataService;
    }

    @Scheduled(fixedRate = 10000)
    public void fetchAndSaveCryptoDataPeriodically() {
        logger.info("Scheduled task: Fetching and saving crypto data...");
        cryptoDataService.fetchAndSaveCryptoData();
    }
}
