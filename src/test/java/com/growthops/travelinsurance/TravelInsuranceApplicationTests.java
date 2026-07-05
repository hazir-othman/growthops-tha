package com.growthops.travelinsurance;

import com.growthops.travelinsurance.dto.CustomerForm;
import com.growthops.travelinsurance.dto.JourneyForm;
import com.growthops.travelinsurance.service.PricingService;
import com.growthops.travelinsurance.validator.CustomerFormValidator;
import com.growthops.travelinsurance.validator.JourneyFormValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TravelInsuranceApplicationTests {

    @Autowired
    private PricingService pricingService;

    @Autowired
    private CustomerFormValidator customerFormValidator;

    @Autowired
    private JourneyFormValidator journeyFormValidator;

    @Test
    void contextLoads() {
    }

    @Test
    void testPricingEngine_SingleTrip() {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(4); // 5 days inclusive
        BigDecimal premium = pricingService.calculatePremium("SINGLE", "AREA 1", "PLAN_A", start, end);
        assertEquals(0, premium.compareTo(BigDecimal.valueOf(50))); // 5 * 10 = 50

        BigDecimal premiumB = pricingService.calculatePremium("SINGLE", "AREA 3", "PLAN_B", start, end);
        assertEquals(0, premiumB.compareTo(BigDecimal.valueOf(200))); // 5 * 40 = 200
    }

    @Test
    void testPricingEngine_AnnualTrip() {
        BigDecimal premiumA = pricingService.calculatePremium("ANNUAL", "AREA 1", "PLAN_A", LocalDate.now(), null);
        assertEquals(0, premiumA.compareTo(BigDecimal.valueOf(100)));

        assertThrows(IllegalArgumentException.class, () -> {
            pricingService.calculatePremium("ANNUAL", "AREA 4", "PLAN_A", LocalDate.now(), null);
        });
    }

    @Test
    void testCustomerFormValidator_ValidNricMale() {
        CustomerForm form = new CustomerForm();
        form.setNric("950704141233"); // last digit odd = Male, first 6 = 95-07-04 (1995-07-04)
        Errors errors = new BeanPropertyBindingResult(form, "customerForm");
        customerFormValidator.validate(form, errors);

        assertFalse(errors.hasErrors());
        assertEquals(LocalDate.of(1995, 7, 4), form.getDob());
        assertEquals("MALE", form.getGender());
    }

    @Test
    void testCustomerFormValidator_ValidNricFemale() {
        CustomerForm form = new CustomerForm();
        form.setNric("050704141234"); // last digit even = Female, first 6 = 05-07-04 (2005-07-04)
        Errors errors = new BeanPropertyBindingResult(form, "customerForm");
        customerFormValidator.validate(form, errors);

        assertFalse(errors.hasErrors());
        assertEquals(LocalDate.of(2005, 7, 4), form.getDob());
        assertEquals("FEMALE", form.getGender());
    }

    @Test
    void testCustomerFormValidator_InvalidNricDate() {
        CustomerForm form = new CustomerForm();
        form.setNric("950231141234"); // Invalid date Feb 31st
        Errors errors = new BeanPropertyBindingResult(form, "customerForm");
        customerFormValidator.validate(form, errors);

        assertTrue(errors.hasErrors());
        assertNotNull(errors.getFieldError("nric"));
    }

    @Test
    void testJourneyFormValidator_ValidSingle() {
        JourneyForm form = new JourneyForm();
        form.setCoverageType("SINGLE");
        form.setAreaOfTravel("AREA 1");
        form.setStartDate(LocalDate.now());
        form.setEndDate(LocalDate.now().plusDays(10));
        Errors errors = new BeanPropertyBindingResult(form, "journeyForm");
        journeyFormValidator.validate(form, errors);

        assertFalse(errors.hasErrors());
    }

    @Test
    void testJourneyFormValidator_InvalidDuration() {
        JourneyForm form = new JourneyForm();
        form.setCoverageType("SINGLE");
        form.setAreaOfTravel("AREA 1");
        form.setStartDate(LocalDate.now());
        form.setEndDate(LocalDate.now().plusDays(185)); // > 180 days
        Errors errors = new BeanPropertyBindingResult(form, "journeyForm");
        journeyFormValidator.validate(form, errors);

        assertTrue(errors.hasErrors());
        assertNotNull(errors.getFieldError("endDate"));
    }

    @Test
    void testJourneyFormValidator_Area4AnnualForbidden() {
        JourneyForm form = new JourneyForm();
        form.setCoverageType("ANNUAL");
        form.setAreaOfTravel("AREA 4");
        form.setStartDate(LocalDate.now());
        Errors errors = new BeanPropertyBindingResult(form, "journeyForm");
        journeyFormValidator.validate(form, errors);

        assertTrue(errors.hasErrors());
        assertNotNull(errors.getFieldError("areaOfTravel"));
    }

    @Test
    void testJourneyFormValidator_AnnualEndDateCalculation() {
        JourneyForm form = new JourneyForm();
        form.setCoverageType("ANNUAL");
        form.setAreaOfTravel("AREA 1");
        LocalDate start = LocalDate.now();
        form.setStartDate(start);
        Errors errors = new BeanPropertyBindingResult(form, "journeyForm");
        journeyFormValidator.validate(form, errors);

        assertFalse(errors.hasErrors());
        assertEquals(start.plusYears(1).minusDays(1), form.getEndDate());
    }
}
