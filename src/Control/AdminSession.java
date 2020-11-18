package Control;

import DataAccessObject.ICourseDataAccessObject;
import DataAccessObject.IRegistrationDataAccessObject;
import DataAccessObject.IUserDataAccessObject;
import Helper.Factory;
import ValueObject.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

public class AdminSession implements ISession{
    private final Scanner _scanner;
    private boolean loggedIn = true;
    private Staff _user;


    public AdminSession(Scanner scanner, AbstractUser user) {
        _scanner = scanner;
        _user = (Staff) user;
    }

    @Override
    public boolean logout() {
        return loggedIn;
    }

    @Override
    public void exit() {
        System.exit(0);
    }

    @Override
    public void run() {
        int choice;

        do {
            System.out.println("_______Admin Dashboard_______");
            System.out.println("1. Edit student access period");
            System.out.println("2. Add a student (name, matric number, gender, nationality, etc)");
            System.out.println("3. Add/Update a course (course code, school, its index numbers and vacancy)");
            System.out.println("4. Check available slot for an index number (vacancy in a class)");
            System.out.println("5. Print student list by index number");
            System.out.println("6. Print student list by course (all students registered for the selected course)");
            System.out.println("7. Log out");
            System.out.println("8. Exit");

            choice = _scanner.nextInt();
            _scanner.nextLine();

            switch (choice) {
                case 1 -> {
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                    System.out.print("new start date: ");
                    LocalDateTime startDate = LocalDateTime.parse(_scanner.nextLine(), format);
                    System.out.print("new end date: ");
                    LocalDateTime endDate = LocalDateTime.parse(_scanner.nextLine(), format);
                    RegistrationPeriod newRP = Factory.createRegistrationPeriod(startDate, endDate);
                    try {
                        changeAccessPeriod(Factory.getTextRegistrationDataAccess(), newRP);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case 2 -> {
                    //add try catch system.out errors
                    System.out.print("name: ");
                    String name = _scanner.nextLine();
                    System.out.print("school: ");
                    String school = _scanner.nextLine();
                    System.out.print("maxAUs: ");
                    int maxAUs = _scanner.nextInt();
                    try {
                        Student newStudent = Factory.createStudent(name, school, maxAUs);
                        addStudent(Factory.getTextUserDataAccess(), newStudent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                case 3 -> {
                    System.out.print("course code: ");
                    String courseCode = _scanner.nextLine();
                    Course course = null;
                    try {
                        course = getCourse(Factory.getTextCourseDataAccess(), courseCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (course == null) {
                        System.out.println("no course found, adding course...");
                        System.out.print("course name: ");
                        String courseName = _scanner.nextLine();
                        System.out.println("___________lecture timing___________");
                        Calendar parsedStartDate = null;
                        do {
                            try {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                System.out.print("start date: ");
                                String startDate = _scanner.nextLine();
                                parsedStartDate.setTime(format.parse(startDate));
                            } catch (Exception p) {
                                p.printStackTrace();
                                System.out.println("invalid format");
                            }
                        } while (parsedStartDate == null);

                        Calendar parsedEndDate = null;
                        do {
                            try {
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                                System.out.print("end date: ");
                                String endDate = _scanner.nextLine();
                                parsedEndDate.setTime(format.parse(endDate));
                            } catch (Exception p) {
                                p.printStackTrace();
                                System.out.println("invalid format");
                            }
                        } while (parsedEndDate == null);

                        Calendar parsedDayAndTiming = null;
                        do {

                            try {
                                SimpleDateFormat format = new SimpleDateFormat("E HH:mm");
                                System.out.print("day and timing: ");
                                String dayAndTiming = _scanner.nextLine();
                                parsedDayAndTiming.setTime(format.parse(dayAndTiming));
                            } catch (Exception p) {
                                p.printStackTrace();
                                System.out.println("invalid format");
                            }
                        } while (parsedDayAndTiming == null);

                        ArrayList<Date> lectureTiming = new ArrayList<>();

                        System.out.print("lecture venue: ");
                        String lectureVenue = _scanner.nextLine();
                        System.out.print("AUs: ");
                        int AUs = _scanner.nextInt();

                        System.out.print("index number: ");
                        int indexNumber = _scanner.nextInt();
                        System.out.print("max class size: ");
                        int maxClassSize = _scanner.nextInt();

                        ArrayList<Index> newIndex = new ArrayList<>();
                        newIndex.add(Factory.createIndex(indexNumber, maxClassSize));

                        try {
                            course = Factory.createCourse(courseCode, courseName, lectureTiming, lectureVenue, AUs, newIndex);
                            addUpdateCourse(Factory.getTextCourseDataAccess(), course);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.print("what to change?");
                        System.out.println("1. Edit course name");
                        //...
                    }
                }
                case 5 -> {
                    Course course = null;
                    do {
                        try {
                            System.out.print("course code: ");
                            String courseCode = _scanner.nextLine();
                            course = Factory.getTextCourseDataAccess().getCourse(courseCode);
                            if (course == null) {
                                System.out.println("no such course");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (course == null);
                    Index index = null;
                    do {
                        try {
                            System.out.print("index number: ");
                            int indexNumber = _scanner.nextInt();
                            _scanner.nextLine();
                            index = course.getIndex(indexNumber);
                            if (index == null) {
                                System.out.println("no such index");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (index == null);
                    System.out.println(index.toString());
                }
                case 6 -> {
                    Course course = null;
                    do {
                        try {
                            System.out.print("course code: ");
                            String courseCode = _scanner.nextLine();
                            course = Factory.getTextCourseDataAccess().getCourse(courseCode);
                            if (course == null) {
                                System.out.println("no such course");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (course == null);
                    System.out.println(course.toString());
                }
                case 7 -> loggedIn = false;
                case 8 -> exit();
            }

        } while (choice > 0 && choice < 7);


    }

    private Course getCourse(ICourseDataAccessObject courseDataAccessObject, String courseCode) {
        return courseDataAccessObject.getCourse(courseCode);
    }

    private void changeAccessPeriod(IRegistrationDataAccessObject registrationDataAccessObject, RegistrationPeriod newRP) throws Exception {
        registrationDataAccessObject.updateRegistrationPeriod(newRP);
    }

    private void addStudent(IUserDataAccessObject userDataAccessObject, Student student) throws Exception {
        userDataAccessObject.addStudent(student);
    }

    private void addUpdateCourse(ICourseDataAccessObject courseDataAccessObject, Course newCourse) {

    }

    private void addUpdateCourse(ICourseDataAccessObject courseDataAccessObject, Course oldCourse, Course newCourse) {

    }

    private void printStudentListByIndex(ICourseDataAccessObject courseDataAccessObject, String courseCode, int indexNumber) {

    }

    private void printStudentListByCourse(ICourseDataAccessObject courseDataAccessObject, String courseCode) {

    }
}