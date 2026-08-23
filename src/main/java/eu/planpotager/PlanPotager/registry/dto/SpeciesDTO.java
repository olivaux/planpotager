package eu.planpotager.PlanPotager.registry.dto;

public record SpeciesDTO(
        String name,
        Double radius,
        int plantationStart,
        int plantationEnd,
        int harvestDuration) {

}
