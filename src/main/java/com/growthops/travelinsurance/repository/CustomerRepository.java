package com.growthops.travelinsurance.repository;

import com.growthops.travelinsurance.entity.Customer;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
  @Cacheable(value = "customerProfiles", key = "#nric", unless = "#result == null")
  Optional<Customer> findByNric(String nric);
}
