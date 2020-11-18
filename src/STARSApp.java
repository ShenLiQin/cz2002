import Control.ISession;
import Control.LoginControl;
import DataAccessObject.ICourseDataAccessObject;
import DataAccessObject.IRegistrationDataAccessObject;
import DataAccessObject.IUserDataAccessObject;
import Helper.Factory;
import ValueObject.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class STARSApp {

    public static void main(String[] args) {
        IUserDataAccessObject userDataAccessObject;
        ICourseDataAccessObject courseDataAccessObject;
        IRegistrationDataAccessObject registrationDataAccessObject;
        try {
            userDataAccessObject = Factory.getTextUserDataAccess();
            Staff newA = Factory.createStaff("richard", "scse");
            userDataAccessObject.addAdmin(newA);

            Student newS1 = Factory.createStudent("ian", "scse", 23);
            newS1.setMatricNumber("U1941314D");
            userDataAccessObject.addStudent(newS1);
            Student newS2 = Factory.createStudent("noah", "scse", 21);
            newS2.setMatricNumber("U1921314F");
            userDataAccessObject.addStudent(newS2);
            Student newS3 = Factory.createStudent("li qin", "scse", 21);
            newS3.setMatricNumber("U1941315R");
            userDataAccessObject.addStudent(newS3);
            Student newS4 = Factory.createStudent("selvira", "scse", 21);
            newS4.setMatricNumber("U1951316D");
            userDataAccessObject.addStudent(newS4);

            courseDataAccessObject = Factory.getTextCourseDataAccess();
            Index index1 = Factory.createIndex(200100, 10);
            Index index2 = Factory.createIndex(200101, 10);
            index2.setLaboratoryVenue("Hardware Lab 2");
            ArrayList<Date> index2Date = new ArrayList<>();
            index2Date.add(new Date(1605360255));
            index2Date.add(new Date(1606137855));
            index2.setLaboratoryTimings(index2Date);

            Index index3 = Factory.createIndex(200102, 10);
            index3.setLaboratoryVenue("Hardware Lab 1");
            ArrayList<Date> index3dates = new ArrayList<>();
            index3dates.add(new Date(1605678778));
            index3dates.add(new Date(1605765178));
            index3.setLaboratoryTimings(index3dates);

            ArrayList<Index> indexes1 = new ArrayList<>();
            indexes1.add(index1);
            indexes1.add(index2);
            indexes1.add(index3);

            Index index4 = Factory.createIndex(200200, 10);
            Index index5 = Factory.createIndex(200201, 10);
            index2.setLaboratoryVenue("Software Lab 2");
            ArrayList<Date> index5Date = new ArrayList<>();
            index2Date.add(new Date(1605765178));
            index2Date.add(new Date(1605783178));
            index2.setLaboratoryTimings(index5Date);

            Index index6 = Factory.createIndex(200202, 10);
            index3.setLaboratoryVenue("Software Project Lab");
            ArrayList<Date> index6dates = new ArrayList<>();
            index3dates.add(new Date(1608375178));
            index3dates.add(new Date(1608461578));
            index3.setLaboratoryTimings(index6dates);

            ArrayList<Index> indexes2 = new ArrayList<>();
            indexes2.add(index4);
            indexes2.add(index5);
            indexes2.add(index6);

            Course course1 = Factory.createCourse("cz2001", "algorithm", new ArrayList<>(), "LT2", 3, indexes1);
            courseDataAccessObject.addCourse(course1);
            Course course2 = Factory.createCourse("cz2002", "object oriented design and programming", new ArrayList<>(), "LT1", 3, indexes2);
            courseDataAccessObject.addCourse(course2);

            registrationDataAccessObject = Factory.getTextRegistrationDataAccess();
            RegistrationKey registrationKey1 = Factory.createRegistrationKey(newS1.getMatricNumber(), course1.getCourseCode(), index2.getIndexNumber());
            registrationDataAccessObject.addRegistration(registrationKey1);
            RegistrationKey registrationKey2 = Factory.createRegistrationKey(newS2.getMatricNumber(), course1.getCourseCode(), index2.getIndexNumber());
            registrationDataAccessObject.addRegistration(registrationKey2);
            RegistrationKey registrationKey3 = Factory.createRegistrationKey(newS3.getMatricNumber(), course1.getCourseCode(), index1.getIndexNumber());
            registrationDataAccessObject.addRegistration(registrationKey3);
            RegistrationKey registrationKey4 = Factory.createRegistrationKey(newS4.getMatricNumber(), course1.getCourseCode(), index3.getIndexNumber());
            registrationDataAccessObject.addRegistration(registrationKey4);
            RegistrationKey registrationKey5 = Factory.createRegistrationKey(newS1.getMatricNumber(), course2.getCourseCode(), index4.getIndexNumber());
            registrationDataAccessObject.addRegistration(registrationKey5);
            RegistrationKey registrationKey6 = Factory.createRegistrationKey(newS2.getMatricNumber(), course2.getCourseCode(), index5.getIndexNumber());
            registrationDataAccessObject.addRegistration(registrationKey6);
            RegistrationKey registrationKey7 = Factory.createRegistrationKey(newS3.getMatricNumber(), course2.getCourseCode(), index6.getIndexNumber());
            registrationDataAccessObject.addRegistration(registrationKey7);

            RegistrationPeriod registrationPeriod = Factory.createRegistrationPeriod(LocalDateTime.now(), LocalDateTime.now().plusDays(3));
            registrationDataAccessObject.setRegistrationPeriod(registrationPeriod);

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
