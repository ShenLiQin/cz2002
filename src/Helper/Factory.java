package Helper;

import Control.*;
import DataAccessObject.*;
import ValueObject.*;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;


public class Factory {
    public static LoginControl createLoginControl() {
        TextIO textIO = TextIoFactory.getTextIO();
        return new LoginControl(textIO, textIO.getTextTerminal());
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
        TextIO textIO = TextIoFactory.getTextIO();
        switch (user.getUserType()) {
            case ADMIN -> {
                return new AdminSession(textIO, textIO.getTextTerminal(), user);
            }
            case USER -> {
                return new UserSession(textIO, textIO.getTextTerminal(), user);
            }
            default -> throw new NullPointerException();
        }
    }

    public static Student createStudent(String name, School school, Gender gender, Nationality nationality, int maxAUs) throws PasswordStorage.CannotPerformOperationException {
        return new Student(name, school, gender, nationality, maxAUs, new Random());
    }

    public static Student createStudent(String name, School school, Gender gender, Nationality nationality) throws PasswordStorage.CannotPerformOperationException {
        return new Student(name, school, gender, nationality, 23 , new Random());
    }

    public static Staff createStaff(String name, School school, Gender gender, Nationality nationality) throws PasswordStorage.CannotPerformOperationException {
        return new Staff(name, school, gender, nationality);
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

    public static Course createCourse(String courseCode, String courseName, School school, Hashtable<DayOfWeek, List<LocalTime>> lectureTimings, Venue lectureVenue, int AUs) {
        return createCourse(courseCode, courseName, school, lectureTimings, lectureVenue, AUs, new ArrayList<>());
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