package com.realestate.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("RENTER")
public class Renter extends SystemUser {

    @Column(name = "monthly_budget")
    private Double monthlyBudget;

    @Column(name = "preferred_areas")
    private String preferredAreas;

    @Column(name = "move_in_date")
    private String moveInDate;

    @Override public String getUserType() { return "Renter"; }
    @Override public String getProfileSummary() {
        return "Property Renter | Monthly: $" + (monthlyBudget != null ? String.format("%,.0f", monthlyBudget) : "Not set");
    }
    @Override public String getDashboardRedirect() { return "/properties/list"; }

    public Double getMonthlyBudget() { return monthlyBudget; }
    public void setMonthlyBudget(Double v) { this.monthlyBudget = v; }
    public String getPreferredAreas() { return preferredAreas; }
    public void setPreferredAreas(String v) { this.preferredAreas = v; }
    public String getMoveInDate() { return moveInDate; }
    public void setMoveInDate(String v) { this.moveInDate = v; }
}
