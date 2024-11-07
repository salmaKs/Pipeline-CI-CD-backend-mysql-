package tn.esprit.spring;
import tn.esprit.spring.services.CourseServicesImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.repositories.ICourseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class CourseServicesImplTest {

    @InjectMocks
    private CourseServicesImpl courseServices;

    @Mock
    private ICourseRepository courseRepository;

    private Course course;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        course = new Course();
        course.setNumCourse(1L);
        // Initialize other properties of the course as needed
    }

    @Test
     void testRetrieveAllCourses() {
        List<Course> courses = new ArrayList<>();
        courses.add(course);
        
        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> result = courseServices.retrieveAllCourses();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(course.getNumCourse(), result.get(0).getNumCourse());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
     void testAddCourse() {
        when(courseRepository.save(course)).thenReturn(course);

        Course result = courseServices.addCourse(course);

        assertNotNull(result);
        assertEquals(course.getNumCourse(), result.getNumCourse());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testUpdateCourse() {
        when(courseRepository.save(course)).thenReturn(course);

        Course result = courseServices.updateCourse(course);

        assertNotNull(result);
        assertEquals(course.getNumCourse(), result.getNumCourse());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
     void testRetrieveCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course result = courseServices.retrieveCourse(1L);

        assertNotNull(result);
        assertEquals(course.getNumCourse(), result.getNumCourse());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
     void testRetrieveCourseNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Course result = courseServices.retrieveCourse(1L);

        assertNull(result);
        verify(courseRepository, times(1)).findById(1L);
    }
}

