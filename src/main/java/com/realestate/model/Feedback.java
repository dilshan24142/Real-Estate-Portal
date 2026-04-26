package com.realestate.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "feedback_type", discriminatorType = DiscriminatorType.STRING)
public abstract class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "user_name")
    private String userName;

    @Column(nullable = false, length = 2000)
    private String content;

    @Column(name = "is_approved")
    private Boolean isApproved = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public abstract String getFeedbackType();
    public abstract String getDisplayTitle();
    public abstract String renderDisplay();

    @PrePersist
    protected void onCreate() { createdAt = LocalDateTime.now(); }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long v) { this.userId = v; }
    public String getUserName() { return userName; }
    public void setUserName(String v) { this.userName = v; }
    public String getContent() { return content; }
    public void setContent(String v) { this.content = v; }
    public Boolean getIsApproved() { return isApproved; }
    public void setIsApproved(Boolean v) { this.isApproved = v; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
