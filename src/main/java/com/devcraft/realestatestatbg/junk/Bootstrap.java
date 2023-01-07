package com.devcraft.realestatestatbg.junk;

import com.devcraft.realestatestatbg.domain.RealEstateAveragePrice;
import com.devcraft.realestatestatbg.services.ImotBgScraper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    ImotBgScraper imotBgScraper;

    public Bootstrap(ImotBgScraper imotBgScraper) {
        this.imotBgScraper = imotBgScraper;
    }

    @Override
    public void run(String... args) {
        System.out.println("Bootstrap is running");
        Map<String, Set<RealEstateAveragePrice>> realEstateResultMap = imotBgScraper.getAllStats();
        realEstateResultMap.forEach((key, values) -> {
            System.out.println(key + " average: ");
            Double average = values.stream().map(RealEstateAveragePrice::getPrice).reduce(0.0, Double::sum) / values.size();
            // print the double with two trailing digits
            System.out.printf("%.2f %s %n", average, values.stream().findFirst().get().getCurrency());
        });
    }

}
