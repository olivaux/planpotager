package eu.planpotager.PlanPotager.user.dto;

public class UserDTO {
    private String email;
    private String unit;
    private String language;
    private String providerId;
    private String provider;

    public UserDTO(String email, String unit, String language, String providerId, String provider) {
        this.email = email;
        this.unit = unit;
        this.language = language;
        this.providerId = providerId;
        this.provider = provider;
    }

    // Getters
    public String getEmail() {
        return email;
    }

    public String getUnit() {
        return unit;
    }

    public String getLanguage() {
        return language;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getProvider() {
        return provider;
    }
}

