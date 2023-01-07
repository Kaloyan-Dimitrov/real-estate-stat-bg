package com.devcraft.realestatestatbg.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class RealEstateAveragePrice {
    Double price;
    String currency;
    String region;
    String desc;
}
