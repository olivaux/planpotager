package eu.planpotager.PlanPotager.user.ui;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import eu.planpotager.PlanPotager.user.dto.UserDTO;
import eu.planpotager.PlanPotager.user.service.ProfileService;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    private static final String EMAIL = "jane.doe@example.com";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProfileService profileService;

    @Test
    void me_shouldReturnCurrentUserProfile_whenAuthenticated() throws Exception {
        UserDTO userDTO = new UserDTO(EMAIL, "METRIC", "fr", "sub-123", "google");
        when(profileService.getProfile(EMAIL)).thenReturn(userDTO);

        mockMvc.perform(get("/api/auth/me")
                .with(oidcLogin().userInfoToken(token -> token.claim("email", EMAIL))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(EMAIL))
                .andExpect(jsonPath("$.unit").value("METRIC"))
                .andExpect(jsonPath("$.language").value("fr"))
                .andExpect(jsonPath("$.provider").value("google"))
                .andExpect(jsonPath("$.providerId").value("sub-123"));
    }

    @Test
    void me_shouldBeRejected_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isUnauthorized());
    }
}