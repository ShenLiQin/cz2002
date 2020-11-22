package Control;

import DataAccessObject.ICourseDataAccessObject;
import DataAccessObject.IRegistrationDataAccessObject;
import DataAccessObject.IUserDataAccessObject;
import Helper.Factory;
import Helper.InputValidator;
import Helper.PasswordStorage;
import ValueObject.*;
import Exception.*;
import org.beryx.textio.*;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class AdminSession implements ISession{
    private final TextIO _textIO;
    private final TextTerminal _terminal;
    private boolean loggedIn = true;
    private final Staff _user;
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

    public AdminSession(TextIO textIO, TextTerminal terminal, AbstractUser user) {
        _textIO = textIO;
        _terminal = terminal;
        _user = (Staff) user;
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

    @Override
    public void run() {
        String keyStrokeAbort = "alt Z";
        boolean registeredAbort =  _terminal.registerHandler(keyStrokeAbort,
                t -> new ReadHandlerData(ReadInterruptionStrategy.Action.ABORT));

        int choice = 0;
        _terminal.getProperties().setPromptBold(true);
        _terminal.resetToBookmark("clear");
        _terminal.println(admin);
        if (registeredAbort) {
            _terminal.println("--------------------------------------------------------------------------------");
            _terminal.println("Press " + keyStrokeAbort + " to go abort your current action");
            _terminal.println("You can use this key combinations at any moment during your session.");
            _terminal.println("--------------------------------------------------------------------------------");
        }
        _terminal.setBookmark("admin");
        _terminal.println(adminOptions);
        _terminal.println("\t\twelcome " + _user.getName());
        do {
            try {
                _terminal.resetToBookmark("admin");
                _terminal.println(adminOptions);
                _terminal.println("\t\twelcome " + _user.getName());
                choice = _textIO.newIntInputReader()
                        .withMinVal(1).withMaxVal(8)
                        .read("Enter your choice: ");
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
                                _terminal.resetToBookmark("update registration period home screen");
                                _terminal.getProperties().setPromptColor("red");
                                _terminal.println("start date should occur after end date");
                                _terminal.getProperties().setPromptColor("white");
                            } else if (startDate.compareTo(endDate) == 0) {
                                _terminal.resetToBookmark("update registration period home screen");
                                _terminal.getProperties().setPromptColor("red");
                                _terminal.println("Both dates are equal");
                                _terminal.getProperties().setPromptColor("white");
                            }
                        } while (!validDateTime);
                        RegistrationPeriod newRP = Factory.createRegistrationPeriod(startDate, endDate);
                        changeAccessPeriod(newRP);
                        _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");
                    }
                    case 2 -> {
                        String name;
                        boolean validName;
                        _terminal.println("add student");
                        _terminal.setBookmark("add student home screen");
                        do {
                            _terminal.setBookmark("student name");
                            name = _textIO.newStringInputReader().withMinLength(3).read("name: ");
                            validName = InputValidator.validateNameInput(name);
                            if (!validName) {
                                _terminal.resetToBookmark("student name");
                                _terminal.getProperties().setPromptColor("red");
                                _terminal.println("name cannot contain number");
                                _terminal.getProperties().setPromptColor("white");
                            }
                        } while (!validName);

                        Gender gender = _textIO.newEnumInputReader(Gender.class).read("Gender: ");
                        Nationality nationality = _textIO.newEnumInputReader(Nationality.class).read("Nationality: ");
                        School school = _textIO.newEnumInputReader(School.class).read("School: ");

                        int maxAUs = _textIO.newIntInputReader()
                                .withDefaultValue(21).withMinVal(0).withMaxVal(25)
                                .read("MaxAUs: (leave blank for default 21 AUs)");
                        try {
                            Student newStudent = Factory.createStudent(name, school, gender, nationality, maxAUs);
                            addStudent(newStudent);
                        } catch (PasswordStorage.CannotPerformOperationException e) {
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("error hashing password");
                        } finally {
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
                                    .read("select course to add/update: ");
                            course = courseDataAccessObject.getCourse(courseCode);

                            if (course == null) {
                                boolean validCourseCode;
                                _terminal.setBookmark("add new course");
                                do {
                                    courseCode = _textIO.newStringInputReader().read("enter new course code");
                                    _terminal.resetToBookmark("add/update course home page");
                                    validCourseCode = InputValidator.courseStrMatcher(courseCode);
                                    if (validCourseCode) {
                                        course = courseDataAccessObject.getCourse(courseCode);
                                        if (course != null) {
                                            _terminal.getProperties().setPromptColor("red");
                                            _terminal.println("Course already exist\nupdating course instead of adding...");
                                            _terminal.getProperties().setPromptColor("white");
                                            _terminal.setBookmark("add/update course home page");
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
                            _terminal.setBookmark("add/update course home page");
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
                                _terminal.setBookmark("add lecture session");
                                if (lectureTimings.containsKey(lectureDay)) { //edited
                                    _terminal.resetToBookmark("add lecture session");
                                    _terminal.getProperties().setPromptColor(Color.RED);
                                    _terminal.println("there is already a lecture session on " + lectureDay + ". please select another day");
                                    _terminal.getProperties().setPromptColor(Color.WHITE);
                                    contAdd = false;
                                    continue;
                                }
                                lectureTiming = getSessionTiming("lecture");
                                lectureTimings.put(lectureDay, lectureTiming);
                                contAdd = _textIO.newBooleanInputReader().read("do you wish to continue adding a lecture session?");
                            } while (contAdd);

                            Venue lectureVenue = _textIO.newEnumInputReader(Venue.class)
                                    .read("add the venue for the lecture session(s): ");

                            _terminal.resetToBookmark("add course details");
                            course = Factory.createCourse(courseCode, courseName, school, lectureTimings, lectureVenue, AUs);
                            _terminal.getProperties().setPromptColor(Color.GREEN);
                            _terminal.println("course details have been recorded.");
                            _terminal.getProperties().setPromptColor("white");

                            String newIndexGroupsubStr = courseCode.substring(2); //get the 4 digits
                            int newIndexGroup = Integer.parseInt(newIndexGroupsubStr) * 100;
                            _terminal.setBookmark("add index info");
                            do {
                                _terminal.resetToBookmark("add index info");
                                course = addUpdateIndex(course, newIndexGroup);
                                contAdd = _textIO.newBooleanInputReader().read("Do you wish to adding another index?");
                                if (contAdd) {
                                    newIndexGroup++;
                                }
                            } while (contAdd);

                            addCourse(course);
                            _textIO.newStringInputReader()
                                    .withDefaultValue(" ").
                                    read("press enter to continue");
                        } else {
                            int option;
                            do {
                                _terminal.resetToBookmark("add/update course home page");
                                _terminal.getProperties().setPromptColor(Color.GREEN);
                                _terminal.println(course.allInfoToString());
                                _terminal.getProperties().setPromptColor("white");
                                _terminal.println("________Select course info to add/update________\n" +
                                        "1. Update course name\n" +
                                        "2. Update school\n" +
                                        "3. Update Lecture Venue\n" +
                                        "4. Add/Update index group\n" +
                                        "5. Exit function");

                                option = _textIO.newIntInputReader().withMinVal(1).withMaxVal(5).read();
                                _terminal.setBookmark("update course details");

                                switch (option) {
                                    case 1 -> {
                                        String newCourseName = _textIO.newStringInputReader()
                                                .read("enter new course name: ");
                                        course.setCourseName(newCourseName);
                                        _terminal.getProperties().setPromptColor(Color.GREEN);
                                        _terminal.println("Successfully updated course name");
                                        _terminal.getProperties().setPromptColor("white");
                                        _textIO.newStringInputReader().withDefaultValue(" ")
                                                .read("press enter to continue");
                                    }
                                    case 2 -> {
                                        school = _textIO.newEnumInputReader(School.class)
                                                .read("enter new school: ");
                                        course.setSchool(school);
                                        _terminal.getProperties().setPromptColor(Color.GREEN);
                                        _terminal.println("Successfully updated school");
                                        _terminal.getProperties().setPromptColor("white");
                                        _textIO.newStringInputReader().withDefaultValue(" ")
                                                .read("press enter to continue");
                                    }
                                    case 3 -> {
                                        Venue lectureVenue = _textIO.newEnumInputReader(Venue.class)
                                                .read("enter new venue for the lecture session(s): ");
                                        course.setLectureVenue(lectureVenue);
                                        _terminal.getProperties().setPromptColor(Color.GREEN);
                                        _terminal.println("Successfully updated lecture venue");
                                        _terminal.getProperties().setPromptColor("white");
                                        _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");
                                    }
                                    case 4 -> {
                                        List<String> indexesString = course.getIndexes();
                                        indexesString.add("add new index");
                                        String indexInput = _textIO.newStringInputReader()
                                                .withNumberedPossibleValues(indexesString).read("select index to add/update:");
                                        boolean validIndexNumber;
                                        _terminal.resetToBookmark("add/update course home page");
                                        _terminal.setBookmark("add new index number");
                                        if (!InputValidator.indexStrMatcher(indexInput)) {
                                            do {
                                                _terminal.setBookmark("add new index number");
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
                                updateCourse(course);
                            } while (option != 5);
                        }
                    }

                    case 4 -> {
                        try {
                            _terminal.println("Check index vacancies");
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
                            if (vacancies <= 0) {
                                _terminal.getProperties().setPromptColor("red");
                                _terminal.printf("There is 0/%d vacancy for %s of %s\n",
                                        course.getIndex(Integer.parseInt(indexNum)).getMaxClassSize(),
                                        indexNum, courseCode);
                                if (vacancies >= -1) {
                                    _terminal.println("There is " + -vacancies + " student in the waiting list");
                                } else {
                                    _terminal.println("There are " + -vacancies + " students in the waiting list");
                                }
                            } else {
                                if (vacancies <= 1) {
                                    _terminal.printf("There is %d/%d vacancy for %s of %s\n",
                                            vacancies, course.getIndex(Integer.parseInt(indexNum)).getMaxClassSize(),
                                            indexNum, courseCode);
                                } else {
                                    _terminal.printf("There are %d/%d vacancies for %s of %s\n",
                                            vacancies, course.getIndex(Integer.parseInt(indexNum)).getMaxClassSize(),
                                            indexNum, courseCode);
                                }
                            }
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

                    case 5 -> {
                        try {
                            ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
                            String courseCode = _textIO.newStringInputReader()
                                    .withNumberedPossibleValues(courseDataAccessObject.getCourses())
                                    .read("print student list by index");
                            Course course = courseDataAccessObject.getCourse(courseCode);

                            String indexNumber = _textIO.newStringInputReader()
                                    .withNumberedPossibleValues(course.getIndexes()).read("index number: ");
                            Index index = course.getIndex(Integer.parseInt(indexNumber));
                            _terminal.getProperties().setPromptColor(Color.GREEN);
                            _terminal.println("Successfully retrieved student list");
                            printStudentListByIndex(index);
                            _terminal.println("End of Student list");
                        } catch (IOException | ClassNotFoundException e) {
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("file not found");
                        } finally {
                            _terminal.getProperties().setPromptColor("white");
                            _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");
                        }
                    }

                    case 6 -> {
                        try {
                            String courseCode;
                            ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
                            courseCode = _textIO.newStringInputReader()
                                    .withNumberedPossibleValues(courseDataAccessObject.getCourses())
                                    .read("print student list by course");
                            Course course = courseDataAccessObject.getCourse(courseCode);

                            _terminal.getProperties().setPromptColor(Color.GREEN);
                            _terminal.println("Successfully retrieved student list");
                            printStudentListByCourse(course);
                            _terminal.println("End of Student list");
                            _terminal.getProperties().setPromptColor("white");
                        } catch (IOException | ClassNotFoundException e) {
                            _terminal.getProperties().setPromptColor("red");
                            _terminal.println("file not found");
                        } finally {
                            _terminal.getProperties().setPromptColor("white");
                            _textIO.newStringInputReader().withDefaultValue(" ").read("press enter to continue");
                        }
                    }
                    case 7 -> loggedIn = false;
                    case 8 -> exit();
                }
            } catch (ReadAbortedException ignored) {
            }
        } while (choice >= 0 && choice < 7);
    }

    private void changeAccessPeriod(RegistrationPeriod newRP) {
        try {
            IRegistrationDataAccessObject registrationDataAccessObject = Factory.getTextRegistrationDataAccess();
            registrationDataAccessObject.updateRegistrationPeriod(newRP);
            _terminal.getProperties().setPromptColor(Color.GREEN);
            _terminal.println("successfully changed access period");
        } catch (IdenticalRegistrationPeriodException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("new registration period same as old registration period");
        } catch (IOException | ClassNotFoundException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("error reading file");
        } finally {
            _terminal.getProperties().setPromptColor("white");
        }
    }

    private void addStudent(Student student) {
        try {
            IUserDataAccessObject userDataAccessObject = Factory.getTextUserDataAccess();
            userDataAccessObject.addStudent(student);
            _terminal.getProperties().setPromptColor(Color.GREEN);
            _terminal.println("successfully added student");
            _terminal.println("list of all students:");
            HashMap<String, Student> students = userDataAccessObject.getAllStudents();
            for (Student existingStudent : students.values()) {
                _terminal.println(existingStudent.toString());
            }
        } catch (ExistingUserException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("Student already exists");
        } catch (IOException | ClassNotFoundException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("Error reading file");
        } finally {
            _terminal.getProperties().setPromptColor("white");
        }
    }

    private void addCourse(Course newCourse) {
        try {
            ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
            courseDataAccessObject.addCourse(newCourse);
            _terminal.getProperties().setPromptColor(Color.GREEN);
            _terminal.println("Successfully added course");
            List<String> courses = courseDataAccessObject.getCourses();
            for (String courseCode : courses) {
                _terminal.println(courseDataAccessObject.getCourse(courseCode).toString());
            }
        } catch(IOException | ClassNotFoundException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("file not found");
        } catch (ExistingCourseException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("existing course");
        } finally {
            _terminal.getProperties().setPromptColor("white");
        }
    }

    private void updateCourse(Course newCourse) {
        try {
            ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
            courseDataAccessObject.updateCourse(newCourse);
            _terminal.getProperties().setPromptColor(Color.GREEN);
            _terminal.println("Successfully updated course");
        } catch(IOException | ClassNotFoundException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("file not found");
        } finally {
            _terminal.getProperties().setPromptColor("white");
        }
    }

    private Course addUpdateIndex(Course course, int index){
        Index existingIndex = course.getIndex(index);
        _terminal.setBookmark("add/update index");
        if(existingIndex == null){
            _terminal.println("____add details for new index group " + index
                    + " of course " + course.getCourseCode() + "____");
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
            _terminal.resetToBookmark("add/update index");
            _terminal.getProperties().setPromptColor(Color.GREEN);
            _terminal.println(existingIndex.toString());
            _terminal.getProperties().setPromptColor("white");
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
                    DayOfWeek sessionDay = _textIO.newEnumInputReader(DayOfWeek.class)
                            .read("enter laboratory day: ");
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
                    int maxClassSize = _textIO.newIntInputReader()
                            .withMinVal(1)
                            .read("update maximum class size to: ");
                    existingIndex.setMaxClassSize(maxClassSize);
                    _terminal.getProperties().setPromptColor(Color.GREEN);
                    _terminal.println("Successfully updated maximum class size");
                    _terminal.getProperties().setPromptColor("white");
                    _textIO.newStringInputReader().withDefaultValue(" ")
                            .read("press enter to continue");
                }
            }
        }while(option!=6);

        return course;
    }

    private void printStudentListByIndex(Index index){
        try {
            IUserDataAccessObject userDataAccessObject = Factory.getTextUserDataAccess();
            ArrayList<String> enrolledStudents = index.getEnrolledStudents();
            Queue<String> waitingListStudents = index.getWaitingList();
            StringBuilder str = new StringBuilder();
            str.append("indexNumber: ").append(index.getIndexNumber()).append('\n');
            if (enrolledStudents.isEmpty()) {
                str.append("no enrolled students");
            } else {
                str.append("enrolled students: ").append('\n');
                for (String matricNumber : enrolledStudents) {
                    Student student = userDataAccessObject.getStudent(matricNumber);
                    str.append("name: \t\t").append(student.getName()).append('\n');
                    str.append("gender: \t").append(student.getGender()).append('\n');
                    str.append("nationality: \t").append(student.getNationality()).append('\n');
                }
                if (waitingListStudents.isEmpty()) {
                    str.append("no students in waiting list");
                } else {
                    str.append("waiting list students: ").append('\n');
                    for (String matricNumber : waitingListStudents) {
                        Student student = userDataAccessObject.getStudent(matricNumber);
                        str.append("name: \t\t").append(student.getName()).append('\n');
                        str.append("gender: \t").append(student.getGender()).append('\n');
                        str.append("nationality: \t").append(student.getNationality()).append('\n');
                    }
                }
                _terminal.getProperties().setPromptColor(Color.GREEN);
                _terminal.println(str.toString());
            }
        } catch (IOException | ClassNotFoundException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("file not found");
        } finally {
            _terminal.getProperties().setPromptColor("white");
        }
    }

    private void printStudentListByCourse(Course course) {
        _terminal.getProperties().setPromptColor(Color.GREEN);
        _terminal.println("courseCode: " + course.getCourseCode() + ",\t" + "courseName: " + course.getCourseName());
        for (String indexNumber : course.getIndexes()) {
            Index index = course.getIndex(Integer.parseInt(indexNumber));
            printStudentListByIndex(index);
            _terminal.println();
        }
        _terminal.getProperties().setPromptColor("white");
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
                .read("enter the duration (1-" + maxDuration + ") of the " + sessionType + "(hrs): ");
        _terminal.setBookmark("start time");
        do{
            startTime = _textIO.newStringInputReader().read("enter the start time in HH:MM (30 min interval, e.g. 16:30): ");
            proceed = InputValidator.validateTimeInput(startTime) &&
                    InputValidator.validateTimeInput(startTime, schoolStartTime, schoolEndTime, duration);
            if(!proceed){
                _terminal.resetToBookmark("start time");
                _terminal.getProperties().setPromptColor("red");
                _terminal.println("timing is invalid. school should start earliest at 07:30 and end latest by 21:30." +
                        "\nAND classes should start at a 30min interval");
                _terminal.getProperties().setPromptColor("white");
            }
        }while(!proceed);

        LocalTime classStartTime = LocalTime.parse(startTime);
        LocalTime classEndTime = classStartTime.plusHours(duration);
        startEndTime.add(classStartTime);
        startEndTime.add(classEndTime);

        return startEndTime;
    }
}
