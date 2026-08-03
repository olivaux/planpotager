package eu.planpotager.PlanPotager.garden.dao;

import eu.planpotager.PlanPotager.garden.domain.Garden;
import eu.planpotager.PlanPotager.garden.domain.GardenPlant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GardenDAO extends JpaRepository<Garden, Long> {

    List<Garden> findByUserEmail(String userEmail);

    @org.springframework.data.jpa.repository.Query("select gp from Garden g join g.gardenPlants gp")
    List<GardenPlant> findAllGardenPlants();
}
