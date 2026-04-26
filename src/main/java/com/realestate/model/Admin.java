package com.realestate.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends SystemUser {

    private String permissions;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Override public String getUserType() { return "Admin"; }
    @Override public String getProfileSummary() {
        return "System Administrator | Permissions: " + (permissions != null ? permissions : "Standard");
    }
    @Override public String getDashboardRedirect() { return "/admin/dashboard"; }

    public String getPermissions() { return permissions; }
    public void setPermissions(String v) { this.permissions = v; }
    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime v) { this.lastLogin = v; }
}
