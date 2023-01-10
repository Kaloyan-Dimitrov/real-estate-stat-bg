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
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImotBgScraper implements ScraperService<RealEstateAveragePrice> {
    public static final int TWO_BEDROOM_PRICE_COL = 5;
    public static final int THREE_BEDROOM_PRICE_COL = TWO_BEDROOM_PRICE_COL + 3;
    public static final int REGION_COL = 0;
    public static final String TWO_BEDROOM_DESC = "Two Bedroom Apartments Prices by Region In Sofia";
    public static final String THREE_BEDROOM_DESC = "Three Bedroom Apartments Prices by Region In Sofia";
    private String url;

    public ImotBgScraper(@Value("${websites.imotbg.stats}") String url) {
        this.url = url;
    }

    @Override
    public Map<String, Set<RealEstateAveragePrice>> scrape() {
        System.out.println(url);
        try {
            Document doc = Jsoup.connect(url).get();
            Element table = doc.select("table[id=tableStats]").first();
            if(table == null) return null;
            Elements rows = table.getElementsByTag("tr");
            System.out.println(rows.size());
            List<Element> valueRows = rows.subList(2, rows.size() - 2);

            Set<RealEstateAveragePrice> twoBedroomSet = getPricesSet(valueRows, TWO_BEDROOM_PRICE_COL, TWO_BEDROOM_DESC);
            Set<RealEstateAveragePrice> threeBedroomSet = getPricesSet(valueRows, THREE_BEDROOM_PRICE_COL, THREE_BEDROOM_DESC);
            return new HashMap<>() {{
                put("twoBedroomByRegion", twoBedroomSet);
                put("threeBedroomByRegion", threeBedroomSet);
            }};
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<RealEstateAveragePrice> getPricesSet(List<Element> valueRows, int priceCol, String desc) {
        return valueRows.stream()
                .map(row -> row.getElementsByTag("td"))
                .map(cols -> parseTableCols(cols, priceCol, desc))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public RealEstateAveragePrice parseTableCols(Elements cols, int priceCol, String desc) {
        String region = cols.get(REGION_COL).text();
        String pricePerSqMString = cols.get(priceCol).text();

        if(pricePerSqMString.equals("-")) return null;

        pricePerSqMString = pricePerSqMString.replaceAll("\\s+", "");
        double pricePerSqM = Double.parseDouble(pricePerSqMString);
        return new RealEstateAveragePrice(pricePerSqM, "EUR", region, desc);
    }
}
