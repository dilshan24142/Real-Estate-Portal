package com.realestate.repository;

import com.realestate.model.AgentReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface AgentReviewRepository extends JpaRepository<AgentReview, Long> {
    List<AgentReview> findByAgentId(Long agentId);
    List<AgentReview> findByIsPublic(Boolean isPublic);
    @Query("SELECT AVG(r.rating) FROM AgentReview r WHERE r.agentId = :agentId AND r.isPublic = true")
    Double findAverageRatingByAgentId(Long agentId);
}
