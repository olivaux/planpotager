package eu.planpotager.PlanPotager.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "User_")
public class User {

    @Id
    private String email;
    private String unit = "cm";

    @Column(name = "language_")
    private String language = "fr";

    @Column(name = "provider_id")
    private String providerId;

    @Column(name = "provider")
    private String provider;

    protected User() {
    }

    public User(String email) {
        this.email = email;
    }

    // Getter
    public String getEmail() {
        return email;
    }

    public String getUnit() {
        return unit;
    }

    public String getLanguage() {
        return language;
    }

    public String getProvider() {
        return provider;
    }

    public String getProviderId() {
        return providerId;
    }

    // Setter
    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    
}
