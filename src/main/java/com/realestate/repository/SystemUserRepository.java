package com.realestate.repository;

import com.realestate.model.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {
    Optional<SystemUser> findByEmail(String email);
    Optional<SystemUser> findByEmailAndPassword(String email, String password);
    List<SystemUser> findByNameContainingIgnoreCase(String name);
}
