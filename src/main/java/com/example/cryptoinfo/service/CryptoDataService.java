package com.example.cryptoinfo.service;

import com.example.cryptoinfo.data.entity.CryptoData;
import com.example.cryptoinfo.data.repository.CryptoDataRepository;
import com.example.cryptoinfo.dto.CryptoDataDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@Service
public class CryptoDataService {

    private static final Logger logger = LoggerFactory.getLogger(CryptoDataService.class);
    private static final String API_URL = "https://api.coincap.io/v2/assets";

    private final CryptoDataRepository cryptoDataRepository;
    private final ModelMapper modelMapper;

    @Value("${coincap.api.key}")
    private String apiKey;

    public CryptoDataService(CryptoDataRepository cryptoDataRepository, ModelMapper modelMapper) {
        this.cryptoDataRepository = cryptoDataRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public List<CryptoDataDTO> fetchAndSaveCryptoData(){
        logger.info("Fetching crypto data from API...");
        cryptoDataRepository.deleteAll();
        List<CryptoDataDTO> cryptoDataDTOList = new ArrayList<>();
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(API_URL);

        httpGet.addHeader("Authorization", "Bearer " + apiKey);
    try{
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity entity = response.getEntity();

        if(entity != null){
            InputStream inputStream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(stringBuilder.toString());
            JsonNode dataNode = rootNode.get("data");

            if (dataNode != null && dataNode.isArray()) {
                for (JsonNode asset : dataNode) {
                    CryptoDataDTO cryptoDataDTO = new CryptoDataDTO();
                    cryptoDataDTO.setCurrency(asset.get("id").asText());
                    cryptoDataDTO.setPrice(asset.get("priceUsd").asDouble());
                    cryptoDataDTO.setMarketCap(asset.get("marketCapUsd").asDouble());
                    cryptoDataDTO.setVolume(asset.get("volumeUsd24Hr").asDouble());
                    cryptoDataDTO.setTimestamp(LocalDateTime.now());

                    cryptoDataDTOList.add(cryptoDataDTO);
                }
            } else {
                System.out.println("No data found in the response.");
                logger.warn("No data found in the response.");
            }

            List<CryptoData> cryptoDataList = mapToEntities(cryptoDataDTOList);
            cryptoDataRepository.saveAll(cryptoDataList);
            logger.info("Crypto data saved to database.");
        }
        } catch (IOException e){
        e.printStackTrace();
        logger.error("Error fetching crypto data", e);
    }
        return cryptoDataDTOList;
    }

    private List<CryptoData> mapToEntities(List<CryptoDataDTO> cryptoDataDTOList) {
        List<CryptoData> cryptoDataList = new ArrayList<>();
        for (CryptoDataDTO dto : cryptoDataDTOList) {
            CryptoData entity = modelMapper.map(dto, CryptoData.class);
            cryptoDataList.add(entity);
        }
        return cryptoDataList;
    }
    /*private List<CryptoData> mapToEntities(List<CryptoDataDTO> cryptoDataDTOList) {
        List<CryptoData> cryptoDataList = new ArrayList<>();
        for (CryptoDataDTO dto : cryptoDataDTOList) {
            CryptoData entity = new CryptoData();
            entity.setCurrency(dto.getCurrency());
            entity.setPrice(dto.getPrice());
            entity.setMarketCap(dto.getMarketCap());
            entity.setVolume(dto.getVolume());
            entity.setTimestamp(dto.getTimestamp());
            cryptoDataList.add(entity);
        }
        return cryptoDataList;
    }*/
}
