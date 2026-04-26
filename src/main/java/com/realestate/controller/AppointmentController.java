package com.realestate.controller;

import com.realestate.model.*;
import com.realestate.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/appointments")
public class AppointmentController {

    @Autowired private AppointmentService appointmentService;
    @Autowired private PropertyService propertyService;
    @Autowired private ListerService listerService;

    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        SystemUser user = (SystemUser) session.getAttribute("currentUser");
        if (user instanceof Admin) {
            model.addAttribute("appointments", appointmentService.getAllAppointments());
        } else {
            model.addAttribute("appointments", appointmentService.getByUserId(user.getId()));
        }
        model.addAttribute("currentUser", user);
        return "appointment/list";
    }

    @GetMapping("/new")
    public String newForm(@RequestParam(required = false) Long propertyId,
                          Model model, HttpSession session) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        model.addAttribute("properties", propertyService.getAvailableProperties());
        model.addAttribute("listers", listerService.getAllListers());
        if (propertyId != null) {
            model.addAttribute("selectedPropertyId", propertyId);
            propertyService.getPropertyById(propertyId).ifPresent(p -> {
                model.addAttribute("selectedProperty", p);
                if (p.getListerId() != null) {
                    listerService.getListerById(p.getListerId())
                        .ifPresent(l -> model.addAttribute("selectedLister", l));
                }
            });
        }
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "appointment/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam Long propertyId, @RequestParam(required = false) Long listerId,
                       @RequestParam String appointmentDate, @RequestParam String appointmentTime,
                       @RequestParam String viewingType, @RequestParam(required = false) String notes,
                       HttpSession session, RedirectAttributes ra) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        SystemUser user = (SystemUser) session.getAttribute("currentUser");

        Appointment appt = new Appointment();
        appt.setUserId(user.getId());
        appt.setUserName(user.getName());
        appt.setPropertyId(propertyId);
        appt.setListerId(listerId);
        appt.setAppointmentDate(appointmentDate);
        appt.setAppointmentTime(appointmentTime);
        appt.setViewingType(viewingType);
        appt.setNotes(notes);
        appt.setStatus("PENDING");

        propertyService.getPropertyById(propertyId)
            .ifPresent(p -> appt.setPropertyTitle(p.getTitle()));
        if (listerId != null) {
            listerService.getListerById(listerId)
                .ifPresent(l -> appt.setListerName(l.getName()));
        }
        appointmentService.save(appt);
        ra.addFlashAttribute("success", "Viewing appointment booked successfully!");
        return "redirect:/appointments/list";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        appointmentService.getById(id).ifPresent(a -> model.addAttribute("appointment", a));
        model.addAttribute("properties", propertyService.getAvailableProperties());
        model.addAttribute("listers", listerService.getAllListers());
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "appointment/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @RequestParam String appointmentDate,
                         @RequestParam String appointmentTime,
                         @RequestParam String viewingType,
                         @RequestParam(required = false) String notes,
                         HttpSession session, RedirectAttributes ra) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        appointmentService.getById(id).ifPresent(a -> {
            a.setAppointmentDate(appointmentDate);
            a.setAppointmentTime(appointmentTime);
            a.setViewingType(viewingType);
            a.setNotes(notes);
            appointmentService.save(a);
        });
        ra.addFlashAttribute("success", "Appointment rescheduled successfully.");
        return "redirect:/appointments/list";
    }

    @PostMapping("/status/{id}")
    public String updateStatus(@PathVariable Long id, @RequestParam String status,
                               HttpSession session, RedirectAttributes ra) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        appointmentService.updateStatus(id, status);
        ra.addFlashAttribute("success", "Appointment status updated to: " + status);
        return "redirect:/appointments/list";
    }

    @PostMapping("/cancel/{id}")
    public String cancel(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        appointmentService.delete(id);
        ra.addFlashAttribute("success", "Appointment cancelled.");
        return "redirect:/appointments/list";
    }
}
