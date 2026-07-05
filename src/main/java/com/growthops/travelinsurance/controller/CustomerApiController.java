package com.growthops.travelinsurance.controller;

import com.growthops.travelinsurance.repository.CustomerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerApiController {

  private final CustomerRepository customerRepository;

  public CustomerApiController(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @GetMapping("/lookup/{nric}")
  public ResponseEntity<CustomerSummaryDto> lookupCustomer(@PathVariable String nric) {
    return customerRepository
        .findByNric(nric)
        .map(
            customer ->
                ResponseEntity.ok(
                    new CustomerSummaryDto(
                        customer.getFullName(),
                        customer.getEmail(),
                        customer.getMobileNo(),
                        customer.getAddressLine1(),
                        customer.getAddressLine2(),
                        customer.getPostcode())))
        .orElse(ResponseEntity.notFound().build());
  }

  public static class CustomerSummaryDto {
    private String fullName;
    private String email;
    private String mobileNo;
    private String addressLine1;
    private String addressLine2;
    private String postcode;

    public CustomerSummaryDto(
        String fullName,
        String email,
        String mobileNo,
        String addressLine1,
        String addressLine2,
        String postcode) {
      this.fullName = fullName;
      this.email = email;
      this.mobileNo = mobileNo;
      this.addressLine1 = addressLine1;
      this.addressLine2 = addressLine2;
      this.postcode = postcode;
    }

    public String getFullName() {
      return fullName;
    }

    public String getEmail() {
      return email;
    }

    public String getMobileNo() {
      return mobileNo;
    }

    public String getAddressLine1() {
      return addressLine1;
    }

    public String getAddressLine2() {
      return addressLine2;
    }

    public String getPostcode() {
      return postcode;
    }
  }
}
