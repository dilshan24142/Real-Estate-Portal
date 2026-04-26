package com.realestate.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("RESIDENTIAL")
public class ResidentialProperty extends Property {

    private Integer bedrooms;
    private Integer bathrooms;
    private Boolean furnished = false;

    @Column(name = "parking_spaces")
    private Integer parkingSpaces;

    @Override public String getPropertyType() { return "Residential"; }

    @Override
    public Double calculateTax() {
        return getPrice() != null ? getPrice() * 0.01 : 0.0;
    }

    @Override
    public String getDisplayDetails() {
        return (bedrooms != null ? bedrooms : 0) + " Bed | "
             + (bathrooms != null ? bathrooms : 0) + " Bath | "
             + (Boolean.TRUE.equals(furnished) ? "Furnished" : "Unfurnished")
             + (parkingSpaces != null && parkingSpaces > 0 ? " | " + parkingSpaces + " Parking" : "");
    }

    public Integer getBedrooms() { return bedrooms; }
    public void setBedrooms(Integer v) { this.bedrooms = v; }
    public Integer getBathrooms() { return bathrooms; }
    public void setBathrooms(Integer v) { this.bathrooms = v; }
    public Boolean getFurnished() { return furnished; }
    public void setFurnished(Boolean v) { this.furnished = v; }
    public Integer getParkingSpaces() { return parkingSpaces; }
    public void setParkingSpaces(Integer v) { this.parkingSpaces = v; }
}
