import Control.ISession;
import Control.LoginControl;
import DataAccessObject.ICourseDataAccessObject;
import DataAccessObject.IRegistrationDataAccessObject;
import DataAccessObject.IUserDataAccessObject;
import Helper.Factory;
import ValueObject.*;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class STARSApp {
    /*
       TODO change output file format to flat
       TODO catch bugs
       TODO think of new functionalities (implement userSession into adminSession)
       TODO fix bug: prevent user from adding course if time table clashes
        (add logic StudentCourseRegistrar.addRegistration) */
    //  start doing report (what design patterns and principals we used, functionalities, etc)
    // https://www.tutorialspoint.com/design_pattern/singleton_pattern.htm
    // https://www.tutorialspoint.com/design_pattern/data_access_object_pattern.htm
    // https://www.tutorialspoint.com/design_pattern/transfer_object_pattern.htm
    // https://www.tutorialspoint.com/design_pattern/factory_pattern.htm
    // hashing https://github.com/defuse/password-hashing
    // information https://crackstation.net/hashing-security.htm#properhashing
    // update our class diagram
    // update sequence diagram
    // for richard (make print student list print the correct info)

    public static void main(String[] args) {
        IUserDataAccessObject userDataAccessObject;
        ICourseDataAccessObject courseDataAccessObject;
        IRegistrationDataAccessObject registrationDataAccessObject;
        try {
            userDataAccessObject = Factory.getTextUserDataAccess();
            Staff newA = Factory.createStaff("richard", School.SCSE, Gender.MALE, Nationality.SINGAPOREAN);
            userDataAccessObject.addAdmin(newA);

            Student newS1 = Factory.createStudent("ian", School.SCSE, Gender.MALE, Nationality.SINGAPOREAN, 23);
            newS1.setMatricNumber("U1941314D");
            userDataAccessObject.addStudent(newS1);
            Student newS2 = Factory.createStudent("noah", School.SCSE, Gender.MALE, Nationality.SINGAPOREAN);
            newS2.setMatricNumber("U1921314F");
            userDataAccessObject.addStudent(newS2);
            Student newS3 = Factory.createStudent("liqin", School.SCSE, Gender.FEMALE, Nationality.SINGAPOREAN);
            newS3.setMatricNumber("U1941315R");
            userDataAccessObject.addStudent(newS3);
            Student newS4 = Factory.createStudent("selvira", School.SCSE, Gender.FEMALE, Nationality.SINGAPOREAN);
            newS4.setMatricNumber("U1951316D");
            userDataAccessObject.addStudent(newS4);

            courseDataAccessObject = Factory.getTextCourseDataAccess();
            Index index1 = Factory.createIndex(200100, 10);

            Index index2 = Factory.createIndex(200101, 10);
            Hashtable<DayOfWeek, List<LocalTime>> index2Date = new Hashtable<>();
            List<LocalTime> timing = new LinkedList<>();
            timing.add(LocalTime.of(10,0));
            timing.add(LocalTime.of(11,0));
            index2Date.put(DayOfWeek.TUESDAY, timing);
            index2.setLaboratoryTimings(index2Date);
            index2.setLaboratoryVenue(Venue.HWL2);

            Index index3 = Factory.createIndex(200102, 10);
            Hashtable<DayOfWeek, List<LocalTime>> index3Date = new Hashtable<>();
            timing = new LinkedList<>();
            timing.add(LocalTime.of(10,0));
            timing.add(LocalTime.of(11,0));
            index3Date.put(DayOfWeek.THURSDAY, timing);
            index3.setLaboratoryTimings(index3Date);
            index3.setLaboratoryVenue(Venue.HWL1);

            ArrayList<Index> indexes1 = new ArrayList<>();
            indexes1.add(index1);
            indexes1.add(index2);
            indexes1.add(index3);

            Hashtable<DayOfWeek, List<LocalTime>> lectureTiming = new Hashtable<>();
            timing = new LinkedList<>();
            timing.add(LocalTime.of(9,0));
            timing.add(LocalTime.of(11,0));
            lectureTiming.put(DayOfWeek.FRIDAY, timing);

            Course course1 = Factory.createCourse("cz2001", "algorithm", School.SCSE, lectureTiming, Venue.LT1, 3, indexes1);
            courseDataAccessObject.addCourse(course1);


            Index index4 = Factory.createIndex(200200, 10);

            Index index5 = Factory.createIndex(200201, 10);
            Hashtable<DayOfWeek, List<LocalTime>> index5Date = new Hashtable<>();
            timing = new LinkedList<>();
            timing.add(LocalTime.of(15,0));
            timing.add(LocalTime.of(16,0));
            index5Date.put(DayOfWeek.WEDNESDAY, timing);
            index5.setLaboratoryTimings(index5Date);
            index5.setLaboratoryVenue(Venue.SWL1);

            Index index6 = Factory.createIndex(200202, 10);
            Hashtable<DayOfWeek, List<LocalTime>> index6Date = new Hashtable<>();
            timing = new LinkedList<>();
            timing.add(LocalTime.of(10,0));
            timing.add(LocalTime.of(11,0));
            index6Date.put(DayOfWeek.FRIDAY, timing);
            index6.setLaboratoryTimings(index6Date);
            index6.setLaboratoryVenue(Venue.SWL2);

            ArrayList<Index> indexes2 = new ArrayList<>();
            indexes2.add(index4);
            indexes2.add(index5);
            indexes2.add(index6);

            lectureTiming = new Hashtable<>();
            timing = new LinkedList<>();
            timing.add(LocalTime.of(10,0));
            timing.add(LocalTime.of(11,0));
            lectureTiming.put(DayOfWeek.MONDAY, timing);
            timing.add(LocalTime.of(15,0));
            timing.add(LocalTime.of(16,0));
            lectureTiming.put(DayOfWeek.FRIDAY, timing);

            Course course2 = Factory.createCourse("cz2002", "object oriented design and programming", School.SCSE, lectureTiming, Venue.LT1, 3, indexes2);
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
//                AbstractUser user = newS1;
                session = Factory.createSession(user);
                session.run();
            } while (!session.logout());

        } catch(IOException | ClassNotFoundException e) {
            TextIoFactory.getTextTerminal().println("error reading files... \nexiting...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
