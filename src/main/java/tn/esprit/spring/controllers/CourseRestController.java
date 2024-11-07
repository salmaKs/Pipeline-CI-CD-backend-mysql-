package tn.esprit.spring.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.spring.entities.CourseDTO;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.services.ICourseServices;

import java.util.List;

@Tag(name = "\uD83D\uDCDA Course Management")
@RestController
@RequestMapping("/course")
@RequiredArgsConstructor
public class CourseRestController {
    
    private final ICourseServices courseServices;

    @Operation(description = "Add Course")
    @PostMapping("/add")
    public CourseDTO addCourse(@RequestBody CourseDTO courseDTO){
        Course course = convertToEntity(courseDTO);
        Course savedCourse = courseServices.addCourse(course);
        return convertToDto(savedCourse);
    }

      @Operation(description = "Retrieve all Courses")
    @GetMapping("/all")
    public List<CourseDTO> getAllCourses(){
        List<Course> courses = courseServices.retrieveAllCourses();
        return courses.stream().map(this::convertToDto).toList();
    }

     @Operation(description = "Update Course")
    @PutMapping("/update")
    public CourseDTO updateCourse(@RequestBody CourseDTO courseDTO){
        Course course = convertToEntity(courseDTO);
        Course updatedCourse = courseServices.updateCourse(course);
        return convertToDto(updatedCourse);
    }

     @Operation(description = "Retrieve Course by Id")
    @GetMapping("/get/{id-course}")
    public CourseDTO getById(@PathVariable("id-course") Long numCourse){
        Course course = courseServices.retrieveCourse(numCourse);
        return convertToDto(course);
    }

	public CourseDTO convertToDto(Course course) {
    CourseDTO courseDTO = new CourseDTO();
    
    courseDTO.setNumCourse(course.getNumCourse());
    courseDTO.setLevel(course.getLevel());
    courseDTO.setTypeCourse(course.getTypeCourse());  
    courseDTO.setSupport(course.getSupport());      
    courseDTO.setPrice(course.getPrice());
    courseDTO.setTimeSlot(course.getTimeSlot());

    return courseDTO;
}

public Course convertToEntity(CourseDTO courseDTO) {
        Course course = new Course();
        course.setNumCourse(courseDTO.getNumCourse());
        course.setLevel(courseDTO.getLevel());
        course.setTypeCourse(courseDTO.getTypeCourse());
        course.setSupport(courseDTO.getSupport());
        course.setPrice(courseDTO.getPrice());
        course.setTimeSlot(courseDTO.getTimeSlot());
        return course;
    }

}

