package com.realestate.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("QUESTION")
public class PropertyQuestion extends Feedback {

    @Column(name = "property_id")
    private Long propertyId;

    @Column(name = "property_title")
    private String propertyTitle;

    @Column(name = "is_answered")
    private Boolean isAnswered = false;

    @Column(name = "agent_response", length = 2000)
    private String agentResponse;

    @Override public String getFeedbackType() { return "Property Inquiry"; }
    @Override public String getDisplayTitle() {
        return "Question about: " + (propertyTitle != null ? propertyTitle : "Property #" + propertyId);
    }
    @Override public String renderDisplay() {
        return "[INQUIRY] " + getContent() + (Boolean.TRUE.equals(isAnswered) ? " [ANSWERED]" : " [PENDING]");
    }

    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long v) { this.propertyId = v; }
    public String getPropertyTitle() { return propertyTitle; }
    public void setPropertyTitle(String v) { this.propertyTitle = v; }
    public Boolean getIsAnswered() { return isAnswered; }
    public void setIsAnswered(Boolean v) { this.isAnswered = v; }
    public String getAgentResponse() { return agentResponse; }
    public void setAgentResponse(String v) { this.agentResponse = v; }
}
