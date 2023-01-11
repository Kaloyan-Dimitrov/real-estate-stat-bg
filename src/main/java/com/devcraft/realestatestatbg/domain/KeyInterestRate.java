package com.devcraft.realestatestatbg.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@AllArgsConstructor
@Entity
@Table(name = "key_interest_rates")
public class KeyInterestRate {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    Date date;
    Double depositFacility;
    Double marginalLendingFacility;

    public KeyInterestRate(Date date, String depositFacilityString, String marginalLendingFacilityString) {
        this.date = date;
        this.depositFacility = Double.valueOf(depositFacilityString);
        this.marginalLendingFacility = Double.valueOf(marginalLendingFacilityString);
    }
}
