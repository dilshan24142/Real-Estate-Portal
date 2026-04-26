package com.realestate.repository;

import com.realestate.model.Renter;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RenterRepository extends JpaRepository<Renter, Long> {
    List<Renter> findByNameContainingIgnoreCase(String name);
    List<Renter> findByIsActive(Boolean isActive);
}
