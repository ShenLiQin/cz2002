package ValueObject;

import Exception.ExistingUserException;
import Exception.NonExistentUserException;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.*;

public class Index implements Serializable {
    private int indexNumber;
    private int maxClassSize;
    private int vacancy;
    private ArrayList<String> enrolledStudents;
    private Queue<String> waitingList;
    private Hashtable<DayOfWeek, List<LocalTime>> tutorialTimings;
    private Venue tutorialVenue;
    private Hashtable<DayOfWeek, List<LocalTime>> laboratoryTimings;
    private Venue laboratoryVenue;
    private static final long serialVersionUID = 1L;

    public Index(int indexNumber, int maxClassSize, Hashtable<DayOfWeek, List<LocalTime>> tutorialTimings, Venue tutorialVenue, Hashtable<DayOfWeek, List<LocalTime>> laboratoryTimings, Venue laboratoryVenue) {
        this.indexNumber = indexNumber;
        this.vacancy = this.maxClassSize = maxClassSize;
        this.enrolledStudents = new ArrayList<>();
        this.waitingList = new LinkedList<>();
        this.tutorialTimings = tutorialTimings;
        this.tutorialVenue = tutorialVenue;
        this.laboratoryTimings = laboratoryTimings;
        this.laboratoryVenue = laboratoryVenue;
    }

    public void enrollStudent(String matricNumber) throws ExistingUserException {
        if (enrolledStudents.contains(matricNumber)) {
            throw new ExistingUserException();
        } else if (vacancy == 0) {
            waitingList.add(matricNumber);
        } else {
            enrolledStudents.add(matricNumber);
            vacancy--;
        }
    }

    public void dropStudent(String matricNumber) throws NonExistentUserException {
        if (!enrolledStudents.contains(matricNumber)) {
            throw new NonExistentUserException();
        } else if (!waitingList.isEmpty()) {
            enrolledStudents.add(waitingList.remove());
        } else {
            enrolledStudents.remove(matricNumber);
            vacancy++;
            //send email
        }
    }

    public int getIndexNumber() {
        return indexNumber;
    }

    public void setIndexNumber(int indexNumber) {
        this.indexNumber = indexNumber;
    }

    public int getMaxClassSize() {
        return maxClassSize;
    }

    public void setMaxClassSize(int maxClassSize) {
        this.maxClassSize = maxClassSize;
    }

    public int getVacancy() {
        return vacancy;
    }

    public void setVacancy(int vacancy) {
        this.vacancy = vacancy;
    }

    public ArrayList<String> getEnrolledStudents() {
        return enrolledStudents;
    }

    public void setEnrolledStudents(ArrayList<String> enrolledStudents) {
        this.enrolledStudents = enrolledStudents;
    }

    public Hashtable<DayOfWeek, List<LocalTime>> getTutorialTimings() {
        return tutorialTimings;
    }

    public void setTutorialTimings(Hashtable<DayOfWeek, List<LocalTime>> tutorialTimings) {
        this.tutorialTimings = tutorialTimings;
    }

    public Hashtable<DayOfWeek, List<LocalTime>> getLaboratoryTimings() {
        return laboratoryTimings;
    }

    public void setLaboratoryTimings(Hashtable<DayOfWeek, List<LocalTime>> laboratoryTimings) {
        this.laboratoryTimings = laboratoryTimings;
    }

    public Venue getLaboratoryTiming() {
        return tutorialVenue;
    }

    public void setTutorialVenue(Venue tutorialVenue) {
        this.tutorialVenue = tutorialVenue;
    }

    public Venue getLaboratoryVenue() {
        return laboratoryVenue;
    }

    public void setLaboratoryVenue(Venue laboratoryVenue) {
        this.laboratoryVenue = laboratoryVenue;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("indexNumber: ").append(indexNumber).append('\t').append("enrolledStudents: ").append(enrolledStudents);
        return str.toString();
    }

    public String allInfoToString(){
        StringBuilder str = new StringBuilder();
        str.append("---------------latest index info---------------");
        str.append("\nindexNumber: ").append(indexNumber).append("\tmax class size: ").append(maxClassSize).append("\tvacancies: ").append(vacancy);
        str.append("\ntutorial timings: ").append(tutorialTimings).append("\ttutorial venue: ").append(tutorialVenue);
        str.append("\nlaboratory timings: ").append(laboratoryTimings).append("\tlaboratory venue: ").append(laboratoryVenue);
        return str.toString();
    }
}
