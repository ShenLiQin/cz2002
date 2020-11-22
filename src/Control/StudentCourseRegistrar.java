package Control;

import DataAccessObject.ICourseDataAccessObject;
import DataAccessObject.IRegistrationDataAccessObject;
import DataAccessObject.IUserDataAccessObject;
import Helper.Factory;
import ValueObject.Course;
import ValueObject.Index;
import ValueObject.RegistrationKey;
import ValueObject.DayOfWeek;
import Exception.*;
import ValueObject.Student;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class StudentCourseRegistrar {

    public void addRegistration(String matricNumber, String courseCode, int indexNumber) throws IOException, ClassNotFoundException, InvalidAccessPeriodException, InsufficientAUsException, ExistingCourseException, ExistingUserException, ExistingRegistrationException, NonExistentUserException, NonExistentCourseException, NonExistentIndexException, MaxEnrolledStudentsException, ClashingTimeTableException {
        IUserDataAccessObject userDataAccess = Factory.getTextUserDataAccess();
        ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
        IRegistrationDataAccessObject registrationDataAccess = Factory.getTextRegistrationDataAccess();

        LocalDateTime startDate = registrationDataAccess.getRegistrationPeriod().getStartDate();
        LocalDateTime endDate = registrationDataAccess.getRegistrationPeriod().getEndDate();
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startDate) || now.isAfter(endDate)) {
            throw new InvalidAccessPeriodException();
        }
        RegistrationKey registrationKey = Factory.createRegistrationKey(matricNumber, courseCode, indexNumber);
        Student student = userDataAccess.getStudent(matricNumber);
        Course course = courseDataAccessObject.getCourse(courseCode);
        if (student.getMaxAUs() - student.getTotalRegisteredAUs() < course.getAUs()) {
            throw new InsufficientAUsException();
        }
        for (String registeredCourseCode : student.getRegisteredCourses().keySet()) {
            int registeredCourseIndexNumber = student.getRegisteredCourses().get(registeredCourseCode);
            Hashtable<DayOfWeek, List<LocalTime>> registeredIndexLaboratoryTimings = courseDataAccessObject.getCourse(registeredCourseCode)
                    .getIndex(registeredCourseIndexNumber).getLaboratoryTimings();
            Hashtable<DayOfWeek, List<LocalTime>> registeredIndexTutorialTimings = courseDataAccessObject.getCourse(registeredCourseCode)
                    .getIndex(registeredCourseIndexNumber).getTutorialTimings();
            Hashtable<DayOfWeek, List<LocalTime>> newIndexLaboratoryTimings = courseDataAccessObject.getCourse(courseCode)
                    .getIndex(indexNumber).getLaboratoryTimings();
            Hashtable<DayOfWeek, List<LocalTime>> newIndexTutorialTimings = courseDataAccessObject.getCourse(courseCode)
                    .getIndex(indexNumber).getTutorialTimings();
            // check for overlapping lectures
            Hashtable<DayOfWeek, List<LocalTime>> registeredCourseLectureTimings = courseDataAccessObject.getCourse(registeredCourseCode)
                    .getLectureTimings();
            Hashtable<DayOfWeek, List<LocalTime>> newCourseLectureTimings = courseDataAccessObject.getCourse(courseCode)
                    .getLectureTimings();
            for (DayOfWeek thisLectureDay : registeredCourseLectureTimings.keySet()) {
                checkTimeTableClash(registeredCourseLectureTimings, newCourseLectureTimings, thisLectureDay);
                checkTimeTableClash(registeredCourseLectureTimings, newIndexLaboratoryTimings, thisLectureDay);
                checkTimeTableClash(registeredCourseLectureTimings, newIndexTutorialTimings, thisLectureDay);
            }
            if (registeredIndexLaboratoryTimings != null) {
                for (DayOfWeek thisLaboratoryDay : registeredIndexLaboratoryTimings.keySet()) {
                    checkTimeTableClash(registeredIndexLaboratoryTimings, newCourseLectureTimings, thisLaboratoryDay);
                    checkTimeTableClash(registeredIndexLaboratoryTimings, newIndexLaboratoryTimings, thisLaboratoryDay);
                    checkTimeTableClash(registeredIndexLaboratoryTimings, newIndexTutorialTimings, thisLaboratoryDay);
                }
            }
            if (registeredIndexTutorialTimings != null) {
                for (DayOfWeek thisTutorialDay : registeredIndexTutorialTimings.keySet()) {
                    checkTimeTableClash(registeredIndexTutorialTimings, newCourseLectureTimings, thisTutorialDay);
                    checkTimeTableClash(registeredIndexTutorialTimings, newIndexLaboratoryTimings, thisTutorialDay);
                    checkTimeTableClash(registeredIndexTutorialTimings, newIndexTutorialTimings, thisTutorialDay);
                }
            }
        }
        registrationDataAccess.addRegistration(registrationKey);
    }

    private void checkTimeTableClash(Hashtable<DayOfWeek, List<LocalTime>> registeredCourseTimings, Hashtable<DayOfWeek, List<LocalTime>> newCourseTimings, DayOfWeek registeredCourseDay) throws ClashingTimeTableException {
        if (registeredCourseDay == null || newCourseTimings == null || registeredCourseTimings == null) {
            return;
        }
        for (DayOfWeek thatLectureDay : newCourseTimings.keySet()) {
            if (registeredCourseDay == thatLectureDay) {
                if (isOverlapping(registeredCourseTimings.get(registeredCourseDay).get(0),
                        registeredCourseTimings.get(registeredCourseDay).get(1),
                        newCourseTimings.get(thatLectureDay).get(0),
                        newCourseTimings.get(thatLectureDay).get(1))) {
                    throw new ClashingTimeTableException();
                }
            }
        }
    }

    private boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    public void deleteRegistration(String matricNumber, String courseCode, int indexNumber) throws IOException, ClassNotFoundException, InvalidAccessPeriodException, NonExistentRegistrationException, NonExistentUserException, NonExistentCourseException, NonExistentIndexException, ExistingCourseException, MaxEnrolledStudentsException, ExistingUserException {
        ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
        IRegistrationDataAccessObject registrationDataAccess = Factory.getTextRegistrationDataAccess();

        LocalDateTime startDate = registrationDataAccess.getRegistrationPeriod().getStartDate();
        LocalDateTime endDate = registrationDataAccess.getRegistrationPeriod().getEndDate();
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startDate) || now.isAfter(endDate)) {
            throw new InvalidAccessPeriodException();
        }
        RegistrationKey registrationKey = Factory.createRegistrationKey(matricNumber, courseCode, indexNumber);
        Course course = courseDataAccessObject.getCourse(courseCode);
        Index index = course.getIndex(indexNumber);
        if (index.getWaitingList().contains(matricNumber)) {
            index.dropStudent(matricNumber);
            course.updateIndex(index);
            courseDataAccessObject.updateCourse(course);

            IUserDataAccessObject userDataAccessObject = Factory.getTextUserDataAccess();
            Student student = userDataAccessObject.getStudent(matricNumber);
            student.deregisterCourse(courseCode);
            userDataAccessObject.updateStudent(student);
        } else {
            registrationDataAccess.deleteRegistration(registrationKey);
        }
    }
}
