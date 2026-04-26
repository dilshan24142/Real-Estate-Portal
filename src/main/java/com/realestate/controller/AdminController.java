package com.realestate.controller;

import com.realestate.model.Admin;
import com.realestate.repository.AdminRepository;
import com.realestate.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UserService userService;
    @Autowired private PropertyService propertyService;
    @Autowired private ListerService listerService;
    @Autowired private AppointmentService appointmentService;
    @Autowired private FeedbackService feedbackService;
    @Autowired private AdminRepository adminRepository;

    private boolean isAdmin(HttpSession session) {
        return session.getAttribute("currentUser") instanceof Admin;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("userCount", userService.getAllUsers().size());
        model.addAttribute("propertyCount", propertyService.getAllProperties().size());
        model.addAttribute("listerCount", listerService.getAllListers().size());
        model.addAttribute("appointmentCount", appointmentService.getAllAppointments().size());
        model.addAttribute("pendingFeedback", feedbackService.getPendingFeedback().size());
        model.addAttribute("recentProperties", propertyService.getAllProperties().stream().limit(5).toList());
        model.addAttribute("pendingAppointments", appointmentService.getByStatus("PENDING").stream().limit(5).toList());
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "admin/dashboard";
    }

    @GetMapping("/admins")
    public String listAdmins(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("admins", adminRepository.findAll());
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "admin/admins";
    }

    @GetMapping("/admins/new")
    public String newAdminForm(Model model, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "admin/admin-form";
    }

    @PostMapping("/admins/save")
    public String saveAdmin(@RequestParam String name, @RequestParam String email,
                            @RequestParam String password, @RequestParam String phone,
                            @RequestParam(required = false) String permissions,
                            HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        if (userService.emailExists(email)) {
            ra.addFlashAttribute("error", "Email already registered.");
            return "redirect:/admin/admins/new";
        }
        Admin admin = new Admin();
        admin.setName(name); admin.setEmail(email);
        admin.setPassword(password); admin.setPhone(phone);
        admin.setPermissions(permissions != null ? permissions : "STANDARD");
        adminRepository.save(admin);
        ra.addFlashAttribute("success", "Admin account created.");
        return "redirect:/admin/admins";
    }

    @PostMapping("/admins/delete/{id}")
    public String deleteAdmin(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        Admin currentAdmin = (Admin) session.getAttribute("currentUser");
        if (currentAdmin.getId().equals(id)) {
            ra.addFlashAttribute("error", "You cannot delete your own account.");
            return "redirect:/admin/admins";
        }
        userService.deleteUser(id);
        ra.addFlashAttribute("success", "Admin account removed.");
        return "redirect:/admin/admins";
    }
}
