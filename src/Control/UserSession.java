package Control;

import DataAccessObject.ICourseDataAccessObject;
import Helper.Factory;
import Helper.InputValidator;
import Helper.PasswordStorage;
import ValueObject.*;
import Exception.*;
import com.sun.mail.imap.protocol.INTERNALDATE;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;

import java.awt.*;
import java.io.Console;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class UserSession implements ISession{
    private TextIO _textIO;
    private TextTerminal _terminal;
    private boolean loggedIn = false;
    private Student _user;

    public UserSession(TextIO textIO, TextTerminal terminal, AbstractUser user) {
        _textIO = textIO;
        _terminal = terminal;
        _user = (Student) user;
    }

    @Override
    public boolean logout() {
        _terminal.resetToBookmark("clear");
        return loggedIn;
    }

    @Override
    public void exit() {
        System.exit(0);
    }

    private final String user =
                    "  _   _                      ___                _      _                            _ \n" +
                    " | | | |  ___  ___   _ _    |   \\   __ _   ___ | |_   | |__   ___   __ _   _ _   __| |\n" +
                    " | |_| | (_-< / -_) | '_|   | |) | / _` | (_-< | ' \\  | '_ \\ / _ \\ / _` | | '_| / _` |\n" +
                    "  \\___/  /__/ \\___| |_|     |___/  \\__,_| /__/ |_||_| |_.__/ \\___/ \\__,_| |_|   \\__,_|\n" +
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
            _terminal.setBookmark("clear");
            _terminal.println(user);
            _terminal.setBookmark("user");
            _terminal.println(userOptions);
            _terminal.println("\t\twelcome " + _user.getName());
            choice = _textIO.newIntInputReader()
                    .withMinVal(1).withMaxVal(8)
                    .read("Enter your choice: ");
            _terminal.resetToBookmark("user");

            switch (choice) {
                case 1 -> {
                    _terminal.println("add course");
                    printRegisteredCourse();
                    try {
                        ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
                        List<String> coursesString = courseDataAccessObject.getCourses();
                        if (_user.getRegisteredCourses().keySet().containsAll(coursesString)) {
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("no available courses to register");
                            break;
                        }
                        String courseCodeInput = _textIO.newStringInputReader()
                                .withNumberedPossibleValues(coursesString)
                                .read("Enter course code: ");
                        Course course = courseDataAccessObject.getCourse(courseCodeInput);

                        String indexInputStr = _textIO.newStringInputReader()
                                .withNumberedPossibleValues(course.getIndexes())
                                .read("Enter index: ");
                        addCourse(courseCodeInput, Integer.parseInt(indexInputStr));
                        printRegisteredCourse();
                    } catch (IOException | ClassNotFoundException e) {
                        _terminal.getProperties().setPromptColor("red");
                        _terminal.println("error reading file");
                    } finally {
                        _terminal.getProperties().setPromptColor("white");
                        _textIO.newStringInputReader().withDefaultValue(" ")
                                .read("press enter to continue");
                    }
                }
                case 2 -> {
                    _terminal.println("drop course");
                    printRegisteredCourse();
                    try {
                        _user = Factory.getTextUserDataAccess().getStudent(_user.getMatricNumber());
                        Hashtable<String, Integer> registered = _user.getRegisteredCourses();
                        List<String> registeredCoursesStr = new ArrayList<>(registered.keySet());
                        if (registeredCoursesStr.size() == 0) {
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("no course to drop");
                            break;
                        }
                        String courseCodeInput = _textIO.newStringInputReader()
                                .withNumberedPossibleValues(registeredCoursesStr)
                                .read("Select course to drop: ");
                        dropCourse(courseCodeInput);
                        printRegisteredCourse();
                    } catch (IOException | ClassNotFoundException e) {
                        _terminal.getProperties().setPromptColor("red");
                        _terminal.println("error reading file");
                    }finally {
                        _terminal.getProperties().setPromptColor("white");
                        _textIO.newStringInputReader().withDefaultValue(" ")
                                .read("press enter to continue");
                    }
                }

                case 3 -> {//print registered course
                    printRegisteredCourse();
                    _textIO.newStringInputReader().withDefaultValue(" ")
                            .read("press enter to continue");
                }

                case 4 -> {
                    try {
                        _terminal.println("Check course vacancies");
                        ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
                        List<String> coursesString = courseDataAccessObject.getCourses();
                        String courseCode = _textIO.newStringInputReader()
                                .withNumberedPossibleValues(coursesString)
                                .read("select course to check vacancies for: ");
                        Course course = courseDataAccessObject.getCourse(courseCode);
                        List<String> indexString = course.getIndexes();
                        String indexNum = _textIO.newStringInputReader()
                                .withNumberedPossibleValues(indexString)
                                .read("select index to check vacancies for: ");
                        int vacancies = course.checkVacancies(Integer.parseInt(indexNum));
                        _terminal.getProperties().setPromptColor(Color.GREEN);
                        _terminal.println("Successfully retrieved vacancies");
                        _terminal.println("The vacancies for " + indexNum + " of " + courseCode + " is " + vacancies);
                    } catch (IOException | ClassNotFoundException e) {
                        _terminal.getProperties().setPromptColor("red");
                        _terminal.println("file not found");
                    } catch (NonExistentIndexException e) {
                        _terminal.getProperties().setPromptColor("red");
                        _terminal.println("no such index");
                    } finally {
                        _terminal.getProperties().setPromptColor("white");
                        _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");
                    }
                }

                case 5 -> { //change index
                    _terminal.println("Change index");
                    try {
                        _user = Factory.getTextUserDataAccess().getStudent(_user.getMatricNumber());
                        Hashtable<String, Integer> registered = _user.getRegisteredCourses();
                        List<String> registeredCoursesStr = new ArrayList<>(registered.keySet());
                        if (registeredCoursesStr.size() == 0) {
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("no course registered");
                            break;
                        }
                        String courseCodeInput = _textIO.newStringInputReader()
                                .withNumberedPossibleValues(registeredCoursesStr)
                                .read("Select course to swap: ");
                        int currIndexNumber = _user.getRegisteredCourses().get(courseCodeInput);
                        _terminal.getProperties().setPromptColor(Color.GREEN);
                        _terminal.println("User current index is: " + currIndexNumber);
                        _terminal.getProperties().setPromptColor("white");
                        ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
                        List<String> indexesStr = courseDataAccessObject.getCourse(courseCodeInput).getIndexes();
                        String indexNumberInput = _textIO.newStringInputReader()
                                .withNumberedPossibleValues(indexesStr)
                                .read("Select index to swap to: ");
                        int newIndexNumber = Integer.parseInt(indexNumberInput);
                        if (newIndexNumber == currIndexNumber) {
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("You are already in the index.");
                            _terminal.getProperties().setPromptColor("white");
                            break;
                        }

                        //get user matricNumber
                        String matricNumber = _user.getMatricNumber();
                        //delete old index
                        StudentCourseRegistrar studentCourseRegistrar = Factory.createStudentCourseRegistrar();
                        studentCourseRegistrar.deleteRegistration(matricNumber, courseCodeInput, currIndexNumber);

                        //add new index
                        studentCourseRegistrar.addRegistration(matricNumber, courseCodeInput, newIndexNumber);
                        _terminal.getProperties().setPromptColor(Color.green);
                        _terminal.printf("successfully swapped %s from %s to %s\n", courseCodeInput, currIndexNumber, newIndexNumber);
                    } catch (IOException | ClassNotFoundException e) {
                        _terminal.getProperties().setPromptColor("red");
                        _terminal.println("file not found");
                    } catch (InvalidAccessPeriodException e) {
                        _terminal.getProperties().setPromptColor("red");
                        _terminal.println("registration period is over/haven't started");
                    } catch (MaxEnrolledStudentsException e) {
                        _terminal.getProperties().setPromptColor("red");
                        _terminal.println("maximum enrolled students, added to waiting list instead");
                    } catch (ClashingTimeTableException e) {
                        _terminal.getProperties().setPromptColor("red");
                        _terminal.println("unable to add course, time table clashes");
                        _terminal.println("deleted course with clashing timetable...");
                    } catch (Exception e) {
                        _terminal.getProperties().setPromptColor("red");
                        _terminal.println("error saving file");
                    } finally {
                        _terminal.getProperties().setPromptColor("white");
                        _textIO.newStringInputReader().withDefaultValue(" ")
                                .read("press enter to continue");
                    }
                }

                case 6 -> { //swap with peer
                    int currIndexNumber;
                    String courseCodeInput;
                    try {
                        _user = Factory.getTextUserDataAccess().getStudent(_user.getMatricNumber());
                        Hashtable<String, Integer> registered = _user.getRegisteredCourses();
                        List<String> registeredCoursesStr = new ArrayList<>(registered.keySet());
                        if (registeredCoursesStr.size() == 0) {
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("no course registered");
                            _textIO.newStringInputReader().withDefaultValue(" ")
                                    .read("press enter to continue");
                            break;
                        }
                        courseCodeInput = _textIO.newStringInputReader()
                                .withNumberedPossibleValues(registeredCoursesStr)
                                .read("Select course to swap: ");
                        currIndexNumber = _user.getRegisteredCourses().get(courseCodeInput);
                        _terminal.getProperties().setPromptColor(Color.green);
                        _terminal.println("User current index is: " + currIndexNumber);
                    } catch (IOException | ClassNotFoundException e) {
                        _terminal.getProperties().setPromptColor("red");
                        _terminal.println("error reading the file");
                        break;
                    } finally {
                        _terminal.getProperties().setPromptColor("white");
                    }

                    AbstractUser absPeer = null;
                    _terminal.setBookmark("Enter peer username:");
                    do {
                        String peerUsername = _textIO.newStringInputReader()
                                .read("Enter peer username: ");
                        String peerPassword = _textIO.newStringInputReader()
                                .withMinLength(6)
                                .withInputMasking(true)
                                .read("Enter peer password: ");
                        //authenticate peer
                        try {
                            absPeer = Factory.getTextUserDataAccess().authenticate(peerUsername, peerPassword);
                            if (!(absPeer instanceof Student)) {
                                _terminal.resetToBookmark("Enter peer username:");
                                _terminal.getProperties().setPromptColor("red");
                                _terminal.println("Wrong username/password. Try again.");
                            } else {
                                //downcast peer from AbstractUser to Student
                                Student studentPeer = (Student) absPeer;
                                //change to auto fetch peer old index.
                                if (studentPeer == _user) {
                                    _terminal.resetToBookmark("Enter peer username:");
                                    _terminal.getProperties().setPromptColor("red");
                                    _terminal.println("you cant swap index with yourself");
                                    break;
                                }
                                boolean containsKey = studentPeer.getRegisteredCourses().containsKey(courseCodeInput);
                                if (!containsKey) {
                                    _terminal.getProperties().setPromptColor("red");
                                    _terminal.println("Peer is not registered for this course");
                                    _terminal.getProperties().setPromptColor("white");
                                } else {
                                    //check if peer and user are already in same index
                                    int peerIndexNumber = studentPeer.getRegisteredCourses().get(courseCodeInput);
                                    if (peerIndexNumber == currIndexNumber) {
                                        //_terminal.println("You are already in the index.");
                                        _terminal.getProperties().setPromptColor("red");
                                        _terminal.println("Both of you are in the same index.");
                                        _terminal.getProperties().setPromptColor("white");
                                    } else {
                                        StudentCourseRegistrar studentCourseRegistrar = Factory.createStudentCourseRegistrar();
                                        //drop course for peer
                                        studentCourseRegistrar.deleteRegistration(studentPeer.getMatricNumber(), courseCodeInput, peerIndexNumber);
                                        //drop course for user
                                        studentCourseRegistrar.deleteRegistration(_user.getMatricNumber(), courseCodeInput, currIndexNumber);
                                        //add course for peer with user index
                                        studentCourseRegistrar.addRegistration(studentPeer.getMatricNumber(), courseCodeInput, currIndexNumber);
                                        //add course for user with peer index
                                        studentCourseRegistrar.addRegistration(_user.getMatricNumber(), courseCodeInput, peerIndexNumber);
                                        _terminal.getProperties().setPromptColor(Color.GREEN);
                                        _terminal.println("Successfully swapped indexes");
                                        _terminal.println("Your new index is: " + peerIndexNumber);
                                    }
                                }

                            }
                        } catch (IOException | ClassNotFoundException e) {
                            _terminal.setBookmark("Enter peer username:");
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("error reading file");
                        } catch (PasswordStorage.InvalidHashException | PasswordStorage.CannotPerformOperationException e) {
                            _terminal.setBookmark("Enter peer username:");
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("error encountered when hashing");
                        } catch (Exception e) {
                            _terminal.setBookmark("Enter peer username:");
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("error encountered when saving");
                        } finally {
                            _terminal.getProperties().setPromptColor("white");
                        }
                    } while (absPeer == null);
                    _textIO.newStringInputReader().withDefaultValue(" ")
                            .read("press enter to continue");
                }
                case 7 -> loggedIn = false;
                case 8 -> exit();
            }
        } while (choice > 0 && choice < 7);
    }

    private void printRegisteredCourse() {
        try {
            _terminal.getProperties().setPromptColor(Color.green);
            _user = Factory.getTextUserDataAccess().getStudent(_user.getMatricNumber());
            Hashtable<String, Integer> registered = _user.getRegisteredCourses();
            _terminal.println("_______Registered Courses_______");
            _terminal.println("Course code |\t Index");
            registered.forEach((key, value) -> _terminal.printf("%s \t:\t %d\n", key, value));
        } catch (IOException | ClassNotFoundException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("error reading file");
        } finally {
            _terminal.getProperties().setPromptColor("white");
        }
    }

    private void addCourse(String courseCode, int indexNumber) {
        String matricNumber = _user.getMatricNumber();
        try {
            StudentCourseRegistrar studentCourseRegistration = Factory.createStudentCourseRegistrar();
            studentCourseRegistration.addRegistration(matricNumber, courseCode, indexNumber);
            _terminal.getProperties().setPromptColor(Color.GREEN);
            _terminal.println("successfully added course");
        } catch (IOException | ClassNotFoundException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("error reading file");
        } catch (InsufficientAUsException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("Course exceeds AU limit.");
        } catch (InvalidAccessPeriodException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("registration period have not started/over");
        } catch (ExistingCourseException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("course already registered");
        } catch (ClashingTimeTableException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("unable to add course, time table clashes");
        } catch (Exception e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("error adding course");
        } finally {
            _terminal.getProperties().setPromptColor("white");
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
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("Course has not been registered yet.");
        }
        try {
            StudentCourseRegistrar studentCourseRegistration = Factory.createStudentCourseRegistrar();
            studentCourseRegistration.deleteRegistration(matricNumber, courseCode, indexNumber);
            _terminal.getProperties().setPromptColor(Color.GREEN);
            _terminal.println("Successfully dropped course");
            _terminal.getProperties().setPromptColor("white");
        } catch (IOException | ClassNotFoundException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("error reading file");
        } catch (NonExistentUserException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("no such student");
        } catch (InvalidAccessPeriodException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("registration period have not started/over");
        } catch (NonExistentRegistrationException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("non existent registration");
        } catch (Exception e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("error dropping course");
        } finally {
            _terminal.getProperties().setPromptColor("white");
        }
    }
}