package eu.planpotager.PlanPotager.registry.dto;

public record VarietyDTO(
        String name,
        Double radius,
        int plantationStart,
        int plantationEnd,
        int harvestDuration) {

}
