package Helper;

import Control.*;
import DataAccessObject.*;
import ValueObject.*;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


public class Factory {
    public static LoginControl createLoginControl() {
        return new LoginControl(new Scanner(System.in));
    }

    public static IUserDataAccessObject getTextUserDataAccess() throws IOException, ClassNotFoundException {
        return TextUserDataAccess.getInstance();
    }

    public static ICourseDataAccessObject getTextCourseDataAccess() throws IOException, ClassNotFoundException {
        return TextCourseDataAccess.getInstance();
    }

    public static IRegistrationDataAccessObject getTextRegistrationDataAccess() throws IOException, ClassNotFoundException {
        return TextRegistrationDataAccess.getInstance();
    }

    public static ISession createSession(AbstractUser user) {
        switch (user.getUserType()) {
            case ADMIN -> {
                return new AdminSession(new Scanner(System.in), user);
            }
            case USER -> {
                return new UserSession(new Scanner(System.in), user);
            }
            default -> throw new NullPointerException();
        }
    }

    public static Student createStudent(String name, String school, int maxAUs) throws PasswordStorage.CannotPerformOperationException {
        return new Student(name, school, maxAUs, new Random());
    }

    public static Student createStudent(String name, String school) throws PasswordStorage.CannotPerformOperationException {
        return new Student(name, school, 21 , new Random());
    }

    public static Staff createStaff(String name, String school) throws PasswordStorage.CannotPerformOperationException {
        return new Staff(name, school);
    }

    public static Index createIndex(int indexNumber, int maxClassSize, Hashtable<DayOfWeek, List<LocalTime>> tutorialTimings, Venue tutorialVenue, Hashtable<DayOfWeek, List<LocalTime>> laboratoryTimings, Venue laboratoryVenue) {
        return new Index(indexNumber, maxClassSize, tutorialTimings, tutorialVenue, laboratoryTimings, laboratoryVenue);
    }

    public static Index createIndex(int indexNumber, int maxClassSize) {
        return createIndex(indexNumber, maxClassSize, null, null, null, null);
    }

    public static Course createCourse(String courseCode, String courseName, School school, Hashtable<DayOfWeek, List<LocalTime>> lectureTimings, Venue lectureVenue, int AUs, ArrayList<Index> indexes) {
        return new Course(courseCode, courseName, school, lectureTimings, lectureVenue, AUs, indexes);
    }

    public static RegistrationPeriod createRegistrationPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return new RegistrationPeriod(startDate, endDate);
    }

    public static RegistrationKey createRegistrationKey(String matricNumber, String courseCode, int indexNumber) {
        return new RegistrationKey(matricNumber, courseCode, indexNumber);
    }

    public static IMessanger createEmailMessanger(String senderEmail, String recipientEmail) {
        return new EmailMessanger(senderEmail, recipientEmail);
    }

    public static StudentCourseRegistrar createStudentCourseRegistrar() {
        return new StudentCourseRegistrar();
    }
}