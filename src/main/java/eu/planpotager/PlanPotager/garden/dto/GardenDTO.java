package eu.planpotager.PlanPotager.garden.dto;

import java.util.List;

public record GardenDTO(Long id, String name, Double longitude, Double latitude, Double score,
        List<AssociationLinkDTO> associationLinks) {

    public GardenDTO(Long id, String name, Double longitude, Double latitude) {
        this(id, name, longitude, latitude, null, List.of());
    }
}
