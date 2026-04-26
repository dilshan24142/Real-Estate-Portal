package com.realestate.service;

import com.realestate.model.*;
import com.realestate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class UserService {

    @Autowired private SystemUserRepository userRepo;
    @Autowired private BuyerRepository buyerRepo;
    @Autowired private RenterRepository renterRepo;
    @Autowired private AdminRepository adminRepo;

    public SystemUser authenticate(String email, String password) {
        return userRepo.findByEmailAndPassword(email, password).orElse(null);
    }

    public boolean emailExists(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    public Buyer registerBuyer(Buyer buyer) {
        return buyerRepo.save(buyer);
    }

    public Renter registerRenter(Renter renter) {
        return renterRepo.save(renter);
    }

    public List<Buyer> getAllBuyers() { return buyerRepo.findAll(); }
    public List<Renter> getAllRenters() { return renterRepo.findAll(); }
    public List<SystemUser> getAllUsers() { return userRepo.findAll(); }

    public Optional<SystemUser> getUserById(Long id) { return userRepo.findById(id); }
    public Optional<Buyer> getBuyerById(Long id) { return buyerRepo.findById(id); }
    public Optional<Renter> getRenterById(Long id) { return renterRepo.findById(id); }

    public Buyer updateBuyer(Buyer buyer) { return buyerRepo.save(buyer); }
    public Renter updateRenter(Renter renter) { return renterRepo.save(renter); }
    public SystemUser updateUser(SystemUser user) { return userRepo.save(user); }

    public void deactivateUser(Long id) {
        userRepo.findById(id).ifPresent(u -> {
            u.setIsActive(false);
            userRepo.save(u);
        });
    }

    public void deleteUser(Long id) { userRepo.deleteById(id); }

    public List<SystemUser> searchUsers(String name) {
        return userRepo.findByNameContainingIgnoreCase(name);
    }
}
