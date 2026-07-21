package eu.planpotager.PlanPotager.registry.service;

import eu.planpotager.PlanPotager.registry.dao.SpeciesDAO;
import eu.planpotager.PlanPotager.registry.dao.VarietyDAO;
import eu.planpotager.PlanPotager.registry.domain.Species;
import eu.planpotager.PlanPotager.registry.domain.Variety;
import eu.planpotager.PlanPotager.registry.dto.SpeciesDTO;
import eu.planpotager.PlanPotager.registry.dto.VarietyDTO;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RegistryService {

    private final SpeciesDAO speciesDAO;
    private final VarietyDAO varietyDAO;

    public RegistryService(SpeciesDAO speciesDAO, VarietyDAO varietyDAO) {
        this.speciesDAO = speciesDAO;
        this.varietyDAO = varietyDAO;
    }

    public List<SpeciesDTO> getAllSpecies() {
        return speciesDAO.findAll().stream()
                .map(this::toSpeciesDTO)
                .toList();
    }

    private SpeciesDTO toSpeciesDTO(Species species) {
        return new SpeciesDTO(
                species.getName(),
                species.getRadius(),
                species.getPlantationStart(),
                species.getPlantationEnd(),
                species.getHarvestStart(),
                species.getHarvestEnd());
    }

    public List<VarietyDTO> getVarietiesBySpecies(String speciesName) {
        return varietyDAO.findBySpeciesName(speciesName).stream()
                .map(this::toVarietyDTO)
                .toList();
    }

    private VarietyDTO toVarietyDTO(Variety variety) {
        return new VarietyDTO(
                variety.getName(),
                variety.getRadius(),
                variety.getPlantationStart(),
                variety.getPlantationEnd(),
                variety.getHarvestStart(),
                variety.getHarvestEnd());
    }
}
