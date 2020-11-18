package Control;

import DataAccessObject.IUserDataAccessObject;
import Helper.Factory;
import ValueObject.AbstractUser;
import ValueObject.Course;
import ValueObject.Index;
import ValueObject.Student;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Scanner;

public class UserSession implements ISession{
    private final Scanner _scanner;
    private boolean loggedIn = false;
    private Student _user;

    public UserSession(Scanner scanner, AbstractUser user) {
        _scanner = scanner;
        _user = (Student) user;
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
            System.out.println("_______User Dashboard_______");
            System.out.println("1. Add Course");
            System.out.println("2. Drop Course");
            System.out.println("3. Check/Print Courses Registered");
            System.out.println("4. Check Vacancies Available");
            System.out.println("5. Change Index Number of Course");
            System.out.println("6. Swap Index Number with Another Student");
            System.out.println("7. Log out");
            System.out.println("8. Exit");

            choice = _scanner.nextInt();
            _scanner.nextLine();

            switch (choice) {
                case 3 -> {
                    //print registered course
                        printRegisteredCourse();
                }
                case 4 -> {
                    boolean c = false;
                    boolean i = false;
                    //2 letter and 4 digits
                    //enter which course user want
                    Course course = null;
                    do {
                        try {
                            System.out.print("course code: ");
                            String courseCode = _scanner.nextLine();
                            //check courseCode format
                            c = checkCourseCodeFormat(courseCode);
                            course = Factory.getTextCourseDataAccess().getCourse(courseCode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (c == false);

                    //enter which index user want
                    Index index = null;
                    do {
                        try {
                            System.out.print("index number: ");
                            int indexNumber = _scanner.nextInt();
                            _scanner.nextLine();

                            //check if index is 6 integers
                            i = checkIndexFormat(indexNumber);
                            index = course.getIndex(indexNumber);
                        } catch (Exception e) {

                        }
                    } while (i == false);

                    //print vacancy
                    try {
                        System.out.println("Number of vacancy :" + course.checkVacancies(index.getIndexNumber()));
                    } catch (Exception e) {

                    }

                }
                case 5 -> { //change index
                    //print registered courses
                    printRegisteredCourse();

                    //which course user want to change
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

                    //user old index number
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

                    //user new index number
                    Index newIndex = null;
                    do {
                        try {
                            System.out.print("index number: ");
                            int indexNumber = _scanner.nextInt();
                            _scanner.nextLine();
                            newIndex = course.getIndex(indexNumber);
                            if (newIndex == null) {
                                System.out.println("no such index");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (index == null);

                    //get user matricNumber
                    String matric = _user.getMatricNumber();
                    try {
                        //delete old index
                        StudentCourseRegistrar studentCourseRegistrar = Factory.createStudentCourseRegistrar();
                        studentCourseRegistrar.deleteRegistration(matric, course.getCourseCode(), index.getIndexNumber());

                        //add new index
                        studentCourseRegistrar.addRegistration(matric, course.getCourseCode(), newIndex.getIndexNumber());
                        System.out.printf("successfully swapped %s from %s to %s\n", course.getCourseCode(), index.getIndexNumber(), newIndex.getIndexNumber());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //now for old index
                    //if vacancy == 1 AND waitlist != 0
                    //add first student in waitlist to enrolledStudents list
                    //send email to notify student

                    //implemented in index class. no need to do here

                }

                case 6 -> { //swap with peer
                    //print registered course
                    printRegisteredCourse();

                    //what course you want to swap
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

                    //what index
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

                    //create peer
                    AbstractUser peer = null;
                    Student peerr = null;
                    while (peer == null) {
                        //peer username
                        System.out.println("Enter peer username:");
                        String peerUsername = _scanner.nextLine();
                        //peer password
                        System.out.println("Enter peer password:");
                        String peerPassword = _scanner.nextLine();

                        //fetch userDatabase
                        try {
                            IUserDataAccessObject userDataAccessObject = Factory.getTextUserDataAccess();
                            peer = userDataAccessObject.authenticate(peerUsername, peerPassword);
                            peerr = (Student) peer;
                        } catch (Exception e) {

                        }

                        //authenticate peer

                    }
                    //downcast peer from AbstractUser to Student


                    //peer index
                    Index peerIndex = null;
                    do {
                        try {
                            System.out.print("index number: ");
                            int indexNumber = _scanner.nextInt();
                            _scanner.nextLine();
                            peerIndex = course.getIndex(indexNumber);
                            if (peerIndex == null) {
                                System.out.println("no such index");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (peerIndex == null);

                    try {
                        //drop course for peer
                        StudentCourseRegistrar studentCourseRegistrar = Factory.createStudentCourseRegistrar();
                        studentCourseRegistrar.deleteRegistration(peerr.getMatricNumber(), course.getCourseCode(), peerIndex.getIndexNumber());

                        //drop course for user
                        studentCourseRegistrar.deleteRegistration(_user.getMatricNumber(), course.getCourseCode(), index.getIndexNumber());

                        //add course for peer with user index
                        studentCourseRegistrar.addRegistration(peerr.getMatricNumber(), course.getCourseCode(), index.getIndexNumber());

                        //add course for user with peer index
                        studentCourseRegistrar.addRegistration(_user.getMatricNumber(), course.getCourseCode(), peerIndex.getIndexNumber());
                    } catch (Exception e) {

                    }
                }
                case 7 -> loggedIn = false;
                case 8 -> exit();
            }
        } while (choice > 0 && choice < 7);
    }
    private void printRegisteredCourse() {
        try {
            _user = Factory.getTextUserDataAccess().getStudent(_user.getMatricNumber());
            Hashtable<String, Integer> registered = _user.getRegisteredCourses();
            System.out.println("Course code | Index");
            registered.forEach((key, value) -> System.out.println(key + ":" + value));
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("error reading file");
        }
    }

    public static boolean isNumeric(String str){
        for (char c : str.toCharArray()){
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean checkCourseCodeFormat(String courseCode){
        char c1 = courseCode.charAt(0);
        char c2 = courseCode.charAt(1);

        if (courseCode.length() != 6) {
            System.out.println("Wrong format");
            return false;
        }
        else if((c1 < 'a' || c1 > 'z') && (c1 < 'A' || c1 > 'Z')){ //first letter not a char
            System.out.println("Wrong format");
            return false;
        }
        else if((c2 < 'a' || c2 > 'z') && (c2 < 'A' || c2 > 'Z')){ // second letter not a char
            System.out.println("Wrong format");
            return false;
        }
        else if(!isNumeric(courseCode.substring(2, 6))){ //last 4 not number
            System.out.println("Wrong format");
            return false;
        }
        else{
            return true;
        }
    }

    public boolean checkIndexFormat(int indexNumber){
        if(indexNumber < 100000 || indexNumber > 999999){
            System.out.println("Wrong format");
            return false;
        }
        else{
            return true;
        }
    }
}