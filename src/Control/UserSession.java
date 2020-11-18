package Control;

import DataAccessObject.ICourseDataAccessObject;
import Helper.Factory;
import Helper.InputValidator;
import Helper.PasswordStorage;
import ValueObject.AbstractUser;
import ValueObject.Course;
import ValueObject.Index;
import ValueObject.Student;
import Exception.*;

import java.io.Console;
import java.io.IOException;
import java.util.Arrays;
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
            System.out.println("\t\twelcome " + _user.getName());
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
                case 1 -> {
                    //get course input
                    boolean validCourseCode;
                    String courseCodeInput;
                    do {
                        try {
                            System.out.println(Factory.getTextCourseDataAccess().toString());
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("error reading file");
                        }
                        System.out.println("Enter course code: ");
                        courseCodeInput = _scanner.nextLine();

                        validCourseCode = InputValidator.courseStrMatcher(courseCodeInput);
                        if (!validCourseCode) {
                            System.out.println("Please enter a course code in its valid format.");
                        }
                    } while (!validCourseCode);

                    //get index input
                    boolean validIndex;
                    String indexInputStr;
                    do {
                        try {
                            System.out.println(Factory.getTextCourseDataAccess().getCourse(courseCodeInput).toString());
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("error reading file");
                        }
                        System.out.println("Enter index: ");
                        indexInputStr = _scanner.nextLine();

                        validIndex = InputValidator.indexStrMatcher(indexInputStr);
                        if (!validIndex) {
                            System.out.println("Please enter an index in its valid format.");
                        }
                    } while (!validIndex);
                    addCourse(courseCodeInput, Integer.parseInt(indexInputStr));
                }
                case 2 -> {
                    //get course input
                    boolean validCourseCode;
                    String courseCodeInput;
                    do {
                        printRegisteredCourse();
                        System.out.println("Enter course code: ");
                        courseCodeInput = _scanner.nextLine();
                        validCourseCode = InputValidator.courseStrMatcher(courseCodeInput);
                        if (!validCourseCode) {
                            System.out.println("Please enter a course code in its valid format.");
                        }
                    } while (!validCourseCode);
                    dropCourse(courseCodeInput);
                }

                case 3 -> //print registered course
                        printRegisteredCourse();
                case 4 -> {
                    boolean validCourseCode = false;
                    Course course = null;
                    do {
                        try {
                            ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
                            System.out.println(courseDataAccessObject.toString());
                            System.out.print("course code: ");
                            String courseCode = _scanner.nextLine();
                            //check courseCode format
                            validCourseCode = InputValidator.courseStrMatcher(courseCode);
                            course = courseDataAccessObject.getCourse(courseCode);
                            //check if course exist in database
                            if(course == null) {
                                System.out.println("Course does not exist");
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("error reading the file");
                        }
                    } while (!validCourseCode || course == null);

                    //enter which index user want
                    boolean validIndex;
                    Index index;
                    do {
                        System.out.println(course.allInfoToString());
                        System.out.print("index number: ");
                        String indexNumber = _scanner.nextLine();

                        //check if index is 6 integers
                        validIndex = InputValidator.indexStrMatcher(indexNumber);
                        index = course.getIndex(Integer.parseInt(indexNumber));
                        //check if index exist in database
                        if(index == null) {
                            System.out.println("Index does not exist");
                        }
                    } while (!validIndex || index == null);

                    //print vacancy
                    try {
                        System.out.println("Number of vacancy :" + course.checkVacancies(index.getIndexNumber()));
                    } catch (NonExistentIndexException e) {
                        System.out.println("no such index");
                    }
                }

                case 5 -> { //change index
                    boolean validCourseCode = false;
                    boolean validIndex;

                    //which course user want to change
                    Course course = null;
                    boolean validRegisteredCourse = false;
                    do {
                        try {
                            //print registered courses
                            printRegisteredCourse();
                            System.out.print("course code: ");
                            String courseCode = _scanner.nextLine();
                            //check courseCode format
                            validCourseCode = InputValidator.courseStrMatcher(courseCode);
                            course = Factory.getTextCourseDataAccess().getCourse(courseCode);
                            //check if course exist in database
                            if(course == null) {
                                System.out.println("Course does not exist");
                            }
                            //check if user registered this course before
                            validRegisteredCourse = _user.getRegisteredCourses().containsKey(courseCode);
                            if(validRegisteredCourse) {
                                System.out.println("course selected is not registered");
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("error reading the file");
                        }
                    } while (!validCourseCode || course == null || !validRegisteredCourse);

                    //user old index number
                    //fetch old index
                    int currIndexNumber = _user.getRegisteredCourses().get(course.getCourseCode());
                    Index index = course.getIndex(currIndexNumber);
                    //get user matricNumber
                    String matric = _user.getMatricNumber();

                    //user new index number
                    Index newIndexNumber;
                    do {
                        System.out.println(course.allInfoToString());
                        System.out.print("Enter new index number: ");
                        String indexNumber = _scanner.nextLine();

                        //check if index is 6 integers
                        validIndex = InputValidator.indexStrMatcher(indexNumber);
                        newIndexNumber = course.getIndex(Integer.parseInt(indexNumber));
                        //check if index exist in database
                        if(newIndexNumber == null) {
                            System.out.println("Index does not exist");
                        }
                    } while (!validIndex || newIndexNumber == null);

                    try {
                        //delete old index
                        StudentCourseRegistrar studentCourseRegistrar = Factory.createStudentCourseRegistrar();
                        studentCourseRegistrar.deleteRegistration(matric, course.getCourseCode(), index.getIndexNumber());

                        //add new index
                        studentCourseRegistrar.addRegistration(matric, course.getCourseCode(), newIndexNumber.getIndexNumber());
                        System.out.printf("successfully swapped %s from %s to %s\n", course.getCourseCode(), index.getIndexNumber(), newIndexNumber.getIndexNumber());
                    } catch (ClassNotFoundException | IOException e) {
                        System.out.println("error reading the file");
                    } catch (Exception e) {
                        System.out.println("error occurred when saving");
                    }
                }

                case 6 -> { //swap with peer
                    boolean validCourseCode = false;
                    boolean validRegisteredCourse = false;

                    //what course you want to swap
                    Course course = null;
                    do {
                        try {
                            //print registered course
                            printRegisteredCourse();
                            System.out.print("course code: ");
                            String courseCode = _scanner.nextLine();
                            //check courseCode format
                            validCourseCode = InputValidator.courseStrMatcher(courseCode);
                            course = Factory.getTextCourseDataAccess().getCourse(courseCode);
                            //check if course exist in database
                            if(course == null) {
                                System.out.println("Course does not exist");
                            }
                            //check if user registered this course before
                            validRegisteredCourse = _user.getRegisteredCourses().containsKey(courseCode);
                            if(validRegisteredCourse) {
                                System.out.println("course selected is not registered");
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("error reading the file");
                        }
                    } while (!validCourseCode || course == null || !validRegisteredCourse);

                    //what index
                    //change to auto fetch user old index
                    int currIndexNumber = _user.getRegisteredCourses().get(course.getCourseCode());
                    Index index = course.getIndex(currIndexNumber);

                    //print user current index
                    System.out.println("User current index is: " + currIndexNumber);

                    //create peer
                    AbstractUser absPeer = null;
                    do {
                        //peer username
                        System.out.println("Enter peer username:");
                        String peerUsername = _scanner.nextLine();
                        //peer password
//                        System.out.println("Enter peer password:");
//                        String peerPassword = _scanner.nextLine();
                        Console console = System.console();
                        if (console == null) {
                            System.out.println("Couldn't get Console instance");
                            System.exit(0);
                        }
                        String peerPassword = Arrays.toString(console.readPassword("Enter your password: "));

                        //authenticate peer
                        try {
                            absPeer = Factory.getTextUserDataAccess().authenticate(peerUsername, peerPassword);
                        } catch (IOException | ClassNotFoundException e) {
                            System.out.println("error reading file");
                        } catch (PasswordStorage.InvalidHashException | PasswordStorage.CannotPerformOperationException e) {
                            System.out.println("error encountered when hashing");
                        }
                    } while(absPeer == null);
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
                        StudentCourseRegistrar studentCourseRegistrar = Factory.createStudentCourseRegistrar();
                        //drop course for peer
                        studentCourseRegistrar.deleteRegistration(studentPeer.getMatricNumber(),course.getCourseCode(),peerIndex.getIndexNumber());
                        //drop course for user
                        studentCourseRegistrar.deleteRegistration(_user.getMatricNumber(),course.getCourseCode(),index.getIndexNumber());
                        //add course for peer with user index
                        studentCourseRegistrar.addRegistration(studentPeer.getMatricNumber(),course.getCourseCode(),index.getIndexNumber());
                        //add course for user with peer index
                        studentCourseRegistrar.addRegistration(_user.getMatricNumber(),course.getCourseCode(),peerIndex.getIndexNumber());
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("error reading/writing to registration file");
                    } catch (Exception e) {
                        System.out.println("error saving registrations");
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
            System.out.println("_______Registered Courses_______");
            System.out.println("Course code |\t Index");
            registered.forEach((key, value) -> System.out.println(key + ":\t" + value));
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("error reading file");
        }
    }

    private void addCourse(String courseCode, int indexNumber) {
        printRegisteredCourse();
        String matricNumber = _user.getMatricNumber();
        try {
            StudentCourseRegistrar studentCourseRegistration = Factory.createStudentCourseRegistrar();
            studentCourseRegistration.addRegistration(matricNumber, courseCode, indexNumber);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("error reading file");
        } catch (NonExistentUserException e) {
            System.out.println("no such student");
        } catch (InsufficientAUsException e) {
            System.out.println("Course exceeds AU limit.");
        } catch (InvalidAccessPeriodException e) {
            System.out.println("registration period have not started/over");
        } catch (ExistingRegistrationException e) {
            System.out.println("course already registered");
        } catch (Exception e) {
            System.out.println("error adding course");
        }
    }

    private void dropCourse(String courseCode) {
        String matricNumber = _user.getMatricNumber();

        //get index number of course that student wants to drop, confirms course is already reg
        Hashtable<String, Integer> registeredCourses = _user.getRegisteredCourses();
        int indexNumber = 0;

        if (registeredCourses.containsKey(courseCode)) {
            indexNumber = registeredCourses.get(courseCode);
        } else {
            System.out.println("Course has not been registered yet.");
        }

        try {
            StudentCourseRegistrar studentCourseRegistration = Factory.createStudentCourseRegistrar();
            studentCourseRegistration.deleteRegistration(matricNumber, courseCode, indexNumber);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("error reading file");
        } catch (NonExistentUserException e) {
            System.out.println("no such student");
        } catch (InvalidAccessPeriodException e) {
            System.out.println("registration period have not started/over");
        } catch (NonExistentRegistrationException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("error dropping course");
        }
    }
}