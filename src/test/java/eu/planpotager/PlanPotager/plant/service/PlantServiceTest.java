package eu.planpotager.PlanPotager.plant.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.planpotager.PlanPotager.plant.dao.PlantDAO;
import eu.planpotager.PlanPotager.plant.domain.Plant;
import eu.planpotager.PlanPotager.plant.dto.PlantDTO;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PlantServiceTest {

    private static final String USER_EMAIL = "jane.doe@example.com";

    @Mock
    private PlantDAO plantDAO;

    @InjectMocks
    private PlantService plantService;

    @Test
    void addPlant_shouldPersistPlant_andReturnMatchingDTO() {
        when(plantDAO.save(any(Plant.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PlantDTO result = plantService.addPlant("Tomate Cerise", "Graines du Midi", USER_EMAIL);

        assertThat(result.variety()).isEqualTo("Tomate Cerise");
        assertThat(result.supplier()).isEqualTo("Graines du Midi");
        verify(plantDAO).save(any(Plant.class));
    }

    @Test
    void removePlant_shouldDeletePlant_whenItBelongsToUser() {
        Plant existing = new Plant("Tomate Cerise", "Graines du Midi", USER_EMAIL);
        when(plantDAO.findById(1L)).thenReturn(Optional.of(existing));

        plantService.removePlant(1L, USER_EMAIL);

        verify(plantDAO, times(1)).delete(existing);
    }

    @Test
    void getAvailablePlants_shouldReturnDTOsForEveryPlantOfUser() {
        Plant tomato = new Plant("Tomate Cerise", "Graines du Midi", USER_EMAIL);
        Plant carrot = new Plant("Carotte de Nantes", "Vilmorin", USER_EMAIL);
        when(plantDAO.findByUserEmail(USER_EMAIL)).thenReturn(List.of(tomato, carrot));

        List<PlantDTO> result = plantService.getAvailablePlants(USER_EMAIL);

        assertThat(result).extracting(PlantDTO::variety)
                .containsExactlyInAnyOrder("Tomate Cerise", "Carotte de Nantes");
    }

    @Test
    void getAvailablePlants_shouldReturnEmptyList_whenUserHasNoPlant() {
        when(plantDAO.findByUserEmail(USER_EMAIL)).thenReturn(List.of());

        List<PlantDTO> result = plantService.getAvailablePlants(USER_EMAIL);

        assertThat(result).isEmpty();
    }
}
