package com.realestate.repository;

import com.realestate.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUserId(Long userId);
    List<Appointment> findByListerId(Long listerId);
    List<Appointment> findByPropertyId(Long propertyId);
    List<Appointment> findByStatus(String status);
    List<Appointment> findByUserIdOrderByAppointmentDateDesc(Long userId);
}
