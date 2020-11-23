package DataAccessObject;

import Exception.ExistingUserException;
import Exception.NonExistentUserException;
import Helper.PasswordStorage;
import ValueObject.AbstractUser;
import ValueObject.Staff;
import ValueObject.Student;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.TreeMap;

public class TextUserDataAccess implements Serializable, IUserDataAccessObject {
    private final TreeMap<String, AbstractUser> loginInformation = new TreeMap<>();
    private static TextUserDataAccess instance = null;

    private TextUserDataAccess() {
        super();
    }

    public static TextUserDataAccess getInstance() throws IOException, ClassNotFoundException{
        initialize();
        if(instance == null){
            instance = new TextUserDataAccess();
        }
        return instance;
    }

    private static void initialize() throws IOException, ClassNotFoundException {
//        InputStream file = new FileInputStream("./data/Users.ser");
//        InputStream buffer = new BufferedInputStream(file);
//        ObjectInput input = new ObjectInputStream(buffer);

//        instance = (UserDatabase) input.readObject();
    }

    private static void persist(){
        FileOutputStream fos;
        ObjectOutputStream out = null;
        try{
            fos = new FileOutputStream("./data/Users.ser");
            out = new ObjectOutputStream(fos);
            out.writeObject(instance);
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void updateStudent(Student student) throws NonExistentUserException {
        HashMap<String, Student> students = getAllStudents();

        if (!students.containsKey(student.getMatricNumber())) {
            throw new NonExistentUserException();
        } else {
            loginInformation.put(student.getUsername(), student);
            persist();
        }
    }

    @Override
    public void addStudent(Student student) throws ExistingUserException {
        HashMap<String, Student> students = getAllStudents();

        if (students.containsKey(student.getMatricNumber())) {
            throw new ExistingUserException();
        } else {
            loginInformation.put(student.getUsername(), student);
            persist();
        }
    }

    @Override
    public void addAdmin(Staff staff) throws ExistingUserException {
        if (loginInformation.containsKey(staff.getUsername())) {
            throw new ExistingUserException();
        } else {
            loginInformation.put(staff.getUsername(), staff);
            persist();
        }
    }

    @Override
    public AbstractUser authenticate(String username, String password) throws PasswordStorage.InvalidHashException, PasswordStorage.CannotPerformOperationException {
        AbstractUser user = loginInformation.get(username);

        if (user != null) {
            if (PasswordStorage.verifyPassword(password, user.getHash())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public Student getStudent(String matricNumber) {
        HashMap<String, Student> students = getAllStudents();
        return students.get(matricNumber);
    }

    @Override
    public String studentsInfoToString() {
        StringBuilder str = new StringBuilder();
        HashMap<String, Student> students = getAllStudents();
        for (Student existingStudent : students.values()) {
            str.append(existingStudent.toString()).append('\n');
        }
        return str.toString();
    }

    private HashMap<String, Student> getAllStudents() {
        HashMap<String, Student> students = new HashMap<>();
        for (AbstractUser user : loginInformation.values()) {
            if (user instanceof Student) {
                Student s = (Student) user;
                students.put(s.getMatricNumber(), s);
            }
        }
        return students;
    }
}
