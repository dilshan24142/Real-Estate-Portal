package com.realestate.repository;

import com.realestate.model.ResidentialProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResidentialPropertyRepository extends JpaRepository<ResidentialProperty, Long> {
    List<ResidentialProperty> findByBedrooms(Integer bedrooms);
    List<ResidentialProperty> findByFurnished(Boolean furnished);
}
