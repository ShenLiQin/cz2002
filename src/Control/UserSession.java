package Control;

import DataAccessObject.ICourseDataAccessObject;
import Helper.Factory;
import Helper.InputValidator;
import Helper.PasswordStorage;
import ValueObject.*;
import Exception.*;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;

import java.io.Console;
import java.io.IOException;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Scanner;

public class UserSession implements ISession{
    private TextIO _textIO;
    private TextTerminal _terminal;
    private final Scanner _scanner;
    private boolean loggedIn = false;
    private Student _user;

    public UserSession(TextIO textIO, TextTerminal terminal, AbstractUser user) {
        _textIO = textIO;
        _terminal = terminal;
        _user = (Student) user;
        _scanner = new Scanner(System.in);
    }

    @Override
    public boolean logout() {
        return loggedIn;
    }

    @Override
    public void exit() {
        System.exit(0);
    }

    private final String user =
            "_   _                      ___                _      _                            _ \n" +
                    " | | | |  ___  ___   _ _    |   \\   __ _   ___ | |_   | |__   ___   __ _   _ _   __| |\n" +
                    " | |_| | (_-< / -_) | '_|   | |) | / _` | (_-< | ' \\  | '_ \\ / _ \\ / _` | | '_| / _` |\n" +
                    " \\___/  /__/ \\___| |_|     |___/  \\__,_| /__/ |_||_| |_.__/ \\___/ \\__,_| |_|   \\__,_|\n" +
                    "                                                                                      ";

    private final String userOptions =
            "1. Add Course\n" +
                    "2. Drop Course\n" +
                    "3. Check/Print Courses Registered\n" +
                    "4. Check Vacancies Available\n" +
                    "5. Change Index Number of Course\n" +
                    "6. Swap Index Number with Another Student\n" +
                    "7. Log out\n" +
                    "8. Exit\n";


    @Override
    public void run() {
        int choice;
        _terminal.getProperties().setPromptBold(true);

        do {
            _terminal.resetToBookmark("clear");
            _terminal.println(user);
            _terminal.setBookmark("user");
            _terminal.println(userOptions);
            _terminal.println("\t\twelcome " + _user.getName());
            choice = _textIO.newIntInputReader().withMinVal(1).withMaxVal(8).read("Enter your choice: ");
            _terminal.resetToBookmark("user");

            /*choice = _scanner.nextInt();
            _scanner.nextLine();*/

            switch (choice) {
                case 1 -> {
                    //get course input
                    boolean validCourseCode = false;
                    String courseCodeInput = "";
                    Course course = null;
                    _terminal.println("add course");
                    _terminal.setBookmark("add course home screen");
                    do {
                        _terminal.setBookmark("add course -- course code");
                        try {
                            _terminal.println(Factory.getTextCourseDataAccess().toString());
                            courseCodeInput = _textIO.newStringInputReader().read("Enter course code: ");
                            validCourseCode = InputValidator.courseStrMatcher(courseCodeInput);
                            ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
                            course = courseDataAccessObject.getCourse(courseCodeInput);

                            if (!validCourseCode) {
                                _terminal.resetToBookmark("add course home screen");
                                _terminal.getProperties().setPromptColor("red");
                                _terminal.println("Please enter a course code in its valid format.");
                                _terminal.getProperties().setPromptColor("white");
                                continue;
                            }
                            if (course == null) {
                                _terminal.resetToBookmark("add course home screen");
                                _terminal.getProperties().setPromptColor("red");
                                _terminal.println("Course does not exist");
                                _terminal.getProperties().setPromptColor("white");
                                continue;
                            }
                        } catch (IOException | ClassNotFoundException e) {
                            _terminal.resetToBookmark("add course home screen");
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("error reading file");
                            _terminal.getProperties().setPromptColor("white");
                        }
                        /*_terminal.println("Enter course code: ");
                        courseCodeInput = _scanner.nextLine();*/
                        //_terminal.setBookmark("add course -- course code");

                    } while (!validCourseCode | course == null);

                    //get index input
                    boolean validIndex;
                    String indexInputStr;
                    //_terminal.setBookmark("add course -- index");
                    do {
                        try {
                            _terminal.println(Factory.getTextCourseDataAccess().getCourse(courseCodeInput).toString());
                        } catch (IOException | ClassNotFoundException e) {
                            _terminal.resetToBookmark("add course home screen");
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("error reading file");
                            _terminal.getProperties().setPromptColor("white");
                            _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");
                        } catch (Exception e) {
                            _terminal.resetToBookmark("add course home screen");
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("error, please try again");
                            _terminal.getProperties().setPromptColor("white");
                            _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");
                        }
                        /*System.out.println("Enter index: ");
                        indexInputStr = _scanner.nextLine(); */
                        indexInputStr = _textIO.newStringInputReader().read("Enter index: ");

                        validIndex = InputValidator.indexStrMatcher(indexInputStr);
                        if (!validIndex) {
                            _terminal.resetToBookmark("add course home screen");
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("Please enter an index in its valid format.");
                            _terminal.getProperties().setPromptColor("white");
                        }
                    } while (!validIndex | course == null);
                    addCourse(courseCodeInput, Integer.parseInt(indexInputStr));
                    _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");
                }
                case 2 -> {
                    //get course input
                    boolean validCourseCode;
                    String courseCodeInput;
                    _terminal.println("drop course");
                    _terminal.setBookmark("drop course home screen");
                    do {
                        printRegisteredCourse();
                        /*System.out.println("Enter course code: ");
                        courseCodeInput = _scanner.nextLine(); */
                        courseCodeInput = _textIO.newStringInputReader().read("Enter course code: ");
                        validCourseCode = InputValidator.courseStrMatcher(courseCodeInput);
                        if (!validCourseCode) {
                            _terminal.resetToBookmark("drop course home screen");
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("Please enter a course code in its valid format.");
                            _terminal.getProperties().setPromptColor("white");
                        }
                    } while (!validCourseCode);
                    dropCourse(courseCodeInput);
                    _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");
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
                        String peerPassword;
                        Console console = System.console();
                        if (console == null) {
//                    System.out.println("Couldn't get Console instance");
                            peerPassword = _scanner.nextLine();
                        } else {
                            peerPassword = Arrays.toString(console.readPassword("Enter your password: "));
                        }

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