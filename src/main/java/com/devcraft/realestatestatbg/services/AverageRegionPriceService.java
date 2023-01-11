package com.devcraft.realestatestatbg.services;

import com.devcraft.realestatestatbg.repositories.AverageRegionPricesRepository;
import com.devcraft.realestatestatbg.repositories.RegionRepository;
import org.springframework.stereotype.Service;

@Service
public class AverageRegionPriceService {
    AverageRegionPricesRepository averageRegionPricesRepository;
    RegionRepository regionRepository;

    public AverageRegionPriceService(AverageRegionPricesRepository averageRegionPricesRepository,
                                     RegionRepository regionRepository) {
        this.averageRegionPricesRepository = averageRegionPricesRepository;
        this.regionRepository = regionRepository;
    }

//    public void saveAll(Flux<AverageRegionPrice> averageRegionPriceFlux) {
//        averageRegionPricesRepository.saveAll(averageRegionPriceFlux).subscribe();
//    }
//
//    public void saveAverageRegionPrice(double price, String currency, Region region, RealEstateType type) {
//        // save the region if it doesn't exist with the same name
//        if(regionRepository.findByName(region.getName()).block() == null) {
//            regionRepository.save(region).subscribe();
//        }
//        AverageRegionPrice avgRegionPrice = AverageRegionPrice.builder()
//                                            .price(price)
//                                            .currency(currency)
//                                            .region(region)
//                                            .type(type)
//                                            .build();
//        averageRegionPricesRepository.save(avgRegionPrice).subscribe();
//    }
}
