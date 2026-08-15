package eu.planpotager.PlanPotager.registry.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import eu.planpotager.PlanPotager.registry.dao.SpeciesDAO;
import eu.planpotager.PlanPotager.registry.dao.VarietyDAO;
import eu.planpotager.PlanPotager.registry.domain.Family;
import eu.planpotager.PlanPotager.registry.domain.Species;
import eu.planpotager.PlanPotager.registry.domain.Type;
import eu.planpotager.PlanPotager.registry.domain.Variety;
import eu.planpotager.PlanPotager.registry.dto.SpeciesDTO;
import eu.planpotager.PlanPotager.registry.dto.VarietyDTO;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegistryServiceTest {

    @Mock
    private SpeciesDAO speciesDAO;

    @Mock
    private VarietyDAO varietyDAO;

    @InjectMocks
    private RegistryService registryService;

    @Test
    void getAllSpecies_shouldReturnDTOsForEverySpeciesInRegistry() {
        Family family = new Family("Solanaceae", new Type("Légume"));
        Species tomato = new Species("Tomate", 0.3, 3, 5, 2, family);
        Species pepper = new Species("Poivron", 0.25, 3, 5, 3, family);
        when(speciesDAO.findAll()).thenReturn(List.of(tomato, pepper));

        List<SpeciesDTO> result = registryService.getAllSpecies();

        assertThat(result).extracting(SpeciesDTO::name).containsExactlyInAnyOrder("Tomate", "Poivron");
    }

    @Test
    void getAllSpecies_shouldReturnEmptyList_whenRegistryIsEmpty() {
        when(speciesDAO.findAll()).thenReturn(List.of());

        List<SpeciesDTO> result = registryService.getAllSpecies();

        assertThat(result).isEmpty();
    }

    @Test
    void getVarietiesBySpecies_shouldReturnOnlyVarietiesOfRequestedSpecies() {
        Family family = new Family("Solanaceae", new Type("Légume"));
        Species tomato = new Species("Tomate", 0.3, 3, 5, 2, family);
        Variety cherry = new Variety("Cerise", 0.2, 3, 5, 2, tomato);
        Variety roma = new Variety("Roma", 0.3, 3, 5, 2, tomato);
        when(varietyDAO.findBySpeciesName("Tomate")).thenReturn(List.of(cherry, roma));

        List<VarietyDTO> result = registryService.getVarietiesBySpecies("Tomate");

        assertThat(result).extracting(VarietyDTO::name).containsExactlyInAnyOrder("Cerise", "Roma");
    }

    @Test
    void getVarietiesBySpecies_shouldReturnEmptyList_whenSpeciesHasNoVariety() {
        when(varietyDAO.findBySpeciesName("Inconnu")).thenReturn(List.of());

        List<VarietyDTO> result = registryService.getVarietiesBySpecies("Inconnu");

        assertThat(result).isEmpty();
    }
}
