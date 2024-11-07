package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.services.IRegistrationServices;

import java.util.List;

@Tag(name = "\uD83D\uDDD3️Registration Management")
@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationRestController {

    private final IRegistrationServices registrationServices;

    @Operation(description = "Add Registration and Assign to Skier")
    @PutMapping("/addAndAssignToSkier/{numSkieur}")
    public RegistrationDTO addAndAssignToSkier(@RequestBody RegistrationDTO registrationDTO,
                                               @PathVariable("numSkieur") Long numSkieur) {
        Registration registration = fromDTO(registrationDTO);
        registration.setNumWeek(registrationDTO.getNumWeek());
        Registration savedRegistration = registrationServices.addRegistrationAndAssignToSkier(registration, numSkieur);
        return toDTO(savedRegistration);
    }

    @Operation(description = "Assign Registration to Course")
    @PutMapping("/assignToCourse/{numRegis}/{numCourse}")
    public RegistrationDTO assignToCourse(@PathVariable("numRegis") Long numRegistration,
                                          @PathVariable("numCourse") Long numCourse) {
        Registration registration = registrationServices.assignRegistrationToCourse(numRegistration, numCourse);
        return toDTO(registration);
    }

    @Operation(description = "Add Registration and Assign to Skier and Course")
    @PutMapping("/addAndAssignToSkierAndCourse/{numSkieur}/{numCourse}")
    public RegistrationDTO addAndAssignToSkierAndCourse(@RequestBody RegistrationDTO registrationDTO,
                                                        @PathVariable("numSkieur") Long numSkieur,
                                                        @PathVariable("numCourse") Long numCourse) {
        Registration registration = fromDTO(registrationDTO);
        registration.setNumWeek(registrationDTO.getNumWeek());
        Registration savedRegistration = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, numSkieur, numCourse);
        return toDTO(savedRegistration);
    }

    @Operation(description = "Numbers of the weeks when an instructor has given lessons in a given support")
    @GetMapping("/numWeeks/{numInstructor}/{support}")
    public List<Integer> numWeeksCourseOfInstructorBySupport(@PathVariable("numInstructor") Long numInstructor,
                                                             @PathVariable("support") Support support) {
        return registrationServices.numWeeksCourseOfInstructorBySupport(numInstructor, support);
    }

    // Convert Registration entity to DTO
    public RegistrationDTO toDTO(Registration registration) {
        if (registration == null) {
            return null;
        }
        RegistrationDTO dto = new RegistrationDTO();
        dto.setNumRegistration(registration.getNumRegistration());
        dto.setNumWeek(registration.getNumWeek());
        // Add skierId and courseId to the DTO from the entities
        dto.setSkierId(registration.getSkier() != null ? registration.getSkier().getNumSkier() : null);
        dto.setCourseId(registration.getCourse() != null ? registration.getCourse().getNumCourse() : null);
        return dto;
    }

    // Convert DTO to Registration entity
    public Registration fromDTO(RegistrationDTO registrationDTO) {
        if (registrationDTO == null) {
            return null;
        }
        Registration registration = new Registration();
        registration.setNumRegistration(registrationDTO.getNumRegistration());
        registration.setNumWeek(registrationDTO.getNumWeek());

        // Assuming you have services to retrieve Skier and Course entities by their IDs
        if (registrationDTO.getSkierId() != null) {
            Skier skier = new Skier(); // Retrieve the Skier entity here if needed
            skier.setNumSkier(registrationDTO.getSkierId());
            registration.setSkier(skier);
        }

        if (registrationDTO.getCourseId() != null) {
            Course course = new Course(); // Retrieve the Course entity here if needed
            course.setNumCourse(registrationDTO.getCourseId());
            registration.setCourse(course);
        }

        return registration;
    }
}

