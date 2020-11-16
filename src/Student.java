import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

public class Student extends AbstractUser {
    private String matricNumber;
    private Hashtable<String, Integer> registeredCourses;
    private int totalRegisteredAUs;
    private int maxAUs;

    public Student(String name, String school, int maxAUs, Random random) {
        super(name, school, UserType.USER);
        this.matricNumber = "U" + 1000000 + (int)(random.nextFloat() * 90000) + (char)(random.nextInt(26) + 'a');
        this.registeredCourses = new Hashtable<>();
        this.totalRegisteredAUs = 0;
        this.maxAUs = maxAUs;
    }

    public String getMatricNumber() {
        return matricNumber;
    }

    public Hashtable<String, Integer> getRegisteredCourses() {
        return registeredCourses;
    }

    public void registerCourse(String courseCode, int indexNumber) throws Exception {
        if (registeredCourses.contains(courseCode)) {
            throw new Exception();
        } else {
            registeredCourses.put(courseCode, indexNumber);
        }
    }

    public int getTotalRegisteredAUs() {
        return totalRegisteredAUs;
    }

    public void setMatricNumber(String matricNumber) {
        this.matricNumber = matricNumber;
    }

    public void setRegisteredCourses(Hashtable<String, Integer> registeredCourses) {
        this.registeredCourses = registeredCourses;
    }

    public void setTotalRegisteredAUs(int totalRegisteredAUs) {
        this.totalRegisteredAUs = totalRegisteredAUs;
    }

    public void setMaxAUs(int maxAUs) {
        this.maxAUs = maxAUs;
    }

    public int getMaxAUs() {
        return maxAUs;
    }
}
