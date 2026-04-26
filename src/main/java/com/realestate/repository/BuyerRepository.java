package com.realestate.repository;

import com.realestate.model.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BuyerRepository extends JpaRepository<Buyer, Long> {
    List<Buyer> findByNameContainingIgnoreCase(String name);
    List<Buyer> findByIsActive(Boolean isActive);
}
