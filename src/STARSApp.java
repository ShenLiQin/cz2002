import Control.ISession;
import Control.LoginControl;
import Control.StudentCourseRegistrar;
import DataAccessObject.ICourseDataAccessObject;
import DataAccessObject.IUserDataAccessObject;
import Helper.Factory;
import ValueObject.*;
import org.beryx.textio.TextIoFactory;

import java.io.IOException;
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
        try {
//            userDataAccessObject = Factory.getTextUserDataAccess();
//            Staff newA = Factory.createStaff("Richard", School.SCSE, Gender.MALE, Nationality.SINGAPOREAN);
//            userDataAccessObject.addAdmin(newA);
//            Student newS1 = Factory.createStudent("Ian", School.SCSE, Gender.MALE, Nationality.SINGAPOREAN, 23);
//            newS1.setMatricNumber("U1941314D");
//            userDataAccessObject.addStudent(newS1);
//            Student newS2 = Factory.createStudent("Noah", School.SCSE, Gender.MALE, Nationality.SINGAPOREAN);
//            newS2.setMatricNumber("U1921314F");
//            userDataAccessObject.addStudent(newS2);
//            Student newS3 = Factory.createStudent("Liqin", School.SCSE, Gender.FEMALE, Nationality.SINGAPOREAN);
//            newS3.setMatricNumber("U1941315R");
//            userDataAccessObject.addStudent(newS3);
//            Student newS4 = Factory.createStudent("Selvira", School.SCSE, Gender.FEMALE, Nationality.SINGAPOREAN);
//            newS4.setMatricNumber("U1951316D");
//            userDataAccessObject.addStudent(newS4);
//            Student newS5 = Factory.createStudent("Jefferson", School.SCSE, Gender.MALE, Nationality.SINGAPOREAN);
//            newS5.setMatricNumber("U1952356D");
//            userDataAccessObject.addStudent(newS5);
//            Student newS6 = Factory.createStudent("Shannon", School.NBS, Gender.FEMALE, Nationality.SINGAPOREAN);
//            newS6.setMatricNumber("U2052356A");
//            userDataAccessObject.addStudent(newS6);
//            Student newS7 = Factory.createStudent("Wilson", School.NBS, Gender.MALE, Nationality.MALAYSIAN);
//            newS7.setMatricNumber("U2059876B");
//            userDataAccessObject.addStudent(newS7);
//            Student newS8 = Factory.createStudent("Isaac", School.NBS, Gender.MALE, Nationality.MALAYSIAN);
//            newS8.setMatricNumber("U2073516A");
//            userDataAccessObject.addStudent(newS8);
//            Student newS9 = Factory.createStudent("Ben", School.NBS, Gender.MALE, Nationality.MALAYSIAN);
//            newS9.setMatricNumber("U2096716F");
//            userDataAccessObject.addStudent(newS9);
//            Student newS10 = Factory.createStudent("Jerald", School.NBS, Gender.MALE, Nationality.MALAYSIAN);
//            newS10.setMatricNumber("U2090621F");
//            userDataAccessObject.addStudent(newS10);
//            Student newS11 = Factory.createStudent("Cedric", School.EEE, Gender.MALE, Nationality.MALAYSIAN);
//            newS11.setMatricNumber("U1997771F");
//            userDataAccessObject.addStudent(newS11);
//            Student newS12 = Factory.createStudent("Sandra", School.EEE, Gender.FEMALE, Nationality.MALAYSIAN);
//            newS12.setMatricNumber("U1951118F");
//            userDataAccessObject.addStudent(newS12);
//            Student newS13 = Factory.createStudent("Cheryl", School.EEE, Gender.FEMALE, Nationality.SINGAPOREAN);
//            newS13.setMatricNumber("U1928018T");
//            userDataAccessObject.addStudent(newS13);
//            Student newS14 = Factory.createStudent("Andrea", School.EEE, Gender.FEMALE, Nationality.MALAYSIAN);
//            newS14.setMatricNumber("U1948483F");
//            userDataAccessObject.addStudent(newS14);
//            Student newS15 = Factory.createStudent("Jennifer", School.EEE, Gender.FEMALE, Nationality.MALAYSIAN);
//            newS15.setMatricNumber("U1949800W");
//            userDataAccessObject.addStudent(newS15);
//            Student newS16 = Factory.createStudent("Irene", School.EEE, Gender.FEMALE, Nationality.MALAYSIAN);
//            newS15.setMatricNumber("U1941258W");
//            userDataAccessObject.addStudent(newS16);
//            Student newS17 = Factory.createStudent("Seulgi", School.EEE, Gender.FEMALE, Nationality.MALAYSIAN);
//            newS15.setMatricNumber("U1941259W");
//            userDataAccessObject.addStudent(newS17);
//            Student newS18 = Factory.createStudent("Wendy", School.EEE, Gender.FEMALE, Nationality.MALAYSIAN);
//            newS15.setMatricNumber("U1941260W");
//            userDataAccessObject.addStudent(newS18);
//            Student newS19 = Factory.createStudent("Joy", School.EEE, Gender.FEMALE, Nationality.MALAYSIAN);
//            newS15.setMatricNumber("U1941261W");
//            userDataAccessObject.addStudent(newS19);
//            Student newS20 = Factory.createStudent("Yeri", School.EEE, Gender.FEMALE, Nationality.MALAYSIAN);
//            newS15.setMatricNumber("U1941262W");
//            userDataAccessObject.addStudent(newS20);
//            Student newS21 = Factory.createStudent("Aespa", School.EEE, Gender.FEMALE, Nationality.MALAYSIAN);
//            newS15.setMatricNumber("U1941263W");
//            userDataAccessObject.addStudent(newS21);
//
//            courseDataAccessObject = Factory.getTextCourseDataAccess();
//            Index index1 = Factory.createIndex(200100, 10);
//            Hashtable<DayOfWeek, List<LocalTime>> index1Date = new Hashtable<>();
//            List<LocalTime> timing = new LinkedList<>();
//            timing.add(LocalTime.of(10,0));
//            timing.add(LocalTime.of(12,0));
//            index1Date.put(DayOfWeek.MONDAY, timing);
//            index1.setLaboratoryTimings(index1Date);
//            index1.setLaboratoryVenue(Venue.HWL2);
//            timing = new LinkedList<>();
//            index1Date = new Hashtable<>();
//            timing.add(LocalTime.of(10,0));
//            timing.add(LocalTime.of(11,0));
//            index1Date.put(DayOfWeek.TUESDAY, timing);
//            index1.setTutorialTimings(index1Date);
//            index1.setTutorialVenue(Venue.TR1);
//
//            Index index2 = Factory.createIndex(200101, 10);
//            Hashtable<DayOfWeek, List<LocalTime>> index2Date = new Hashtable<>();
//            timing = new LinkedList<>();
//            timing.add(LocalTime.of(10,0));
//            timing.add(LocalTime.of(12,0));
//            index2Date.put(DayOfWeek.TUESDAY, timing);
//            index2.setLaboratoryTimings(index2Date);
//            index2.setLaboratoryVenue(Venue.HWL2);
//            timing = new LinkedList<>();
//            index2Date = new Hashtable<>();
//            timing.add(LocalTime.of(15,0));
//            timing.add(LocalTime.of(16,0));
//            index2Date.put(DayOfWeek.WEDNESDAY, timing);
//            index2.setTutorialTimings(index2Date);
//            index2.setTutorialVenue(Venue.TR1);
//
//            Index index3 = Factory.createIndex(200102, 10);
//            Hashtable<DayOfWeek, List<LocalTime>> index3Date = new Hashtable<>();
//            timing = new LinkedList<>();
//            timing.add(LocalTime.of(10,0));
//            timing.add(LocalTime.of(12,0));
//            index3Date.put(DayOfWeek.THURSDAY, timing);
//            index3.setLaboratoryTimings(index3Date);
//            index3.setLaboratoryVenue(Venue.HWL1);
//            timing = new LinkedList<>();
//            index3Date = new Hashtable<>();
//            timing.add(LocalTime.of(11,0));
//            timing.add(LocalTime.of(12,0));
//            index3Date.put(DayOfWeek.FRIDAY, timing);
//            index3.setTutorialTimings(index3Date);
//            index3.setTutorialVenue(Venue.TR1);
//
//            ArrayList<Index> indexes1 = new ArrayList<>();
//            indexes1.add(index1);
//            indexes1.add(index2);
//            indexes1.add(index3);
//
//            Hashtable<DayOfWeek, List<LocalTime>> lectureTiming = new Hashtable<>();
//            timing = new LinkedList<>();
//            timing.add(LocalTime.of(9,0));
//            timing.add(LocalTime.of(11,0));
//            lectureTiming.put(DayOfWeek.FRIDAY, timing);
//
//            Course course1 = Factory.createCourse("cz2001", "algorithm", School.SCSE, lectureTiming, Venue.LT1, 3, indexes1);
//            courseDataAccessObject.addCourse(course1);
//
//
//            Index index4 = Factory.createIndex(200200, 10);
//            Hashtable<DayOfWeek, List<LocalTime>> index4Date = new Hashtable<>();
//            timing = new LinkedList<>();
//            timing.add(LocalTime.of(14,0));
//            timing.add(LocalTime.of(16,0));
//            index4Date.put(DayOfWeek.WEDNESDAY, timing);
//            index4.setLaboratoryTimings(index4Date);
//            index4.setLaboratoryVenue(Venue.SWL2);
//            timing = new LinkedList<>();
//            index4Date = new Hashtable<>();
//            timing.add(LocalTime.of(9,0));
//            timing.add(LocalTime.of(10,0));
//            index4Date.put(DayOfWeek.TUESDAY, timing);
//            index4.setTutorialTimings(index4Date);
//            index4.setTutorialVenue(Venue.TR3);
//
//            Index index5 = Factory.createIndex(200201, 10);
//            Hashtable<DayOfWeek, List<LocalTime>> index5Date = new Hashtable<>();
//            timing = new LinkedList<>();
//            timing.add(LocalTime.of(14,0));
//            timing.add(LocalTime.of(16,0));
//            index5Date.put(DayOfWeek.WEDNESDAY, timing);
//            index5.setLaboratoryTimings(index5Date);
//            index5.setLaboratoryVenue(Venue.SWL1);
//            timing = new LinkedList<>();
//            index5Date = new Hashtable<>();
//            timing.add(LocalTime.of(9,0));
//            timing.add(LocalTime.of(10,0));
//            index5Date.put(DayOfWeek.TUESDAY, timing);
//            index5.setTutorialTimings(index5Date);
//            index5.setTutorialVenue(Venue.TR4);
//
//            Index index6 = Factory.createIndex(200202, 10);
//            Hashtable<DayOfWeek, List<LocalTime>> index6Date = new Hashtable<>();
//            timing = new LinkedList<>();
//            timing.add(LocalTime.of(9,0));
//            timing.add(LocalTime.of(11,0));
//            index6Date.put(DayOfWeek.FRIDAY, timing);
//            index6.setLaboratoryTimings(index6Date);
//            index6.setLaboratoryVenue(Venue.SWL2);
//            timing = new LinkedList<>();
//            index6Date = new Hashtable<>();
//            timing.add(LocalTime.of(10,0));
//            timing.add(LocalTime.of(11,0));
//            index6Date.put(DayOfWeek.TUESDAY, timing);
//            index6.setTutorialTimings(index6Date);
//            index6.setTutorialVenue(Venue.TR4);
//
//            ArrayList<Index> indexes2 = new ArrayList<>();
//            indexes2.add(index4);
//            indexes2.add(index5);
//            indexes2.add(index6);
//
//            lectureTiming = new Hashtable<>();
//            timing = new LinkedList<>();
//            timing.add(LocalTime.of(10,0));
//            timing.add(LocalTime.of(11,0));
//            lectureTiming.put(DayOfWeek.MONDAY, timing);
//            timing.add(LocalTime.of(15,0));
//            timing.add(LocalTime.of(16,0));
//            lectureTiming.put(DayOfWeek.FRIDAY, timing);
//
//            Course course2 = Factory.createCourse("cz2002", "object oriented design and programming", School.SCSE, lectureTiming, Venue.LT1, 3, indexes2);
//            courseDataAccessObject.addCourse(course2);
//
//            Index index7 = Factory.createIndex(100100, 10);
//            ArrayList<Index> indexes3 = new ArrayList<>();
//            indexes3.add(index7);
//
//            lectureTiming = new Hashtable<>();
//            timing = new LinkedList<>();
//            timing.add(LocalTime.of(10,0));
//            timing.add(LocalTime.of(11,0));
//            lectureTiming.put(DayOfWeek.WEDNESDAY, timing);
//            Course course3 = Factory.createCourse("et0001", "ENTERPRISE & INNOVATION", School.NBS, lectureTiming, Venue.LT3, 1, indexes3);
//            courseDataAccessObject.addCourse(course3);
//
//            Index index8 = Factory.createIndex(190100, 10);
//            ArrayList<Index> indexes4 = new ArrayList<>();
//            Hashtable<DayOfWeek, List<LocalTime>> index8Date;
//            timing = new LinkedList<>();
//            index8Date = new Hashtable<>();
//            timing.add(LocalTime.of(10,0));
//            timing.add(LocalTime.of(11,0));
//            index8Date.put(DayOfWeek.TUESDAY, timing);
//            index8.setTutorialTimings(index8Date);
//            index8.setTutorialVenue(Venue.TR2);
//            indexes4.add(index8);
//
//            lectureTiming = new Hashtable<>();
//            timing = new LinkedList<>();
//            timing.add(LocalTime.of(9,30));
//            timing.add(LocalTime.of(11,30));
//            lectureTiming.put(DayOfWeek.THURSDAY, timing);
//            Course course4 = Factory.createCourse("he0901", "PRINCIPLES OF ECONOMICS", School.NBS, lectureTiming, Venue.LT3, 3, indexes4);
//            courseDataAccessObject.addCourse(course4);
//
//            RegistrationPeriod registrationPeriod = Factory.createRegistrationPeriod(LocalDateTime.now(), LocalDateTime.now().plusDays(3));
//            Factory.getTextRegistrationDataAccess().setRegistrationPeriod(registrationPeriod);
//
//            StudentCourseRegistrar studentCourseRegistrar = Factory.createStudentCourseRegistrar();
//            studentCourseRegistrar.addRegistration(newS1.getMatricNumber(), course1.getCourseCode(), index1.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS2.getMatricNumber(), course1.getCourseCode(), index1.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS3.getMatricNumber(), course1.getCourseCode(), index1.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS4.getMatricNumber(), course1.getCourseCode(), index1.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS5.getMatricNumber(), course1.getCourseCode(), index1.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS6.getMatricNumber(), course1.getCourseCode(), index1.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS7.getMatricNumber(), course1.getCourseCode(), index1.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS8.getMatricNumber(), course1.getCourseCode(), index1.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS9.getMatricNumber(), course1.getCourseCode(), index1.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS10.getMatricNumber(), course1.getCourseCode(), index1.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS11.getMatricNumber(), course2.getCourseCode(), index4.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS12.getMatricNumber(), course2.getCourseCode(), index4.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS13.getMatricNumber(), course2.getCourseCode(), index4.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS14.getMatricNumber(), course2.getCourseCode(), index4.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS15.getMatricNumber(), course2.getCourseCode(), index4.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS16.getMatricNumber(), course2.getCourseCode(), index4.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS17.getMatricNumber(), course2.getCourseCode(), index4.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS18.getMatricNumber(), course2.getCourseCode(), index4.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS19.getMatricNumber(), course2.getCourseCode(), index4.getIndexNumber());
//            studentCourseRegistrar.addRegistration(newS20.getMatricNumber(), course2.getCourseCode(), index4.getIndexNumber());

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
