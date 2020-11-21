package Control;

import DataAccessObject.ICourseDataAccessObject;
import DataAccessObject.IRegistrationDataAccessObject;
import DataAccessObject.IUserDataAccessObject;
import Helper.Factory;
import Helper.InputValidator;
import Helper.PasswordStorage;
import ValueObject.*;
import Exception.*;
import jline.Terminal;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
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

//
                    _terminal.println("add/update courseSystem.out.print(\"enter a course code to add/update: \");\n" +
                            "//                    courseCode = getCourseCode();\n" +
                            "//                    Course course = getCourse(courseCode);");
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

                        try {
                            System.out.println("____input the following details to add the course____"); //changed
                            System.out.print("course name: ");
                            courseName = _scanner.nextLine();

                            System.out.print("school: ");
                            school = getSchool();

                            System.out.print("number of AUs (1-4): ");
                            AUs = getInt("academic units", 1, 5);

                            System.out.println("____add a day and time period of a lecture session____");
                            boolean contAdd;
                            do {
                                lectureDay = getSessionDay();
                                lectureTiming = getSessionTiming("lecture");
                                lectureTimings.put(lectureDay, lectureTiming);
                                contAdd = getYNBool("lecture Session");
                            } while (contAdd);

                            Venue lectureVenue = getSessionVenue("lecture");
                            System.out.println("course details have been recorded.\n");

                            String newIndexGroupsubStr = courseCode.substring(2); //get the 4 digits
                            int newIndexGroup = Integer.parseInt(newIndexGroupsubStr)*100;
                            contAdd = false;
                            int maxSize;

                            do {
                                System.out.println("____add details for new index group " + newIndexGroup
                                        + " of the course____");
                                System.out.print("maximum class size of the index group: ");
                                maxSize = getInt("maximum class size", 0);
                                Index newIndex = Factory.createIndex(newIndexGroup, maxSize);
                                indexes.add(newIndex);
                                contAdd = getYNBool("index group");
                                if(contAdd){
                                    newIndexGroup++;
                                }
                            } while (contAdd);

                            course = Factory.createCourse(courseCode, courseName, school, lectureTimings, lectureVenue, AUs, indexes);
                            addCourse(Factory.getTextCourseDataAccess(), course);

                        } catch(IOException |ClassNotFoundException e){
                            System.out.println("file/class not found");
                        }

                    } else {
                        int option = 0;
                        System.out.println(course.allInfoToString());
                        do {
                            System.out.println("________Select course info to add/update________");
                            System.out.println("1. Edit course name");
                            System.out.println("2. Edit school");
                            System.out.println("3. Add/Update Lecture Timing");
                            System.out.println("4. Update Lecture Venue");
                            System.out.println("5. Add/Update index group");
                            System.out.println("6. Quit");

                            option = getInt("option", 1, 7);

                            try {
                                switch (option) {
                                    case 1 -> {
                                        System.out.print("enter new course name: ");
                                        String newCourseName = _scanner.nextLine();
                                        course.setCourseName(newCourseName);
                                    }
                                    case 2 -> {
                                        System.out.print("enter new school: ");
                                        school = getSchool();
                                        course.setSchool(school);
                                    }
                                    case 3->{
                                        DayOfWeek lectureDay = getSessionDay();
                                        List<LocalTime> lectureTiming = getSessionTiming("lecture");
                                        Hashtable<DayOfWeek, List<LocalTime>> lectureTimings = course.getLectureTimings();
                                        lectureTimings.put(lectureDay, lectureTiming);
                                        course.setLectureTimings(lectureTimings);
                                    }
                                    case 4->{
                                        Venue lectureVenue = getSessionVenue("lecture");
                                        course.setLectureVenue(lectureVenue);
                                    }
                                    case 5->{
                                        System.out.print("enter an index to add/update: ");
                                        String indexInput = _scanner.nextLine();
                                        boolean validIndexInput = InputValidator.indexStrMatcher(indexInput);
                                        if(validIndexInput){
                                            course = addUpdateIndex(course, Integer.parseInt(indexInput));
                                        }
                                        else{
                                            System.out.println("index input is invalid.");
                                        }
                                    }
                                }
                                updateCourse(Factory.getTextCourseDataAccess(), course);
                            } catch(IOException|ClassNotFoundException e){
                                System.out.println("file/class not found");
                            }

                        } while (option != 6);

                    }
                }
                case 4 -> {
                    Course course = null;
                    System.out.print("course code: ");
                    do {
                        try {
                            String courseCode = getCourseCode();
                            course = Factory.getTextCourseDataAccess().getCourse(courseCode);
                            if (course == null) {
                                System.out.println("no such course");
                                System.out.println("please re-enter course code: ");
                            }
                        } catch(IOException | ClassNotFoundException e){
                            System.out.println("file/class not found");
                        }
                    } while (course == null);
                    System.out.println(course.allInfoToString());
                    try {
                        System.out.print("index number: ");
                        int indexNumber = getIndex();
                        System.out.println("vacancies: " + checkForIndexVacancy(course, indexNumber));
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

    private Course getCourse(String courseCode) {
        try {
            ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
            return courseDataAccessObject.getCourse(courseCode);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("error reading file");
            return null;
        }
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

    private void printStudentListByIndex(ICourseDataAccessObject courseDataAccessObject, String courseCode, int indexNumber) {

    }

    private void printStudentListByCourse(ICourseDataAccessObject courseDataAccessObject, String courseCode) {

    }

    private int checkForIndexVacancy(Course course, int indexNum) throws NonExistentIndexException {
        return course.checkVacancies(indexNum);
    }

    private Course addUpdateIndex(Course course, int index){
        Hashtable<DayOfWeek, List<LocalTime>> laboratoryTimings;
        Hashtable<DayOfWeek, List<LocalTime>> tutorialTimings;
        Index existingIndex = course.getIndex(index);
        if(existingIndex == null){
            Index newIndex = createNewIndex(index);
            course.addIndex(newIndex);
            existingIndex = newIndex;
        }
        int option;
        do{
            System.out.println(existingIndex.allInfoToString());
            System.out.println("____Select index info to add/update____");
            System.out.println("1. Add/Update Tutorial Timing");
            System.out.println("2. Add/Update Laboratory Timing");
            System.out.println("3. Add/Update Tutorial Venue");
            System.out.println("4. Add/Update Laboratory Venue");
            System.out.println("5. Update Maximum Class Size");
            System.out.println("6. Update Vacancies");
            System.out.println("7. Return to previous menu");
            option = getInt("option", 1, 8);

            switch(option){
                case 1 ->{
                    tutorialTimings = existingIndex.getTutorialTimings();
                    if(tutorialTimings == null){
                        tutorialTimings = new Hashtable<>();
                    }
                    DayOfWeek sessionDay = getSessionDay();
                    List<LocalTime> sessionTiming = getSessionTiming("tutorial");
                    tutorialTimings.put(sessionDay, sessionTiming);
                    existingIndex.setTutorialTimings(tutorialTimings);
                }
                case 2 ->{
                    laboratoryTimings = existingIndex.getLaboratoryTimings();
                    if(laboratoryTimings == null){
                        laboratoryTimings = new Hashtable<>();
                    }
                    DayOfWeek sessionDay = getSessionDay();
                    List<LocalTime> sessionTiming = getSessionTiming("laboratory");
                    laboratoryTimings.put(sessionDay, sessionTiming);
                    existingIndex.setLaboratoryTimings(laboratoryTimings);
                }
                case 3->{
                    Venue tutorialVenue = getSessionVenue("tutorial");
                    existingIndex.setTutorialVenue(tutorialVenue);
                }
                case 4->{
                    Venue laboratoryVenue = getSessionVenue("laboratory");
                    existingIndex.setTutorialVenue(laboratoryVenue);
                }
                case 5->{
                    System.out.print("update maximum class size to: ");
                    int maxClassSize = getInt("maximum class size", 1);
                    existingIndex.setMaxClassSize(maxClassSize);
                }
                case 6->{
                    System.out.print("update vacancies to: ");
                    int vacancies = getInt("vacancies", 1);
                    System.out.println(vacancies);
                    existingIndex.setVacancy(vacancies);
                }

            }
        }while(option!=7);

        return course;
    }

    private Index createNewIndex(int newIndexGroup){
        System.out.println("____add details for new index group " + newIndexGroup
                + " of the course____");
        System.out.print("maximum class size of the index group: ");
        int maxSize = getInt("maximum class size", 0);
        Index newIndex = Factory.createIndex(newIndexGroup, maxSize);
        return newIndex;
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

    private School getSchool(){
        boolean validSchool;
        String school;
        do {
            school = _scanner.nextLine().toUpperCase();
            validSchool = InputValidator.schoolStrMatcher(school);
            if (!validSchool) {
                System.out.println("school does not exist in ntu.");
                System.out.println("please re-enter a valid school: ");
            }
        } while (!validSchool);
        return School.valueOf(school);
    }

    private Gender getGender(){
        boolean validGender;
        String gender;
        do {
            gender = _scanner.nextLine().toUpperCase();
            validGender = InputValidator.genderStrMatcher(gender);
            if (!validGender) {
                System.out.println("invalid gender: only male/female allowed.");
                System.out.println("please re-enter a gender: ");
            }
        } while (!validGender);
        return Gender.valueOf(gender);
    }

    private Nationality getNationality(){
        boolean validNationality;
        String nationality;
        do {
            nationality = _scanner.nextLine().toUpperCase();
            validNationality = InputValidator.nationalityStrMatcher(nationality);
            if (!validNationality) {
                System.out.println("invalid nationality.");
                System.out.println("please re-enter a valid nationality: ");
            }
        } while (!validNationality);
        return Nationality.valueOf(nationality);
    }

    private List<LocalTime> getSessionTiming(String sessionType){
        List<LocalTime> startEndTime = new ArrayList<>();
        boolean proceed = false;
        String startTime = "00:00";
        String schoolStartTime = "07:30";
        String schoolEndTime = "21:30";
        int maxDuration = 5;
        int duration = 0;
        do {
            System.out.print("enter the duration (1-5) of the " + sessionType + "(hrs): ");
            duration = getInt("duration of lecture(hrs)", 1, maxDuration+1);
            System.out.print("enter the start time in HH:MM(30 min interval, e.g. 16:30): ");
            do{
                startTime = _scanner.nextLine();
                proceed = InputValidator.validateTimeInput(startTime);
                if(!proceed){
                    System.out.println("invalid time format.");
                    System.out.print("please re-enter start time: ");
                }
            }while(!proceed);
            proceed = InputValidator.validateTimeInput(startTime, schoolStartTime, schoolEndTime, duration);
            if(!proceed){
                System.out.println("timing is invalid. school should start earliest at 07:30 and end latest by 21:30.\n");
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
        System.out.print("do you wish to continue adding a " + info + " (Y/N)? ");
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
