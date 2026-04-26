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
@RequestMapping("/users")
public class UserController {

    @Autowired private UserService userService;

    private boolean isAdmin(HttpSession session) {
        Object user = session.getAttribute("currentUser");
        return user instanceof Admin;
    }

    @GetMapping("/list")
    public String list(HttpSession session, Model model,
                       @RequestParam(required = false) String search) {
        if (!isAdmin(session)) return "redirect:/login";
        if (search != null && !search.isEmpty()) {
            model.addAttribute("users", userService.searchUsers(search));
            model.addAttribute("search", search);
        } else {
            model.addAttribute("users", userService.getAllUsers());
        }
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "user/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        userService.getUserById(id).ifPresent(u -> model.addAttribute("user", u));
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "user/view";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        SystemUser current = (SystemUser) session.getAttribute("currentUser");
        if (!isAdmin(session) && !current.getId().equals(id)) return "redirect:/";
        userService.getUserById(id).ifPresent(u -> model.addAttribute("user", u));
        model.addAttribute("currentUser", current);
        return "user/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @RequestParam String name,
                         @RequestParam String phone,
                         @RequestParam(required = false) Double budget,
                         @RequestParam(required = false) Double monthlyBudget,
                         @RequestParam(required = false) String preferredLocations,
                         @RequestParam(required = false) String preferredAreas,
                         @RequestParam(required = false) String moveInDate,
                         HttpSession session, RedirectAttributes ra) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        userService.getUserById(id).ifPresent(u -> {
            u.setName(name);
            u.setPhone(phone);
            if (u instanceof Buyer b) {
                b.setBudget(budget);
                b.setPreferredLocations(preferredLocations);
            } else if (u instanceof Renter r) {
                r.setMonthlyBudget(monthlyBudget);
                r.setPreferredAreas(preferredAreas);
                r.setMoveInDate(moveInDate);
            }
            SystemUser saved = userService.updateUser(u);
            if (((SystemUser) session.getAttribute("currentUser")).getId().equals(id)) {
                session.setAttribute("currentUser", saved);
            }
        });
        ra.addFlashAttribute("success", "Profile updated successfully.");
        return "redirect:/users/view/" + id;
    }

    @PostMapping("/deactivate/{id}")
    public String deactivate(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        userService.deactivateUser(id);
        ra.addFlashAttribute("success", "User account deactivated.");
        return "redirect:/users/list";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (!isAdmin(session)) return "redirect:/login";
        userService.deleteUser(id);
        ra.addFlashAttribute("success", "User permanently deleted.");
        return "redirect:/users/list";
    }
}
