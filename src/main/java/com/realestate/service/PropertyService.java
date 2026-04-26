package com.realestate.service;

import com.realestate.model.*;
import com.realestate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class PropertyService {

    @Autowired private PropertyRepository propertyRepo;
    @Autowired private ResidentialPropertyRepository residentialRepo;
    @Autowired private CommercialPropertyRepository commercialRepo;

    public Property save(Property property) { return propertyRepo.save(property); }
    public ResidentialProperty saveResidential(ResidentialProperty p) { return residentialRepo.save(p); }
    public CommercialProperty saveCommercial(CommercialProperty p) { return commercialRepo.save(p); }

    public List<Property> getAllProperties() { return propertyRepo.findAll(); }
    public List<ResidentialProperty> getAllResidential() { return residentialRepo.findAll(); }
    public List<CommercialProperty> getAllCommercial() { return commercialRepo.findAll(); }

    public Optional<Property> getPropertyById(Long id) { return propertyRepo.findById(id); }

    public List<Property> searchByLocation(String location) {
        return propertyRepo.findByLocationContainingIgnoreCase(location);
    }

    public List<Property> searchByTitle(String title) {
        return propertyRepo.findByTitleContainingIgnoreCase(title);
    }

    public List<Property> getByStatus(String status) {
        return propertyRepo.findByStatus(status);
    }

    public List<Property> getByPriceRange(Double min, Double max) {
        return propertyRepo.findByPriceBetween(min, max);
    }

    public void deleteProperty(Long id) { propertyRepo.deleteById(id); }

    public void markAsSold(Long id) {
        propertyRepo.findById(id).ifPresent(p -> {
            p.setStatus("SOLD");
            propertyRepo.save(p);
        });
    }

    public List<Property> getAvailableProperties() {
        return propertyRepo.findByStatus("AVAILABLE");
    }
}
