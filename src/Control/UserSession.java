package Control;

import DataAccessObject.IUserDataAccessObject;
import Helper.Factory;
import Helper.PasswordStorage;
import ValueObject.AbstractUser;
import ValueObject.Course;
import ValueObject.Index;
import ValueObject.Student;
import Exception.*;

import java.io.IOException;
import java.util.HashMap;
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
                    boolean courseFormat = false;
                    boolean indexFormat = false;
                    //2 letter and 4 digits
                    //enter which course user want
                    Course course = null;
                    do {
                        try {
                            System.out.print("course code: ");
                            String courseCode = _scanner.nextLine();
                            //check courseCode format
                            courseFormat = checkCourseCodeFormat(courseCode);
                            course = Factory.getTextCourseDataAccess().getCourse(courseCode);
                            //check if course exist in database
                            if(course == null){
                                System.out.println("Course does not exist");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (courseFormat == false || course == null);

                    //enter which index user want

                    //enter which index user want
                    Index index = null;
                    do {
                        try {
                            System.out.print("index number: ");
                            int indexNumber = _scanner.nextInt();
                            _scanner.nextLine();

                            //check if index is 6 integers
                            indexFormat = checkIndexFormat(indexNumber);
                            index = course.getIndex(indexNumber);
                            //check if index exist in database
                            if(index == null){
                                System.out.println("Index does not exist");
                            }
                        }catch(Exception e){

                        }
                    } while (indexFormat == false || index == null);

                    //print vacancy
                    try {
                        System.out.println("Number of vacancy :" + course.checkVacancies(index.getIndexNumber()));
                    } catch (Exception e) {

                    }

                }
                case 5 -> { //change index
                    //print registered courses
                    printRegisteredCourse();

                    boolean courseFormat = false;
                    boolean indexFormat = false;

                    //which course user want to change
                    Course course = null;
                    boolean userRegisteredCourse = false;
                    do {
                        try {
                            System.out.print("Enter course code: ");
                            String courseCode = _scanner.nextLine();
                            //check courseCode format
                            courseFormat = checkCourseCodeFormat(courseCode);
                            course = Factory.getTextCourseDataAccess().getCourse(courseCode);
                            //check if course exist in database
                            if(course == null){
                                System.out.println("Course does not exist");
                            }
                            //check if user registered this course before
                            userRegisteredCourse = gotRegisterThisCourse(_user, courseCode);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (!courseFormat || course == null || !userRegisteredCourse);

                    //user old index number
                    //fetch old index
                    int currIndexNumber = _user.getRegisteredCourses().get(course.getCourseCode());
                    Index index = course.getIndex(currIndexNumber);

                    //user new index number
                    Index newIndexNumber = null;
                    do {
                        System.out.print("Enter new index number: ");
                        int indexNumber = _scanner.nextInt();
                        _scanner.nextLine();

                        //check if index is 6 integers
                        indexFormat = checkIndexFormat(indexNumber);
                        newIndexNumber = course.getIndex(indexNumber);
                        //check if index exist in database
                        if(newIndexNumber == null) {
                            System.out.println("Index does not exist");
                        }
                    } while (!indexFormat || newIndexNumber == null);

                    //get user matricNumber
                    String matric = _user.getMatricNumber();
                    try {
                        //delete old index
                        StudentCourseRegistrar studentCourseRegistrar = Factory.createStudentCourseRegistrar();
                        studentCourseRegistrar.deleteRegistration(matric, course.getCourseCode(), index.getIndexNumber());

                        //add new index
                        studentCourseRegistrar.addRegistration(matric, course.getCourseCode(), newIndexNumber.getIndexNumber());
                        System.out.printf("successfully swapped %s from %s to %s\n", course.getCourseCode(), index.getIndexNumber(), newIndexNumber.getIndexNumber());
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

                    boolean courseFormat = false;
                    boolean userRegisteredCourse = false;

                    //what course you want to swap
                    Course course = null;
                    do {
                        try {
                            System.out.print("Enter course code: ");
                            String courseCode = _scanner.nextLine();
                            //check courseCode format
                            courseFormat = checkCourseCodeFormat(courseCode);
                            course = Factory.getTextCourseDataAccess().getCourse(courseCode);
                            //check if course exist in database
                            if(course == null){
                                System.out.println("Course does not exist");
                            }
                            //check if user registered this course before
                            userRegisteredCourse = gotRegisterThisCourse(_user, courseCode);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (!courseFormat || !userRegisteredCourse);

                    //what index
                    //change to auto fetch user old index
                    int currIndexNumber = _user.getRegisteredCourses().get(course.getCourseCode());
                    Index index = course.getIndex(currIndexNumber);

                    //print user current index
                    System.out.println("User current index is: " + currIndexNumber);

                    //create peer
                    AbstractUser absPeer = null;
                    while(absPeer == null){
                        //peer username
                        System.out.println("Enter peer username:");
                        String peerUsername = _scanner.nextLine();
                        //peer password
                        System.out.println("Enter peer password:");
                        String peerPassword = _scanner.nextLine();

                        //authenticate peer
                        try {
                            absPeer = Factory.getTextUserDataAccess().authenticate(peerUsername, peerPassword);
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("error reading files");
                        } catch (PasswordStorage.InvalidHashException | PasswordStorage.CannotPerformOperationException e) {
                            System.out.println("error encountered when hashing");
                        }
                    }
                    //downcast peer from AbstractUser to Student
                    Student studentPeer = (Student) absPeer;

                    //change to auto fetch peer old index
                    int peerCurrIndex = studentPeer.getRegisteredCourses().get(course.getCourseCode());
                    Index peerIndex = course.getIndex(peerCurrIndex);
                    //print peer name
                    System.out.println("Peer name: " + studentPeer.getName());
                    //print peer matric number
                    System.out.println("Peer matric number: " + studentPeer.getMatricNumber());
                    //print peer current index
                    System.out.println("Peer current index is: " + peerCurrIndex);

                    try{
                        //drop course for peer
                        StudentCourseRegistrar studentCourseRegistrar = Factory.createStudentCourseRegistrar();
                        studentCourseRegistrar.deleteRegistration(studentPeer.getMatricNumber(),course.getCourseCode(),peerIndex.getIndexNumber());

                        //drop course for user
                        studentCourseRegistrar.deleteRegistration(_user.getMatricNumber(),course.getCourseCode(),index.getIndexNumber());

                        //add course for peer with user index
                        studentCourseRegistrar.addRegistration(studentPeer.getMatricNumber(),course.getCourseCode(),index.getIndexNumber());

                        //add course for user with peer index
                        studentCourseRegistrar.addRegistration(_user.getMatricNumber(),course.getCourseCode(),peerIndex.getIndexNumber());
                    } catch (NonExistentCourseException e) {
                        System.out.println("no such course");
                    } catch (NonExistentRegistrationException e) {
                        System.out.println("no such registration");
                    } catch (InsufficientAUsException e) {
                        System.out.println("enough AUs");
                    } catch (NonExistentUserException e) {
                        System.out.println("no such user");
                    } catch (InvalidAccessPeriodException e) {
                        System.out.println("registration period have not started/ is over");
                    } catch (ExistingRegistrationException e) {
                        System.out.println("existing registration");
                    } catch (ExistingCourseException e) {
                        System.out.println("existing course");
                    } catch (ExistingUserException e) {
                        System.out.println("existing user in course");
                    } catch (MaxClassSizeException e) {
                        System.out.println("max class size");
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("error reading/writing to registration file");
                    }

                    System.out.println("Swap successful!!");

                    int newIndex = _user.getRegisteredCourses().get(course.getCourseCode());
                    int peerNewIndex = studentPeer.getRegisteredCourses().get(course.getCourseCode());

                    //print user new index
                    System.out.println("User new index is: " + newIndex);
                    //print peer new index
                    System.out.println("Peer new index is: " + peerNewIndex);
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


    public boolean gotRegisterThisCourse(Student student, String courseCode){
        Hashtable<String, Integer> userCourseList = student.getRegisteredCourses();
        boolean userRegisteredCourse = userCourseList.containsKey(courseCode);
        if(!userRegisteredCourse){
            System.out.println("You did not register for this course. Try again.");
            return false;
        }
        else{
            return true;
        }
    }
}