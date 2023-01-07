package com.devcraft.realestatestatbg.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
public class KeyInterestRate {
    Date date;
    Double depositFacility;
    Double marginalLendingFacility;
}
