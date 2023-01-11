package com.devcraft.realestatestatbg.services;

import com.devcraft.realestatestatbg.domain.KeyInterestRate;
import com.devcraft.realestatestatbg.repositories.KeyInterestRateRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class KeyInterestRateService {
    KeyInterestRateRepository keyInterestRateRepository;

    public KeyInterestRateService(KeyInterestRateRepository keyInterestRateRepository) {
        this.keyInterestRateRepository = keyInterestRateRepository;
    }

    public void saveAll(Set<KeyInterestRate> averageRegionPriceFlux) {
        keyInterestRateRepository.saveAll(averageRegionPriceFlux);
    }
}
