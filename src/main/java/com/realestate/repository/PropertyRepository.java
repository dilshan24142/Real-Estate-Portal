package com.realestate.repository;

import com.realestate.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByLocationContainingIgnoreCase(String location);
    List<Property> findByStatus(String status);
    List<Property> findByListerId(Long listerId);
    List<Property> findByTitleContainingIgnoreCase(String title);
    List<Property> findByPriceBetween(Double min, Double max);
    @Query("SELECT p FROM Property p WHERE p.price <= :maxPrice AND p.status = 'AVAILABLE'")
    List<Property> findAvailableUnderPrice(Double maxPrice);
}
