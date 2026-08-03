package eu.planpotager.PlanPotager.garden.dto;

import eu.planpotager.PlanPotager.garden.domain.PlantState;


public record GardenPlantDTO(Long id, int x, int y, PlantState state, Long plantId) {

    

}
