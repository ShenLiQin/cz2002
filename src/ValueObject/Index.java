package ValueObject;

import Exception.ExistingUserException;
import Exception.MaxClassSizeException;
import Exception.NonExistentUserException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class Index implements Serializable {
    private int indexNumber;
    private int maxClassSize;
    private int vacancy;
    private ArrayList<String> enrolledStudents;
    private Queue<String> waitingList;
    private ArrayList<Date> tutorialTimings;
    private String tutorialVenue;
    private ArrayList<Date> laboratoryTimings;
    private String laboratoryVenue;
    private static final long serialVersionUID = 1L;

    public Index(int indexNumber, int maxClassSize, ArrayList<Date> tutorialTimings, String tutorialVenue, ArrayList<Date> laboratoryTimings, String laboratoryVenue) {
        this.indexNumber = indexNumber;
        this.maxClassSize = maxClassSize;
        this.enrolledStudents = new ArrayList<>();
        this.waitingList = new LinkedList<>();
        this.tutorialTimings = tutorialTimings;
        this.tutorialVenue = tutorialVenue;
        this.laboratoryTimings = laboratoryTimings;
        this.laboratoryVenue = laboratoryVenue;
    }

    public void enrollStudent(String matricNumber) throws ExistingUserException, MaxClassSizeException {
        if (enrolledStudents.contains(matricNumber)) {
            throw new ExistingUserException();
        } else if (enrolledStudents.size() >= maxClassSize) {
            waitingList.add(matricNumber);
            throw new MaxClassSizeException();
        } else {
            enrolledStudents.add(matricNumber);
        }
    }

    public void dropStudent(String matricNumber) throws NonExistentUserException {
        if (!enrolledStudents.contains(matricNumber)) {
            throw new NonExistentUserException();
        } else {
            enrolledStudents.remove(matricNumber);
            if (!waitingList.isEmpty()) {
                enrolledStudents.add(waitingList.remove());
                //send email
            }
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

    public ArrayList<Date> getTutorialTimings() {
        return tutorialTimings;
    }

    public void setTutorialTimings(ArrayList<Date> tutorialTimings) {
        this.tutorialTimings = tutorialTimings;
    }

    public String getTutorialVenue() {
        return tutorialVenue;
    }

    public void setTutorialVenue(String tutorialVenue) {
        this.tutorialVenue = tutorialVenue;
    }

    public ArrayList<Date> getLaboratoryTimings() {
        return laboratoryTimings;
    }

    public void setLaboratoryTimings(ArrayList<Date> laboratoryTimings) {
        this.laboratoryTimings = laboratoryTimings;
    }

    public String getLaboratoryVenue() {
        return laboratoryVenue;
    }

    public void setLaboratoryVenue(String laboratoryVenue) {
        this.laboratoryVenue = laboratoryVenue;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("indexNumber: ").append(indexNumber).append('\t').append("enrolledStudents: ").append(enrolledStudents);
        return str.toString();
    }
}
