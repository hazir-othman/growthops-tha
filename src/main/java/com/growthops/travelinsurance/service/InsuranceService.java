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

    Optional<Customer> existingCustomerOpt = customerRepository.findByNric(customerForm.getNric());
    Customer customer;

    // update customer details if IC already exists
    if (existingCustomerOpt.isPresent()) {
      customer = existingCustomerOpt.get();
      customer.setFullName(customerForm.getFullName());
      customer.setEmail(customerForm.getEmail());
      customer.setMobileNo(customerForm.getMobileNo());
      customer.setAddressLine1(customerForm.getAddressLine1());
      customer.setAddressLine2(customerForm.getAddressLine2());
      customer.setPostcode(customerForm.getPostcode());
      customer.setDob(customerForm.getDob());
      customer.setGender(customerForm.getGender());
    } else {
      customer = new Customer();
      customer.setFullName(customerForm.getFullName());
      customer.setNric(customerForm.getNric());
      customer.setDob(customerForm.getDob());
      customer.setGender(customerForm.getGender());
      customer.setEmail(customerForm.getEmail());
      customer.setMobileNo(customerForm.getMobileNo());
      customer.setAddressLine1(customerForm.getAddressLine1());
      customer.setAddressLine2(customerForm.getAddressLine2());
      customer.setPostcode(customerForm.getPostcode());
    }

    customer = customerRepository.save(customer);

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
}
