package ValueObject;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.*;

import Exception.NonExistentIndexException;
import Exception.ClashingTimeTableException;

public class Course implements Serializable {
    private String courseCode;
    private String courseName;
    private School school;
    private Hashtable<DayOfWeek, List<LocalTime>> lectureTimings;
    private Venue lectureVenue;
    private int AUs;
    private TreeMap<Integer, Index> indexes;
    private static final long serialVersionUID = 1L;

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode.toLowerCase();
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public Hashtable<DayOfWeek, List<LocalTime>> getLectureTimings() {
        return lectureTimings;
    }

    public void setLectureTimings(Hashtable<DayOfWeek, List<LocalTime>> lectureTimings) {
        this.lectureTimings = lectureTimings;
    }

    public Venue getLectureVenue() {
        return lectureVenue;
    }

    public void setLectureVenue(Venue lectureVenue) {
        this.lectureVenue = lectureVenue;
    }

    public int getAUs() {
        return AUs;
    }

    public void setAUs(int AUs) {
        this.AUs = AUs;
    }

    public int checkVacancies(int indexNumber) throws NonExistentIndexException {
        Index index = indexes.get(indexNumber);
        if (index == null) {
            throw new NonExistentIndexException();
        } else {
            return index.getVacancy();
        }
    }

    public boolean getStudent(String matricNumber) {
        for (Index index : indexes.values()) {
            if (index.getEnrolledStudents().contains(matricNumber.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public void addStudent(String matricNumber, int indexNumber) throws Exception {
        Index index = indexes.get(indexNumber);
        if (index == null) {
            throw new Exception();
        } else {
            index.enrollStudent(matricNumber.toLowerCase());
        }
    }

    public void addIndex(Index index) {
        indexes.put(index.getIndexNumber(), index);
    }

    public void deleteIndex(int indexNumber) throws NonExistentIndexException {
        if (!indexes.containsKey(indexNumber)) {
            throw new NonExistentIndexException();
        } else {
            indexes.remove(indexNumber);
        }
    }

    public Course(String courseCode, String courseName, School school, Hashtable<DayOfWeek, List<LocalTime>> lectureTimings, Venue lectureVenue, int AUs, ArrayList<Index> indexes) {
        this.courseCode = courseCode.toLowerCase();
        this.courseName = courseName;
        this.school = school;
        this.lectureTimings = lectureTimings;
        this.lectureVenue = lectureVenue;
        this.AUs = AUs;
        this.indexes = new TreeMap<>();
        for (Index index : indexes) {
            this.indexes.put(index.getIndexNumber(), index);
        }
    }

    public Index getIndex(int indexNumber) {
        return indexes.get(indexNumber);
    }

    public List<String> getIndexes(){
        List<String> indexList = new ArrayList<>();
        for (Integer indexNumber : indexes.keySet()) {
            indexList.add(indexNumber.toString());
        }
        return indexList;
    }

    public void updateIndex(Index index) {
        indexes.replace(index.getIndexNumber(), index);
    }

    @Override
    public String toString() {
        return "courseCode : " + courseCode + "\tcourseName: " + courseName + "\tschool: " + school +
                "\nlecture timings: " + lectureTimings + "\tlecture venue: " + lectureVenue;
    }

    public String allInfoToString(){
        StringBuilder str = new StringBuilder();
        str.append("---------------latest course info---------------\n").append(toString());
        str.append("\nindex group numbers: ");
        int i = 1;
        for (Index index : indexes.values()) {
            str.append('\n').append(i++).append(") ").append(index.getIndexNumber());
        }
        return str.toString();
    }

    public boolean isClashing(Course c) {
        for(DayOfWeek thisLectureDay: this.lectureTimings.keySet()) {
            if (isTimeTableClash(lectureTimings, c.lectureTimings, thisLectureDay)) {
                return true;
            }
        }
        return false;
    }

    public boolean isClashing(Index i) {
        Hashtable<DayOfWeek, List<LocalTime>> laboratoryTimings = i.getLaboratoryTimings();
        Hashtable<DayOfWeek, List<LocalTime>> tutorialTimings = i.getTutorialTimings();
        for (DayOfWeek lectureDay : lectureTimings.keySet()) {
            if (isTimeTableClash(lectureTimings, laboratoryTimings, lectureDay) ||
                    isTimeTableClash(lectureTimings, tutorialTimings, lectureDay)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTimeTableClash(Hashtable<DayOfWeek, List<LocalTime>> thisCourseTimings,
                                     Hashtable<DayOfWeek, List<LocalTime>> newCourseTimings,
                                     DayOfWeek thisCourseDay) {
        if (thisCourseDay == null || newCourseTimings == null || thisCourseTimings == null) {
            return false;
        }
        for (DayOfWeek thatLectureDay : newCourseTimings.keySet()) {
            if (thisCourseDay == thatLectureDay) {
                if (isOverlapping(thisCourseTimings.get(thisCourseDay).get(0),
                        thisCourseTimings.get(thisCourseDay).get(1),
                        newCourseTimings.get(thatLectureDay).get(0),
                        newCourseTimings.get(thatLectureDay).get(1))) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}