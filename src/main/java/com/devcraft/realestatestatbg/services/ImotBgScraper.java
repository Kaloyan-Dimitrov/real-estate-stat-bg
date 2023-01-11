package com.devcraft.realestatestatbg.services;

import com.devcraft.realestatestatbg.domain.AverageRegionPrice;
import com.devcraft.realestatestatbg.domain.RealEstateType;
import com.devcraft.realestatestatbg.domain.Region;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImotBgScraper implements ScraperService<AverageRegionPrice> {
    public static final int TWO_BEDROOM_PRICE_COL = 5;
    public static final int THREE_BEDROOM_PRICE_COL = TWO_BEDROOM_PRICE_COL + 3;
    public static final int REGION_COL = 0;
    private final String url;

    public ImotBgScraper(@Value("${websites.imotbg.stats}") String url) {
        this.url = url;
    }

    @Override
    public Set<AverageRegionPrice> scrape() {
        System.out.println(url);
        try {
            Document doc = Jsoup.connect(url).get();
            Element table = doc.select("table[id=tableStats]").first();
            if(table == null) return null;
            Elements rows = table.getElementsByTag("tr");
            System.out.println(rows.size());
            List<Element> valueRows = rows.subList(2, rows.size() - 2);

            Set<AverageRegionPrice> twoBedroomSet = getPricesSet(valueRows, TWO_BEDROOM_PRICE_COL, RealEstateType.TWO_BEDROOM_APARTMENT);
            Set<AverageRegionPrice> threeBedroomSet = getPricesSet(valueRows, THREE_BEDROOM_PRICE_COL, RealEstateType.THREE_BEDROOM_APARTMENT);
            return new HashSet<>() {{
                addAll(twoBedroomSet);
                addAll(threeBedroomSet);
            }};
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<AverageRegionPrice> getPricesSet(List<Element> valueRows, int priceCol, RealEstateType type) {
        return valueRows.stream()
                .map(row -> row.getElementsByTag("td"))
                .map(cols -> parseTableCols(cols, priceCol, type))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public AverageRegionPrice parseTableCols(Elements cols, int priceCol, RealEstateType type) {
        String region = cols.get(REGION_COL).text();
        String pricePerSqMString = cols.get(priceCol).text();

        if(pricePerSqMString.equals("-")) return null;

        pricePerSqMString = pricePerSqMString.replaceAll("\\s+", "");
        double pricePerSqM = Double.parseDouble(pricePerSqMString);
        return AverageRegionPrice.builder()
                .region(Region.builder().name(region).build())
                .price(pricePerSqM)
                .currency("EUR")
                .type(type)
                .build();
    }
}
