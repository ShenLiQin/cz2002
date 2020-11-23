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
import java.rmi.activation.ActivationGroup_Stub;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class STARSApp {
    /*
       TODO change output file format to flat
       TODO catch bugs
       TODO think of new functionalities (implement userSession into adminSession)
        */
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
            Staff newA = Factory.createStaff("Richard", School.SCSE, Gender.MALE, Nationality.SINGAPOREAN);
            userDataAccessObject.addAdmin(newA);
            Student newS1 = Factory.createStudent("Ian", School.SCSE, Gender.MALE, Nationality.SINGAPOREAN, 23);
            newS1.setMatricNumber("U1941314D");
            userDataAccessObject.addStudent(newS1);
            Student newS2 = Factory.createStudent("Noah", School.SCSE, Gender.MALE, Nationality.SINGAPOREAN);
            newS2.setMatricNumber("U1921314F");
            userDataAccessObject.addStudent(newS2);
            Student newS3 = Factory.createStudent("Liqin", School.SCSE, Gender.FEMALE, Nationality.SINGAPOREAN);
            newS3.setMatricNumber("U1941315R");
            userDataAccessObject.addStudent(newS3);
            Student newS4 = Factory.createStudent("Selvira", School.SCSE, Gender.FEMALE, Nationality.SINGAPOREAN);
            newS4.setMatricNumber("U1951316D");
            userDataAccessObject.addStudent(newS4);
            Student newS5 = Factory.createStudent("Jefferson", School.SCSE, Gender.MALE, Nationality.SINGAPOREAN);
            newS5.setMatricNumber("U1952356D");
            userDataAccessObject.addStudent(newS5);
            Student newS6 = Factory.createStudent("Shannon", School.NBS, Gender.FEMALE, Nationality.SINGAPOREAN);
            newS6.setMatricNumber("U2052356A");
            userDataAccessObject.addStudent(newS6);
            Student newS7 = Factory.createStudent("Wilson", School.NBS, Gender.MALE, Nationality.MALAYSIAN);
            newS7.setMatricNumber("U2059876B");
            userDataAccessObject.addStudent(newS7);
            Student newS8 = Factory.createStudent("Isaac", School.NBS, Gender.MALE, Nationality.MALAYSIAN);
            newS8.setMatricNumber("U2073516A");
            userDataAccessObject.addStudent(newS8);
            Student newS9 = Factory.createStudent("Ben", School.NBS, Gender.MALE, Nationality.MALAYSIAN);
            newS9.setMatricNumber("U2096716F");
            userDataAccessObject.addStudent(newS9);
            Student newS10 = Factory.createStudent("Jerald", School.NBS, Gender.MALE, Nationality.MALAYSIAN);
            newS10.setMatricNumber("U2090621F");
            userDataAccessObject.addStudent(newS10);
            Student newS11 = Factory.createStudent("Cedric", School.EEE, Gender.MALE, Nationality.MALAYSIAN);
            newS11.setMatricNumber("U1997771F");
            userDataAccessObject.addStudent(newS11);
            Student newS12 = Factory.createStudent("Sandra", School.EEE, Gender.FEMALE, Nationality.MALAYSIAN);
            newS12.setMatricNumber("U1951118F");
            userDataAccessObject.addStudent(newS12);
            Student newS13 = Factory.createStudent("Cheryl", School.EEE, Gender.FEMALE, Nationality.SINGAPOREAN);
            newS13.setMatricNumber("U1928018T");
            userDataAccessObject.addStudent(newS13);
            Student newS14 = Factory.createStudent("Andrea", School.EEE, Gender.FEMALE, Nationality.MALAYSIAN);
            newS14.setMatricNumber("U1948483F");
            userDataAccessObject.addStudent(newS14);
            Student newS15 = Factory.createStudent("Jennifer", School.EEE, Gender.FEMALE, Nationality.MALAYSIAN);
            newS15.setMatricNumber("U1949800W");
            userDataAccessObject.addStudent(newS15);

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

            Index index7 = Factory.createIndex(200300, 1);
            ArrayList<Index> indexes3 = new ArrayList<>();
            indexes3.add(index7);

            lectureTiming = new Hashtable<>();
            timing = new LinkedList<>();
            timing.add(LocalTime.of(10,0));
            timing.add(LocalTime.of(11,0));
            lectureTiming.put(DayOfWeek.WEDNESDAY, timing);
            Course course3 = Factory.createCourse("cz2003", "CGV", School.SCSE, lectureTiming, Venue.LT2, 3, indexes3);
            courseDataAccessObject.addCourse(course3);

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
            RegistrationKey registrationKey8 = Factory.createRegistrationKey(newS1.getMatricNumber(), course3.getCourseCode(), index7.getIndexNumber());
            registrationDataAccessObject.addRegistration(registrationKey8);

            RegistrationPeriod registrationPeriod = Factory.createRegistrationPeriod(LocalDateTime.now(), LocalDateTime.now().plusDays(3));
            registrationDataAccessObject.setRegistrationPeriod(registrationPeriod);

            ISession session;
            do {
                LoginControl loginControl = Factory.createLoginControl();
                AbstractUser user = loginControl.login(Factory.getTextUserDataAccess());
//                AbstractUser user = newA;
                session = Factory.createSession(user);
                session.run();
            } while (!session.logout());

        } catch(IOException | ClassNotFoundException e) {
            TextIoFactory.getTextTerminal().println("error reading files... \nexiting...");
        } catch (SecurityException e) {
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
