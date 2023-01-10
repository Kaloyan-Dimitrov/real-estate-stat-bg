package com.devcraft.realestatestatbg.services;

import java.util.Map;
import java.util.Set;

public interface ScraperService<T> {

    Map<String, Set<T>> scrape();
}
