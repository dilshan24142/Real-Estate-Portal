package com.realestate.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("AGENT")
public class Agent extends Lister {

    @Column(name = "agency_name")
    private String agencyName;

    @Column(name = "license_number")
    private String licenseNumber;

    @Override public String getListerType() { return "Agent"; }
    @Override public String getContactInfo() {
        return agencyName != null ? agencyName + " | " + getEmail() : getEmail();
    }
    @Override public boolean canApproveListings() { return true; }

    public String getAgencyName() { return agencyName; }
    public void setAgencyName(String v) { this.agencyName = v; }
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String v) { this.licenseNumber = v; }
}
