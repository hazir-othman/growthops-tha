package com.growthops.travelinsurance.validator;

import com.growthops.travelinsurance.dto.JourneyForm;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class JourneyFormValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return JourneyForm.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    JourneyForm form = (JourneyForm) target;

    LocalDate startDate = form.getStartDate();
    String coverageType = form.getCoverageType();
    String areaOfTravel = form.getAreaOfTravel();

    if ("ANNUAL".equals(coverageType) && startDate != null) {
      form.setEndDate(startDate.plusYears(1).minusDays(1));
    }

    LocalDate endDate = form.getEndDate();
    LocalDate today = LocalDate.now();

    if (startDate != null) {
      if (startDate.isBefore(today)) {
        errors.rejectValue(
            "startDate", "invalid.startDate", "Start date must be today or in the future");
      }
      if (startDate.isAfter(today.plusYears(1))) {
        errors.rejectValue(
            "startDate", "invalid.startDate", "Start date cannot be more than 1 year from today");
      }
    }

    if ("ANNUAL".equals(coverageType) && "AREA 4".equals(areaOfTravel)) {
      errors.rejectValue(
          "areaOfTravel",
          "invalid.areaOfTravel",
          "Area 4 is strictly not applicable for Annual coverage");
    }

    if ("SINGLE".equals(coverageType)) {
      if (endDate == null) {
        errors.rejectValue(
            "endDate", "required.endDate", "End date is required for Single Trip coverage");
      } else if (startDate != null) {
        if (endDate.isBefore(startDate)) {
          errors.rejectValue("endDate", "invalid.endDate", "End date cannot be before start date");
        } else {
          long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
          if (days > 180) {
            errors.rejectValue(
                "endDate",
                "invalid.endDate",
                "End date cannot exceed 180 days from start date (current duration: "
                    + days
                    + " days)");
          }
        }
      }
    }
  }
}
