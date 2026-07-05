package com.growthops.travelinsurance.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "policies")
public class Policy {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id", nullable = false)
  private Customer customer;

  @Column(name = "coverage_type", nullable = false)
  private String coverageType;

  @Column(name = "area_of_travel", nullable = false)
  private String areaOfTravel;

  @Column(name = "start_date", nullable = false)
  private LocalDate startDate;

  @Column(name = "end_date", nullable = false)
  private LocalDate endDate;

  @Column(name = "plan_type", nullable = false)
  private String planType;

  @Column(name = "total_amount", nullable = false)
  private BigDecimal totalAmount;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @PrePersist
  protected void onCreate() {
    createdAt = LocalDateTime.now();
  }

  public Policy() {}

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Customer getCustomer() {
    return customer;
  }

  public void setCustomer(Customer customer) {
    this.customer = customer;
  }

  public String getCoverageType() {
    return coverageType;
  }

  public void setCoverageType(String coverageType) {
    this.coverageType = coverageType;
  }

  public String getAreaOfTravel() {
    return areaOfTravel;
  }

  public void setAreaOfTravel(String areaOfTravel) {
    this.areaOfTravel = areaOfTravel;
  }

  public LocalDate getStartDate() {
    return startDate;
  }

  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  public LocalDate getEndDate() {
    return endDate;
  }

  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  public String getPlanType() {
    return planType;
  }

  public void setPlanType(String planType) {
    this.planType = planType;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getAreaOfTravelDescription() {
    if (areaOfTravel == null) {
      return "";
    }
    switch (areaOfTravel.toUpperCase()) {
      case "AREA 1":
        return "Australia, Brunei, Cambodia, China, Hong Kong, India, Indonesia, Japan, Korea,"
                   + " Laos, Macau, Maldives, Myanmar, New Zealand, Pakistan, Philippines,"
                   + " Singapore, Sri Lanka, Taiwan, Thailand and Vietnam";
      case "AREA 2":
        return "Europe, Tibet, Nepal, Mongolia, Bhutan and Countries in Area 1";
      case "AREA 3":
        return "Worldwide and countries in Area 1 and 2 but excluding Afghanistan, Cuba, Democratic"
                   + " Republic of Congo, Iran, Iraq, Sudan and Syria";
      case "AREA 4":
        return "Malaysia (single trip between Peninsular and East Malaysia and vice versa)";
      default:
        return "";
    }
  }
}
