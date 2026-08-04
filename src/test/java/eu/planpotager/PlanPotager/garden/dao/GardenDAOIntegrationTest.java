package eu.planpotager.PlanPotager.garden.dao;

import static org.assertj.core.api.Assertions.assertThat;

import eu.planpotager.PlanPotager.TestcontainersConfiguration;
import eu.planpotager.PlanPotager.garden.domain.Area;
import eu.planpotager.PlanPotager.garden.domain.Garden;
import eu.planpotager.PlanPotager.garden.domain.GardenPlant;
import eu.planpotager.PlanPotager.garden.domain.PlantState;
import eu.planpotager.PlanPotager.plant.domain.Plant;
import eu.planpotager.PlanPotager.registry.domain.Family;
import eu.planpotager.PlanPotager.registry.domain.Species;
import eu.planpotager.PlanPotager.registry.domain.Type;
import eu.planpotager.PlanPotager.registry.domain.Variety;
import eu.planpotager.PlanPotager.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestcontainersConfiguration.class)
class GardenDAOIntegrationTest {

    private static final String EMAIL = "jane.doe@example.com";

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GardenDAO gardenDAO;

    @Autowired
    private AreaDAO areaDAO;

    @Test
    void save_shouldPersistCascadedGardenPlant_withoutViolatingNotNullConstraintOnIdGarden() {
        User user = new User(EMAIL);
        entityManager.persist(user);
        Variety variety = persistVarietyChain("Tomate Cerise");
        Plant plant = new Plant(variety.getName(), "Graines du Midi", EMAIL);
        entityManager.persist(plant);

        Garden garden = new Garden("Potager du fond", 2.35, 48.85, user);
        garden.addPlant(plant, 10, 20);

        Garden saved = gardenDAO.save(garden);
        entityManager.flush();
        entityManager.clear();

        Garden reloaded = gardenDAO.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getGardenPlants()).hasSize(1);
        GardenPlant gardenPlant = reloaded.getGardenPlants().get(0);
        assertThat(gardenPlant.getX()).isEqualTo(10);
        assertThat(gardenPlant.getY()).isEqualTo(20);
        assertThat(gardenPlant.getState()).isEqualTo(PlantState.A_PLANTER);
    }

    @Test
    void delete_shouldCascadeDeleteGardenPlants_whenGardenIsDeleted() {
        User user = new User(EMAIL);
        entityManager.persist(user);
        Variety variety = persistVarietyChain("Tomate Cerise");
        Plant plant = new Plant(variety.getName(), "Graines du Midi", EMAIL);
        entityManager.persist(plant);

        Garden garden = new Garden("Potager du fond", 2.35, 48.85, user);
        garden.addPlant(plant, 10, 20);
        Garden saved = gardenDAO.save(garden);
        entityManager.flush();
        Long gardenPlantId = saved.getGardenPlants().get(0).getId();

        gardenDAO.delete(saved);
        entityManager.flush();

        assertThat(entityManager.find(GardenPlant.class, gardenPlantId)).isNull();
    }

    @Test
    void save_shouldPersistArea_associatedToItsGarden() {
        User user = new User(EMAIL);
        entityManager.persist(user);
        Garden garden = new Garden("Potager du fond", 2.35, 48.85, user);
        entityManager.persist(garden);

        Area area = new Area(garden, 0.0, 0.0, 10.0, 0.0, 10.0, 10.0, 0.0, 10.0);

        Area saved = areaDAO.save(area);
        entityManager.flush();
        entityManager.clear();

        Area reloaded = areaDAO.findById(saved.getId()).orElseThrow();
        assertThat(reloaded.getGarden().getId()).isEqualTo(garden.getId());
        assertThat(reloaded.getLeftUpX()).isEqualTo(0.0);
        assertThat(reloaded.getRightDownY()).isEqualTo(10.0);
    }

    private Variety persistVarietyChain(String varietyName) {
        Type type = new Type("Legume");
        entityManager.persist(type);
        Family family = new Family("Solanaceae", type);
        entityManager.persist(family);
        Species species = new Species("Tomate", 0.3, 3, 5, 2, family);
        entityManager.persist(species);
        Variety variety = new Variety(varietyName, 0.2, 3, 5, 2, species);
        entityManager.persist(variety);
        return variety;
    }
}