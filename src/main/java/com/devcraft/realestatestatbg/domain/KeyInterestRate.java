package com.devcraft.realestatestatbg.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class KeyInterestRate {
    Date date;
    Double depositFacility;
    Double marginalLendingFacility;
}
