package eu.planpotager.PlanPotager.garden.dto;

import eu.planpotager.PlanPotager.garden.domain.PlantState;

public record SetStateRequest(PlantState state) {

}