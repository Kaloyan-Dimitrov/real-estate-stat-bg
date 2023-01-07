package com.devcraft.realestatestatbg.services;

import com.devcraft.realestatestatbg.domain.RealEstateAveragePrice;
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
public class ImotBgScraper implements ScraperService<RealEstateAveragePrice> {
    public static final int TWO_ROOM_PRICE_COL = 5;
    public static final int THREE_ROOM_PRICE_COL = TWO_ROOM_PRICE_COL + 3;
    @Value("${websites.imotbg.stats}")
    private String url;

    @Override
    public Map<String, Set<RealEstateAveragePrice>> getAllStats() {
        Map<String, Set<RealEstateAveragePrice>> resultMap = new HashMap<>();
        String twoRoomDesc = "Two Room Apartments Prices by Region In Sofia";
        String threeRoomDesc = "Three Room Prices Apartments by Region In Sofia";
        resultMap.put("twoRoomByRegion", getTwoRoomPricesByRegion(twoRoomDesc));
        resultMap.put("threeRoomByRegion", getThreeRoomPricesByRegion(threeRoomDesc));
        return resultMap;
    }

    public Set<RealEstateAveragePrice> getTwoRoomPricesByRegion(String desc) {
        return scrapeImotBg(url, TWO_ROOM_PRICE_COL, desc);
    }

    public Set<RealEstateAveragePrice> getThreeRoomPricesByRegion(String desc) {
        return scrapeImotBg(url, THREE_ROOM_PRICE_COL, desc);
    }

    private Set<RealEstateAveragePrice> scrapeImotBg(String url, int priceCol, String desc) {
        System.out.println(url);
        Set<RealEstateAveragePrice> realEstateAveragePriceSet = new HashSet<>();
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
                    RealEstateAveragePrice realEstateAveragePrice = new RealEstateAveragePrice(pricePerSqM, "EUR", region, desc);
                    realEstateAveragePriceSet.add(realEstateAveragePrice);
//                    System.out.println(realEstateAveragePricePerSqM);
                }
            }
            return realEstateAveragePriceSet;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
