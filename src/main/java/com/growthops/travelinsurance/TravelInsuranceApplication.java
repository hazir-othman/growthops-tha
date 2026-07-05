package com.growthops.travelinsurance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TravelInsuranceApplication {
  public static void main(String[] args) {
    SpringApplication.run(TravelInsuranceApplication.class, args);
  }
}
