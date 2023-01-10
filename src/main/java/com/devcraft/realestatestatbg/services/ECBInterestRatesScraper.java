package com.devcraft.realestatestatbg.services;

import com.devcraft.realestatestatbg.domain.KeyInterestRate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ECBInterestRatesScraper implements ScraperService<KeyInterestRate> {
    public static final int DEPOSIT_FACILITY_COL = 2;
    public static final int MARGINAL_LENDING_FACILITY_COL = DEPOSIT_FACILITY_COL + 3;
    public static final int YEAR_COL = 0;
    public static final int DATE_COL = 1;
    public final String DATE_FORMAT;

    private String lastParsedYear = null;

    private final String url;

    public ECBInterestRatesScraper(@Value("${websites.ecb.key_interest_rates}") String url,
                                   @Value("${websites.ecb.date_format}") String DATE_FORMAT) {
        this.url = url;
        this.DATE_FORMAT = DATE_FORMAT;
    }

    @Override
    public Map<String, Set<KeyInterestRate>> scrape() {
        System.out.println(url);
        try {
            Document doc = Jsoup.connect(url).get();
            Element table = doc.select("table").first();
            if(table == null) return null;
            Element tableBody = table.select("tbody").first();
            if(tableBody == null) return null;
            Elements rows = tableBody.getElementsByTag("tr");

            Set<KeyInterestRate> resultSet = rows.stream()
                        .map(row -> row.getElementsByTag("td"))
                        .map(this::parseTableCols)
                        .collect(Collectors.toSet());

            return new HashMap<>() {{
                put("keyECBInterestRates", resultSet);
            }};
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public KeyInterestRate parseTableCols(Elements cols) {
        String yearString = cols.get(YEAR_COL).text();
        String dateString = cols.get(DATE_COL).text();
        String depositFacilityString = cols.get(DEPOSIT_FACILITY_COL).text();
        String marginalLendingFacilityString = cols.get(MARGINAL_LENDING_FACILITY_COL).text();

        Date date = parseDateFromString(yearString, dateString);

        depositFacilityString = depositFacilityString.replaceAll("\u2212", "-");
        marginalLendingFacilityString = marginalLendingFacilityString.replaceAll("\u2212", "-");

        return new KeyInterestRate(date, depositFacilityString, marginalLendingFacilityString);
    }

    public Date parseDateFromString(String yearString, String dateString) {
        if(yearString.isEmpty()) yearString = this.lastParsedYear;
        else this.lastParsedYear = yearString;

        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
        String dateToParse = yearString + " " + dateString;
        if(dateToParse.contains("."))
            dateToParse = dateToParse.substring(0, dateToParse.indexOf('.'));

        Date date;
        try {
            date = formatter.parse(dateToParse);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return date;
    }
}
