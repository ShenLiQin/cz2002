import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Index implements Serializable {
    private int indexNumber;
    private int maxClassSize;
    private int vacancy;
    private ArrayList<String> enrolledStudents;
    private ArrayList<Date> tutorialTimings;
    private String tutorialVenue;
    private ArrayList<Date> laboratoryTimings;
    private String laboratoryVenue;
    private static final long serialVersionUID = 1L;

    public void enrollStudent(String matricNumber) throws Exception {
        if (enrolledStudents.contains(matricNumber)) {
            throw new Exception();
        } else {
            enrolledStudents.add(matricNumber);
        }
    }

    public Index(int indexNumber, int maxClassSize, ArrayList<Date> tutorialTimings, String tutorialVenue, ArrayList<Date> laboratoryTimings, String laboratoryVenue) {
        this.indexNumber = indexNumber;
        this.maxClassSize = maxClassSize;
        this.enrolledStudents = new ArrayList<>();
        this.tutorialTimings = tutorialTimings;
        this.tutorialVenue = tutorialVenue;
        this.laboratoryTimings = laboratoryTimings;
        this.laboratoryVenue = laboratoryVenue;
    }

    public void dropStudent(String matricNumber) throws Exception {
        if (!enrolledStudents.contains(matricNumber)) {
            throw new Exception();
        } else {
            enrolledStudents.remove(matricNumber);
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
