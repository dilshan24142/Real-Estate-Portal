package com.realestate.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("BUYER")
public class Buyer extends SystemUser {

    private Double budget;

    @Column(name = "preferred_locations")
    private String preferredLocations;

    @Column(name = "property_preferences")
    private String propertyPreferences;

    @Override public String getUserType() { return "Buyer"; }
    @Override public String getProfileSummary() {
        return "Property Buyer | Budget: $" + (budget != null ? String.format("%,.0f", budget) : "Not set");
    }
    @Override public String getDashboardRedirect() { return "/properties/list"; }

    public Double getBudget() { return budget; }
    public void setBudget(Double budget) { this.budget = budget; }
    public String getPreferredLocations() { return preferredLocations; }
    public void setPreferredLocations(String v) { this.preferredLocations = v; }
    public String getPropertyPreferences() { return propertyPreferences; }
    public void setPropertyPreferences(String v) { this.propertyPreferences = v; }
}
