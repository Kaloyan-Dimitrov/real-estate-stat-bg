package com.devcraft.realestatestatbg.services;

import com.devcraft.realestatestatbg.domain.RealEstateAveragePrice;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImotBgScraperTest {
    public static final Double TWO_BED = 78280.0;
    public static final Double THREE_BED = 126345.0;
    public static final String REGION = "Банишора";
    String URL = "https://www.imot.bg/pcgi/imot.cgi?act=14";

    ImotBgScraper imotBgScraper = new ImotBgScraper(URL);

    @Mock
    Connection connection;

    Elements testCols;
    Elements testRows;
    @BeforeEach
    void setUp() {
        testCols = new Elements();
        testCols.add(new Element("td").text(REGION));
        for(int i = 0; i < 4; i ++) testCols.add(new Element("td"));
        testCols.add(new Element("td").text(String.valueOf(TWO_BED)));
        for(int i = 0; i < 2; i ++) testCols.add(new Element("td"));
        testCols.add(new Element("td").text(String.valueOf(THREE_BED)));
        for(int i = 0; i < 3; i ++) testCols.add(new Element("td"));

        testRows = new Elements();
        for(int i = 0; i < 2; i ++) testRows.add(new Element("tr"));
        testRows.add(new Element("tr").appendChildren(testCols));
        for(int i = 0; i < 2; i ++) testRows.add(new Element("tr"));

    }

    @Test
    void scrape() {
        Document doc = new Document(URL);
        Element table = new Element("table");
        table.id("tableStats");
        table.appendChildren(testRows);
        doc.appendChild(table);

        try(MockedStatic<Jsoup> jsoup = mockStatic(Jsoup.class)) {
            jsoup.when(() -> Jsoup.connect(anyString())).thenReturn(connection);
            when(connection.get()).thenReturn(doc);

            Set<RealEstateAveragePrice> threeBedroomResultSet = imotBgScraper.scrape().get("threeBedroomByRegion");
            Set<RealEstateAveragePrice> twoBedroomResultSet = imotBgScraper.scrape().get("twoBedroomByRegion");

            assertEquals(1, twoBedroomResultSet.size());
            assertEquals(1, threeBedroomResultSet.size());

            RealEstateAveragePrice twoBedroomResult = twoBedroomResultSet.stream().findFirst().orElseThrow();
            RealEstateAveragePrice threeBedroomResult = threeBedroomResultSet.stream().findFirst().orElseThrow();

            assertEquals(REGION, twoBedroomResult.getRegion());
            assertEquals(REGION, threeBedroomResult.getRegion());
            assertEquals(TWO_BED, twoBedroomResult.getPrice());
            assertEquals(THREE_BED, threeBedroomResult.getPrice());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getPricesSet() {
        Set<RealEstateAveragePrice> pricesSet = imotBgScraper.getPricesSet(testRows.eq(2),
                                                                        ImotBgScraper.THREE_BEDROOM_PRICE_COL,
                                                                        ImotBgScraper.THREE_BEDROOM_DESC);
        assertEquals(1, pricesSet.size());
        RealEstateAveragePrice price = pricesSet.iterator().next();
        assertEquals(REGION, price.getRegion());
        assertEquals(THREE_BED, price.getPrice());
        assertEquals(ImotBgScraper.THREE_BEDROOM_DESC, price.getDesc());
        assertEquals(REGION, price.getRegion());
    }

    @Test
    void parseTableCols() {
        RealEstateAveragePrice realEstateAveragePrice = imotBgScraper.parseTableCols(testCols,
                                                                                    ImotBgScraper.TWO_BEDROOM_PRICE_COL,
                                                                                    ImotBgScraper.TWO_BEDROOM_DESC);

        assertEquals(TWO_BED, realEstateAveragePrice.getPrice());
        assertEquals(ImotBgScraper.TWO_BEDROOM_DESC, realEstateAveragePrice.getDesc());
        assertEquals(REGION, realEstateAveragePrice.getRegion());
    }
}