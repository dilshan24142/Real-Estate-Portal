package com.realestate.service;

import com.realestate.model.Appointment;
import com.realestate.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class AppointmentService {

    @Autowired private AppointmentRepository appointmentRepo;

    public Appointment save(Appointment appointment) { return appointmentRepo.save(appointment); }

    public List<Appointment> getAllAppointments() { return appointmentRepo.findAll(); }
    public List<Appointment> getByUserId(Long userId) {
        return appointmentRepo.findByUserIdOrderByAppointmentDateDesc(userId);
    }
    public List<Appointment> getByListerId(Long listerId) {
        return appointmentRepo.findByListerId(listerId);
    }
    public List<Appointment> getByPropertyId(Long propertyId) {
        return appointmentRepo.findByPropertyId(propertyId);
    }
    public List<Appointment> getByStatus(String status) {
        return appointmentRepo.findByStatus(status);
    }

    public Optional<Appointment> getById(Long id) { return appointmentRepo.findById(id); }

    public void updateStatus(Long id, String status) {
        appointmentRepo.findById(id).ifPresent(a -> {
            a.setStatus(status);
            appointmentRepo.save(a);
        });
    }

    public void delete(Long id) { appointmentRepo.deleteById(id); }
}
