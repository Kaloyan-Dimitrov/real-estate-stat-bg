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

@Service
public class ECBInterestRatesScraper implements ScraperService<KeyInterestRate> {
    public static final int DEPOSIT_FACILITY_COL = 2;
    public static final int MARGINAL_LENDING_FACILITY_COL = DEPOSIT_FACILITY_COL + 3;
    public static final int YEAR_COL = 0;
    public static final int DATE_COL = 1;

    @Value("${websites.ecb.key_interest_rates}")
    private String url;
    @Override
    public Map<String, Set<KeyInterestRate>> getAllStats() {
        Map<String, Set<KeyInterestRate>> resultMap = new HashMap<>();
        resultMap.put("keyECBInterestRates", scrapeECBInterestRates(url));
        return resultMap;
    }

    private Set<KeyInterestRate> scrapeECBInterestRates(String url) {
        System.out.println(url);
        Set<KeyInterestRate> realEstateAveragePricePerSqMSet = new HashSet<>();
        try {
            Document doc = Jsoup.connect(url).get();
            Element table = doc.select("table").first();
            if(table == null) return null;
            Element tableBody = table.select("tbody").first();
            if(tableBody == null) return null;
            Elements rows = tableBody.getElementsByTag("tr");
            String lastYear = null;
            for (Element row : rows) {
                Elements cols = row.getElementsByTag("td");
//                System.out.println(cols.size());
//                System.out.println(cols);
                String yearString = cols.get(YEAR_COL).text();
                String dateString = cols.get(DATE_COL).text();
                String depositFacilityString = cols.get(DEPOSIT_FACILITY_COL).text();
                String marginalLendingFacilityString = cols.get(MARGINAL_LENDING_FACILITY_COL).text();
                if(yearString.isEmpty()) {
                    yearString = lastYear;
                } else {
                    lastYear = yearString;
                }
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy dd MMM");
                String dateToParse = yearString + " " + dateString;
                if(dateToParse.contains(".")) {
                dateToParse = dateToParse.substring(0, dateToParse.indexOf('.'));
                }
                Date date = formatter.parse(dateToParse);
                KeyInterestRate keyInterestRate = new KeyInterestRate();
                keyInterestRate.setDate(date);
                depositFacilityString = depositFacilityString.replaceAll("\u2212", "-");
                marginalLendingFacilityString = marginalLendingFacilityString.replaceAll("\u2212", "-");
                keyInterestRate.setDepositFacility(Double.valueOf(depositFacilityString));
                keyInterestRate.setMarginalLendingFacility(Double.valueOf(marginalLendingFacilityString));
                realEstateAveragePricePerSqMSet.add(keyInterestRate);
                System.out.println(keyInterestRate);
            }
            return realEstateAveragePricePerSqMSet;
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
