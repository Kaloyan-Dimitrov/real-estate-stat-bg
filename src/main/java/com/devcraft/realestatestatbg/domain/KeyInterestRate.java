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

    public KeyInterestRate(Date date, String depositFacilityString, String marginalLendingFacilityString) {
        this.date = date;
        this.depositFacility = Double.valueOf(depositFacilityString);
        this.marginalLendingFacility = Double.valueOf(marginalLendingFacilityString);
    }
}
