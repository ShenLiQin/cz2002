import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class STARSApp {

    public static void main(String[] args) {
        UserDatabase userDatabase;
        CourseDatabase courseDatabase;
        RegistrationDatabase registrationDatabase;
        try {
            userDatabase = Factory.getUserDatabase();
            Student newS = Factory.createStudent("ian", "scse", 23);
            newS.setMatricNumber("U1274243D");
            Staff newA = Factory.createStaff("richard", "scse");
            userDatabase.addStudent(newS);
            userDatabase.addAdmin(newA);

            courseDatabase = Factory.getCourseDatabase();
            Index index1 = Factory.createIndex(20000, 31);
            Index index2 = Factory.createIndex(20001, 30);
            index2.setLaboratoryVenue("Hardware Lab 2");
            ArrayList<Date> dates = new ArrayList<>();
            dates.add(new Date(1605360255));
            dates.add(new Date(1606137855));
            index2.setLaboratoryTimings(dates);

            ArrayList<Index> indexes = new ArrayList<>();
            indexes.add(index1);
            indexes.add(index2);

            Course course = Factory.createCourse("cz2001", "algorithm", new ArrayList<Date>(), "LT2", 3, indexes);
            courseDatabase.addCourse(course);

            registrationDatabase = Factory.getRegistrationDatabase();
            RegistrationKey registrationKey = Factory.createRegistrationKey(newS.getMatricNumber(), course.getCourseCode(), index2.getIndexNumber());
            registrationDatabase.addRegistration(registrationKey);

            ISession session;
            do {
                LoginControl loginControl = Factory.createLoginControl();
                AbstractUser user = loginControl.login(userDatabase);

                session = Factory.createSession(user);
                session.run();
            } while (!session.logout());

        } catch(IOException | ClassNotFoundException e) {
            System.out.println("error reading files... \nexiting...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
