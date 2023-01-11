package com.devcraft.realestatestatbg.bootstrap;

import com.devcraft.realestatestatbg.domain.AverageRegionPrice;
import com.devcraft.realestatestatbg.domain.KeyInterestRate;
import com.devcraft.realestatestatbg.services.AverageRegionPriceService;
import com.devcraft.realestatestatbg.services.ECBInterestRatesScraper;
import com.devcraft.realestatestatbg.services.ImotBgScraper;
import com.devcraft.realestatestatbg.services.KeyInterestRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    ImotBgScraper imotBgScraper;
    ECBInterestRatesScraper ecbInterestRatesScraper;
    AverageRegionPriceService averageRegionPriceService;
    KeyInterestRateService keyInterestRateService;

    public Bootstrap(ImotBgScraper imotBgScraper,
                     ECBInterestRatesScraper ecbInterestRatesScraper,
                     AverageRegionPriceService averageRegionPriceService,
                     KeyInterestRateService keyInterestRateService) {
        this.imotBgScraper = imotBgScraper;
        this.ecbInterestRatesScraper = ecbInterestRatesScraper;
        this.averageRegionPriceService = averageRegionPriceService;
        this.keyInterestRateService = keyInterestRateService;
    }

    @Override
    public void run(String... args) {
        System.out.println("Bootstrap is running");
        Set<AverageRegionPrice> realEstateResultSet = imotBgScraper.scrape();
        averageRegionPriceService.saveAll(realEstateResultSet);
//        realEstateResultMap.forEach((key, values) -> {
//            System.out.println(key + " average: ");
//            Double average = values.stream().map(AverageRegionPrice::getPrice).reduce(0.0, Double::sum) / values.size();
//            // print the double with two trailing digits
//            System.out.printf("%.2f %s %n", average, values.stream().findFirst().get().getCurrency());
//        });
        Set<KeyInterestRate> ecbInterestRatesResultSet = ecbInterestRatesScraper.scrape();
        keyInterestRateService.saveAll(ecbInterestRatesResultSet);
    }

}
