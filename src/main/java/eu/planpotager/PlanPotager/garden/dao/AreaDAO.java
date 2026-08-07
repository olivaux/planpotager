package eu.planpotager.PlanPotager.garden.dao;

import eu.planpotager.PlanPotager.garden.domain.Area;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaDAO extends JpaRepository<Area, Long> {

    List<Area> findByGardenId(Long gardenId);
}
