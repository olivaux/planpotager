package eu.planpotager.PlanPotager.plant.service;

import eu.planpotager.PlanPotager.plant.dao.PlantDAO;
import eu.planpotager.PlanPotager.plant.domain.Plant;
import eu.planpotager.PlanPotager.plant.dto.PlantDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PlantService {

    private final PlantDAO plantDAO;

    public PlantService(PlantDAO plantDAO) {
        this.plantDAO = plantDAO;
    }

    public PlantDTO addPlant(String variety, String supplier, String userEmail) {
        Plant plant = new Plant(variety, supplier, userEmail);
        
        plantDAO.save(plant);
        return new PlantDTO(plant.getId(), plant.getVariety(), plant.getSupplier());
    }

    public void removePlant(Long plantId, String userEmail) {
        Plant plant = plantDAO.findById(plantId)
                .orElseThrow(() -> new IllegalArgumentException("Plant not found"));

        if (!plant.getUserEmail().equals(userEmail)) {
            throw new IllegalArgumentException("User does not own this plant");
        }

        plantDAO.delete(plant);
    }

    public List<PlantDTO> getAvailablePlants(String userEmail) {
        return plantDAO.findByUserEmail(userEmail).stream()
                .map(plant -> new PlantDTO(plant.getId(), plant.getVariety(), plant.getSupplier()))
                .toList();
    }
}
