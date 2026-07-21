package eu.planpotager.PlanPotager.registry.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;

@Entity
@Table(name = "Association")
public class Association {

    @EmbeddedId
    private AssociationId id;

    @ManyToOne
    @MapsId("species")
    @JoinColumn(name = "name_species")
    private Species species;

    @ManyToOne
    @MapsId("associatedSpecies")
    @JoinColumn(name = "name_species_1")
    private Species associatedSpecies;

    @Column(name = "isPositive")
    private boolean positive;

    protected Association() {
    }

    public Association(Species species, Species associatedSpecies, boolean positive) {
        this.id = new AssociationId(species.getName(), associatedSpecies.getName());
        this.species = species;
        this.associatedSpecies = associatedSpecies;
        this.positive = positive;
    }

    public AssociationId getId() {
        return id;
    }

    public Species getSpecies() {
        return species;
    }

    public Species getAssociatedSpecies() {
        return associatedSpecies;
    }

    public boolean isPositive() {
        return positive;
    }
}
