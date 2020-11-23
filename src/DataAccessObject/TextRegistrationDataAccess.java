package DataAccessObject;

import Exception.*;
import Helper.Factory;
import Helper.IMessenger;
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
//        InputStream file = new FileInputStream("./data/Registrations.ser");
//        InputStream buffer = new BufferedInputStream(file);
//        ObjectInput input = new ObjectInputStream(buffer);
//
//        instance = (RegistrationDatabase) input.readObject();
    }

    private static void persist(){
        FileOutputStream fos;
        ObjectOutputStream out = null;
        try{
            fos = new FileOutputStream("./data/Registrations.ser");
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
            throw new IdenticalRegistrationPeriodException();
        } else {
            registrationPeriod = newRegistrationPeriod;
        }
    }

    @Override
    public void addRegistration(RegistrationKey registrationKey) throws IOException, ClassNotFoundException, ExistingCourseException, ExistingUserException, NonExistentUserException, MaxEnrolledStudentsException {
        registrations.put(registrationKey, new Date().getTime());
        persist();

        //enroll student
        ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
        Course course = courseDataAccessObject.getCourse(registrationKey.getCourseCode());
        Index index = course.getIndex(registrationKey.getIndexNumber());
        String waitingListStudent =  index.enrollStudent(registrationKey.getMatricNumber());

        course.updateIndex(index);
        courseDataAccessObject.updateCourse(course);
        IUserDataAccessObject userDataAccess = Factory.getTextUserDataAccess();
        Student student = userDataAccess.getStudent(registrationKey.getMatricNumber());

        if (waitingListStudent != null) {

            //update student info
            student.registerWaitListCourse(registrationKey.getCourseCode(), registrationKey.getIndexNumber());
            userDataAccess.updateStudent(student);
            throw new MaxEnrolledStudentsException();
        } else {

            //update student info
            student.registerCourse(registrationKey.getCourseCode(), registrationKey.getIndexNumber());
            student.registerAUs(course.getAUs());
            userDataAccess.updateStudent(student);
        }
    }

    @Override
    public void deleteRegistration(RegistrationKey registrationKey) throws IOException, ClassNotFoundException, NonExistentUserException, NonExistentCourseException, ExistingCourseException, MaxEnrolledStudentsException, ExistingUserException {
        registrations.remove(registrationKey);

        ICourseDataAccessObject courseDataAccessObject = Factory.getTextCourseDataAccess();
        Course course = courseDataAccessObject.getCourse(registrationKey.getCourseCode());
        Index index = course.getIndex(registrationKey.getIndexNumber());

        String waitingListStudentMatricNumber = index.dropStudent(registrationKey.getMatricNumber());
        course.updateIndex(index);
        courseDataAccessObject.updateCourse(course);

        //update student
        IUserDataAccessObject userDataAccessObject = Factory.getTextUserDataAccess();
        Student student = userDataAccessObject.getStudent(registrationKey.getMatricNumber());
        student.deregisterCourse(registrationKey.getCourseCode());
        student.deregisterAUs(course.getAUs());
        userDataAccessObject.updateStudent(student);

        //add course for waiting list student
        if (waitingListStudentMatricNumber != null) {
            RegistrationKey newRegistrationKey = Factory.createRegistrationKey(waitingListStudentMatricNumber,
                    registrationKey.getCourseCode(),
                    registrationKey.getIndexNumber());
            addRegistration(newRegistrationKey);

            String waitingListStudentEmail = userDataAccessObject.getStudent(waitingListStudentMatricNumber).getEmail();
            IMessenger messenger = Factory.createEmailMessenger(waitingListStudentEmail);
            messenger.addRecipientEmail(student.getEmail());
            messenger.sendMessage("Course registered",
                    "Waiting list course " + course.getCourseCode() + ' ' +  course.getCourseName() + " index: "
                            + index.getIndexNumber() + " successfully added.\nPlease log in to check your STARS");
        }
    }
}
