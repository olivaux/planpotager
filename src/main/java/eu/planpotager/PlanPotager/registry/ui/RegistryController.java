package eu.planpotager.PlanPotager.registry.ui;

import eu.planpotager.PlanPotager.registry.dto.SpeciesDTO;
import eu.planpotager.PlanPotager.registry.dto.VarietyDTO;
import eu.planpotager.PlanPotager.registry.service.RegistryService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/registry")
public class RegistryController {

    private final RegistryService registryService;

    public RegistryController(RegistryService registryService) {
        this.registryService = registryService;
    }

    @GetMapping("/species")
    public ResponseEntity<List<SpeciesDTO>> getAllSpecies() {
        return ResponseEntity.ok(registryService.getAllSpecies());
    }

    @GetMapping("/species/{speciesName}/varieties")
    public ResponseEntity<List<VarietyDTO>> getVarietiesBySpecies(@PathVariable String speciesName) {
        return ResponseEntity.ok(registryService.getVarietiesBySpecies(speciesName));
    }
}
