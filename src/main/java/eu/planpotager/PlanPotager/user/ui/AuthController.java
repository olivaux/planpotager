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
@RequestMapping("/api/auth/me")
public class AuthController {

    private final ProfileService profileService; 

    public AuthController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /*
     Retourne le profil de l'utilisateur actuellement connecté.
     Appelée par la SPA au chargement pour savoir si une session est active
     et récupérer les informations associées (email, unit, language...).
     */
    @GetMapping
    public ResponseEntity<UserDTO> me(@AuthenticationPrincipal OidcUser principal) {
        return ResponseEntity.ok(profileService.getProfile(principal.getEmail()));
    }

}
