import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Course implements Serializable {
    private String courseCode;
    private String courseName;
    private ArrayList<Date> lectureTimings;
    private String lectureVenue;
    private int AUs;
    private HashMap<Integer, Index> indexes;
    private static final long serialVersionUID = 1L;

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public ArrayList<Date> getLectureTimings() {
        return lectureTimings;
    }

    public void setLectureTimings(ArrayList<Date> lectureTimings) {
        this.lectureTimings = lectureTimings;
    }

    public String getLectureVenue() {
        return lectureVenue;
    }

    public void setLectureVenue(String lectureVenue) {
        this.lectureVenue = lectureVenue;
    }

    public int getAUs() {
        return AUs;
    }

    public void setAUs(int AUs) {
        this.AUs = AUs;
    }

    public int checkVacancies(int indexNumber) throws Exception {
        Index index = indexes.get(indexNumber);
        if (index == null) {
            throw new Exception();
        } else {
            return index.getVacancy();
        }
    }

    public boolean checkForStudent(String matricNumber) {
        for (Index index : indexes.values()) {
            if (index.getEnrolledStudents().contains(matricNumber)) {
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
            index.enrollStudent(matricNumber);
        }
    }

    public void addIndex(Index index) throws Exception {
        if (indexes.containsKey(index.getIndexNumber())) {
            throw new Exception();
        } else {
            indexes.put(index.getIndexNumber(), index);
        }
    }

    public void deleteIndex(int indexNumber) throws Exception {
        if (!indexes.containsKey(indexNumber)) {
            throw new Exception();
        } else {
            indexes.remove(indexNumber);
        }
    }

    public Course(String courseCode, String courseName, ArrayList<Date> lectureTimings, String lectureVenue, int AUs, ArrayList<Index> indexes) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.lectureTimings = lectureTimings;
        this.lectureVenue = lectureVenue;
        this.AUs = AUs;
        this.indexes = new HashMap<>();
        for (Index index : indexes) {
            this.indexes.put(index.getIndexNumber(), index);
        }
    }

    public Index checkForIndex(int indexNumber) {
        return indexes.get(indexNumber);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("courseCode: ").append(courseCode).append(",\t").append("courseName: ").append(courseName).append('\n');
        for (Index index : indexes.values()) {
            str.append(index.toString()).append('\n');
        }
        return str.toString();
    }
}