package com.devcraft.realestatestatbg.repositories;

import com.devcraft.realestatestatbg.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
}