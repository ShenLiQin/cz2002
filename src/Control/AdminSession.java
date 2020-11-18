package Control;

import DataAccessObject.ICourseDataAccessObject;
import DataAccessObject.IRegistrationDataAccessObject;
import DataAccessObject.IUserDataAccessObject;
import Helper.Factory;
import Helper.InputValidator;
import ValueObject.*;
import Exception.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
                    Course course = null;
                    InputValidator inputValidator = new InputValidator();
                    String courseCode;
                    String courseName = "";
                    School school;

                    boolean validCourse = true;
                    System.out.print("enter a course code to add/update: ");
                    courseCode = getcourseCode(inputValidator);

                    try {
                        course = getCourse(Factory.getTextCourseDataAccess(), courseCode);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (course == null) {
                        Hashtable<DayOfWeek, List<LocalTime>> lectureTimings = new Hashtable<>();
                        DayOfWeek lectureDay;
                        List<LocalTime> lectureTiming;
                        int AUs;
                        ArrayList<Index> indexes = new ArrayList<>();

                        try {
                            System.out.println("no course found, input some details to add the course.");
                            System.out.print("course name: ");
                            courseName = _scanner.nextLine();

                            System.out.print("school: ");
                            school = getSchool(inputValidator);

                            System.out.print("number of AUs (1-4): ");
                            AUs = getInt(inputValidator, "academic units", 1, 5);

                            System.out.println("____add a day and time period of a lecture session____");
                            boolean contAdd;
                            do {
                                lectureDay = getSessionDay(inputValidator);
                                lectureTiming = getSessionTiming(inputValidator, "lecture");
                                lectureTimings.put(lectureDay, lectureTiming);
                                contAdd = getYNBool(inputValidator, "lecture Session");
                            } while (contAdd);

                            Venue lectureVenue = getSessionVenue(inputValidator, "lecture");
                            System.out.println("course details have been recorded.\n");

                            int newIndexGroup = 100000;
                            contAdd = false;
                            int maxSize;

                            do {
                                newIndexGroup += 1;
                                System.out.println("____add details for new index group " + newIndexGroup
                                        + " of the course____");
                                System.out.print("maximum class size of the index group: ");
                                maxSize = getInt(inputValidator, "maximum class size", 0);
                                Index newIndex = Factory.createIndex(newIndexGroup, maxSize);
                                indexes.add(newIndex);
                                contAdd = getYNBool(inputValidator, "index group");
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

                            option = getInt(inputValidator, "option", 1, 6);

                            try {
                                switch (option) {
                                    case 1 -> {
                                        System.out.print("enter new course name: ");
                                        String newCourseName = _scanner.nextLine();
                                        course.setCourseName(newCourseName);
                                    }
                                    case 2 -> {
                                        System.out.print("enter new school: ");
                                        school = getSchool(inputValidator);
                                        course.setSchool(school);
                                    }
                                    case 3->{
                                        DayOfWeek lectureDay = getSessionDay(inputValidator);
                                        List<LocalTime> lectureTiming = getSessionTiming(inputValidator, "lecture");
                                        Hashtable<DayOfWeek, List<LocalTime>> lectureTimings = course.getLectureTimings();
                                        lectureTimings.put(lectureDay, lectureTiming);
                                        course.setLectureTimings(lectureTimings);
                                    }
                                    case 4->{
                                        Venue lectureVenue = getSessionVenue(inputValidator, "lecture");
                                        course.setLectureVenue(lectureVenue);
                                    }
                                    case 5->{
                                        System.out.print("enter an index to add/update: ");
                                        String indexInput = _scanner.nextLine();
                                        boolean validIndexInput = inputValidator.indexStrMatcher(indexInput);
                                        if(validIndexInput){
                                            course = addUpdateIndex(inputValidator, course, Integer.parseInt(indexInput));
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

                        } while (option != 5);

                    }
                }
                case 4 -> {
                    Course course = null;
                    InputValidator inputValidator = new InputValidator();
                    System.out.print("course code: ");
                    do {
                        try {
                            String courseCode = getcourseCode(inputValidator);
                            course = Factory.getTextCourseDataAccess().getCourse(courseCode);
                            if (course == null) {
                                System.out.println("no such course");
                                System.out.println("please re-enter course code: ");
                            }
                        } catch(IOException | ClassNotFoundException e){
                            System.out.println("file/class not found");
                        }
                    } while (course == null);
                    try {
                        System.out.print("index number: ");
                        int indexNumber = getIndex(inputValidator);
                        System.out.println("vacancies: " + checkForIndexVacancy(course, indexNumber));
                    } catch (NonExistentIndexException e) {
                        System.out.println("no such index");
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

    private Course addUpdateIndex(InputValidator inputValidator, Course course, int index){
        Hashtable<DayOfWeek, List<LocalTime>> laboratoryTimings;
        Hashtable<DayOfWeek, List<LocalTime>> tutorialTimings;
        Index existingIndex = course.getIndex(index);
        if(existingIndex == null){
            Index newIndex = createNewIndex(inputValidator, index);
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
            option = getInt(inputValidator, "option", 1, 8);

            switch(option){
                case 1 ->{
                    tutorialTimings = existingIndex.getTutorialTimings();
                    if(tutorialTimings == null){
                        tutorialTimings = new Hashtable<>();
                    }
                    DayOfWeek sessionDay = getSessionDay(inputValidator);
                    List<LocalTime> sessionTiming = getSessionTiming(inputValidator, "tutorial");
                    tutorialTimings.put(sessionDay, sessionTiming);
                    existingIndex.setTutorialTimings(tutorialTimings);
                }
                case 2 ->{
                    laboratoryTimings = existingIndex.getLaboratoryTimings();
                    if(laboratoryTimings == null){
                        laboratoryTimings = new Hashtable<>();
                    }
                    DayOfWeek sessionDay = getSessionDay(inputValidator);
                    List<LocalTime> sessionTiming = getSessionTiming(inputValidator, "laboratory");
                    laboratoryTimings.put(sessionDay, sessionTiming);
                    existingIndex.setLaboratoryTimings(laboratoryTimings);
                }
                case 3->{
                    Venue tutorialVenue = getSessionVenue(inputValidator, "tutorial");
                    existingIndex.setTutorialVenue(tutorialVenue);
                }
                case 4->{
                    Venue laboratoryVenue = getSessionVenue(inputValidator, "laboratory");
                    existingIndex.setTutorialVenue(laboratoryVenue);
                }
                case 5->{
                    System.out.print("update maximum class size to: ");
                    int maxClassSize = getInt(inputValidator, "maximum class size", 1);
                    existingIndex.setMaxClassSize(maxClassSize);
                }
                case 6->{
                    System.out.print("update vacancies to: ");
                    int vacancies = getInt(inputValidator, "vacancies", 1);
                    System.out.println(vacancies);
                    existingIndex.setVacancy(vacancies);
                }

            }
        }while(option!=7);

        return course;
    }

    private Index createNewIndex(InputValidator inputValidator, int newIndexGroup){
        System.out.println("____add details for new index group " + newIndexGroup
                + " of the course____");
        System.out.print("maximum class size of the index group: ");
        int maxSize = getInt(inputValidator, "maximum class size", 0);
        Index newIndex = Factory.createIndex(newIndexGroup, maxSize);
        return newIndex;
    }

    private String getcourseCode(InputValidator inputValidator){
        String courseCode = "";
        boolean validCourse;
        do {
            courseCode = _scanner.nextLine();
            validCourse = inputValidator.courseStrMatcher(courseCode);
            if (!validCourse) {
                System.out.println("course code is not valid.");
                System.out.print("please re-enter course code: ");
            }
        } while (!validCourse);
        return courseCode;
    }

    private int getIndex(InputValidator inputValidator){
        String indexStr;
        boolean validIndex;
        do {
            indexStr = _scanner.nextLine();
            validIndex = inputValidator.indexStrMatcher(indexStr);
            if (!validIndex) {
                System.out.println("index number is not valid.");
                System.out.print("please re-enter index number: ");
            }
        } while (!validIndex);
        return Integer.parseInt(indexStr);
    }
    private School getSchool(InputValidator inputValidator){
        boolean validSchool = false;
        String school = "";
        do {
            school = _scanner.nextLine().toUpperCase();
            validSchool = inputValidator.schoolStrMatcher(school);
            if (!validSchool) {
                System.out.println("school does not exist in ntu.");
                System.out.println("please re-enter a valid school: ");
            }
        } while (!validSchool);
        return School.valueOf(school);
    }

    private List<LocalTime> getSessionTiming(InputValidator inputValidator, String sessionType){
        List<LocalTime> startEndTime = new ArrayList<>();
        boolean proceed = false;
        String startTime = "00:00";
        String schoolStartTime = "07:30";
        String schoolEndTime = "21:31";
        int duration = 0;
        do {
            System.out.print("enter the duration of the " + sessionType + "(hrs): ");
            duration = getInt(inputValidator, "duration", 0);
            System.out.print("enter the start time in HH:MM(30 min interval, e.g. 16:30): ");
            do{
                startTime = _scanner.nextLine();
                proceed = inputValidator.validateTimeInput(startTime);
                if(!proceed){
                    System.out.println("invalid time format.");
                    System.out.print("please re-enter start time: ");
                }
            }while(!proceed);
            proceed = inputValidator.validateTimeInput(startTime, schoolStartTime, schoolEndTime, duration);
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

    private DayOfWeek getSessionDay(InputValidator inputValidator){
        int day = -1;
        System.out.print("select a working day(1 - 6, e.g. 1 -> Monday): ");
        day = getInt(inputValidator, "selection", 1, 7);
        return DayOfWeek.of(day);
    }

    private Venue getSessionVenue(InputValidator inputValidator, String sessionType){
        boolean validVenue = false;
        String venue;
        System.out.println("\nthe valid venues in NTU are LT(1-5), TR(1-5), SWL1&2, HWL1&2.");
        System.out.print("add the venue for the " + sessionType + " session(s): ");
        do{
            venue = _scanner.nextLine().toUpperCase();
            validVenue = inputValidator.validateVenue(venue);
            if(!validVenue){
                System.out.print("please re-enter a valid venue in NTU: ");
            }
        }while(!validVenue);
        return Venue.valueOf(venue);
    }

    private int getInt(InputValidator inputValidator, String info, int startRange, int endRange){
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

    private int getInt(InputValidator inputValidator, String info, int startRange){
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

    private boolean getYNBool(InputValidator inputValidator, String info){
        System.out.print("do you wish to continue adding a " + info + " (Y/N)? ");
        boolean proceed;
        String ynStr;
        do{
            ynStr = _scanner.nextLine();
            proceed = inputValidator.validateYNInput(ynStr);
            if(!proceed){
                System.out.print("please re-enter either Y or N: ");
            }
        }while(!proceed);

        List<String> yList = List.of("y", "Y", "yes", "Yes");
        return yList.contains(ynStr);
    }
}
