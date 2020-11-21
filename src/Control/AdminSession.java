package Control;

import DataAccessObject.ICourseDataAccessObject;
import DataAccessObject.IRegistrationDataAccessObject;
import DataAccessObject.IUserDataAccessObject;
import Helper.Factory;
import Helper.InputValidator;
import Helper.PasswordStorage;
import ValueObject.*;
import Exception.*;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextTerminal;

import java.awt.*;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class AdminSession implements ISession{
    private TextIO _textIO;
    private TextTerminal _terminal;
    private boolean loggedIn = true;
    private final Staff _user;
    private Scanner _scanner;

    public AdminSession(TextIO textIO, TextTerminal terminal, AbstractUser user) {
        _textIO = textIO;
        _terminal = terminal;
        _user = (Staff) user;
        _scanner = new Scanner(System.in);
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

    private final String admin =
            "    _        _           _            ___                _      _                            _ \n" +
                    "   /_\\    __| |  _ __   (_)  _ _     |   \\   __ _   ___ | |_   | |__   ___   __ _   _ _   __| |\n" +
                    "  / _ \\  / _` | | '  \\  | | | ' \\    | |) | / _` | (_-< | ' \\  | '_ \\ / _ \\ / _` | | '_| / _` |\n" +
                    " /_/ \\_\\ \\__,_| |_|_|_| |_| |_||_|   |___/  \\__,_| /__/ |_||_| |_.__/ \\___/ \\__,_| |_|   \\__,_|\n" +
                    "                                                                                               ";
    private final String adminOptions =
                    "1. Edit student access period\n" +
                    "2. Add a student (name, matric number, gender, nationality, etc)\n" +
                    "3. Add/Update a course (course code, school, its index numbers and vacancy)\n" +
                    "4. Check available slot for an index number (vacancy in a class)\n" +
                    "5. Print student list by index number\n" +
                    "6. Print student list by course (all students registered for the selected course)\n" +
                    "7. Log out\n" +
                    "8. Exit\n";

    @Override
    public void run() {
        _terminal.println();
        int choice;
        _terminal.getProperties().setPromptBold(true);
        do {
            _terminal.resetToBookmark("clear");
            _terminal.println(admin);
            _terminal.setBookmark("admin");
            _terminal.println(adminOptions);
            _terminal.println("\t\twelcome " + _user.getName());
            choice = _textIO.newIntInputReader().withMinVal(1).withMaxVal(8).read("Enter your choice: ");
            _terminal.resetToBookmark("admin");

            switch (choice) {
                case 1 -> {
                    boolean validDateTime;
                    LocalDateTime startDate, endDate;
                    _terminal.println("update registration period");
                    _terminal.setBookmark("update registration period home screen");
                    do {
                        String startDateStr;
                        _terminal.setBookmark("start date");
                        do {
//                            System.out.print("new start date in yy-MM-dd HH:mm format: ");
                            startDateStr = _textIO.newStringInputReader().read("new start date in yyyy-MM-dd HH:mm format: ");
                            validDateTime = InputValidator.validateDateTimeInput(startDateStr);
                            if (!validDateTime) {
                                _terminal.resetToBookmark("start date");
                                _terminal.getProperties().setPromptColor("red");
                                _terminal.println("invalid date format");
                                _terminal.getProperties().setPromptColor("white");
                            }
                        } while (!validDateTime);
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        startDate = LocalDateTime.parse(startDateStr, format);

                        String endDateStr;
                        _terminal.setBookmark("end date");
                        do {
//                            System.out.print("new end date in yy-MM-dd HH:mm format: ");
                            endDateStr = _textIO.newStringInputReader().read("new end date in yyyy-MM-dd HH:mm format: ");
                            validDateTime = InputValidator.validateDateTimeInput(endDateStr);
                            if (!validDateTime) {
                                _terminal.resetToBookmark("end date");
                                _terminal.getProperties().setPromptColor("red");
                                _terminal.println("invalid date format");
                                _terminal.getProperties().setPromptColor("white");
                            }
                        } while (!validDateTime);
                        endDate = LocalDateTime.parse(endDateStr, format);

                        validDateTime = startDate.compareTo(endDate) < 0;
                        if (startDate.compareTo(endDate) > 0) {
//                            System.out.println("start date should occur after end date");
                            _terminal.resetToBookmark("update registration period home screen");
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("start date should occur after end date");
                            _terminal.getProperties().setPromptColor("white");
                        } else if (startDate.compareTo(endDate) == 0) {
//                            System.out.println("Both dates are equal");
                            _terminal.resetToBookmark("update registration period home screen");
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("Both dates are equal");
                            _terminal.getProperties().setPromptColor("white");
                        }
                    } while (!validDateTime);
                    RegistrationPeriod newRP = Factory.createRegistrationPeriod(startDate, endDate);
                    changeAccessPeriod(newRP);
                    _terminal.getProperties().setPromptColor(Color.GREEN);
                    _terminal.println("successfully changed access period");
                    _terminal.getProperties().setPromptColor("white");
                    _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");
                }
                case 2 -> {
                    String name;
                    boolean validName;
                    _terminal.println("add student");
                    _terminal.setBookmark("add student home screen");
                    do {
//                        System.out.print("name: ");
//                        name = _scanner.nextLine();
                        _terminal.setBookmark("student name");
                        name = _textIO.newStringInputReader().withMinLength(3).read("name: ");
                        validName = InputValidator.validateNameInput(name);
                        if (!validName) {
//                            System.out.println("name cannot contain number");
                            _terminal.resetToBookmark("student name");
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("name cannot contain number");
                            _terminal.getProperties().setPromptColor("white");
                        }
                    }while (!validName);

//                    System.out.print("gender: ");
//                    Gender gender = getGender();
//                    System.out.print("nationality: ");
//                    Nationality nationality = getNationality();
//                    System.out.print("school: ");
//                    School school = getSchool();
                    Gender gender = _textIO.newEnumInputReader(Gender.class).read("Gender: ");
                    Nationality nationality = _textIO.newEnumInputReader(Nationality.class).read("Nationality: ");
                    School school = _textIO.newEnumInputReader(School.class).read("School: ");

//                    int maxAUs = 0;
//                    do {
//                        System.out.print("maxAUs: ");
                        int maxAUs = _textIO.newIntInputReader().withDefaultValue(21).withMinVal(0).withMaxVal(25).read("MaxAUs: (leave blank for default 21 AUs)");
//                        if (maxAUs <25 && maxAUs >=0) {
//                            System.out.println("you have set MaxAUs as " + maxAUs);
//                        }
//                        else {
//                            System.out.println("Number of AUs cannot be 25 or more");
//                        }
//                    } while(maxAUs >=25 || maxAUs <= 0);
                    try {
                        Student newStudent = Factory.createStudent(name, school, gender, nationality, maxAUs);
                        addStudent(newStudent);
                        _terminal.getProperties().setPromptColor(Color.GREEN);
                        _terminal.println("successfully added student");
                        _terminal.getProperties().setPromptColor("white");
                        _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");
                    } catch (PasswordStorage.CannotPerformOperationException e) {
//                        System.out.println("error hashing password");
                        _terminal.getProperties().setPromptColor("red");
                        _terminal.println("error hashing password");
                        _terminal.getProperties().setPromptColor("white");
                        _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");
                    }
                }
                case 3 -> {
                    String courseCode = null;
                    Course course = null;
                    String courseName;
                    School school;

                    _terminal.println("add/update course");
                    _terminal.setBookmark("add/update course home page");
                    try {
                        ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
                        List<String> coursesString = courseDataAccessObject.getCourses();
                        coursesString.add("add new course");
                        courseCode = _textIO.newStringInputReader()
                                .withNumberedPossibleValues(coursesString)
                                .read("select option to add/update: ");
                        course = courseDataAccessObject.getCourse(courseCode);

                        if (course == null) {
                            boolean validCourseCode;
                            _terminal.setBookmark("add new course");
                            do {
                                courseCode = _textIO.newStringInputReader().read("enter new course code");
                                validCourseCode = InputValidator.courseStrMatcher(courseCode);
                                if (validCourseCode) {
                                    course = courseDataAccessObject.getCourse(courseCode);
                                    if (course != null) {
                                        _terminal.println("Course already exist\nupdating course instead of adding...");
                                    }
                                } else {
                                    _terminal.resetToBookmark("add new course");
                                    _terminal.getProperties().setPromptColor("red");
                                    _terminal.println("invalid course code format");
                                    _terminal.getProperties().setPromptColor("white");
                                }
                            } while (!validCourseCode);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    if (course == null) {
                        Hashtable<DayOfWeek, List<LocalTime>> lectureTimings = new Hashtable<>();
                        DayOfWeek lectureDay;
                        List<LocalTime> lectureTiming;
                        int AUs;
                        ArrayList<Index> indexes = new ArrayList<>();
                        _terminal.setBookmark("add course details");
                        _terminal.println("____input the following details to add the course____");
                        courseName = _textIO.newStringInputReader()
                                .withMinLength(3)
                                .read("course name: ");
                        school = _textIO.newEnumInputReader(School.class).read("school: ");
                        AUs = _textIO.newIntInputReader().withMinVal(1).withMaxVal(4)
                                .read("number of AUs (1-4): ");

                        boolean contAdd;
                        _terminal.println("____add a day and time period for lecture session____");
                        do {
                            lectureDay = _textIO.newEnumInputReader(DayOfWeek.class).read("Enter lecture day: ");
                            lectureTiming = getSessionTiming("lecture");
                            lectureTimings.put(lectureDay, lectureTiming);
                            contAdd = _textIO.newBooleanInputReader().read("do you wish to continue adding a lecture session?");
                        } while (contAdd);

                        Venue lectureVenue = _textIO.newEnumInputReader(Venue.class)
                                .read("add the venue for the lecture session(s): ");

                        _terminal.resetToBookmark("add course details");
                        _terminal.getProperties().setPromptColor(Color.GREEN);
                        _terminal.println("course details have been recorded.");
                        _terminal.getProperties().setPromptColor("white");

                        String newIndexGroupsubStr = courseCode.substring(2); //get the 4 digits
                        int newIndexGroup = Integer.parseInt(newIndexGroupsubStr)*100;
                        do {
                            _terminal.println("____add details for new index group " + newIndexGroup
                                    + "of course" + courseCode + "____");
                            int maxSize = _textIO.newIntInputReader()
                                    .withMinVal(1)
                                    .read("maximum class size");
                            Index newIndex = Factory.createIndex(newIndexGroup, maxSize);
                            indexes.add(newIndex);
                            contAdd = _textIO.newBooleanInputReader().read("do you wish to continue adding a index group?");
                            if(contAdd){
                                newIndexGroup++;
                            }
                        } while (contAdd);

                        course = Factory.createCourse(courseCode, courseName, school, lectureTimings, lectureVenue, AUs, indexes);

                        try {
                            addCourse(Factory.getTextCourseDataAccess(), course);
                            _terminal.getProperties().setPromptColor(Color.GREEN);
                            _terminal.println("Successfully added course");
                            _terminal.println(Factory.getTextCourseDataAccess().getCourse(courseCode).allInfoToString());
                            _terminal.getProperties().setPromptColor("white");
                            _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");
                        } catch(IOException |ClassNotFoundException e){
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("file not found");
                            _terminal.getProperties().setPromptColor("white");
                            _textIO.newStringInputReader().withDefaultValue(" ")
                                    .read("press enter to continue");
                        }
                    } else {
                        int option;
                        _terminal.setBookmark("update course details");
                        _terminal.println(course.allInfoToString());
                        do {
                            _terminal.println("________Select course info to add/update________\n" +
                                    "1. Update course name\n" +
                                    "2. Update school\n" +
                                    "3. Add/Update Lecture Timing\n" +
                                    "4. Update Lecture Venue\n" +
                                    "5. Add/Update index group\n" +
                                    "6. Exit function");

                            option = _textIO.newIntInputReader().withMinVal(1).withMaxVal(6).read();
                            _terminal.resetToBookmark("update course details");

                            switch (option) {
                                case 1 -> {
                                    String newCourseName = _textIO.newStringInputReader()
                                            .read("enter new course name: ");
                                    course.setCourseName(newCourseName);
                                    _terminal.getProperties().setPromptColor(Color.GREEN);
                                    _terminal.println("Successfully updated course name");
                                    _terminal.getProperties().setPromptColor("white");
                                    _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");
                                }
                                case 2 -> {
                                    school = _textIO.newEnumInputReader(School.class)
                                            .read("enter new school: ");
                                    course.setSchool(school);
                                    _terminal.getProperties().setPromptColor(Color.GREEN);
                                    _terminal.println("Successfully updated school");
                                    _terminal.getProperties().setPromptColor("white");
                                    _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");

                                }
                                case 3->{
                                    DayOfWeek lectureDay = _textIO.newEnumInputReader(DayOfWeek.class).read("Enter lecture day: ");
                                    List<LocalTime> lectureTiming = getSessionTiming("lecture");
                                    Hashtable<DayOfWeek, List<LocalTime>> lectureTimings = course.getLectureTimings();
                                    if (lectureTimings.containsKey(lectureDay)) {
                                        _terminal.println("there's already a lecture on the same day," +
                                                "updated lecture timing on " + lectureDay + "instead");
                                        lectureTimings.replace(lectureDay, lectureTiming);
                                    } else{
                                        lectureTimings.put(lectureDay, lectureTiming);
                                        _terminal.println("new lecture added on " + lectureDay);
                                    }
                                    course.setLectureTimings(lectureTimings);
                                    _terminal.getProperties().setPromptColor(Color.GREEN);
                                    _terminal.println("Successfully changed lecture timing");
                                    _terminal.getProperties().setPromptColor("white");
                                    _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");
                                }
                                case 4->{
                                    Venue lectureVenue = _textIO.newEnumInputReader(Venue.class)
                                            .read("enter new venue for the lecture session(s): ");
                                    course.setLectureVenue(lectureVenue);
                                    _terminal.getProperties().setPromptColor(Color.GREEN);
                                    _terminal.println("Successfully updated lecture venue");
                                    _terminal.getProperties().setPromptColor("white");
                                    _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");
                                }
                                case 5->{
                                    List<String> indexesString = course.getIndexes();
                                    indexesString.add("add new index");
                                    String indexInput = _textIO.newStringInputReader()
                                            .withNumberedPossibleValues(indexesString).read("select index to add/update:");
                                    boolean validIndexNumber;
                                    _terminal.setBookmark("add new index number");
                                    if (!InputValidator.indexStrMatcher(indexInput)) {
                                        do {
                                            indexInput = _textIO.newStringInputReader()
                                                    .read("enter new index number: ");
                                            validIndexNumber = InputValidator.indexStrMatcher(indexInput);
                                            if (!validIndexNumber) {
                                                _terminal.resetToBookmark("add new index number");
                                                _terminal.getProperties().setPromptColor("red");
                                                _terminal.println("invalid course code format");
                                                _terminal.getProperties().setPromptColor("white");
                                                }
                                        } while (!validIndexNumber);
                                    }
                                    course = addUpdateIndex(course, Integer.parseInt(indexInput));
                                }
                            }
                            try {
                                updateCourse(Factory.getTextCourseDataAccess(), course);
                            } catch(IOException|ClassNotFoundException e){
                                System.out.println("file/class not found");
                            }

                        } while (option != 6);
                    }
                }

                case 4 -> {
                    try {
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
                        int vacancies = checkForIndexVacancy(course, Integer.parseInt(indexNum));
                        _terminal.print("the vacancies for " + indexNum + " of " + courseCode + " is ");
                        _terminal.getProperties().setPromptColor(Color.GREEN);
                        _terminal.println(String.valueOf(vacancies));
                        _terminal.getProperties().setPromptColor("white");
                        _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");

                    } catch(IOException | ClassNotFoundException e){
                        e.printStackTrace();
                    } catch (NonExistentIndexException e) {
                        System.out.println("no such index");
                    }

                }

                case 5 -> {
                    Course course = null;
                    do {
                        try {
                            ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
                            System.out.println(courseDataAccessObject.toString());
                            System.out.print("course code: ");
                            String courseCode = _scanner.nextLine();
                            course = courseDataAccessObject.getCourse(courseCode);
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
                            System.out.println(course.allInfoToString());
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
                            ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
                            System.out.println(courseDataAccessObject.toString());
                            System.out.print("course code: ");
                            String courseCode = _scanner.nextLine();
                            course = courseDataAccessObject.getCourse(courseCode);
                            if (course == null) {
                                System.out.println("no such course");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } while (course == null);
                    System.out.println(course.StudentListToString());
                }
                case 7 -> loggedIn = false;
                case 8 -> exit();
            }
        } while (choice > 0 && choice < 7);
    }

    private void changeAccessPeriod(RegistrationPeriod newRP) {
        try {
            IRegistrationDataAccessObject registrationDataAccessObject = Factory.getTextRegistrationDataAccess();
            registrationDataAccessObject.updateRegistrationPeriod(newRP);
        } catch (IdenticalRegistrationPeriodException e) {
            System.out.println("new registration period same as old registration period");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("error reading file");
        }
    }

    private void addStudent(Student student) {
        try {
            IUserDataAccessObject userDataAccessObject = Factory.getTextUserDataAccess();
            userDataAccessObject.addStudent(student);
        } catch (ExistingUserException e) {
            System.out.println("student already exists");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("error reading file");
        }
    }

    private void addCourse(ICourseDataAccessObject courseDataAccessObject, Course newCourse) {
        try {
            System.out.println(newCourse.allInfoToString());
            courseDataAccessObject.addCourse(newCourse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCourse(ICourseDataAccessObject courseDataAccessObject, Course newCourse) {
        try {
            System.out.println(newCourse.allInfoToString());
            courseDataAccessObject.updateCourse(newCourse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int checkForIndexVacancy(Course course, int indexNum) throws NonExistentIndexException {
        return course.checkVacancies(indexNum);
    }

    private Course addUpdateIndex(Course course, int index){
        Index existingIndex = course.getIndex(index);
        _terminal.setBookmark("add/update index");
        if(existingIndex == null){
            _terminal.println("____add details for new index group " + index
                    + " of course" + course.getCourseCode() + "____");
            int maxSize = _textIO.newIntInputReader()
                    .withMinVal(1)
                    .read("maximum class size of the index group: ");
            Index newIndex = Factory.createIndex(index, maxSize);
            course.addIndex(newIndex);
            existingIndex = newIndex;
            _terminal.getProperties().setPromptColor(Color.GREEN);
            _terminal.println("Successfully added new index");
            _terminal.getProperties().setPromptColor("white");
            _textIO.newStringInputReader().withDefaultValue(" ")
                    .read("press enter to continue");
            _terminal.resetToBookmark("add/update index");
        }
        int option;
        do{
            _terminal.println(course.allInfoToString());
            _terminal.setBookmark("add/update index");
            _terminal.println("____Select index info to add/update____\n" +
                    "1. Add/Update Tutorial Timing\n" +
                    "2. Add/Update Laboratory Timing\n" +
                    "3. Add/Update Tutorial Venue\n" +
                    "4. Add/Update Laboratory Venue\n" +
                    "5. Update Maximum Class Size\n" +
                    "6. Return to previous menu");
            option = _textIO.newIntInputReader()
                    .withMinVal(1).withMaxVal(6)
                    .read("Enter choice: ");
            _terminal.resetToBookmark("add/update index");

            switch(option){
                case 1 ->{
                    Hashtable<DayOfWeek, List<LocalTime>> tutorialTimings = existingIndex.getTutorialTimings();
                    _terminal.println("Add/Update Tutorial Timing");
                    if(tutorialTimings == null){
                        tutorialTimings = new Hashtable<>();
                    }
                    DayOfWeek sessionDay = _textIO.newEnumInputReader(DayOfWeek.class)
                            .read("enter tutorial day: ");
                    List<LocalTime> sessionTiming = getSessionTiming("tutorial");
                    tutorialTimings.put(sessionDay, sessionTiming);
                    existingIndex.setTutorialTimings(tutorialTimings);
                    _terminal.getProperties().setPromptColor(Color.GREEN);
                    _terminal.println("Successfully added/updated tutorial timing");
                    _terminal.getProperties().setPromptColor("white");

                    if (existingIndex.getLaboratoryTiming() == null) {
                        _terminal.getProperties().setPromptColor("red");
                        _terminal.println("Tutorial timing cannot exist without venue");
                        _terminal.getProperties().setPromptColor("white");
                        Venue tutorialVenue = _textIO.newEnumInputReader(Venue.class)
                                .read("Please add tutorial venue: ");
                        existingIndex.setTutorialVenue(tutorialVenue);
                        _terminal.getProperties().setPromptColor(Color.GREEN);
                        _terminal.println("Successfully added tutorial venue");
                        _terminal.getProperties().setPromptColor("white");
                    }
                    _textIO.newStringInputReader().withDefaultValue(" ")
                            .read("press enter to continue");
                }
                case 2 ->{
                    Hashtable<DayOfWeek, List<LocalTime>> laboratoryTimings = existingIndex.getLaboratoryTimings();
                    _terminal.println("Add/Update Laboratory Timing");
                    if(laboratoryTimings == null){
                        laboratoryTimings = new Hashtable<>();
                    }
                    DayOfWeek sessionDay = getSessionDay();
                    List<LocalTime> sessionTiming = getSessionTiming("laboratory");
                    laboratoryTimings.put(sessionDay, sessionTiming);
                    existingIndex.setLaboratoryTimings(laboratoryTimings);
                    _terminal.getProperties().setPromptColor(Color.GREEN);
                    _terminal.println("Successfully added/updated laboratory timing");
                    _terminal.getProperties().setPromptColor("white");

                    if (existingIndex.getLaboratoryVenue() == null) {
                        _terminal.getProperties().setPromptColor("red");
                        _terminal.println("Laboratory timing cannot exist without venue");
                        _terminal.getProperties().setPromptColor("white");
                        Venue laboratoryVenue = _textIO.newEnumInputReader(Venue.class)
                                .read("Please add laboratory venue: ");
                        existingIndex.setLaboratoryVenue(laboratoryVenue);
                        _terminal.getProperties().setPromptColor(Color.GREEN);
                        _terminal.println("Successfully added laboratory venue");
                        _terminal.getProperties().setPromptColor("white");
                    }
                    _textIO.newStringInputReader().withDefaultValue(" ")
                            .read("press enter to continue");
                }
                case 3->{
                    _terminal.println("Add/Update tutorial venue");
                    Venue tutorialVenue = _textIO.newEnumInputReader(Venue.class)
                            .read("add the venue for the tutorial session(s): ");
                    existingIndex.setTutorialVenue(tutorialVenue);
                    _terminal.getProperties().setPromptColor(Color.GREEN);
                    _terminal.println("Successfully updated tutorial venue");
                    _terminal.getProperties().setPromptColor("white");
                    if (existingIndex.getTutorialTimings() == null) {
                        _terminal.getProperties().setPromptColor("red");
                        _terminal.println("Tutorial venue cannot exist without timing");
                        _terminal.getProperties().setPromptColor("white");
                        Hashtable<DayOfWeek, List<LocalTime>> tutorialTimings = new Hashtable<>();
                        DayOfWeek sessionDay = _textIO.newEnumInputReader(DayOfWeek.class)
                                .read("Please enter tutorial day: ");
                        List<LocalTime> sessionTiming = getSessionTiming("tutorial");
                        tutorialTimings.put(sessionDay, sessionTiming);
                        existingIndex.setTutorialTimings(tutorialTimings);
                        _terminal.getProperties().setPromptColor(Color.GREEN);
                        _terminal.println("Successfully added tutorial timing");
                        _terminal.getProperties().setPromptColor("white");
                    }
                    _textIO.newStringInputReader().withDefaultValue(" ")
                            .read("press enter to continue");
                }
                case 4->{
                    _terminal.println("Add/Update laboratory venue");
                    Venue laboratoryVenue = _textIO.newEnumInputReader(Venue.class)
                            .read("add the venue for the laboratory session(s): ");
                    existingIndex.setLaboratoryVenue(laboratoryVenue);
                    _terminal.getProperties().setPromptColor(Color.GREEN);
                    _terminal.println("Successfully updated laboratory venue");
                    _terminal.getProperties().setPromptColor("white");
                    if (existingIndex.getLaboratoryTimings() == null) {
                        _terminal.getProperties().setPromptColor("red");
                        _terminal.println("Laboratory venue cannot exist without timing");
                        _terminal.getProperties().setPromptColor("white");
                        Hashtable<DayOfWeek, List<LocalTime>> laboratoryTimings = new Hashtable<>();
                        DayOfWeek sessionDay = _textIO.newEnumInputReader(DayOfWeek.class)
                                .read("Please enter laboratory day: ");
                        List<LocalTime> sessionTiming = getSessionTiming("laboratory");
                        laboratoryTimings.put(sessionDay, sessionTiming);
                        existingIndex.setLaboratoryTimings(laboratoryTimings);
                        _terminal.getProperties().setPromptColor(Color.GREEN);
                        _terminal.println("Successfully added laboratory timing");
                        _terminal.getProperties().setPromptColor("white");
                    }
                    _textIO.newStringInputReader().withDefaultValue(" ")
                            .read("press enter to continue");
                }
                case 5->{
                    System.out.print("update maximum class size to: ");
                    int maxClassSize = getInt("maximum class size", 1);
                    existingIndex.setMaxClassSize(maxClassSize);
                }
            }
        }while(option!=6);

        return course;
    }

    private String getCourseCode(){
        String courseCode;
        boolean validCourse;
        do {
            courseCode = _scanner.nextLine();
            validCourse = InputValidator.courseStrMatcher(courseCode);
            if (!validCourse) {
                System.out.println("course code is not valid.");
                System.out.print("please re-enter course code: ");
            }
        } while (!validCourse);
        return courseCode;
    }

    private int getIndex(){
        String indexStr;
        boolean validIndex;
        do {
            indexStr = _scanner.nextLine();
            validIndex = InputValidator.indexStrMatcher(indexStr);
            if (!validIndex) {
                System.out.println("index number is not valid.");
                System.out.print("please re-enter index number: ");
            }
        } while (!validIndex);
        return Integer.parseInt(indexStr);
    }

    private List<LocalTime> getSessionTiming(String sessionType){
        List<LocalTime> startEndTime = new ArrayList<>();
        boolean proceed;
        String startTime;
        String schoolStartTime = "07:30";
        String schoolEndTime = "21:30";
        int maxDuration = 4;
        int duration = _textIO.newIntInputReader()
                .withMinVal(1).withMaxVal(maxDuration)
                .read("enter the duration (1-5) of the " + sessionType + "(hrs): ");
        _terminal.setBookmark("start time");
        do{
            startTime = _textIO.newStringInputReader().read("enter the start time in HH:MM(30 min interval, e.g. 16:30): ");
            proceed = InputValidator.validateTimeInput(startTime) &&
                    InputValidator.validateTimeInput(startTime, schoolStartTime, schoolEndTime, duration);
            if(!proceed){
                _terminal.resetToBookmark("start time");
                _terminal.getProperties().setPromptColor("red");
                _terminal.println("timing is invalid. school should start earliest at 07:30 and end latest by 21:30.");
                _terminal.getProperties().setPromptColor("white");
            }
        }while(!proceed);

        LocalTime classStartTime = LocalTime.parse(startTime);
        LocalTime classEndTime = classStartTime.plusHours(duration);
        startEndTime.add(classStartTime);
        startEndTime.add(classEndTime);

        return startEndTime;
    }

    private DayOfWeek getSessionDay(){
        int day = -1;
        System.out.print("select a working day(1 - 6, e.g. 1 -> Monday): ");
        day = getInt("selection", 1, 7);
        return DayOfWeek.of(day);
    }

    private Venue getSessionVenue(String sessionType){
        boolean validVenue = false;
        String venue;
        System.out.println("\nthe valid venues in NTU are LT(1-5), TR(1-5), SWL1&2, HWL1&2.");
        System.out.print("add the venue for the " + sessionType + " session(s): ");
        do{
            venue = _scanner.nextLine().toUpperCase();
            validVenue = InputValidator.validateVenue(venue);
            if(!validVenue){
                System.out.print("please re-enter a valid venue in NTU: ");
            }
        }while(!validVenue);
        return Venue.valueOf(venue);
    }

    private int getInt(String info, int startRange, int endRange){
        boolean validInt;
        int intInput = -1;
        do{
            String input = _scanner.nextLine();
            if(!input.matches("[0-9]+")){
                System.out.print("please re-enter a (positive) integer: ");
                validInt = false;
            }
            else{
                intInput = Integer.parseInt(input);
                if(!(intInput >= startRange && intInput < endRange)){
                    System.out.println(info + " should be between " + startRange + "-" + (endRange-1));
                    System.out.print("please re-enter a valid integer: ");
                    validInt = false;
                }
                else{
                    validInt = true;
                }
            }
        }while(!validInt);
        return intInput;
    }

    private int getInt(String info, int startRange){
        boolean validInt;
        int intInput = -1;

        do{
            String input = _scanner.nextLine();
            if(!input.matches("[0-9]+")){
                System.out.print("please re-enter a (positive) integer: ");
                validInt = false;
            }
            else{
                intInput = Integer.parseInt(input);
                if(intInput <= startRange){
                    System.out.println(info + " should be greater than " + startRange);
                    System.out.print("please re-enter a valid integer: ");
                    validInt = false;
                }
                else{
                    validInt = true;
                }
            }
        }while(!validInt);
        return intInput;
    }

    private boolean getYNBool(String info){
        _terminal.println("do you wish to continue adding a " + info + " (Y/N)? ");
        boolean proceed;
        String ynStr;
        do{
            ynStr = _scanner.nextLine();
            proceed = InputValidator.validateYNInput(ynStr);
            if(!proceed){
                System.out.print("please re-enter either Y or N: ");
            }
        }while(!proceed);

        List<String> yList = List.of("y", "Y", "yes", "Yes");
        return yList.contains(ynStr);
    }
}
