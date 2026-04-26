package com.realestate.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("INDEPENDENT")
public class IndependentSeller extends Lister {

    @Column(name = "owner_name")
    private String ownerName;

    @Column(name = "properties_owned")
    private Integer propertiesOwned = 1;

    @Override public String getListerType() { return "Independent Seller"; }
    @Override public String getContactInfo() { return getPhone() + " | " + getEmail(); }
    @Override public boolean canApproveListings() { return false; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String v) { this.ownerName = v; }
    public Integer getPropertiesOwned() { return propertiesOwned; }
    public void setPropertiesOwned(Integer v) { this.propertiesOwned = v; }
}
