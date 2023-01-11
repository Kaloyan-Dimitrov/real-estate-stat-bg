package com.devcraft.realestatestatbg.domain;

import jakarta.persistence.*;
import lombok.*;
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "average_region_prices")
public class AverageRegionPrice {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    private Double price;
    private String currency;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "region_id")
    @ToString.Exclude
    private Region region;

    @Enumerated(EnumType.STRING)
    private RealEstateType type;
}
