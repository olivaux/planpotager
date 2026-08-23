package eu.planpotager.PlanPotager.plant.domain;

import eu.planpotager.PlanPotager.registry.domain.Variety;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

// Divergence LDD documentee (specs/03_Conception_V1.0.md) : mappe sur la table SQL SeedPacket, pas Plant.
@Entity
@Table(name = "SeedPacket")
public class Plant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seedpacket")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "name_variety")
    private Variety variety;

    @Column(name = "brand")
    private String supplier;

    @Column(name = "email")
    private String userEmail;

    protected Plant() {
    }

    public Plant(Variety variety, String supplier, String userEmail) {
        this.variety = variety;
        this.supplier = supplier;
        this.userEmail = userEmail;
    }

    public Long getId() {
        return id;
    }

    public Variety getVariety() {
        return variety;
    }

    public String getSupplier() {
        return supplier;
    }

    public String getUserEmail() {
        return userEmail;
    }
}
