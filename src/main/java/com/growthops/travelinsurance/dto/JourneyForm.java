package com.growthops.travelinsurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

public class JourneyForm {

  @NotBlank(message = "Coverage type is required")
  private String coverageType;

  @NotBlank(message = "Area of travel is required")
  private String areaOfTravel;

  @NotNull(message = "Start date is required")
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate startDate;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate endDate;

  public JourneyForm() {}

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
