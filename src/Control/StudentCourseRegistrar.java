package Control;

import DataAccessObject.ICourseDataAccessObject;
import DataAccessObject.IRegistrationDataAccessObject;
import DataAccessObject.IUserDataAccessObject;
import Helper.Factory;
import ValueObject.Course;
import ValueObject.RegistrationKey;
import Exception.*;
import ValueObject.Student;

import java.io.IOException;
import java.time.LocalDateTime;

public class StudentCourseRegistrar {

    public void addRegistration(String matricNumber, String courseCode, int indexNumber) throws IOException, ClassNotFoundException, InvalidAccessPeriodException, InsufficientAUsException, ExistingCourseException, ExistingUserException, MaxClassSizeException, ExistingRegistrationException, NonExistentUserException {
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
        course.getIndex(indexNumber);
        if (student.getMaxAUs() - student.getTotalRegisteredAUs() < course.getAUs()) {
            throw new InsufficientAUsException();
        }
        registrationDataAccess.addRegistration(registrationKey);
    }

    public void deleteRegistration(String matricNumber, String courseCode, int indexNumber) throws IOException, ClassNotFoundException, InvalidAccessPeriodException, InsufficientAUsException, NonExistentRegistrationException, NonExistentUserException, NonExistentCourseException {
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
        course.getIndex(indexNumber);
        if (student.getMaxAUs() - student.getTotalRegisteredAUs() < course.getAUs()) {
            throw new InsufficientAUsException();
        }
        registrationDataAccess.deleteRegistration(registrationKey);
    }
}
