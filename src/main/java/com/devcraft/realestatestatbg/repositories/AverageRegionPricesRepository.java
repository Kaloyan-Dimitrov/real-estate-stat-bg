package com.devcraft.realestatestatbg.repositories;

import com.devcraft.realestatestatbg.domain.AverageRegionPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AverageRegionPricesRepository extends JpaRepository<AverageRegionPrice, String> {
//    Flux<AverageRegionPrice> findByRegion_NameIgnoreCase(String name);
}
