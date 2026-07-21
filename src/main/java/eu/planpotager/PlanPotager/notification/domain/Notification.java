package eu.planpotager.PlanPotager.notification.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "Notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notif")
    private Long id;
    private String message;
    private String type;

    @Column(name = "isRead")
    private Boolean isRead;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "email")
    private String userEmail;

    protected Notification() {
    }

    public Notification(String message, String type, LocalDateTime createdAt, String userEmail) {
        this.message = message;
        this.type = type;
        this.createdAt = createdAt;
        this.userEmail = userEmail;
        this.isRead = false;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        this.isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
