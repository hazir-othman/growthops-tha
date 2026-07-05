package com.growthops.travelinsurance.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "insurance_rates")
public class Rate {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "coverage_type", nullable = false)
  private String coverageType;

  @Column(name = "area", nullable = false)
  private String area;

  @Column(name = "plan_type", nullable = false)
  private String planType;

  @Column(name = "rate", nullable = false, precision = 10, scale = 2)
  private BigDecimal rate;

  @Column(name = "is_flat_rate", nullable = false)
  private boolean isFlatRate;

  public Rate() {}

  public Long getId() {
    return id;
  }

  public String getCoverageType() {
    return coverageType;
  }

  public String getArea() {
    return area;
  }

  public String getPlanType() {
    return planType;
  }

  public BigDecimal getRate() {
    return rate;
  }

  public boolean isFlatRate() {
    return isFlatRate;
  }
}
