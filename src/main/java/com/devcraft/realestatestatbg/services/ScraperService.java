package com.devcraft.realestatestatbg.services;

import java.util.Set;

public interface ScraperService<T> {

    Set<T> scrape();
}
