package ValueObject;

import Exception.ExistingUserException;
import Exception.NonExistentUserException;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.*;

/**
 * Represents the index of a particular course in NTU
 */
public class Index implements Serializable {
    /**
     * Index number of the course
     */
    private int indexNumber;
    /**
     * Maximum number of student this index can have
     */
    private int maxClassSize;
    /**
     * Amount of vacancy left
     */
    private int vacancy;
    /**
     * A list of students matric number that are enrolled in this index
     */
    private ArrayList<String> enrolledStudents;
    /**
     * A list of enrolled students' matric number who are on waiting list
     */
    private Queue<String> waitingList;
    /**
     * TreeMap of tutorial timing. TreeMap contains DayOfWeek and a list of timing
     */
    private Hashtable<DayOfWeek, List<LocalTime>> tutorialTimings;
    /**
     * Venue for tutorial sessions
     */
    private Venue tutorialVenue;
    /**
     * TreeMap of laboratory timing. TreeMap contains DayOfWeek and a list of timing
     */
    private Hashtable<DayOfWeek, List<LocalTime>> laboratoryTimings;
    /**
     * Venue for laboratory sessions
     */
    private Venue laboratoryVenue;
    /**
     * Serialized ID that is tagged to the index object
     */
    private static final long serialVersionUID = 1L;

    /**
     * Creates index containing index specific information
     * Index information to be set by the administrative user
     *
     * Initialize all all attributes of index
     *
     * @param indexNumber Integer that represent the index number
     * @param maxClassSize Integer that represents the maximum class size
     * @param tutorialTimings TreeMap that represents tutorial timings
     * @param tutorialVenue An Enum value of the tutorial venue
     * @param laboratoryTimings TreeMap that represents laboratory timings
     * @param laboratoryVenue An Enum value of the laboratory venue
     */
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

    /**
     * Add student matric number to enrollStudent ArrayList
     * If student matric number is already in the ArrayList, throw ExistingUserException
     * Else if there is no vacancy and matric number is not in waitList TreeMap, add it to waitingList and decrement vacancy and return matric number
     * Else if matric number if in waitingList, remove it from there and add it to enrolledStudents ArrayList and return null
     * Else add matric number to enrolledStudents ArrayList and return null
     * @param matricNumber String that represents the matric number of a student
     * @throws ExistingUserException existing user
     * @return String
     */
    public String enrollStudent(String matricNumber) throws ExistingUserException {
        if (enrolledStudents.contains(matricNumber)) {
            throw new ExistingUserException();
        } else if (vacancy <= 0 && !waitingList.contains(matricNumber)) {
            waitingList.add(matricNumber);
            vacancy--;
            return matricNumber;
        } else if (waitingList.contains(matricNumber)) {
            waitingList.remove(matricNumber);
            enrolledStudents.add(matricNumber);
            vacancy--;
            return null;
        } else {
            enrolledStudents.add(matricNumber);
            vacancy--;
            return null;
        }
    }

    /**
     * Remove student matric number from enrolledStudents ArrayList
     *
     * If the matric number is in waitingList ArrayList, remove it from there and increment the vacancy
     * Else if the matric number is not in enrolledStudents ArrayList, throw NonExistentUserException
     * Else if waitingList is not empty, remove matric number from enrolledStudent ArrayList and increment vacancy by 2 and return the matric number at the top of the waitingList
     * Else remove matric number from enrolledStudents ArrayList, increment vacancy and return matric number that is at the top of the waitingList
     * @param matricNumber String that represents the matric number of a student
     */
    public String dropStudent(String matricNumber) throws NonExistentUserException {
        if (waitingList.contains(matricNumber)) {
            waitingList.remove(matricNumber);
            vacancy++;
            return null;
        } else if (!enrolledStudents.contains(matricNumber)) {
            throw new NonExistentUserException();
        } else if (!waitingList.isEmpty()){
            enrolledStudents.remove(matricNumber);
            vacancy+=2;
            return waitingList.peek();
        } else {
            enrolledStudents.remove(matricNumber);
            vacancy++;
            return waitingList.peek();
        }
    }

    /**
     * Gets index number of the index
     * @return indexNumber Integer that represents the index number
     */
    public int getIndexNumber() {
        return indexNumber;
    }

    /**
     * Gets maximum class size of the index
     * @return maximum class size of the index
     */
    public int getMaxClassSize() {
        return maxClassSize;
    }

    /**
     * Sets maximum class size of the index
     * @param maxClassSize Integer that represents maximum class size of the index
     */
    public void setMaxClassSize(int maxClassSize) {
        this.maxClassSize = maxClassSize;
        vacancy = maxClassSize - enrolledStudents.size();
    }

    /**
     * Gets vacancy of the index
     * @return vacancy of the index
     */
    public int getVacancy() {
        return vacancy;
    }

    /**
     * Gets ArrayList of enrolledStudents, contains matric number of students that are enrolled in the index
     * @return ArrayList as described above
     */
    public ArrayList<String> getEnrolledStudents() {
        return enrolledStudents;
    }

    /**
     * Gets TreeMap of tutorial timing. TreeMap contains DayOfWeek and a list of timing
     * @return TreeMap of tutorialTimings
     */
    public Hashtable<DayOfWeek, List<LocalTime>> getTutorialTimings() {
        return tutorialTimings;
    }

    /**
     * Overrides current TreeMap of tutorialTimings
     * @param tutorialTimings TreeMap of tutorial timing. TreeMap contains DayOfWeek and a list of timing
     */
    public void setTutorialTimings(Hashtable<DayOfWeek, List<LocalTime>> tutorialTimings) {
        this.tutorialTimings = tutorialTimings;
    }

    /**
     * Gets TreeMap of laboratory timing. TreeMap contains DayOfWeek and a list of timing
     * @return TreeMap of laboratory timing.
     */
    public Hashtable<DayOfWeek, List<LocalTime>> getLaboratoryTimings() {
        return laboratoryTimings;
    }

    /**
     * Overrides current TreeMap of laboratory timing. TreeMap contains DayOfWeek and a list of timing
     * @param laboratoryTimings TreeMap of laboratory timing.
     */
    public void setLaboratoryTimings(Hashtable<DayOfWeek, List<LocalTime>> laboratoryTimings) {
        this.laboratoryTimings = laboratoryTimings;
    }

    /**
     * Gets TreeMap of laboratory timing. TreeMap contains DayOfWeek and a list of timing
     * @return TreeMap of laboratory timing.
     */
    public Venue getTutorialVenue() {
        return tutorialVenue;
    }

    /**
     * Sets tutorial venue
     * @param tutorialVenue An Enum value that represents Venue for tutorial sessions
     */
    public void setTutorialVenue(Venue tutorialVenue) {
        this.tutorialVenue = tutorialVenue;
    }

    /**
     * Gets laboratory venue
     * @return An Enum value that represents Venue for laboratory sessions
     */
    public Venue getLaboratoryVenue() {
        return laboratoryVenue;
    }

    /**
     * Sets laboratory venue
     * @param laboratoryVenue An Enum value that represents Venue for laboratory sessions
     */
    public void setLaboratoryVenue(Venue laboratoryVenue) {
        this.laboratoryVenue = laboratoryVenue;
    }

    public Queue<String> getWaitingList() {
        return waitingList;
    }

    /**
     * Append all attributes of this index into a String
     * @return A String as described above
     */
    @Override
    public String toString(){
        StringBuilder str = new StringBuilder();
        str.append("---------------latest index info---------------");
        str.append("\nindexNumber: ").append(indexNumber).append("\tmax class size: ").append(maxClassSize).append("\tvacancies: ").append(vacancy);
        str.append("\ntutorial timings: ").append(tutorialTimings).append("\ttutorial venue: ").append(tutorialVenue);
        str.append("\nlaboratory timings: ").append(laboratoryTimings).append("\tlaboratory venue: ").append(laboratoryVenue);
        return str.toString();
    }

    /**
     *
     * @param i
     * @return
     */
    public boolean isClashing(Index i) {
        Hashtable<DayOfWeek, List<LocalTime>> laboratoryTimings = i.getLaboratoryTimings();
        Hashtable<DayOfWeek, List<LocalTime>> tutorialTimings = i.getTutorialTimings();
        if (this.laboratoryTimings != null) {
            for (DayOfWeek thisLaboratoryDay : this.laboratoryTimings.keySet()) {
                if (clashingTimetable(this.laboratoryTimings, laboratoryTimings, thisLaboratoryDay) ||
                        clashingTimetable(this.laboratoryTimings, tutorialTimings, thisLaboratoryDay)) {
                    return true;
                }
            }
        }
        if (this.tutorialTimings != null) {
            for (DayOfWeek thisTutorialDay : this.tutorialTimings.keySet()) {
                if (clashingTimetable(this.tutorialTimings, laboratoryTimings, thisTutorialDay) ||
                        clashingTimetable(this.tutorialTimings, tutorialTimings, thisTutorialDay)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param thisCourseTimings
     * @param newCourseTimings
     * @param thisCourseDay
     * @return
     */
    private boolean clashingTimetable(Hashtable<DayOfWeek, List<LocalTime>> thisCourseTimings,
                                      Hashtable<DayOfWeek, List<LocalTime>> newCourseTimings,
                                      DayOfWeek thisCourseDay) {
        if (thisCourseDay == null || newCourseTimings == null || thisCourseTimings == null) {
            return false;
        }
        for (DayOfWeek thatLectureDay : newCourseTimings.keySet()) {
            if (thisCourseDay == thatLectureDay) {
                if (overlappingTimeslot(thisCourseTimings.get(thisCourseDay).get(0),
                        thisCourseTimings.get(thisCourseDay).get(1),
                        newCourseTimings.get(thatLectureDay).get(0),
                        newCourseTimings.get(thatLectureDay).get(1))) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     *
     * @param start1
     * @param end1
     * @param start2
     * @param end2
     * @return
     */
    private boolean overlappingTimeslot(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}
