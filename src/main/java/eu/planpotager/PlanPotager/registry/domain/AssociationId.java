package eu.planpotager.PlanPotager.registry.domain;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class AssociationId implements Serializable {

    private String species;
    private String associatedSpecies;

    protected AssociationId() {
    }

    public AssociationId(String species, String associatedSpecies) {
        this.species = species;
        this.associatedSpecies = associatedSpecies;
    }

    public String getSpecies() {
        return species;
    }

    public String getAssociatedSpecies() {
        return associatedSpecies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssociationId that)) {
            return false;
        }
        return Objects.equals(species, that.species) && Objects.equals(associatedSpecies, that.associatedSpecies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(species, associatedSpecies);
    }
}
