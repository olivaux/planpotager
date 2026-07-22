package eu.planpotager.PlanPotager.registry.ui;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oidcLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import eu.planpotager.PlanPotager.config.SecurityConfig;
import eu.planpotager.PlanPotager.registry.dto.SpeciesDTO;
import eu.planpotager.PlanPotager.registry.dto.VarietyDTO;
import eu.planpotager.PlanPotager.registry.service.RegistryService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(RegistryController.class)
@Import(SecurityConfig.class)
class RegistryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegistryService registryService;

    @Test
    void getAllSpecies_shouldReturnSpeciesList_whenAuthenticated() throws Exception {
        SpeciesDTO tomato = new SpeciesDTO("Tomate", 0.3, 3, 5, 7, 9);
        when(registryService.getAllSpecies()).thenReturn(List.of(tomato));

        mockMvc.perform(get("/api/registry/species").with(oidcLogin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Tomate"))
                .andExpect(jsonPath("$[0].radius").value(0.3));
    }

    @Test
    void getAllSpecies_shouldBeRejected_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/registry/species"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void getVarietiesBySpecies_shouldReturnVarietyList_whenAuthenticated() throws Exception {
        VarietyDTO cherry = new VarietyDTO("Cerise", 0.2, 3, 5, 7, 9);
        when(registryService.getVarietiesBySpecies("Tomate")).thenReturn(List.of(cherry));

        mockMvc.perform(get("/api/registry/species/Tomate/varieties").with(oidcLogin()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Cerise"));
    }

    @Test
    void getVarietiesBySpecies_shouldBeRejected_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/api/registry/species/Tomate/varieties"))
                .andExpect(status().isUnauthorized());
    }
}
