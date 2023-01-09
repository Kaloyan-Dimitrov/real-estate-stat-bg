package com.devcraft.realestatestatbg.services;

import com.devcraft.realestatestatbg.domain.KeyInterestRate;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class ECBInterestRatesScraperTest {
    public static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy");
    public static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("dd MMM");
    private final String url = "https://www.ecb.europa.eu/stats/policy_and_exchange_rates/key_ecb_interest_rates/html/index.en.html";
    private final String DATE_FORMAT = "yyyy dd MMM";
    ECBInterestRatesScraper ecbInterestRatesScraper = new ECBInterestRatesScraper(url, DATE_FORMAT);

    @Mock
    private Connection connection;

    Elements testCols;
    @BeforeEach
    void setUp() {
        testCols = new Elements();
        testCols.add(new Element("td").text("2020"));
        testCols.add(new Element("td").text("01 Jan"));
        testCols.add(new Element("td").text("-0.50"));
        testCols.add(new Element("td"));
        testCols.add(new Element("td"));
        testCols.add(new Element("td").text("0.75"));
    }

    @Test
    void scrapeECBInterestRates() throws IOException {
        Document doc = new Document(url);
        Element table = new Element("table");
        Element tableBody = new Element("tbody");
        Elements rows = new Elements();
        rows.add(new Element("tr").appendChildren(testCols));
        tableBody.appendChildren(rows);
        table.appendChild(tableBody);
        doc.appendChild(table);

        try(MockedStatic<Jsoup> jsoup = mockStatic(Jsoup.class)) {
            given(connection.get()).willReturn(doc);
            jsoup.when(() -> Jsoup.connect(anyString())).thenReturn(connection);

            Set<KeyInterestRate> keyInterestRates = ecbInterestRatesScraper.scrapeECBInterestRates(url);

            assertEquals(1, keyInterestRates.size());

            KeyInterestRate keyInterestRate = keyInterestRates.iterator().next();
            assertEquals("2020", YEAR_FORMAT.format(keyInterestRate.getDate()));
            assertEquals("01 Jan", DAY_FORMAT.format(keyInterestRate.getDate()));
            assertEquals(-0.5, keyInterestRate.getDepositFacility());
            assertEquals(0.75, keyInterestRate.getMarginalLendingFacility());
        }
    }

    @Test
    void parseTableCols() {
        KeyInterestRate result = ecbInterestRatesScraper.parseTableCols(testCols);

        assertEquals("2020", YEAR_FORMAT.format(result.getDate()));
        assertEquals("01 Jan", DAY_FORMAT.format(result.getDate()));
        assertEquals(-0.50, result.getDepositFacility());
        assertEquals(0.75, result.getMarginalLendingFacility());
    }

    @Test
    void parseDateFromString() {
        String yearString = "2020";
        String dateString = "01 Jan";

        Date result = ecbInterestRatesScraper.parseDateFromString(yearString, dateString);

        assertEquals("2020 01 Jan", new SimpleDateFormat(DATE_FORMAT).format(result));
    }
}