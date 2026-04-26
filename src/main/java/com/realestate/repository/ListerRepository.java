package com.realestate.repository;

import com.realestate.model.Lister;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ListerRepository extends JpaRepository<Lister, Long> {
    Optional<Lister> findByEmail(String email);
    List<Lister> findByNameContainingIgnoreCase(String name);
    List<Lister> findByRegionContainingIgnoreCase(String region);
    List<Lister> findByIsActive(Boolean isActive);
}
