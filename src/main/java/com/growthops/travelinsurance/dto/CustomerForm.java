package com.growthops.travelinsurance.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;

public class CustomerForm {

  @NotBlank(message = "Full name is required")
  private String fullName;

  @NotBlank(message = "NRIC is required")
  @Pattern(regexp = "^\\d{12}$", message = "NRIC must be exactly 12 digits")
  private String nric;

  private LocalDate dob;
  private String gender;

  @NotBlank(message = "Email is required")
  @Pattern(
      regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
      message = "Invalid email format")
  private String email;

  @NotBlank(message = "Mobile number is required")
  @Pattern(regexp = "^01\\d{7,9}$", message = "Mobile number must be 9-11 digits and start with 01")
  private String mobileNo;

  @NotBlank(message = "Address line 1 is required")
  private String addressLine1;

  private String addressLine2;

  @NotBlank(message = "Postcode is required")
  @Pattern(regexp = "^\\d{5}$", message = "Postcode must be exactly 5 digits")
  private String postcode;

  public CustomerForm() {}

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getNric() {
    return nric;
  }

  public void setNric(String nric) {
    this.nric = nric;
  }

  public LocalDate getDob() {
    return dob;
  }

  public void setDob(LocalDate dob) {
    this.dob = dob;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getMobileNo() {
    return mobileNo;
  }

  public void setMobileNo(String mobileNo) {
    this.mobileNo = mobileNo;
  }

  public String getAddressLine1() {
    return addressLine1;
  }

  public void setAddressLine1(String addressLine1) {
    this.addressLine1 = addressLine1;
  }

  public String getAddressLine2() {
    return addressLine2;
  }

  public void setAddressLine2(String addressLine2) {
    this.addressLine2 = addressLine2;
  }

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }
}
