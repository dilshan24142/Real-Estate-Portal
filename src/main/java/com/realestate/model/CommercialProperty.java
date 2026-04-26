package com.realestate.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("COMMERCIAL")
public class CommercialProperty extends Property {

    @Column(name = "commercial_type")
    private String commercialType;

    @Column(name = "floor_area")
    private Double floorArea;

    private String zoning;

    @Override public String getPropertyType() { return "Commercial"; }

    @Override
    public Double calculateTax() {
        return getPrice() != null ? getPrice() * 0.02 : 0.0;
    }

    @Override
    public String getDisplayDetails() {
        return (commercialType != null ? commercialType : "Commercial")
             + (floorArea != null ? " | " + String.format("%.0f", floorArea) + " sq ft" : "")
             + (zoning != null ? " | Zone: " + zoning : "");
    }

    public String getCommercialType() { return commercialType; }
    public void setCommercialType(String v) { this.commercialType = v; }
    public Double getFloorArea() { return floorArea; }
    public void setFloorArea(Double v) { this.floorArea = v; }
    public String getZoning() { return zoning; }
    public void setZoning(String v) { this.zoning = v; }
}
