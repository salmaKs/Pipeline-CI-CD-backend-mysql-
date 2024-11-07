package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.PisteDTO;
import tn.esprit.spring.entities.SkierDTO;
import tn.esprit.spring.entities.Subscription;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.services.IPisteServices;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "\uD83C\uDFBF Piste Management")
@RestController
@RequestMapping("/piste")
@RequiredArgsConstructor
public class PisteRestController {

    private final IPisteServices pisteServices;

    @Operation(description = "Add Piste")
    @PostMapping("/add")
    public PisteDTO addPiste(@RequestBody PisteDTO pisteDTO) {
    Piste piste = fromDTO(pisteDTO);
        Piste savedPiste = pisteServices.addPiste(piste);
        return toDTO(savedPiste);
    }

    @Operation(description = "Retrieve all Pistes")
    @GetMapping("/all")
    public List<PisteDTO> getAllPistes() {
        List<Piste> pistes = pisteServices.retrieveAllPistes();
        return pistes.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Operation(description = "Retrieve Piste by Id")
    @GetMapping("/get/{id-piste}")
    public PisteDTO getById(@PathVariable("id-piste") Long numPiste) {
        Piste piste = pisteServices.retrievePiste(numPiste);
        return toDTO(piste);
    }

    @Operation(description = "Delete Piste by Id")
    @DeleteMapping("/delete/{id-piste}")
    public void deleteById(@PathVariable("id-piste") Long numPiste) {
        pisteServices.removePiste(numPiste);
    }

   public PisteDTO toDTO(Piste piste) {
    PisteDTO dto = new PisteDTO();
    dto.setNumPiste(piste.getNumPiste());
    dto.setNamePiste(piste.getNamePiste());
    dto.setColor(piste.getColor());
    dto.setLength(piste.getLength());
    dto.setSlope(piste.getSlope());
    
    if (piste.getSkiers() != null) {
        Set<Long> skierIds = piste.getSkiers().stream()
            .map(Skier::getNumSkier)
            .collect(Collectors.toSet());
        dto.setSkierIds(skierIds);
    }
    
    return dto;
}

public Piste fromDTO(PisteDTO pisteDTO) {
    Piste piste = new Piste();
    piste.setNumPiste(pisteDTO.getNumPiste());
    piste.setNamePiste(pisteDTO.getNamePiste());
    piste.setColor(pisteDTO.getColor());
    piste.setLength(pisteDTO.getLength());
    piste.setSlope(pisteDTO.getSlope());
    if (pisteDTO.getSkierIds() != null) {
        Set<Skier> skiers = pisteDTO.getSkierIds().stream()
            .map(id -> { 
                Skier skier = new Skier(); 
                skier.setNumSkier(id); 
                return skier;
            })
            .collect(Collectors.toSet());
        piste.setSkiers(skiers);
}
 return piste;
}

}

