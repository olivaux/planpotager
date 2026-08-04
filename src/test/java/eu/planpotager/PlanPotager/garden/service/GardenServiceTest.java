package eu.planpotager.PlanPotager.garden.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import eu.planpotager.PlanPotager.garden.dao.AreaDAO;
import eu.planpotager.PlanPotager.garden.dao.GardenDAO;
import eu.planpotager.PlanPotager.garden.domain.Area;
import eu.planpotager.PlanPotager.garden.domain.Garden;
import eu.planpotager.PlanPotager.garden.domain.GardenPlant;
import eu.planpotager.PlanPotager.garden.domain.PlantState;
import eu.planpotager.PlanPotager.garden.dto.AreaDTO;
import eu.planpotager.PlanPotager.garden.dto.GardenDTO;
import eu.planpotager.PlanPotager.garden.dto.GardenPlantDTO;
import eu.planpotager.PlanPotager.plant.dao.PlantDAO;
import eu.planpotager.PlanPotager.plant.domain.Plant;
import eu.planpotager.PlanPotager.user.dao.UserDAO;
import eu.planpotager.PlanPotager.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class GardenServiceTest {

    private static final String USER_EMAIL = "jane.doe@example.com";
    private static final String OTHER_USER_EMAIL = "intrus@example.com";

    @Mock
    private GardenDAO gardenDAO;

    @Mock
    private AreaDAO areaDAO;

    @Mock
    private UserDAO userDAO;

    @Mock
    private PlantDAO plantDAO;

    @InjectMocks
    private GardenService gardenService;

    @Test
    void createGarden_shouldPersistGarden_andReturnMatchingDTO() {
        User user = new User(USER_EMAIL);
        when(userDAO.findByEmail(USER_EMAIL)).thenReturn(Optional.of(user));
        when(gardenDAO.save(any(Garden.class))).thenAnswer(invocation -> invocation.getArgument(0));

        GardenDTO result = gardenService.createGarden("Potager du fond", 2.35, 48.85, USER_EMAIL);

        assertThat(result.name()).isEqualTo("Potager du fond");
        assertThat(result.longitude()).isEqualTo(2.35);
        assertThat(result.latitude()).isEqualTo(48.85);
        verify(gardenDAO).save(any(Garden.class));
    }

    @Test
    void getGardensByUser_shouldReturnDTOsForEveryGardenOfUser() {
        Garden g1 = new Garden("Potager du fond", 2.35, 48.85, null);
        Garden g2 = new Garden("Potager avant", 2.36, 48.86, null);
        when(gardenDAO.findByUserEmail(USER_EMAIL)).thenReturn(List.of(g1, g2));

        List<GardenDTO> result = gardenService.getGardensByUser(USER_EMAIL);

        assertThat(result).extracting(GardenDTO::name)
                .containsExactlyInAnyOrder("Potager du fond", "Potager avant");
    }

    @Test
    void getGardensByUser_shouldReturnEmptyList_whenUserHasNoGarden() {
        when(gardenDAO.findByUserEmail(USER_EMAIL)).thenReturn(List.of());

        List<GardenDTO> result = gardenService.getGardensByUser(USER_EMAIL);

        assertThat(result).isEmpty();
    }

    @Test
    void getGardenById_shouldReturnMatchingDTO_whenGardenExists() {
        User user = new User(USER_EMAIL);
        Garden garden = new Garden("Potager du fond", 2.35, 48.85, user);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));

        GardenDTO result = gardenService.getGardenById(USER_EMAIL, 1L);

        assertThat(result.name()).isEqualTo("Potager du fond");
    }

    @Test
    void getGardenById_shouldThrow_whenUserDoesNotOwnGarden() {
        User owner = new User(USER_EMAIL);
        Garden garden = new Garden("Potager du fond", 2.35, 48.85, owner);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));

        assertThatThrownBy(() -> gardenService.getGardenById(OTHER_USER_EMAIL, 1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateGarden_shouldUpdateNameAndCoordinates_andPersist() {
        User user = new User(USER_EMAIL);
        Garden garden = new Garden("Ancien nom", 0.0, 0.0, user);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));
        when(gardenDAO.save(garden)).thenReturn(garden);

        GardenDTO result = gardenService.updateGarden(USER_EMAIL, 1L, "Nouveau nom", 2.35, 48.85);

        assertThat(result.name()).isEqualTo("Nouveau nom");
        assertThat(result.longitude()).isEqualTo(2.35);
        assertThat(result.latitude()).isEqualTo(48.85);
        verify(gardenDAO).save(garden);
    }

    @Test
    void updateGarden_shouldThrow_whenUserDoesNotOwnGarden() {
        User owner = new User(USER_EMAIL);
        Garden garden = new Garden("Ancien nom", 0.0, 0.0, owner);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));

        assertThatThrownBy(() -> gardenService.updateGarden(OTHER_USER_EMAIL, 1L, "Nouveau nom", 2.35, 48.85))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deleteGarden_shouldDeleteGarden_whenItExists() {
        User user = new User(USER_EMAIL);
        Garden garden = new Garden("Potager du fond", 2.35, 48.85, user);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));

        gardenService.deleteGarden(1L, USER_EMAIL);

        verify(gardenDAO).delete(garden);
    }

    @Test
    void deleteGarden_shouldThrow_whenUserDoesNotOwnGarden() {
        User owner = new User(USER_EMAIL);
        Garden garden = new Garden("Potager du fond", 2.35, 48.85, owner);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));

        assertThatThrownBy(() -> gardenService.deleteGarden(1L, OTHER_USER_EMAIL))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void addPlantToGarden_shouldDelegateToGardenEntity_andPersist() {
        User user = new User(USER_EMAIL);
        Garden garden = mock(Garden.class);
        Plant plantRef = new Plant("Tomate Cerise", "Graines du Midi", USER_EMAIL);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));
        when(garden.getUser()).thenReturn(user);
        when(plantDAO.findById(42L)).thenReturn(Optional.of(plantRef));
        when(gardenDAO.save(garden)).thenReturn(garden);
        when(garden.getId()).thenReturn(1L);
        when(garden.getName()).thenReturn("Potager du fond");
        when(garden.getLongitude()).thenReturn(2.35);
        when(garden.getLatitude()).thenReturn(48.85);

        GardenDTO result = gardenService.addPlantToGarden(USER_EMAIL, 1L, 42L, 10, 20);

        verify(garden).addPlant(plantRef, 10, 20);
        verify(gardenDAO).save(garden);
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void addPlantToGarden_shouldThrow_whenUserDoesNotOwnGarden() {
        User owner = new User(USER_EMAIL);
        Garden garden = mock(Garden.class);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));
        when(garden.getUser()).thenReturn(owner);

        assertThatThrownBy(() -> gardenService.addPlantToGarden(OTHER_USER_EMAIL, 1L, 42L, 10, 20))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getPlantCurrentPosition_shouldReturnDTO_forRequestedPlant() {
        User user = new User(USER_EMAIL);
        Garden garden = mock(Garden.class);
        GardenPlant gardenPlant = mock(GardenPlant.class);
        Plant plant = new Plant("Tomate Cerise", "Graines du Midi", USER_EMAIL);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));
        when(garden.getUser()).thenReturn(user);
        when(garden.findPlant(42L)).thenReturn(gardenPlant);
        when(gardenPlant.getX()).thenReturn(10);
        when(gardenPlant.getY()).thenReturn(20);
        when(gardenPlant.getState()).thenReturn(PlantState.PLANTEE);
        when(gardenPlant.getPlant()).thenReturn(plant);

        GardenPlantDTO result = gardenService.getPlantCurrentPosition(USER_EMAIL, 1L, 42L);

        assertThat(result.x()).isEqualTo(10);
        assertThat(result.y()).isEqualTo(20);
        assertThat(result.state()).isEqualTo(PlantState.PLANTEE);
    }

    @Test
    void getPlantCurrentPosition_shouldThrow_whenUserDoesNotOwnGarden() {
        User owner = new User(USER_EMAIL);
        Garden garden = mock(Garden.class);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));
        when(garden.getUser()).thenReturn(owner);

        assertThatThrownBy(() -> gardenService.getPlantCurrentPosition(OTHER_USER_EMAIL, 1L, 42L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void changePlantPosition_shouldUpdatePositionOnEntity_andPersist() {
        User user = new User(USER_EMAIL);
        Garden garden = mock(Garden.class);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));
        when(garden.getUser()).thenReturn(user);
        when(gardenDAO.save(garden)).thenReturn(garden);
        when(garden.getId()).thenReturn(1L);

        GardenDTO result = gardenService.changePlantPosition(USER_EMAIL, 1L, 42L, 30, 40);

        verify(garden).updatePlantPosition(42L, 30, 40);
        verify(gardenDAO).save(garden);
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void changePlantPosition_shouldThrow_whenUserDoesNotOwnGarden() {
        User owner = new User(USER_EMAIL);
        Garden garden = mock(Garden.class);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));
        when(garden.getUser()).thenReturn(owner);

        assertThatThrownBy(() -> gardenService.changePlantPosition(OTHER_USER_EMAIL, 1L, 42L, 30, 40))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getAvailableStates_shouldReturnAllFourStates() {
        List<PlantState> result = gardenService.getAvailableStates();

        assertThat(result).containsExactlyInAnyOrder(
                PlantState.A_PLANTER, PlantState.PLANTEE, PlantState.A_RECOLTER, PlantState.RECOLTEE);
    }

    @Test
    void setPlantState_shouldUpdateStateOnEntity_andPersist() {
        User user = new User(USER_EMAIL);
        Garden garden = mock(Garden.class);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));
        when(garden.getUser()).thenReturn(user);
        when(gardenDAO.save(garden)).thenReturn(garden);
        when(garden.getId()).thenReturn(1L);

        GardenDTO result = gardenService.setPlantState(USER_EMAIL, 1L, 42L, PlantState.RECOLTEE);

        verify(garden).setPlantState(42L, PlantState.RECOLTEE);
        verify(gardenDAO).save(garden);
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void setPlantState_shouldThrow_whenUserDoesNotOwnGarden() {
        User owner = new User(USER_EMAIL);
        Garden garden = mock(Garden.class);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));
        when(garden.getUser()).thenReturn(owner);

        assertThatThrownBy(() -> gardenService.setPlantState(OTHER_USER_EMAIL, 1L, 42L, PlantState.RECOLTEE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void removePlantFromGarden_shouldRemovePlant_andPersist() {
        User user = new User(USER_EMAIL);
        Garden garden = mock(Garden.class);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));
        when(garden.getUser()).thenReturn(user);
        when(gardenDAO.save(garden)).thenReturn(garden);
        when(garden.getId()).thenReturn(1L);

        GardenDTO result = gardenService.removePlantFromGarden(USER_EMAIL, 1L, 42L);

        verify(garden).removePlant(42L);
        verify(gardenDAO).save(garden);
        assertThat(result.id()).isEqualTo(1L);
    }

    @Test
    void removePlantFromGarden_shouldThrow_whenUserDoesNotOwnGarden() {
        User owner = new User(USER_EMAIL);
        Garden garden = mock(Garden.class);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));
        when(garden.getUser()).thenReturn(owner);

        assertThatThrownBy(() -> gardenService.removePlantFromGarden(OTHER_USER_EMAIL, 1L, 42L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void addArea_shouldPersistArea_andReturnMatchingDTO() {
        User user = new User(USER_EMAIL);
        Garden garden = new Garden("Potager du fond", 2.35, 48.85, user);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));
        when(areaDAO.save(any(Area.class))).thenAnswer(invocation -> invocation.getArgument(0));
        AreaDTO points = new AreaDTO(null, 0.0, 10.0, 10.0, 10.0, 10.0, 0.0, 0.0, 0.0);

        AreaDTO result = gardenService.addArea(USER_EMAIL, 1L, points);

        assertThat(result.leftUpX()).isEqualTo(0.0);
        assertThat(result.rightUpX()).isEqualTo(10.0);
        verify(areaDAO).save(any(Area.class));
    }

    @Test
    void addArea_shouldThrow_whenUserDoesNotOwnGarden() {
        User owner = new User(USER_EMAIL);
        Garden garden = new Garden("Potager du fond", 2.35, 48.85, owner);
        when(gardenDAO.findById(1L)).thenReturn(Optional.of(garden));
        AreaDTO points = new AreaDTO(null, 0.0, 10.0, 10.0, 10.0, 10.0, 0.0, 0.0, 0.0);

        assertThatThrownBy(() -> gardenService.addArea(OTHER_USER_EMAIL, 1L, points))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateArea_shouldUpdatePointsOnExistingArea_andPersist() {
        User user = new User(USER_EMAIL);
        Garden garden = new Garden("Potager du fond", 2.35, 48.85, user);
        Area existing = new Area(garden, 0.0, 0.0, 10.0, 0.0, 10.0, 10.0, 0.0, 10.0);
        when(areaDAO.findById(5L)).thenReturn(Optional.of(existing));
        when(areaDAO.save(existing)).thenReturn(existing);
        AreaDTO newPoints = new AreaDTO(5L, 1.0, 1.0, 11.0, 1.0, 11.0, 11.0, 1.0, 11.0);

        AreaDTO result = gardenService.updateArea(USER_EMAIL, 1L, 5L, newPoints);

        assertThat(result.leftUpX()).isEqualTo(1.0);
        verify(areaDAO).save(existing);
    }

    @Test
    void updateArea_shouldThrow_whenUserDoesNotOwnGarden() {
        User owner = new User(USER_EMAIL);
        Garden garden = new Garden("Potager du fond", 2.35, 48.85, owner);
        Area existing = new Area(garden, 0.0, 0.0, 10.0, 0.0, 10.0, 10.0, 0.0, 10.0);
        when(areaDAO.findById(5L)).thenReturn(Optional.of(existing));
        AreaDTO newPoints = new AreaDTO(5L, 1.0, 1.0, 11.0, 1.0, 11.0, 11.0, 1.0, 11.0);

        assertThatThrownBy(() -> gardenService.updateArea(OTHER_USER_EMAIL, 1L, 5L, newPoints))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deleteArea_shouldDeleteArea_whenItExists() {
        User user = new User(USER_EMAIL);
        Garden garden = new Garden("Potager du fond", 2.35, 48.85, user);
        Area existing = new Area(garden, 0.0, 0.0, 10.0, 0.0, 10.0, 10.0, 0.0, 10.0);
        when(areaDAO.findById(5L)).thenReturn(Optional.of(existing));

        gardenService.deleteArea(USER_EMAIL, 1L, 5L);

        verify(areaDAO).delete(existing);
    }

    @Test
    void deleteArea_shouldThrow_whenUserDoesNotOwnGarden() {
        User owner = new User(USER_EMAIL);
        Garden garden = new Garden("Potager du fond", 2.35, 48.85, owner);
        Area existing = new Area(garden, 0.0, 0.0, 10.0, 0.0, 10.0, 10.0, 0.0, 10.0);
        when(areaDAO.findById(5L)).thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> gardenService.deleteArea(OTHER_USER_EMAIL, 1L, 5L))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
