import Control.ISession;
import Control.LoginControl;
import DataAccessObject.ICourseDataAccessObject;
import DataAccessObject.IRegistrationDataAccessObject;
import DataAccessObject.IUserDataAccessObject;
import Helper.Factory;
import ValueObject.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class STARSApp {

    public static void main(String[] args) {
        IUserDataAccessObject userDataAccessObject;
        ICourseDataAccessObject courseDataAccessObject;
        IRegistrationDataAccessObject registrationDataAccessObject;
        try {
            userDataAccessObject = Factory.getTextUserDataAccess();
            Student newS = Factory.createStudent("ian", "scse", 23);
            newS.setMatricNumber("U1274243D");
            Staff newA = Factory.createStaff("richard", "scse");
            userDataAccessObject.addStudent(newS);
            userDataAccessObject.addAdmin(newA);

            courseDataAccessObject = Factory.getTextCourseDataAccess();
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

            Course course = Factory.createCourse("cz2001", "algorithm", new ArrayList<>(), "LT2", 3, indexes);
            courseDataAccessObject.addCourse(course);

            registrationDataAccessObject = Factory.getTextRegistrationDataAccess();
            RegistrationKey registrationKey = Factory.createRegistrationKey(newS.getMatricNumber(), course.getCourseCode(), index2.getIndexNumber());
            registrationDataAccessObject.addRegistration(registrationKey);

            ISession session;
            do {
                LoginControl loginControl = Factory.createLoginControl();
                AbstractUser user = loginControl.login(Factory.getTextUserDataAccess());

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
