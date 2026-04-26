package com.realestate.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("REVIEW")
public class AgentReview extends Feedback {

    @Column(name = "agent_id")
    private Long agentId;

    @Column(name = "agent_name")
    private String agentName;

    private Integer rating;

    @Column(name = "is_public")
    private Boolean isPublic = true;

    @Override public String getFeedbackType() { return "Agent Review"; }
    @Override public String getDisplayTitle() {
        return "Review for: " + (agentName != null ? agentName : "Agent #" + agentId);
    }
    @Override public String renderDisplay() {
        String stars = "★".repeat(rating != null ? rating : 0) + "☆".repeat(5 - (rating != null ? rating : 0));
        return "[REVIEW] " + stars + " - " + getContent();
    }

    public Long getAgentId() { return agentId; }
    public void setAgentId(Long v) { this.agentId = v; }
    public String getAgentName() { return agentName; }
    public void setAgentName(String v) { this.agentName = v; }
    public Integer getRating() { return rating; }
    public void setRating(Integer v) { this.rating = v; }
    public Boolean getIsPublic() { return isPublic; }
    public void setIsPublic(Boolean v) { this.isPublic = v; }
}
