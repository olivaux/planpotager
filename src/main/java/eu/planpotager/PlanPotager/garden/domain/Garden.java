package eu.planpotager.PlanPotager.garden.domain;

import eu.planpotager.PlanPotager.garden.dto.AreaDTO;
import eu.planpotager.PlanPotager.plant.domain.Plant;
import eu.planpotager.PlanPotager.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Garden")
public class Garden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_garden")
    private Long id;
    private String name;

    @Column(name = "location_longitude")
    private Double longitude;

    @Column(name = "location_latitude")
    private Double latitude;

    @ManyToOne
    @JoinColumn(name = "email")
    private User user;

    @OneToMany(mappedBy = "garden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GardenPlant> gardenPlants = new ArrayList<>();

    @OneToMany(mappedBy = "garden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Area> areas = new ArrayList<>();

    protected Garden() {
    }

    public Garden(String name, Double longitude, Double latitude, User user) {
        this.name = name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public User getUser() {
        return user;
    }

    public List<GardenPlant> getGardenPlants() {
        return gardenPlants;
    }

    public void addPlant(Plant plant, int x, int y) {
        gardenPlants.add(new GardenPlant(this, plant, x, y));
    }

    public GardenPlant findPlant(Long plantId) {
        return gardenPlants.stream()
                .filter(gardenPlant -> gardenPlant.getPlant().getId().equals(plantId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Plant not found in the garden"));
    }

    public void updatePlantPosition(Long plantId, int newX, int newY) {
        GardenPlant gardenPlant = findPlant(plantId);
        gardenPlant.setPosition(newX, newY);
    }

    public void setPlantState(Long plantId, PlantState state) {
        GardenPlant gardenPlant = findPlant(plantId);
        gardenPlant.setState(state);
    }

    public void removePlant(Long plantId) {
        GardenPlant gardenPlant = findPlant(plantId);
        gardenPlants.remove(gardenPlant);
    }

    public void addArea(AreaDTO points) {
        new Area(this, points);
    }

    public void updateArea(Long areaId, AreaDTO points) {
        Area area = findArea(areaId);
        area.setPoints(points);
    }

    private Area findArea(Long areaId) {
        return areas.stream()
                .filter(area -> area.getId().equals(areaId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Area not found in the garden"));
    }

}
