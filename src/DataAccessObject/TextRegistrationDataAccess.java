package DataAccessObject;

import Exception.*;
import Helper.Factory;
import ValueObject.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.TreeMap;

public class TextRegistrationDataAccess implements Serializable, IRegistrationDataAccessObject {
    private final TreeMap<RegistrationKey, Long> registrations = new TreeMap<>();
    private RegistrationPeriod registrationPeriod = null;
    private static TextRegistrationDataAccess instance = null;

    private TextRegistrationDataAccess() {
        super();
    }

    public static TextRegistrationDataAccess getInstance() throws IOException, ClassNotFoundException{
        initialize();
        if(instance == null){
            instance = new TextRegistrationDataAccess();
        }
        return instance;
    }

    private static void initialize() throws IOException, ClassNotFoundException {
//        InputStream file = new FileInputStream("./data/Registrations.txt");
//        InputStream buffer = new BufferedInputStream(file);
//        ObjectInput input = new ObjectInputStream(buffer);
//
//        instance = (RegistrationDatabase) input.readObject();
    }

    public static void persist(){
        FileOutputStream fos;
        ObjectOutputStream out = null;
        try{
            fos = new FileOutputStream("./data/Registrations.txt");
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

    @Override
    public RegistrationPeriod getRegistrationPeriod() {
        return registrationPeriod;
    }

    public void setRegistrationPeriod(RegistrationPeriod registrationPeriod) {
        this.registrationPeriod = registrationPeriod;
    }

    @Override
    public void updateRegistrationPeriod(RegistrationPeriod newRegistrationPeriod) throws IdenticalRegistrationPeriodException {
        if (registrationPeriod.equals(newRegistrationPeriod)) {
            System.out.println("same registration period");
            throw new IdenticalRegistrationPeriodException();
        } else {
            registrationPeriod = newRegistrationPeriod;
        }
    }

    @Override
    public void addRegistration(RegistrationKey registrationKey) throws ExistingRegistrationException, IOException, ClassNotFoundException, ExistingCourseException, ExistingUserException, MaxClassSizeException {
        if (registrations.containsKey(registrationKey)) {
            System.out.println("registration already exists");
            throw new ExistingRegistrationException();
        }
        registrations.put(registrationKey, new Date().getTime());
        persist();

        ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
        Course course = courseDataAccessObject.getCourse(registrationKey.getCourseCode());
        Index index = course.getIndex(registrationKey.getIndexNumber());
        index.enrollStudent(registrationKey.getMatricNumber());
        course.updateIndex(index);
        courseDataAccessObject.updateCourse(course);

        IUserDataAccessObject userDataAccess = Factory.getTextUserDataAccess();
        Student student = userDataAccess.getStudent(registrationKey.getMatricNumber());
        student.registerCourse(registrationKey.getCourseCode(), registrationKey.getIndexNumber());
    }

    @Override
    public void deleteRegistration(RegistrationKey registrationKey) throws NonExistentRegistrationException, IOException, ClassNotFoundException, NonExistentUserException, NonExistentCourseException {
        if (!registrations.containsKey(registrationKey)) {
            System.out.println("no such registration");
            throw new NonExistentRegistrationException();
        }
        registrations.remove(registrationKey);

        ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
        Course course = courseDataAccessObject.getCourse(registrationKey.getCourseCode());
        Index index = course.getIndex(registrationKey.getIndexNumber());
        index.dropStudent(registrationKey.getMatricNumber());
        course.updateIndex(index);
        courseDataAccessObject.updateCourse(course);

        IUserDataAccessObject userDataAccess = Factory.getTextUserDataAccess();
        Student student = userDataAccess.getStudent(registrationKey.getMatricNumber());
        student.deregisterCourse(registrationKey.getCourseCode());
        userDataAccess.updateStudent(student);
    }
}
