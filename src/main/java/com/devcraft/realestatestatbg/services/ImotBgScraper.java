package com.devcraft.realestatestatbg.services;

import com.devcraft.realestatestatbg.domain.RealEstateAveragePricePerSqM;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Slf4j
public class ImotBgScraper implements ScraperService<RealEstateAveragePricePerSqM> {
    public static final int TWO_ROOM_PRICE_COL = 6;
    public static final int THREE_ROOM_PRICE_COL = TWO_ROOM_PRICE_COL + 3;
    @Value("#{websites.urls.imotbg")
    private String url;

    @Override
    public Map<String, Set<RealEstateAveragePricePerSqM>> getAllStats() {
        Map<String, Set<RealEstateAveragePricePerSqM>> resultMap = new HashMap<>();
        resultMap.put("Two Room Prices by Region In Sofia", getTwoRoomPricesByRegion());
        resultMap.put("Three Room Prices by Region In Sofia", getThreeRoomPricesByRegion());
        return resultMap;
    }

    public Set<RealEstateAveragePricePerSqM> getTwoRoomPricesByRegion() {
        return scrapeImotBg(url, TWO_ROOM_PRICE_COL);
    }

    public Set<RealEstateAveragePricePerSqM> getThreeRoomPricesByRegion() {
        return scrapeImotBg(url, THREE_ROOM_PRICE_COL);
    }

    private Set<RealEstateAveragePricePerSqM> scrapeImotBg(String url, int priceCol) {
        System.out.println(url);
        Set<RealEstateAveragePricePerSqM> realEstateAveragePricePerSqMSet = new HashSet<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Element table = doc.select("table[id=tableStats]").first();
            if(table == null) return null;
            Elements rows = table.getElementsByTag("tr");
            List<Element> valueRows = rows.subList(2, rows.size() - 2);
            for (Element row : valueRows) {
                Elements cols = row.getElementsByTag("td");
                String region = cols.get(0).text();
                String pricePerSqMString = cols.get(priceCol).text();
                if(!pricePerSqMString.equals("-")) {
                    pricePerSqMString = pricePerSqMString.replaceAll("\\s+", "");
                    double pricePerSqM = Double.parseDouble(pricePerSqMString);
                    RealEstateAveragePricePerSqM realEstateAveragePricePerSqM = new RealEstateAveragePricePerSqM(pricePerSqM, "EUR", region, "Two Room Apartment");
                    realEstateAveragePricePerSqMSet.add(realEstateAveragePricePerSqM);
                    System.out.println(realEstateAveragePricePerSqM);
                }
            }
            return realEstateAveragePricePerSqMSet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
