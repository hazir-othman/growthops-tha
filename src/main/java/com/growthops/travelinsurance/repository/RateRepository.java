package com.growthops.travelinsurance.repository;

import com.growthops.travelinsurance.entity.Rate;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RateRepository extends JpaRepository<Rate, Long> {

  @Cacheable(
      value = "rates",
      key =
          "#coverageType.toUpperCase() + '-' + #area.toUpperCase() + '-' + #planType.toUpperCase()")
  @Query(
      "SELECT r FROM Rate r WHERE UPPER(r.coverageType) = UPPER(:coverageType) AND UPPER(r.area) ="
          + " UPPER(:area) AND (UPPER(r.planType) = UPPER(:planType) OR REPLACE(UPPER(r.planType),"
          + " ' ', '_') = REPLACE(UPPER(:planType), ' ', '_'))")
  Optional<Rate> findRate(
      @Param("coverageType") String coverageType,
      @Param("area") String area,
      @Param("planType") String planType);
}
