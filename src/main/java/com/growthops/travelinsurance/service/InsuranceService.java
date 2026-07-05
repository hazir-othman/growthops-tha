package com.growthops.travelinsurance.service;

import com.growthops.travelinsurance.dto.CustomerForm;
import com.growthops.travelinsurance.dto.JourneyForm;
import com.growthops.travelinsurance.entity.Customer;
import com.growthops.travelinsurance.entity.Policy;
import com.growthops.travelinsurance.repository.CustomerRepository;
import com.growthops.travelinsurance.repository.PolicyRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InsuranceService {

  private final CustomerRepository customerRepository;
  private final PolicyRepository policyRepository;
  private final PricingService pricingService;

  @Autowired
  public InsuranceService(
      CustomerRepository customerRepository,
      PolicyRepository policyRepository,
      PricingService pricingService) {
    this.customerRepository = customerRepository;
    this.policyRepository = policyRepository;
    this.pricingService = pricingService;
  }

  @Transactional
  public Policy createInsurancePurchase(
      JourneyForm journeyForm, String planType, CustomerForm customerForm) {
    BigDecimal totalAmount =
        pricingService.calculatePremium(
            journeyForm.getCoverageType(),
            journeyForm.getAreaOfTravel(),
            planType,
            journeyForm.getStartDate(),
            journeyForm.getEndDate());

    Customer customer = this.saveOrUpdateCustomer(customerForm);

    Policy policy = new Policy();
    policy.setCustomer(customer);
    policy.setCoverageType(journeyForm.getCoverageType());
    policy.setAreaOfTravel(journeyForm.getAreaOfTravel());
    policy.setStartDate(journeyForm.getStartDate());
    policy.setEndDate(journeyForm.getEndDate());
    policy.setPlanType(planType);
    policy.setTotalAmount(totalAmount);

    return policyRepository.save(policy);
  }

  @CachePut(value = "customerProfiles", key = "#form.nric")
  public Customer saveOrUpdateCustomer(CustomerForm form) {
    Optional<Customer> existingCustomerOpt = customerRepository.findByNric(form.getNric());
    Customer customer;

    if (existingCustomerOpt.isPresent()) {
      customer = existingCustomerOpt.get();
      customer.setFullName(form.getFullName());
      customer.setEmail(form.getEmail());
      customer.setMobileNo(form.getMobileNo());
      customer.setAddressLine1(form.getAddressLine1());
      customer.setAddressLine2(form.getAddressLine2());
      customer.setPostcode(form.getPostcode());
      customer.setDob(form.getDob());
      customer.setGender(form.getGender());
    } else {
      customer = new Customer();
      customer.setFullName(form.getFullName());
      customer.setNric(form.getNric());
      customer.setDob(form.getDob());
      customer.setGender(form.getGender());
      customer.setEmail(form.getEmail());
      customer.setMobileNo(form.getMobileNo());
      customer.setAddressLine1(form.getAddressLine1());
      customer.setAddressLine2(form.getAddressLine2());
      customer.setPostcode(form.getPostcode());
    }

    return customerRepository.save(customer);
  }
}
