import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class Factory {
    public static LoginControl createLoginControl() {
        return new LoginControl(new Scanner(System.in));
    }

    public static UserDatabase getUserDatabase() throws IOException, ClassNotFoundException {
        return UserDatabase.getInstance();
    }

    public static CourseDatabase getCourseDatabase() throws IOException, ClassNotFoundException {
        return CourseDatabase.getInstance();
    }

    public static RegistrationDatabase getRegistrationDatabase() throws IOException, ClassNotFoundException {
        return RegistrationDatabase.getInstance();
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
        return new Student(name, school, 23 , new Random());
    }

    public static Staff createStaff(String name, String school) throws PasswordStorage.CannotPerformOperationException {
        return new Staff(name, school);
    }

    public static Index createIndex(int indexNumber, int maxClassSize, ArrayList<Date> tutorialTimings, String tutorialVenue, ArrayList<Date> laboratoryTimings, String laboratoryVenue) {
        return new Index(indexNumber, maxClassSize, tutorialTimings, tutorialVenue, laboratoryTimings, laboratoryVenue);
    }

    public static Index createIndex(int indexNumber, int maxClassSize) {
        return createIndex(indexNumber, maxClassSize, null, null, null, null);
    }

    public static Course createCourse(String courseCode, String courseName, ArrayList<Date> lectureTimings, String lectureVenue, int AUs, ArrayList<Index> indexes) {
        return new Course(courseCode, courseName, lectureTimings, lectureVenue, AUs, indexes);
    }

    public static RegistrationPeriod createRegistrationPeriod(LocalDateTime startDate, LocalDateTime endDate) {
        return new RegistrationPeriod(startDate, endDate);
    }

    public static RegistrationKey createRegistrationKey(String matricNumber, String courseCode, int indexNumber) {
        return new RegistrationKey(matricNumber, courseCode, indexNumber);
    }

    public static IMessanger createEmailMessanger(String senderEmail, String v) {
        return new EmailMessanger(senderEmail, senderEmail);
    }
}