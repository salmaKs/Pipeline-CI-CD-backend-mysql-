package tn.esprit.spring.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
@Slf4j
@AllArgsConstructor
@Service
public class RegistrationServicesImpl implements  IRegistrationServices{

    private IRegistrationRepository registrationRepository;
    private ISkierRepository skierRepository;
    private ICourseRepository courseRepository;


    @Override
    public Registration addRegistrationAndAssignToSkier(Registration registration, Long numSkier) {
        Skier skier = skierRepository.findById(numSkier).orElse(null);
        registration.setSkier(skier);
        return registrationRepository.save(registration);
    }

    @Override
    public Registration assignRegistrationToCourse(Long numRegistration, Long numCourse) {
        Registration registration = registrationRepository.findById(numRegistration).orElse(null);
        Course course = courseRepository.findById(numCourse).orElse(null);
        registration.setCourse(course);
        return registrationRepository.save(registration);
    }

    @Transactional
@Override
public Registration addRegistrationAndAssignToSkierAndCourse(Registration registration, Long numSkieur, Long numCours) {
    Skier skier = skierRepository.findById(numSkieur).orElse(null);
    Course course = courseRepository.findById(numCours).orElse(null);

    if (!validateEntities(skier, course, numSkieur, numCours)) {
        return null;
    }

    int ageSkieur = calculateSkierAge(skier);
    log.info("Age " + ageSkieur);

    if (isIndividualCourse(course)) {
        return assignRegistration(registration, skier, course);
    } 

    if (isChildCollectiveCourse(course, ageSkieur)) {
        return processRegistrationForCourse(registration, skier, course);
    } 

    if (isAdultEligibleForCourse(ageSkieur)) {
        return processRegistrationForCourse(registration, skier, course);
    }

    log.info("Sorry, your age doesn't allow you to register for this course.");
    return null;
}

private boolean validateEntities(Skier skier, Course course, Long numSkieur, Long numCours) {
    if (skier == null) {
        log.info("Skier with ID " + numSkieur + " not found.");
        return false;
    }
    if (course == null) {
        log.info("Course with ID " + numCours + " not found.");
        return false;
    }
    return true;
}

private int calculateSkierAge(Skier skier) {
    return Period.between(skier.getDateOfBirth(), LocalDate.now()).getYears();
}

private boolean isIndividualCourse(Course course) {
    return course.getTypeCourse() == TypeCourse.INDIVIDUAL;
}

private boolean isChildCollectiveCourse(Course course, int ageSkieur) {
    return course.getTypeCourse() == TypeCourse.COLLECTIVE_CHILDREN && ageSkieur < 16;
}

private boolean isAdultEligibleForCourse(int ageSkieur) {
    return ageSkieur >= 16;
}

private Registration processRegistrationForCourse(Registration registration, Skier skier, Course course) {
    if (registrationRepository.countByCourseAndNumWeek(course, registration.getNumWeek()) < 6) {
        log.info("Course successfully added!");
        return assignRegistration(registration, skier, course);
    } else {
        log.info("Full Course! Please choose another week to register.");
        return null;
    }
}

    private Registration assignRegistration (Registration registration, Skier skier, Course course){
        registration.setSkier(skier);
        registration.setCourse(course);
        return registrationRepository.save(registration);
    }

    @Override
    public List<Integer> numWeeksCourseOfInstructorBySupport(Long numInstructor, Support support) {
        return registrationRepository.numWeeksCourseOfInstructorBySupport(numInstructor, support);
    }

}
