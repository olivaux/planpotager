package eu.planpotager.PlanPotager.garden.service;

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
import org.springframework.stereotype.Service;

@Service
public class GardenService {

    private final GardenDAO gardenDAO;
    private final AreaDAO areaDAO;
    private final UserDAO userDAO;
    private final PlantDAO plantDAO;

    public GardenService(GardenDAO gardenDAO, AreaDAO areaDAO, UserDAO userDAO, PlantDAO plantDAO) {
        this.gardenDAO = gardenDAO;
        this.areaDAO = areaDAO;
        this.userDAO = userDAO;
        this.plantDAO = plantDAO;

    }

    public GardenDTO createGarden(String name, Double longitude, Double latitude, String userEmail) {
        User user = userDAO.findByEmail(userEmail)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        Garden garden = new Garden(name, longitude, latitude, user);
        gardenDAO.save(garden);
        return new GardenDTO(garden.getId(), garden.getName(), garden.getLongitude(), garden.getLatitude());
    }

    public List<GardenDTO> getGardensByUser(String userEmail) {
        List<Garden> gardens = gardenDAO.findByUserEmail(userEmail);
        List<GardenDTO> gardenDTOs = gardens.stream()
            .map(garden -> new GardenDTO(garden.getId(), garden.getName(), garden.getLongitude(), garden.getLatitude()))
            .toList();
        return gardenDTOs;
    }

    public GardenDTO getGardenById(String userEmail, Long gardenId) {
        Garden garden = gardenDAO.findById(gardenId)
            .orElseThrow(() -> new IllegalArgumentException("Garden not found"));
        
        checkUserAccess(userEmail, garden);
        
        return new GardenDTO(garden.getId(), garden.getName(), garden.getLongitude(), garden.getLatitude());
    }

    public GardenDTO updateGarden(String userEmail, Long gardenId, String name, Double longitude, Double latitude) {
        Garden garden = gardenDAO.findById(gardenId)
            .orElseThrow(() -> new IllegalArgumentException("Garden not found"));
        
        checkUserAccess(userEmail, garden);
        
        garden.setName(name);
        garden.setLongitude(longitude);
        garden.setLatitude(latitude);
        gardenDAO.save(garden);
        
        return new GardenDTO(garden.getId(), garden.getName(), garden.getLongitude(), garden.getLatitude());
    }

    public void deleteGarden(Long gardenId, String userEmail) {
        Garden garden = gardenDAO.findById(gardenId)
            .orElseThrow(() -> new IllegalArgumentException("Garden not found"));
        
        checkUserAccess(userEmail, garden);
        
        gardenDAO.delete(garden);
    }

    public GardenPlantDTO addPlantToGarden(String userEmail, Long gardenId, Long plantId, int x, int y) {
        Garden garden = gardenDAO.findById(gardenId)
            .orElseThrow(() -> new IllegalArgumentException("Garden not found"));

        checkUserAccess(userEmail, garden);

        Plant plant = plantDAO.findById(plantId)
            .orElseThrow(() -> new IllegalArgumentException("Plant not found"));

        GardenPlant gardenPlant = garden.addPlant(plant, x, y);
        gardenDAO.save(garden);

        return toGardenPlantDTO(gardenPlant);
    }

    public GardenPlantDTO getPlantCurrentPosition(String userEmail, Long gardenId, Long gardenPlantId) {
        Garden garden = gardenDAO.findById(gardenId)
            .orElseThrow(() -> new IllegalArgumentException("Garden not found"));

        checkUserAccess(userEmail, garden);

        GardenPlant gardenPlant = garden.findGardenPlant(gardenPlantId);
        return toGardenPlantDTO(gardenPlant);
    }

    public GardenDTO changePlantPosition(String userEmail, Long gardenId, Long gardenPlantId, int newX, int newY) {
        Garden garden = gardenDAO.findById(gardenId)
            .orElseThrow(() -> new IllegalArgumentException("Garden not found"));

        checkUserAccess(userEmail, garden);

        garden.updatePlantPosition(gardenPlantId, newX, newY);
        gardenDAO.save(garden);

        return new GardenDTO(garden.getId(), garden.getName(), garden.getLongitude(), garden.getLatitude());
    }

    public List<PlantState> getAvailableStates() {
        return List.of(PlantState.values());
    }

    public List<GardenPlantDTO> getGardenPlants(String userEmail, Long gardenId) {
        Garden garden = gardenDAO.findById(gardenId)
            .orElseThrow(() -> new IllegalArgumentException("Garden not found"));

        checkUserAccess(userEmail, garden);

        return garden.getGardenPlants().stream()
            .map(this::toGardenPlantDTO)
            .toList();
    }

    public List<AreaDTO> getGardenAreas(String userEmail, Long gardenId) {
        Garden garden = gardenDAO.findById(gardenId)
            .orElseThrow(() -> new IllegalArgumentException("Garden not found"));

        checkUserAccess(userEmail, garden);

        return areaDAO.findByGardenId(gardenId).stream()
            .map(this::toAreaDTO)
            .toList();
    }

    public GardenDTO setPlantState(String userEmail, Long gardenId, Long gardenPlantId, PlantState state) {
        Garden garden = gardenDAO.findById(gardenId)
            .orElseThrow(() -> new IllegalArgumentException("Garden not found"));

        checkUserAccess(userEmail, garden);

        garden.setPlantState(gardenPlantId, state);
        gardenDAO.save(garden);
        return new GardenDTO(garden.getId(), garden.getName(), garden.getLongitude(), garden.getLatitude());
    }

    public GardenDTO removePlantFromGarden(String userEmail, Long gardenId, Long gardenPlantId) {
        Garden garden = gardenDAO.findById(gardenId)
            .orElseThrow(() -> new IllegalArgumentException("Garden not found"));

        checkUserAccess(userEmail, garden);

        garden.removePlant(gardenPlantId);
        gardenDAO.save(garden);
        return new GardenDTO(garden.getId(), garden.getName(), garden.getLongitude(), garden.getLatitude());
    }

    public AreaDTO addArea(String userEmail, Long gardenId, AreaDTO points) {
        Garden garden = gardenDAO.findById(gardenId)
            .orElseThrow(() -> new IllegalArgumentException("Garden not found"));

        checkUserAccess(userEmail, garden);

        Area area = new Area(garden, points);
        areaDAO.save(area);

        return toAreaDTO(area);
    }

    public AreaDTO updateArea(String userEmail, Long gardenId, Long areaId, AreaDTO points) {
        Area existing = areaDAO.findById(areaId)
            .orElseThrow(() -> new IllegalArgumentException("Area not found"));

        checkUserAccess(userEmail, existing.getGarden());

        existing.setPoints(points);
        areaDAO.save(existing);

        return toAreaDTO(existing);
    }

    public void deleteArea(String userEmail, Long gardenId, Long areaId) {
        Area existing = areaDAO.findById(areaId)
            .orElseThrow(() -> new IllegalArgumentException("Area not found"));

        checkUserAccess(userEmail, existing.getGarden());

        areaDAO.delete(existing);
    }

    private AreaDTO toAreaDTO(Area area) {
        return new AreaDTO(area.getId(), area.getLeftUpX(), area.getLeftUpY(), area.getRightUpX(), area.getRightUpY(),
                area.getRightDownX(), area.getRightDownY(), area.getLeftDownX(), area.getLeftDownY());
    }

    private GardenPlantDTO toGardenPlantDTO(GardenPlant gardenPlant) {
        return new GardenPlantDTO(gardenPlant.getId(), gardenPlant.getX(), gardenPlant.getY(),
                gardenPlant.getState(), gardenPlant.getPlant().getId());
    }

    private void checkUserAccess(String userEmail, Garden garden) {
        if (!garden.getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("User does not have access to this garden");
        }
    }

    
}
