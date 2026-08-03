package eu.planpotager.PlanPotager.garden.dto;


public record AreaDTO(
        Long id,
        Double leftUpX,
        Double leftUpY,
        Double rightUpX,
        Double rightUpY,
        Double rightDownX,
        Double rightDownY,
        Double leftDownX,
        Double leftDownY) {
}
