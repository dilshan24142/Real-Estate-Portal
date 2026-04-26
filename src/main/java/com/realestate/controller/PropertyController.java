package com.realestate.controller;

import com.realestate.model.*;
import com.realestate.service.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequestMapping("/properties")
public class PropertyController {

    @Autowired private PropertyService propertyService;
    @Autowired private ListerService listerService;
    @Autowired private FeedbackService feedbackService;
    @Autowired private AppointmentService appointmentService;

    @GetMapping("/list")
    public String list(Model model, HttpSession session,
                       @RequestParam(required = false) String location,
                       @RequestParam(required = false) String title,
                       @RequestParam(required = false) String status,
                       @RequestParam(required = false) Double minPrice,
                       @RequestParam(required = false) Double maxPrice) {
        List<Property> properties;
        if (location != null && !location.isEmpty()) {
            properties = propertyService.searchByLocation(location);
        } else if (title != null && !title.isEmpty()) {
            properties = propertyService.searchByTitle(title);
        } else if (status != null && !status.isEmpty()) {
            properties = propertyService.getByStatus(status);
        } else if (minPrice != null && maxPrice != null) {
            properties = propertyService.getByPriceRange(minPrice, maxPrice);
        } else {
            properties = propertyService.getAllProperties();
        }
        model.addAttribute("properties", properties);
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        model.addAttribute("location", location);
        model.addAttribute("title", title);
        return "property/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable Long id, Model model, HttpSession session) {
        propertyService.getPropertyById(id).ifPresent(p -> {
            model.addAttribute("property", p);
            model.addAttribute("questions", feedbackService.getQuestionsByProperty(id));
            if (p.getListerId() != null) {
                listerService.getListerById(p.getListerId()).ifPresent(l -> model.addAttribute("lister", l));
            }
        });
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "property/view";
    }

    @GetMapping("/new")
    public String newForm(Model model, HttpSession session) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        model.addAttribute("listers", listerService.getAllListers());
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "property/form";
    }

    @PostMapping("/save")
    public String save(@RequestParam String propertyType, @RequestParam String title,
                       @RequestParam String description, @RequestParam Double price,
                       @RequestParam String location, @RequestParam Double size,
                       @RequestParam String status,
                       @RequestParam(required = false) Long listerId,
                       @RequestParam(required = false) Integer bedrooms,
                       @RequestParam(required = false) Integer bathrooms,
                       @RequestParam(required = false) Boolean furnished,
                       @RequestParam(required = false) Integer parkingSpaces,
                       @RequestParam(required = false) String commercialType,
                       @RequestParam(required = false) Double floorArea,
                       @RequestParam(required = false) String zoning,
                       HttpSession session, RedirectAttributes ra) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        Property p;
        if ("RESIDENTIAL".equals(propertyType)) {
            ResidentialProperty rp = new ResidentialProperty();
            rp.setBedrooms(bedrooms); rp.setBathrooms(bathrooms);
            rp.setFurnished(furnished != null && furnished);
            rp.setParkingSpaces(parkingSpaces);
            p = rp;
        } else {
            CommercialProperty cp = new CommercialProperty();
            cp.setCommercialType(commercialType);
            cp.setFloorArea(floorArea); cp.setZoning(zoning);
            p = cp;
        }
        p.setTitle(title); p.setDescription(description);
        p.setPrice(price); p.setLocation(location);
        p.setSize(size); p.setStatus(status);
        p.setListerId(listerId);
        Property saved = propertyService.save(p);
        ra.addFlashAttribute("success", "Property listed successfully!");
        return "redirect:/properties/view/" + saved.getId();
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        propertyService.getPropertyById(id).ifPresent(p -> model.addAttribute("property", p));
        model.addAttribute("listers", listerService.getAllListers());
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "property/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @RequestParam String title,
                         @RequestParam String description, @RequestParam Double price,
                         @RequestParam String location, @RequestParam Double size,
                         @RequestParam String status,
                         @RequestParam(required = false) Long listerId,
                         @RequestParam(required = false) Integer bedrooms,
                         @RequestParam(required = false) Integer bathrooms,
                         @RequestParam(required = false) Boolean furnished,
                         @RequestParam(required = false) Integer parkingSpaces,
                         @RequestParam(required = false) String commercialType,
                         @RequestParam(required = false) Double floorArea,
                         @RequestParam(required = false) String zoning,
                         HttpSession session, RedirectAttributes ra) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        propertyService.getPropertyById(id).ifPresent(p -> {
            p.setTitle(title); p.setDescription(description);
            p.setPrice(price); p.setLocation(location);
            p.setSize(size); p.setStatus(status);
            p.setListerId(listerId);
            if (p instanceof ResidentialProperty rp) {
                rp.setBedrooms(bedrooms); rp.setBathrooms(bathrooms);
                rp.setFurnished(furnished != null && furnished);
                rp.setParkingSpaces(parkingSpaces);
            } else if (p instanceof CommercialProperty cp) {
                cp.setCommercialType(commercialType);
                cp.setFloorArea(floorArea); cp.setZoning(zoning);
            }
            propertyService.save(p);
        });
        ra.addFlashAttribute("success", "Property updated successfully!");
        return "redirect:/properties/view/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        propertyService.deleteProperty(id);
        ra.addFlashAttribute("success", "Property removed from listings.");
        return "redirect:/properties/list";
    }
}
