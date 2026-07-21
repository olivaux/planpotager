package eu.planpotager.PlanPotager.registry.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Variety")
public class Variety {

    @Id
    @Column(name = "name_variety")
    private String name;
    private Double radius;

    @Column(name = "plantation_start")
    private int plantationStart;

    @Column(name = "plantation_end")
    private int plantationEnd;

    @Column(name = "harvest_start")
    private int harvestStart;

    @Column(name = "harvest_end")
    private int harvestEnd;

    @ManyToOne
    @JoinColumn(name = "name_species")
    private Species species;

    protected Variety() {
    }

    public Variety(String name, Double radius, int plantationStart, int plantationEnd, int harvestStart, int harvestEnd, Species species) {
        this.name = name;
        this.radius = radius;
        this.plantationStart = plantationStart;
        this.plantationEnd = plantationEnd;
        this.harvestStart = harvestStart;
        this.harvestEnd = harvestEnd;
        this.species = species;
    }

    public String getName() {
        return name;
    }

    public Double getRadius() {
        return radius;
    }

    public int getPlantationStart() {
        return plantationStart;
    }

    public int getPlantationEnd() {
        return plantationEnd;
    }

    public int getHarvestStart() {
        return harvestStart;
    }

    public int getHarvestEnd() {
        return harvestEnd;
    }

    public Species getSpecies() {
        return species;
    }
}
