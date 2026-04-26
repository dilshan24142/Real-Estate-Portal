package com.realestate.service;

import com.realestate.model.*;
import com.realestate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class ListerService {

    @Autowired private ListerRepository listerRepo;
    @Autowired private AgentRepository agentRepo;
    @Autowired private IndependentSellerRepository sellerRepo;

    public Agent saveAgent(Agent agent) { return agentRepo.save(agent); }
    public IndependentSeller saveSeller(IndependentSeller seller) { return sellerRepo.save(seller); }

    public List<Lister> getAllListers() { return listerRepo.findAll(); }
    public List<Agent> getAllAgents() { return agentRepo.findAll(); }
    public List<IndependentSeller> getAllSellers() { return sellerRepo.findAll(); }

    public Optional<Lister> getListerById(Long id) { return listerRepo.findById(id); }
    public Optional<Agent> getAgentById(Long id) { return agentRepo.findById(id); }

    public List<Lister> searchByName(String name) {
        return listerRepo.findByNameContainingIgnoreCase(name);
    }

    public List<Lister> searchByRegion(String region) {
        return listerRepo.findByRegionContainingIgnoreCase(region);
    }

    public boolean emailExists(String email) {
        return listerRepo.findByEmail(email).isPresent();
    }

    public void deactivateLister(Long id) {
        listerRepo.findById(id).ifPresent(l -> {
            l.setIsActive(false);
            listerRepo.save(l);
        });
    }

    public void deleteLister(Long id) { listerRepo.deleteById(id); }
    public Lister updateLister(Lister lister) { return listerRepo.save(lister); }
}
