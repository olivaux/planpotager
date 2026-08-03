package eu.planpotager.PlanPotager.garden.domain;

import eu.planpotager.PlanPotager.plant.domain.Plant;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// Divergence LDD documentee (specs/03_Conception_V1.0.md) : mappe sur la table SQL Plant (x/y remplacent position_ GEOMETRY).
@Entity
@Table(name = "Plant")
public class GardenPlant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_plant")
    private Long id;
    private int x;
    private int y;

    @Enumerated(EnumType.STRING)
    private PlantState state;

    @ManyToOne
    @JoinColumn(name = "id_seedpacket")
    private Plant plant;

    @ManyToOne 
    @JoinColumn(name = "id_garden", nullable = false) 
    private Garden garden;

    protected GardenPlant() {
    }

    public GardenPlant(Garden garden, Plant plant, int x, int y) {
        this.garden = garden;
        this.plant = plant;
        this.x = x;
        this.y = y;
        this.state = PlantState.A_PLANTER;
    }

    public Long getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public PlantState getState() {
        return state;
    }

    public void setState(PlantState state) {
        this.state = state;
    }

    public Plant getPlant() {
        return plant;
    }
}
