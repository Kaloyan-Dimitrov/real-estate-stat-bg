package com.devcraft.realestatestatbg.junk;

import com.devcraft.realestatestatbg.services.ImotBgScraper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Bootstrap implements CommandLineRunner {

    ImotBgScraper imotBgScraper;

    public Bootstrap(ImotBgScraper imotBgScraper) {
        this.imotBgScraper = imotBgScraper;
    }

    @Override
    public void run(String... args) {
        log.debug("Bootstrap is running");
        imotBgScraper.getAllStats();
    }

}
