package com.realestate.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "property_id", nullable = false)
    private Long propertyId;

    @Column(name = "property_title")
    private String propertyTitle;

    @Column(name = "lister_id")
    private Long listerId;

    @Column(name = "lister_name")
    private String listerName;

    @Column(name = "appointment_date", nullable = false)
    private String appointmentDate;

    @Column(name = "appointment_time", nullable = false)
    private String appointmentTime;

    private String status = "PENDING";

    @Column(name = "viewing_type")
    private String viewingType = "IN_PERSON";

    private String notes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long v) { this.userId = v; }
    public String getUserName() { return userName; }
    public void setUserName(String v) { this.userName = v; }
    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long v) { this.propertyId = v; }
    public String getPropertyTitle() { return propertyTitle; }
    public void setPropertyTitle(String v) { this.propertyTitle = v; }
    public Long getListerId() { return listerId; }
    public void setListerId(Long v) { this.listerId = v; }
    public String getListerName() { return listerName; }
    public void setListerName(String v) { this.listerName = v; }
    public String getAppointmentDate() { return appointmentDate; }
    public void setAppointmentDate(String v) { this.appointmentDate = v; }
    public String getAppointmentTime() { return appointmentTime; }
    public void setAppointmentTime(String v) { this.appointmentTime = v; }
    public String getStatus() { return status; }
    public void setStatus(String v) { this.status = v; }
    public String getViewingType() { return viewingType; }
    public void setViewingType(String v) { this.viewingType = v; }
    public String getNotes() { return notes; }
    public void setNotes(String v) { this.notes = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
