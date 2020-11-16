import java.util.ArrayList;
import java.util.Date;

public class STARSApp {

    public static void main(String[] args) {
        UserDatabase userDatabase;
        CourseDatabase courseDatabase;
        RegistrationDatabase registrationDatabase;
        try {
            userDatabase = Factory.getUserDatabase();
//            Student newS = Factory.createStudent("meg", "scse", 23);
//            Staff newA = Factory.createStaff("richard", "scse");
//            userDatabase.addStudent(newS);
//            userDatabase.addAdmin(newA);

            courseDatabase = Factory.getCourseDatabase();
            Index index1 = Factory.createIndex(20000, 31);
            Index index2 = Factory.createIndex(20001, 30);
            index2.setLaboratoryVenue("Hardware Lab 2");
            ArrayList<Date> dates = new ArrayList<>();
            dates.add(new Date(1605360255));
            dates.add(new Date(1606137855));
            index2.setLaboratoryTimings(dates);
            ArrayList<String> enrolledStudents = new ArrayList<>();
            enrolledStudents.add("U1921319D");
            index2.setEnrolledStudents(enrolledStudents);

            ArrayList<Index> indexes = new ArrayList<>();
            indexes.add(index1);
            indexes.add(index2);


            Course course = Factory.createCourse("cz2001", "algorithm", new ArrayList<Date>(), "LT2", 3, indexes);
            courseDatabase.addCourse(course);

//            registrationDatabase = Factory.getRegistrationDatabase();
            ISession session;
            do {
                LoginControl loginControl = Factory.createLoginControl();
                UserType userType = loginControl.login(userDatabase);

                session = Factory.createSession(userType);
                session.run();
            } while (!session.logout());

        } catch (Exception e) {
            System.out.println("failed to read files");
            e.printStackTrace();
        }
    }
}
