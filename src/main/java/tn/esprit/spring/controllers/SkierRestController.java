package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Subscription;

import tn.esprit.spring.entities.SkierDTO;
import tn.esprit.spring.entities.TypeSubscription;
import tn.esprit.spring.services.ISkierServices;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "\uD83C\uDFC2 Skier Management")
@RestController
@RequestMapping("/skier")
@RequiredArgsConstructor
public class SkierRestController {

    private final ISkierServices skierServices;

    @Operation(description = "Add Skier")
    @PostMapping("/add")
    public SkierDTO addSkier(@RequestBody SkierDTO skierDTO){
    	Skier skier = fromDTO(skierDTO);
        Skier savedSkier = skierServices.addSkier(skier);
        return toDTO(savedSkier);
    }

    @Operation(description = "Add Skier And Assign To Course")
    @PostMapping("/addAndAssign/{numCourse}")
    public SkierDTO addSkierAndAssignToCourse(@RequestBody SkierDTO skierDTO,
                                           @PathVariable("numCourse") Long numCourse){
                                           Skier skier = fromDTO(skierDTO);
        Skier savedSkier = skierServices.addSkierAndAssignToCourse(skier, numCourse);
        return toDTO(savedSkier);
    }
    
   @Operation(description = "Assign Skier To Subscription")
    @PutMapping("/assignToSub/{numSkier}/{numSub}")
    public SkierDTO assignToSubscription(@PathVariable("numSkier") Long numSkier,
                                         @PathVariable("numSub") Long numSub) {
        Skier skier = skierServices.assignSkierToSubscription(numSkier, numSub);
        return toDTO(skier);
    }
    

     @Operation(description = "Assign Skier To Piste")
    @PutMapping("/assignToPiste/{numSkier}/{numPiste}")
    public SkierDTO assignToPiste(@PathVariable("numSkier") Long numSkier,
                                   @PathVariable("numPiste") Long numPiste) {
        Skier skier = skierServices.assignSkierToPiste(numSkier, numPiste);
        return toDTO(skier);
    }
    
     @Operation(description = "Retrieve Skiers By Subscription Type")
    @GetMapping("/getSkiersBySubscription")
    public List<SkierDTO> retrieveSkiersBySubscriptionType(TypeSubscription typeSubscription) {
        List<Skier> skiers = skierServices.retrieveSkiersBySubscriptionType(typeSubscription);
        return skiers.stream().map(this::toDTO).collect(Collectors.toList());
    }
    
    @Operation(description = "Retrieve Skier by Id")
    @GetMapping("/get/{id-skier}")
    public SkierDTO getById(@PathVariable("id-skier") Long numSkier) {
        Skier skier = skierServices.retrieveSkier(numSkier);
        return toDTO(skier);
    }

    @Operation(description = "Delete Skier by Id")
    @DeleteMapping("/delete/{id-skier}")
    public void deleteById(@PathVariable("id-skier") Long numSkier){
        skierServices.removeSkier(numSkier);
    }

    @Operation(description = "Retrieve all Skiers")
    @GetMapping("/all")
    public List<SkierDTO> getAllSkiers() {
        List<Skier> skiers = skierServices.retrieveAllSkiers();
        return skiers.stream().map(this::toDTO).collect(Collectors.toList());
    }

	// Convert Skier entity to SkierDTO
public SkierDTO toDTO(Skier skier) {
    if (skier == null) {
        return null;
    }
    SkierDTO dto = new SkierDTO();
    dto.setNumSkier(skier.getNumSkier());
    dto.setFirstName(skier.getFirstName());
    dto.setLastName(skier.getLastName());
    dto.setDateOfBirth(skier.getDateOfBirth());
    dto.setCity(skier.getCity());
    dto.setSubscriptionId(skier.getSubscription() != null ? skier.getSubscription().getNumSub() : null);
    dto.setPisteIds(skier.getPistes() != null ? skier.getPistes().stream().map(Piste::getNumPiste).collect(Collectors.toSet()) : null);
    dto.setRegistrationIds(skier.getRegistrations() != null ? skier.getRegistrations().stream().map(Registration::getNumRegistration).collect(Collectors.toSet()) : null);
    return dto;
}

public Skier fromDTO(SkierDTO skierDTO) {
    if (skierDTO == null) {
        return null;
    }
    Skier skier = new Skier();
    skier.setNumSkier(skierDTO.getNumSkier());
    skier.setFirstName(skierDTO.getFirstName());
    skier.setLastName(skierDTO.getLastName());
    skier.setDateOfBirth(skierDTO.getDateOfBirth());
    skier.setCity(skierDTO.getCity());
   
    if (skierDTO.getSubscriptionId() != null) {
        Subscription subscription = new Subscription();
        subscription.setNumSub(skierDTO.getSubscriptionId());
        skier.setSubscription(subscription);
    }
    if (skierDTO.getPisteIds() != null) {
        Set<Piste> pistes = skierDTO.getPisteIds().stream().map(id -> {
            Piste piste = new Piste();
            piste.setNumPiste(id);
            return piste;
        }).collect(Collectors.toSet());
        skier.setPistes(pistes);
    }
    
    if (skierDTO.getRegistrationIds() != null) {
        Set<Registration> registrations = skierDTO.getRegistrationIds().stream().map(id -> {
            Registration registration = new Registration();
            registration.setNumRegistration(id);
            return registration;
        }).collect(Collectors.toSet());
        skier.setRegistrations(registrations);
    }

    return skier;
}

}
