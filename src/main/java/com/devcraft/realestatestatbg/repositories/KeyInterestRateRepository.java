package com.devcraft.realestatestatbg.repositories;

import com.devcraft.realestatestatbg.domain.KeyInterestRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyInterestRateRepository extends JpaRepository<KeyInterestRate, Long> {
}