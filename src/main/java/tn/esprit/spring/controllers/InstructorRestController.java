package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.entities.InstructorDTO;
import tn.esprit.spring.entities.CourseDTO;
import tn.esprit.spring.entities.Course; // Make sure this import is correct
import tn.esprit.spring.services.IInstructorServices;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "\uD83D\uDC69\u200D\uD83C\uDFEB Instructor Management")
@RestController
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorRestController {

    private final IInstructorServices instructorServices;

    @Operation(description = "Add Instructor")
    @PostMapping("/add")
    public InstructorDTO addInstructor(@RequestBody InstructorDTO instructorDTO) {
    Instructor instructor = fromDTO (instructorDTO);
        Instructor savedInstructor = instructorServices.addInstructor(instructor);
        return toDTO(savedInstructor); 
    }

    @Operation(description = "Add Instructor and Assign To Course")
    @PutMapping("/addAndAssignToCourse/{numCourse}")
    public InstructorDTO addAndAssignToInstructor(@RequestBody InstructorDTO instructorDTO, @PathVariable("numCourse") Long numCourse) {
    Instructor instructor = fromDTO (instructorDTO);
        Instructor savedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, numCourse);
        return toDTO(savedInstructor);
    }

    @Operation(description = "Retrieve all Instructors")
    @GetMapping("/all")
    public List<InstructorDTO> getAllInstructors() {
        List<Instructor> instructors = instructorServices.retrieveAllInstructors();
        return instructors.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Operation(description = "Update Instructor")
    @PutMapping("/update")
    public InstructorDTO updateInstructor(@RequestBody InstructorDTO instructorDTO) {
    Instructor instructor = fromDTO (instructorDTO);
        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);
        return toDTO(updatedInstructor); 
    }

    @Operation(description = "Retrieve Instructor by Id")
    @GetMapping("/get/{id-instructor}")
    public InstructorDTO getById(@PathVariable("id-instructor") Long numInstructor) {
        Instructor instructor = instructorServices.retrieveInstructor(numInstructor);
        return toDTO(instructor); 
    }

    public InstructorDTO toDTO(Instructor instructor) {
    if (instructor == null) {
        return null;
    }
    
    InstructorDTO dto = new InstructorDTO();
    dto.setNumInstructor(instructor.getNumInstructor());
    dto.setFirstName(instructor.getFirstName());
    dto.setLastName(instructor.getLastName());
    dto.setDateOfHire(instructor.getDateOfHire());
    if (instructor.getCourses() != null) {
        Set<CourseDTO> courseDTOs = instructor.getCourses().stream()
            .map(course -> {
                CourseDTO courseDTO = new CourseDTO();
                courseDTO.setNumCourse(course.getNumCourse());
                courseDTO.setLevel(course.getLevel());
                courseDTO.setTypeCourse(course.getTypeCourse());
                courseDTO.setSupport(course.getSupport());
                courseDTO.setPrice(course.getPrice());
                courseDTO.setTimeSlot(course.getTimeSlot()); 
                return courseDTO;
            })
            .collect(Collectors.toSet());
        dto.setCourses(courseDTOs);
    }
    
    return dto;
}

public Instructor fromDTO(InstructorDTO instructorDTO) {
    if (instructorDTO == null) {
        return null;
    }
    
    Instructor instructor = new Instructor();
    instructor.setNumInstructor(instructorDTO.getNumInstructor());
    instructor.setFirstName(instructorDTO.getFirstName());
    instructor.setLastName(instructorDTO.getLastName());
    instructor.setDateOfHire(instructorDTO.getDateOfHire());
    
    if (instructorDTO.getCourses() != null) {
        Set<Course> courses = instructorDTO.getCourses().stream()
            .map(courseDTO -> {
                Course course = new Course();
                course.setNumCourse(courseDTO.getNumCourse());
                course.setLevel(courseDTO.getLevel());
                course.setTypeCourse(courseDTO.getTypeCourse());
                course.setSupport(courseDTO.getSupport());
                course.setPrice(courseDTO.getPrice());
                course.setTimeSlot(courseDTO.getTimeSlot()); 
                return course;
            })
            .collect(Collectors.toSet());
        instructor.setCourses(courses);
    }
    
    return instructor;
}


}

