package com.devcraft.realestatestatbg.services;

import com.devcraft.realestatestatbg.domain.AverageRegionPrice;
import com.devcraft.realestatestatbg.repositories.AverageRegionPricesRepository;
import com.devcraft.realestatestatbg.repositories.RegionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AverageRegionPriceService {
    AverageRegionPricesRepository averageRegionPricesRepository;
    RegionRepository regionRepository;

    public AverageRegionPriceService(AverageRegionPricesRepository averageRegionPricesRepository,
                                     RegionRepository regionRepository) {
        this.averageRegionPricesRepository = averageRegionPricesRepository;
        this.regionRepository = regionRepository;
    }

    public void saveAll(Set<AverageRegionPrice> averageRegionPriceFlux) {
        averageRegionPricesRepository.saveAll(averageRegionPriceFlux);
    }

    @Scheduled(cron = "40 11 * * 1 ?")
    public void print() {
        System.out.println("Hello");
    }
}
