import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Student extends AbstractUser {
    private String matricNumber;
    private ArrayList<String> registeredCourseCodes;
    private int totalRegisteredAUs;
    private int maxAUs;

    public Student(String name, String school, int maxAUs, Random random) {
        super(name, school, UserType.USER);
        this.matricNumber = "U" + 1000000 + (int)(random.nextFloat() * 9000000) + (char)(random.nextInt(26) + 'a');;
        this.registeredCourseCodes = new ArrayList<>();
        this.totalRegisteredAUs = 0;
        this.maxAUs = maxAUs;
    }

    public String getMatricNumber() {
        return matricNumber;
    }

    public ArrayList<String> getRegisteredCourseCodes() {
        return registeredCourseCodes;
    }

    public void registerCourse(String courseCode) throws Exception {
        if (registeredCourseCodes.contains(courseCode)) {
            throw new Exception();
        } else {
            registeredCourseCodes.add(courseCode);
        }
    }

    public int getTotalRegisteredAUs() {
        return totalRegisteredAUs;
    }

    public void setMatricNumber(String matricNumber) {
        this.matricNumber = matricNumber;
    }

    public void setRegisteredCourseCodes(ArrayList<String> registeredCourseCodes) {
        this.registeredCourseCodes = registeredCourseCodes;
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
