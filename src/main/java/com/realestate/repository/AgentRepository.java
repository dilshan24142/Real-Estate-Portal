package com.realestate.repository;

import com.realestate.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AgentRepository extends JpaRepository<Agent, Long> {
    List<Agent> findByAgencyNameContainingIgnoreCase(String agencyName);
    List<Agent> findByRegionContainingIgnoreCase(String region);
}
