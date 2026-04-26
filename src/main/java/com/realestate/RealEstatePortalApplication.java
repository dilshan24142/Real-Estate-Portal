package com.realestate;

import com.realestate.model.Admin;
import com.realestate.repository.AdminRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RealEstatePortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(RealEstatePortalApplication.class, args);
    }

    // Seeds a default admin on first startup
    @Bean
    CommandLineRunner init(AdminRepository adminRepository) {
        return args -> {
            if (adminRepository.count() == 0) {
                Admin admin = new Admin();
                admin.setName("System Administrator");
                admin.setEmail("admin@realestate.com");
                admin.setPassword("admin123");
                admin.setPhone("+94 11 000 0000");
                admin.setPermissions("ALL");
                adminRepository.save(admin);
                System.out.println("==============================================");
                System.out.println("Default Admin Created:");
                System.out.println("  Email:    admin@realestate.com");
                System.out.println("  Password: admin123");
                System.out.println("==============================================");
            }
        };
    }
}
