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
@RequestMapping("/agents")
public class AgentController {

    @Autowired private ListerService listerService;
    @Autowired private FeedbackService feedbackService;
    @Autowired private PropertyService propertyService;

    @GetMapping("/list")
    public String list(Model model, HttpSession session,
                       @RequestParam(required = false) String name,
                       @RequestParam(required = false) String region) {
        if (name != null && !name.isEmpty()) {
            model.addAttribute("listers", listerService.searchByName(name));
        } else if (region != null && !region.isEmpty()) {
            model.addAttribute("listers", listerService.searchByRegion(region));
            model.addAttribute("region", region);
        } else {
            model.addAttribute("listers", listerService.getAllListers());
        }
        model.addAttribute("name", name);
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "agent/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model, HttpSession session) {
        listerService.getListerById(id).ifPresent(l -> {
            model.addAttribute("lister", l);
            model.addAttribute("reviews", feedbackService.getReviewsByAgent(id));
            model.addAttribute("avgRating", feedbackService.getAgentAverageRating(id));
            model.addAttribute("properties", propertyService.getAllProperties().stream()
                .filter(p -> id.equals(p.getListerId())).toList());
        });
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "agent/view";
    }

    @GetMapping("/new")
    public String newForm(Model model, HttpSession session) {
        if (!(session.getAttribute("currentUser") instanceof Admin)) return "redirect:/login";
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "agent/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam String listerType, @RequestParam String name,
                       @RequestParam String email, @RequestParam String phone,
                       @RequestParam String region, @RequestParam(required = false) String bio,
                       @RequestParam(required = false) String agencyName,
                       @RequestParam(required = false) String licenseNumber,
                       @RequestParam(required = false) String ownerName,
                       HttpSession session, RedirectAttributes ra) {
        if (!(session.getAttribute("currentUser") instanceof Admin)) return "redirect:/login";
        if (listerService.emailExists(email)) {
            ra.addFlashAttribute("error", "Email already registered.");
            return "redirect:/agents/new";
        }
        Lister saved;
        if ("AGENT".equals(listerType)) {
            Agent agent = new Agent();
            agent.setName(name); agent.setEmail(email);
            agent.setPhone(phone); agent.setRegion(region); agent.setBio(bio);
            agent.setAgencyName(agencyName); agent.setLicenseNumber(licenseNumber);
            saved = listerService.saveAgent(agent);
        } else {
            IndependentSeller seller = new IndependentSeller();
            seller.setName(name); seller.setEmail(email);
            seller.setPhone(phone); seller.setRegion(region); seller.setBio(bio);
            seller.setOwnerName(ownerName);
            saved = listerService.saveSeller(seller);
        }
        ra.addFlashAttribute("success", "Lister registered successfully!");
        return "redirect:/agents/view/" + saved.getId();
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        if (!(session.getAttribute("currentUser") instanceof Admin)) return "redirect:/login";
        listerService.getListerById(id).ifPresent(l -> model.addAttribute("lister", l));
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "agent/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @RequestParam String name,
                         @RequestParam String phone, @RequestParam String region,
                         @RequestParam(required = false) String bio,
                         @RequestParam(required = false) String agencyName,
                         @RequestParam(required = false) String licenseNumber,
                         HttpSession session, RedirectAttributes ra) {
        if (!(session.getAttribute("currentUser") instanceof Admin)) return "redirect:/login";
        listerService.getListerById(id).ifPresent(l -> {
            l.setName(name); l.setPhone(phone);
            l.setRegion(region); l.setBio(bio);
            if (l instanceof Agent a) {
                a.setAgencyName(agencyName);
                a.setLicenseNumber(licenseNumber);
            }
            listerService.updateLister(l);
        });
        ra.addFlashAttribute("success", "Profile updated successfully.");
        return "redirect:/agents/view/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (!(session.getAttribute("currentUser") instanceof Admin)) return "redirect:/login";
        listerService.deactivateLister(id);
        ra.addFlashAttribute("success", "Agent deactivated.");
        return "redirect:/agents/list";
    }
}
