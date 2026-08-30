package eu.planpotager.PlanPotager.registry.service;

import eu.planpotager.PlanPotager.registry.dao.AssociationDAO;
import eu.planpotager.PlanPotager.registry.dao.SpeciesDAO;
import eu.planpotager.PlanPotager.registry.dao.VarietyDAO;
import eu.planpotager.PlanPotager.registry.domain.Species;
import eu.planpotager.PlanPotager.registry.domain.Variety;
import eu.planpotager.PlanPotager.registry.dto.AssociationDTO;
import eu.planpotager.PlanPotager.registry.dto.SpeciesDTO;
import eu.planpotager.PlanPotager.registry.dto.VarietyDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class RegistryService {

    private final SpeciesDAO speciesDAO;
    private final VarietyDAO varietyDAO;
    private final AssociationDAO associationDAO;

    public RegistryService(SpeciesDAO speciesDAO, VarietyDAO varietyDAO, AssociationDAO associationDAO) {
        this.speciesDAO = speciesDAO;
        this.varietyDAO = varietyDAO;
        this.associationDAO = associationDAO;
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
                species.getHarvestDuration());
    }

    public List<VarietyDTO> getVarietiesBySpecies(String speciesName) {
        return varietyDAO.findBySpeciesName(speciesName).stream()
                .map(this::toVarietyDTO)
                .toList();
    }

    private VarietyDTO toVarietyDTO(Variety variety) {
        return new VarietyDTO(
                variety.getName(),
                variety.getEffectiveRadius(),
                variety.getEffectivePlantationStart(),
                variety.getEffectivePlantationEnd(),
                variety.getEffectiveHarvestDuration());
    }

    public Optional<AssociationDTO> getAssociation(String speciesA, String speciesB) {
        return findAssociationOneWay(speciesA, speciesB)
                .or(() -> findAssociationOneWay(speciesB, speciesA));
    }

    private Optional<AssociationDTO> findAssociationOneWay(String speciesName, String otherSpeciesName) {
        return associationDAO.findBySpeciesName(speciesName).stream()
                .filter(association -> association.getAssociatedSpecies().getName().equals(otherSpeciesName))
                .findFirst()
                .map(association -> new AssociationDTO(speciesName, otherSpeciesName, association.isPositive()));
    }
}
