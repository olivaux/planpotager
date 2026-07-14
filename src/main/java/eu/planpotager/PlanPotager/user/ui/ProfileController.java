package eu.planpotager.PlanPotager.user.ui;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import eu.planpotager.PlanPotager.user.dto.UserDTO;
import eu.planpotager.PlanPotager.user.service.ProfileService;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ProfileService profileService; 

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }
   
    @GetMapping
    public ResponseEntity<UserDTO> getProfile(@AuthenticationPrincipal OidcUser principal) {
        return ResponseEntity.ok(profileService.getProfile(principal.getEmail()));
    }
}
