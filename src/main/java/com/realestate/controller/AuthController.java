package com.realestate.controller;

import com.realestate.model.*;
import com.realestate.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired private UserService userService;

    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("currentUser") != null) return "redirect:/";
        return "auth/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password,
                        HttpSession session, RedirectAttributes ra) {
        SystemUser user = userService.authenticate(email, password);
        if (user == null || !Boolean.TRUE.equals(user.getIsActive())) {
            ra.addFlashAttribute("error", "Invalid email or password.");
            return "redirect:/login";
        }
        session.setAttribute("currentUser", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userType", user.getUserType());
        return "redirect:" + user.getDashboardRedirect();
    }

    @GetMapping("/register")
    public String registerPage(HttpSession session) {
        if (session.getAttribute("currentUser") != null) return "redirect:/";
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email,
                           @RequestParam String password, @RequestParam String phone,
                           @RequestParam String userType,
                           @RequestParam(required = false) Double budget,
                           @RequestParam(required = false) Double monthlyBudget,
                           @RequestParam(required = false) String preferredLocations,
                           @RequestParam(required = false) String moveInDate,
                           HttpSession session, RedirectAttributes ra) {
        if (userService.emailExists(email)) {
            ra.addFlashAttribute("error", "Email already registered.");
            return "redirect:/register";
        }
        SystemUser user;
        if ("BUYER".equals(userType)) {
            Buyer buyer = new Buyer();
            buyer.setName(name); buyer.setEmail(email);
            buyer.setPassword(password); buyer.setPhone(phone);
            buyer.setBudget(budget);
            buyer.setPreferredLocations(preferredLocations);
            user = userService.registerBuyer(buyer);
        } else {
            Renter renter = new Renter();
            renter.setName(name); renter.setEmail(email);
            renter.setPassword(password); renter.setPhone(phone);
            renter.setMonthlyBudget(monthlyBudget);
            renter.setMoveInDate(moveInDate);
            user = userService.registerRenter(renter);
        }
        session.setAttribute("currentUser", user);
        session.setAttribute("userId", user.getId());
        session.setAttribute("userType", user.getUserType());
        ra.addFlashAttribute("success", "Welcome, " + name + "! Account created successfully.");
        return "redirect:" + user.getDashboardRedirect();
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
