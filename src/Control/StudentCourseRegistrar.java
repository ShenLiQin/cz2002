package Control;

import DataAccessObject.ICourseDataAccessObject;
import DataAccessObject.IRegistrationDataAccessObject;
import DataAccessObject.IUserDataAccessObject;
import Helper.Factory;
import ValueObject.Course;
import ValueObject.Index;
import ValueObject.RegistrationKey;
import Exception.*;
import ValueObject.Student;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

public class StudentCourseRegistrar {

    public void addRegistration(String matricNumber, String courseCode, int indexNumber) throws IOException, ClassNotFoundException, InvalidAccessPeriodException, InsufficientAUsException, ExistingCourseException, ExistingUserException, ExistingRegistrationException, NonExistentUserException, NonExistentCourseException, NonExistentIndexException, MaxEnrolledStudentsException, ClashingTimeTableException {
        IUserDataAccessObject userDataAccessObject = Factory.getTextUserDataAccess();
        ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
        IRegistrationDataAccessObject registrationDataAccessObject = Factory.getTextRegistrationDataAccess();

        if (registrationDataAccessObject.getRegistrationPeriod().notWithinRegistrationPeriod()) {
            throw new InvalidAccessPeriodException();
        }
        RegistrationKey registrationKey = Factory.createRegistrationKey(matricNumber, courseCode, indexNumber);
        Student student = userDataAccessObject.getStudent(matricNumber);
        Course course = courseDataAccessObject.getCourse(courseCode);
        Index index = course.getIndex(indexNumber);

        if (student.getMaxAUs() - student.getTotalRegisteredAUs() < course.getAUs()) {
            throw new InsufficientAUsException();
        }

        Set<String> registeredCourses = student.getRegisteredCourses().keySet();
        Set<String> waitlist = student.getWaitingListCourses().keySet();
        ArrayList<String> allRegisteredCourses = new ArrayList<>();
        allRegisteredCourses.addAll(registeredCourses);
        allRegisteredCourses.addAll(waitlist);
        for (String registeredCourseCode : allRegisteredCourses) {
            int registeredCourseIndexNumber;
            if (student.getRegisteredCourses() == null || !student.getRegisteredCourses().containsKey(registeredCourseCode)) {
                registeredCourseIndexNumber = student.getWaitingListCourses().get(registeredCourseCode);
            } else {
                registeredCourseIndexNumber = student.getRegisteredCourses().get(registeredCourseCode);
            }
            Course registeredCourse = courseDataAccessObject.getCourse(registeredCourseCode);
            Index registeredIndex = registeredCourse.getIndex(registeredCourseIndexNumber);

            if (registeredCourse.isClashing(course) || registeredCourse.isClashing(index) ||
                    registeredIndex.isClashing(index)) {
                throw new ClashingTimeTableException();
            }
        }

        registrationDataAccessObject.addRegistration(registrationKey);
    }

    public void deleteRegistration(String matricNumber, String courseCode, int indexNumber) throws IOException, ClassNotFoundException, InvalidAccessPeriodException, NonExistentRegistrationException, NonExistentUserException, NonExistentCourseException, NonExistentIndexException, ExistingCourseException, MaxEnrolledStudentsException, ExistingUserException {
        ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
        IRegistrationDataAccessObject registrationDataAccessObject = Factory.getTextRegistrationDataAccess();
        if (registrationDataAccessObject.getRegistrationPeriod().notWithinRegistrationPeriod()) {
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
            registrationDataAccessObject.deleteRegistration(registrationKey);
        }
    }
}
