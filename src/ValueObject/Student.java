package ValueObject;

import Exception.ExistingCourseException;
import Exception.NonExistentCourseException;
import Helper.PasswordStorage;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Random;
import java.util.TreeMap;

public class Student extends AbstractUser {
    private String matricNumber;
    private TreeMap<String, Integer> registeredCourses;
    private TreeMap<String, Integer> waitingListCourses;
    private int totalRegisteredAUs;
    private static int count = 0;
    private int maxAUs;

    public Student(String name, School school, Gender gender, Nationality nationality, int maxAUs, Random random) throws PasswordStorage.CannotPerformOperationException {
        super(name, school, gender, nationality, UserType.USER);
        int year = (Calendar.getInstance().get(Calendar.YEAR))%100 ;
        this.matricNumber = "U" + year +
                String.format("%05d", count++) +
                (char)(random.nextInt(26) + 'A');
        this.registeredCourses = new TreeMap<>();
        this.waitingListCourses = new TreeMap<>();
        this.totalRegisteredAUs = 0;
        this.maxAUs = maxAUs;
    }

    public String getMatricNumber() {
        return matricNumber;
    }

    public void setMatricNumber(String matricNumber) {
        this.matricNumber = matricNumber;
    }

    public void registerCourse(String courseCode, int indexNumber) throws ExistingCourseException {
        if (registeredCourses.containsKey(courseCode)) {
            throw new ExistingCourseException();
        } else if (waitingListCourses.containsKey(courseCode)) {
            waitingListCourses.remove(courseCode);
            registeredCourses.put(courseCode, indexNumber);
        } else {
            registeredCourses.put(courseCode, indexNumber);
        }
    }

    public void deregisterCourse(String courseCode) throws NonExistentCourseException {
        if (registeredCourses.containsKey(courseCode)) {
            registeredCourses.remove(courseCode);
        } else if (waitingListCourses.containsKey(courseCode)) {
            waitingListCourses.remove(courseCode);
        } else {
            throw new NonExistentCourseException();
        }
    }

    public void registerWaitListCourse(String courseCode, int indexNumber) throws ExistingCourseException {
        if (waitingListCourses.containsKey(courseCode)) {
            throw new ExistingCourseException();
        } else {
            waitingListCourses.put(courseCode, indexNumber);
        }
    }

    public TreeMap<String, Integer> getRegisteredCourses() {
        return registeredCourses;
    }

    public TreeMap<String, Integer> getWaitingListCourses() {
        return waitingListCourses;
    }

    public void registerAUs(int AUs) {
        this.totalRegisteredAUs -= AUs;
    }

    public void deregisterAUs(int AUs) {
        this.totalRegisteredAUs += AUs;
    }

    public int getMaxAUs() {
        return maxAUs;
    }

    public int getTotalRegisteredAUs() {
        return totalRegisteredAUs;
    }

    @Override
    public String toString() {
        return "Name: " + super.getName() + "\t\tMatric Number: " + matricNumber;
    }
}
