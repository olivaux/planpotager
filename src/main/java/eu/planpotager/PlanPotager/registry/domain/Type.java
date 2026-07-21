package eu.planpotager.PlanPotager.registry.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Type")
public class Type {

    @Id
    @Column(name = "name_type")
    private String name;

    protected Type() {
    }

    public Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
