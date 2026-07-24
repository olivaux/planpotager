package eu.planpotager.PlanPotager.plant.dao;

import eu.planpotager.PlanPotager.plant.domain.Plant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantDAO extends JpaRepository<Plant, Long> {

    List<Plant> findByUserEmail(String userEmail);
}
