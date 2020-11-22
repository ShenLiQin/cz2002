package ValueObject;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.*;

import Exception.NonExistentIndexException;

public class Course implements Serializable, Comparable<Course> {
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

    public void deleteIndex(int indexNumber) throws Exception {
        if (!indexes.containsKey(indexNumber)) {
            throw new Exception();
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

    public String StudentListToString() {
        StringBuilder str = new StringBuilder();
        str.append("courseCode: ").append(courseCode).append(",\t").append("courseName: ").append(courseName).append('\n');
        for (Index index : indexes.values()) {
            str.append(index.studentInfoToString()).append('\n');
        }
        return str.toString();
    }

    @Override
    public String toString() {
        return "courseCode : " + courseCode + "\t\t" + "courseName: " + courseName + "\t\t" +  "school: " + school;
    }

    public String allInfoToString(){
        StringBuilder str = new StringBuilder();
        str.append("---------------latest course info---------------");
        str.append("\ncourseCode: ").append(courseCode).append("\tcourseName: ").append(courseName);
        str.append("\tschool: ").append(school);
        str.append("\nlecture timings: ").append(lectureTimings).append("\tlecture venue: ").append(lectureVenue);
        str.append("\nindex group numbers: ");
        int i = 1;
        for (Index index : indexes.values()) {
            str.append('\n').append(i++).append(") ").append(index.getIndexNumber());
        }
        return str.toString();
    }

    public String indexNumberToString() {
        StringBuilder str = new StringBuilder();
        str.append("courseCode : ").append(courseCode).append("\t\t").append("courseName: ").append(courseName).append("\t\t");
        for (Index index : indexes.values()) {
            str.append('\n').append(index.getIndexNumber());
        }
        return str.toString();
    }

    @Override
    public int compareTo(Course c) {
        for(DayOfWeek thisLectureDay: this.lectureTimings.keySet()) {
            for(DayOfWeek thatLectureDay: c.lectureTimings.keySet()) {
                if (thisLectureDay == thatLectureDay) {
                    if(isOverlapping(this.lectureTimings.get(thisLectureDay).get(0),
                            this.lectureTimings.get(thisLectureDay).get(1),
                            c.lectureTimings.get(thatLectureDay).get(0),
                            c.lectureTimings.get(thatLectureDay).get(1))) {
                        return 0;
                    }
                }
            }
        }
        return 1;
    }

    private boolean isOverlapping(LocalTime start1, LocalTime end1, LocalTime start2, LocalTime end2) {
        return start1.isBefore(end2) && start2.isBefore(end1);
    }
}