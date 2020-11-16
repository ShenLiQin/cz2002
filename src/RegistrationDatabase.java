import java.io.*;
import java.util.*;

public class RegistrationDatabase implements Serializable{
    private final TreeMap<RegistrationKey, Long> registrations = new TreeMap<>();
    private RegistrationPeriod registrationPeriod = null;
    private static RegistrationDatabase instance = null;
    private CourseDatabase courseDatabase;
    private UserDatabase userDatabase;

    private RegistrationDatabase() {
        super();
    }

    public static RegistrationDatabase getInstance() throws IOException, ClassNotFoundException{
        initialize();
        if(instance == null){
            instance = new RegistrationDatabase();
        }
        return instance;
    }

    private static void initialize() throws IOException, ClassNotFoundException {
        InputStream file = new FileInputStream("Registrations.txt");
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);

        instance = (RegistrationDatabase) input.readObject();
    }

    public static void persist(){
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try{
            fos = new FileOutputStream("Registrations.txt");
            out = new ObjectOutputStream(fos);
            out.writeObject(instance);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public RegistrationPeriod getRegistrationPeriod() {
        return registrationPeriod;
    }

    public void setRegistrationPeriod(RegistrationPeriod newRegistrationPeriod) throws Exception {
        if (registrationPeriod.equals(newRegistrationPeriod)) {
            System.out.println("same registration period");
//            throw new Exception();
        } else {
            registrationPeriod = newRegistrationPeriod;
        }
    }

    public void addRegistration(RegistrationKey registrationKey) throws Exception {
        if (registrations.containsKey(registrationKey)) {
            System.out.println("registration already exists");
            throw new Exception();
        } else {
            for (RegistrationKey registrationKey1 : registrations.keySet()) {
                if (registrationKey.compareTo(registrationKey1) == 0) {
                    System.out.println("course already registered");
                    throw new Exception();
                }
            }
            registrations.put(registrationKey, new Date().getTime());

            Course course = courseDatabase.getCourse(registrationKey.getCourseCode());
            Index index = course.getIndex(registrationKey.getIndexNumber());
            index.enrollStudent(registrationKey.getMatricNumber());
            course.updateIndex(index);
            courseDatabase.updateCourse(course);

            Student student = userDatabase.getStudent(registrationKey.getMatricNumber());
            student.registerCourse(registrationKey.getCourseCode(), registrationKey.getIndexNumber());
        }
    }

    private void notifyStudentCourseDatabase(RegistrationKey registrationKey) {

    }
}
