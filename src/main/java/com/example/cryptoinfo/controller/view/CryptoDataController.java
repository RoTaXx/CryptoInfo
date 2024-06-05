package com.example.cryptoinfo.controller.view;

import com.example.cryptoinfo.dto.CryptoDataDTO;
import com.example.cryptoinfo.service.CryptoDataService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;


@Controller
public class CryptoDataController {

    private final CryptoDataService cryptoDataService;
    private final ModelMapper modelMapper;

    public CryptoDataController(CryptoDataService cryptoDataService, ModelMapper modelMapper) {
        this.cryptoDataService = cryptoDataService;
        this.modelMapper = modelMapper;
    }

    @Value("${coincap.api.key}")
    private String apiKey;

    @GetMapping("/")
    public String index() {
        return "redirect:/cryptoChart";
    }

    @GetMapping("/cryptoChart")
    public String getCryptoChart(Model model) {
        model.addAttribute("view", "chart");
        return "index";
    }

    @GetMapping("/cryptoData")
    public String getCryptoData(Model model) {
        List<CryptoDataDTO> cryptoDataDTOList = cryptoDataService.fetchAndSaveCryptoData();
        List<CryptoDataViewModel> cryptoDataViewModelList = cryptoDataDTOList.stream()
                .map(dto -> modelMapper.map(dto, CryptoDataViewModel.class))
                .collect(Collectors.toList());

        model.addAttribute("cryptoData", cryptoDataViewModelList);
        return "cryptoData";
    }

    @GetMapping("/cryptoData/sortByPrice")
    public String getCryptoDataSortedByPrice(Model model) {
        List<CryptoDataDTO> cryptoDataDTOList = cryptoDataService.fetchAndSaveCryptoData();
        List<CryptoDataDTO> sortedCryptoDataDTOList = cryptoDataService.sortByPrice(cryptoDataDTOList);
        List<CryptoDataViewModel> cryptoDataViewModelList = sortedCryptoDataDTOList.stream()
                .map(dto -> modelMapper.map(dto, CryptoDataViewModel.class))
                .collect(Collectors.toList());

        model.addAttribute("cryptoData", cryptoDataViewModelList);
        return "cryptoData";
    }

}
