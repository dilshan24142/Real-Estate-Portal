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
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired private FeedbackService feedbackService;
    @Autowired private PropertyService propertyService;
    @Autowired private ListerService listerService;

    @GetMapping("/list")
    public String list(Model model, HttpSession session) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        model.addAttribute("questions", feedbackService.getAllQuestions());
        model.addAttribute("reviews", feedbackService.getAllReviews());
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "feedback/list";
    }

    @GetMapping("/inquiry/new")
    public String newInquiry(@RequestParam(required = false) Long propertyId,
                             Model model, HttpSession session) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        model.addAttribute("properties", propertyService.getAvailableProperties());
        if (propertyId != null) model.addAttribute("selectedPropertyId", propertyId);
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "feedback/inquiry-form";
    }

    @PostMapping("/inquiry/save")
    public String saveInquiry(@RequestParam Long propertyId, @RequestParam String content,
                              HttpSession session, RedirectAttributes ra) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        SystemUser user = (SystemUser) session.getAttribute("currentUser");
        PropertyQuestion q = new PropertyQuestion();
        q.setUserId(user.getId()); q.setUserName(user.getName());
        q.setContent(content); q.setPropertyId(propertyId);
        propertyService.getPropertyById(propertyId).ifPresent(p -> q.setPropertyTitle(p.getTitle()));
        feedbackService.saveQuestion(q);
        ra.addFlashAttribute("success", "Your inquiry has been submitted!");
        return "redirect:/properties/view/" + propertyId;
    }

    @GetMapping("/review/new")
    public String newReview(@RequestParam(required = false) Long agentId,
                            Model model, HttpSession session) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        model.addAttribute("listers", listerService.getAllListers());
        if (agentId != null) model.addAttribute("selectedAgentId", agentId);
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "feedback/review-form";
    }

    @PostMapping("/review/save")
    public String saveReview(@RequestParam Long agentId, @RequestParam String content,
                             @RequestParam Integer rating, HttpSession session, RedirectAttributes ra) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        SystemUser user = (SystemUser) session.getAttribute("currentUser");
        AgentReview review = new AgentReview();
        review.setUserId(user.getId()); review.setUserName(user.getName());
        review.setContent(content); review.setAgentId(agentId); review.setRating(rating);
        listerService.getListerById(agentId).ifPresent(l -> review.setAgentName(l.getName()));
        feedbackService.saveReview(review);
        ra.addFlashAttribute("success", "Your review has been submitted!");
        return "redirect:/agents/view/" + agentId;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model, HttpSession session) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        feedbackService.getById(id).ifPresent(f -> model.addAttribute("feedback", f));
        model.addAttribute("currentUser", session.getAttribute("currentUser"));
        return "feedback/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id, @RequestParam String content,
                         @RequestParam(required = false) Integer rating,
                         HttpSession session, RedirectAttributes ra) {
        if (session.getAttribute("currentUser") == null) return "redirect:/login";
        feedbackService.getById(id).ifPresent(f -> {
            f.setContent(content);
            if (f instanceof AgentReview r && rating != null) r.setRating(rating);
            if (f instanceof PropertyQuestion q) feedbackService.saveQuestion(q);
            else if (f instanceof AgentReview r) feedbackService.saveReview(r);
        });
        ra.addFlashAttribute("success", "Feedback updated.");
        return "redirect:/feedback/list";
    }

    @PostMapping("/approve/{id}")
    public String approve(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (!(session.getAttribute("currentUser") instanceof Admin)) return "redirect:/login";
        feedbackService.approveFeedback(id);
        ra.addFlashAttribute("success", "Feedback approved.");
        return "redirect:/feedback/list";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id, HttpSession session, RedirectAttributes ra) {
        if (!(session.getAttribute("currentUser") instanceof Admin)) return "redirect:/login";
        feedbackService.deleteFeedback(id);
        ra.addFlashAttribute("success", "Feedback deleted.");
        return "redirect:/feedback/list";
    }
}
