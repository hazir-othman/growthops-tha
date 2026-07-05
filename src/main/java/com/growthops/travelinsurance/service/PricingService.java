package com.growthops.travelinsurance.service;

import com.growthops.travelinsurance.entity.Rate;
import com.growthops.travelinsurance.repository.RateRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Service;

@Service
public class PricingService {

  private final RateRepository rateRepository;

  public PricingService(RateRepository rateRepository) {
    this.rateRepository = rateRepository;
  }

  public BigDecimal calculatePremium(
      String coverageType, String area, String planType, LocalDate startDate, LocalDate endDate) {

    if ("ANNUAL".equalsIgnoreCase(coverageType) && "AREA 4".equalsIgnoreCase(area)) {
      throw new IllegalArgumentException("Area 4 is not available for Annual plans");
    }

    Rate rateConfig =
        rateRepository
            .findRate(coverageType, area, planType)
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        String.format(
                            "No rate configuration found for Coverage: %s, Area: %s, Plan: %s",
                            coverageType, area, planType)));

    if ("SINGLE".equalsIgnoreCase(coverageType)) {
      if (startDate == null || endDate == null) {
        throw new IllegalArgumentException("Start date and End date are required for Single Trip");
      }

      long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
      if (days <= 0) {
        throw new IllegalArgumentException("End date must be on or after Start date");
      }

      return rateConfig.getRate().multiply(BigDecimal.valueOf(days));

    } else if ("ANNUAL".equalsIgnoreCase(coverageType)) {
      return rateConfig.getRate();

    } else {
      throw new IllegalArgumentException("Unknown coverage type: " + coverageType);
    }
  }
}
