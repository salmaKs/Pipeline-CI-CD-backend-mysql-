package tn.esprit.spring;
import tn.esprit.spring.services.InstructorServicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class InstructorServicesImplTest {

    @InjectMocks
    private InstructorServicesImpl instructorServices;

    @Mock
    private IInstructorRepository instructorRepository;

    @Mock
    private ICourseRepository courseRepository;

    private Instructor instructor;
    private Course course;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        instructor = new Instructor();
        instructor.setNumInstructor(1L);
        instructor.setFirstName("John");
        instructor.setLastName("Doe");

        course = new Course();
        course.setNumCourse(1L);
    }

    @Test
    void testAddInstructor() {
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor savedInstructor = instructorServices.addInstructor(instructor);

        assertNotNull(savedInstructor);
        assertEquals(instructor.getNumInstructor(), savedInstructor.getNumInstructor());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveAllInstructors() {
        List<Instructor> instructors = new ArrayList<>();
        instructors.add(instructor);

        when(instructorRepository.findAll()).thenReturn(instructors);

        List<Instructor> result = instructorServices.retrieveAllInstructors();

        assertEquals(1, result.size());
        assertEquals(instructor.getNumInstructor(), result.get(0).getNumInstructor());
        verify(instructorRepository, times(1)).findAll();
    }

    @Test
    void testUpdateInstructor() {
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor updatedInstructor = instructorServices.updateInstructor(instructor);

        assertNotNull(updatedInstructor);
        assertEquals(instructor.getNumInstructor(), updatedInstructor.getNumInstructor());
        verify(instructorRepository, times(1)).save(instructor);
    }

    @Test
    void testRetrieveInstructor() {
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.of(instructor));

        Instructor foundInstructor = instructorServices.retrieveInstructor(instructor.getNumInstructor());

        assertNotNull(foundInstructor);
        assertEquals(instructor.getNumInstructor(), foundInstructor.getNumInstructor());
        verify(instructorRepository, times(1)).findById(instructor.getNumInstructor());
    }

    @Test
    void testRetrieveInstructor_NotFound() {
        when(instructorRepository.findById(anyLong())).thenReturn(Optional.empty());

        Instructor foundInstructor = instructorServices.retrieveInstructor(99L);

        assertNull(foundInstructor);
        verify(instructorRepository, times(1)).findById(99L);
    }

    @Test
    void testAddInstructorAndAssignToCourse() {
        when(courseRepository.findById(anyLong())).thenReturn(Optional.of(course));
        when(instructorRepository.save(any(Instructor.class))).thenReturn(instructor);

        Instructor savedInstructor = instructorServices.addInstructorAndAssignToCourse(instructor, course.getNumCourse());

        assertNotNull(savedInstructor);
        assertTrue(savedInstructor.getCourses().contains(course));
        verify(courseRepository, times(1)).findById(course.getNumCourse());
        verify(instructorRepository, times(1)).save(savedInstructor);
    }
}

