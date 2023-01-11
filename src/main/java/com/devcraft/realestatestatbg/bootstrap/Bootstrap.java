package com.devcraft.realestatestatbg.bootstrap;

import com.devcraft.realestatestatbg.domain.AverageRegionPrice;
import com.devcraft.realestatestatbg.repositories.AverageRegionPricesRepository;
import com.devcraft.realestatestatbg.repositories.RegionRepository;
import com.devcraft.realestatestatbg.services.AverageRegionPriceService;
import com.devcraft.realestatestatbg.services.ECBInterestRatesScraper;
import com.devcraft.realestatestatbg.services.ImotBgScraper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    ImotBgScraper imotBgScraper;
    ECBInterestRatesScraper ecbInterestRatesScraper;
    AverageRegionPricesRepository averageRegionPricesRepository;
    RegionRepository regionRepository;
    AverageRegionPriceService averageRegionPriceService;

    public Bootstrap(ImotBgScraper imotBgScraper,
                     ECBInterestRatesScraper ecbInterestRatesScraper,
                     AverageRegionPricesRepository averageRegionPricesRepository,
                     RegionRepository regionRepository,
                     AverageRegionPriceService averageRegionPriceService) {
        this.imotBgScraper = imotBgScraper;
        this.ecbInterestRatesScraper = ecbInterestRatesScraper;
        this.averageRegionPricesRepository = averageRegionPricesRepository;
        this.regionRepository = regionRepository;
        this.averageRegionPriceService = averageRegionPriceService;
    }

    @Override
    public void run(String... args) {
        System.out.println("Bootstrap is running");
        Set<AverageRegionPrice> realEstateResultSet = imotBgScraper.scrape();
//        Region sofia = Region.builder().name("Sofia").build();
//        Region varna = Region.builder().name("Varna").build();
//        averageRegionPriceService.saveAverageRegionPrice(1000, "EUR", sofia, RealEstateType.TWO_BEDROOM_APARTMENT);
//        regionRepository.saveAll(Flux.just(sofia, varna)).subscribe();
//        sofia.setId(regionRepository.findByName("Sofia").block().getId());
//
//
//        averageRegionPricesRepository.save(
//                AverageRegionPrice.builder()
//                        .region(sofia)
//                        .type(RealEstateType.TWO_BEDROOM_APARTMENT)
//                        .price(1000d)
//                        .build())
//                .subscribe();
        averageRegionPricesRepository.saveAll(realEstateResultSet);
//        realEstateResultMap.forEach((key, values) -> {
//            System.out.println(key + " average: ");
//            Double average = values.stream().map(AverageRegionPrice::getPrice).reduce(0.0, Double::sum) / values.size();
//            // print the double with two trailing digits
//            System.out.printf("%.2f %s %n", average, values.stream().findFirst().get().getCurrency());
//        });
        ecbInterestRatesScraper.scrape();
    }

}
