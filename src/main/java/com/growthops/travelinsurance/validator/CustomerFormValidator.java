package com.growthops.travelinsurance.validator;

import com.growthops.travelinsurance.dto.CustomerForm;
import java.time.LocalDate;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class CustomerFormValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return CustomerForm.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    CustomerForm form = (CustomerForm) target;
    String nric = form.getNric();

    if (nric != null && nric.matches("^\\d{12}$")) {
      String yy = nric.substring(0, 2);
      String mm = nric.substring(2, 4);
      String dd = nric.substring(4, 6);

      int yearVal = Integer.parseInt(yy);
      int monthVal = Integer.parseInt(mm);
      int dayVal = Integer.parseInt(dd);

      if (monthVal < 1 || monthVal > 12) {
        errors.rejectValue(
            "nric", "invalid.nric.dob", "NRIC contains an invalid month in birth date part");
        return;
      }

      int currentYearLastTwo = LocalDate.now().getYear() % 100;
      int fullYear = (yearVal <= currentYearLastTwo) ? (2000 + yearVal) : (1900 + yearVal);

      try {
        LocalDate parsedDob = LocalDate.of(fullYear, monthVal, dayVal);

        if (parsedDob.isAfter(LocalDate.now())) {
          errors.rejectValue(
              "nric", "invalid.nric.future", "NRIC birth date cannot be in the future");
        } else {
          form.setDob(parsedDob);
          int lastDigit = Character.getNumericValue(nric.charAt(11));
          form.setGender(lastDigit % 2 != 0 ? "MALE" : "FEMALE");
        }
      } catch (Exception e) {
        errors.rejectValue(
            "nric", "invalid.nric.dob", "NRIC contains an invalid day/date combination");
      }
    }
  }
}
