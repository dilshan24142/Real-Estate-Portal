package com.realestate.service;

import com.realestate.model.*;
import com.realestate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class FeedbackService {

    @Autowired private FeedbackRepository feedbackRepo;
    @Autowired private PropertyQuestionRepository questionRepo;
    @Autowired private AgentReviewRepository reviewRepo;

    public PropertyQuestion saveQuestion(PropertyQuestion q) { return questionRepo.save(q); }
    public AgentReview saveReview(AgentReview r) { return reviewRepo.save(r); }

    public List<Feedback> getAllFeedback() { return feedbackRepo.findAll(); }
    public List<PropertyQuestion> getAllQuestions() { return questionRepo.findAll(); }
    public List<AgentReview> getAllReviews() { return reviewRepo.findAll(); }

    public List<PropertyQuestion> getQuestionsByProperty(Long propertyId) {
        return questionRepo.findByPropertyId(propertyId);
    }
    public List<AgentReview> getReviewsByAgent(Long agentId) {
        return reviewRepo.findByAgentId(agentId);
    }
    public Double getAgentAverageRating(Long agentId) {
        return reviewRepo.findAverageRatingByAgentId(agentId);
    }

    public Optional<Feedback> getById(Long id) { return feedbackRepo.findById(id); }
    public Optional<PropertyQuestion> getQuestionById(Long id) { return questionRepo.findById(id); }
    public Optional<AgentReview> getReviewById(Long id) { return reviewRepo.findById(id); }

    public void approveFeedback(Long id) {
        feedbackRepo.findById(id).ifPresent(f -> {
            f.setIsApproved(true);
            feedbackRepo.save(f);
        });
    }

    public void deleteFeedback(Long id) { feedbackRepo.deleteById(id); }

    public List<Feedback> getPendingFeedback() {
        return feedbackRepo.findByIsApproved(false);
    }
}
