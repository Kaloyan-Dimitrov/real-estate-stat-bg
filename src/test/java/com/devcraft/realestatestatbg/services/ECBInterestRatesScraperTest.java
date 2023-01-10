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
    private static final String URL = "https://www.ecb.europa.eu/stats/policy_and_exchange_rates/key_ecb_interest_rates/html/index.en.html";
    public static final Double DEPOSIT_FACILITY = -0.5;
    public static final Double LENDING_FACILITY = 0.75;
    private final String DATE_FORMAT = "yyyy dd MMM";
    ECBInterestRatesScraper ecbInterestRatesScraper = new ECBInterestRatesScraper(URL, DATE_FORMAT);

    @Mock
    private Connection connection;

    Elements testCols;
    @BeforeEach
    void setUp() {
        testCols = new Elements();
        testCols.add(new Element("td").text("2020"));
        testCols.add(new Element("td").text("01 Jan"));
        testCols.add(new Element("td").text(String.valueOf(DEPOSIT_FACILITY)));
        testCols.add(new Element("td"));
        testCols.add(new Element("td"));
        testCols.add(new Element("td").text(String.valueOf(LENDING_FACILITY)));
    }

    @Test
    void scrape() {
        Document doc = new Document(URL);
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

            Set<KeyInterestRate> keyInterestRates = ecbInterestRatesScraper.scrape().get("keyECBInterestRates");

            assertEquals(1, keyInterestRates.size());

            KeyInterestRate keyInterestRate = keyInterestRates.iterator().next();
            assertEquals("2020", YEAR_FORMAT.format(keyInterestRate.getDate()));
            assertEquals("01 Jan", DAY_FORMAT.format(keyInterestRate.getDate()));
            assertEquals(DEPOSIT_FACILITY, keyInterestRate.getDepositFacility());
            assertEquals(LENDING_FACILITY, keyInterestRate.getMarginalLendingFacility());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void parseTableCols() {
        KeyInterestRate result = ecbInterestRatesScraper.parseTableCols(testCols);

        assertEquals("2020", YEAR_FORMAT.format(result.getDate()));
        assertEquals("01 Jan", DAY_FORMAT.format(result.getDate()));
        assertEquals(DEPOSIT_FACILITY, result.getDepositFacility());
        assertEquals(LENDING_FACILITY, result.getMarginalLendingFacility());
    }

    @Test
    void parseDateFromString() {
        String yearString = "2022";
        String dateString = "21 Dec.";

        Date result = ecbInterestRatesScraper.parseDateFromString(yearString, dateString);
        assertEquals("2022 21 Dec", new SimpleDateFormat(DATE_FORMAT).format(result));
    }
}