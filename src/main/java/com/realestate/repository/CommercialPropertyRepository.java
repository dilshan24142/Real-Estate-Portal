package com.realestate.repository;

import com.realestate.model.CommercialProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommercialPropertyRepository extends JpaRepository<CommercialProperty, Long> {
    List<CommercialProperty> findByCommercialType(String commercialType);
}
