import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class UserDatabase implements Serializable{
    private final TreeMap<String, AbstractUser> loginInformation = new TreeMap<>();
    private static UserDatabase instance = null;

    private UserDatabase() {
        super();
    }

    public static UserDatabase getInstance() throws IOException, ClassNotFoundException{
        initialize();
        if(instance == null){
            instance = new UserDatabase();
        }
        return instance;
    }

    private static void initialize() throws IOException, ClassNotFoundException {
        InputStream file = new FileInputStream("Users.txt");
        InputStream buffer = new BufferedInputStream(file);
        ObjectInput input = new ObjectInputStream(buffer);

        instance = (UserDatabase) input.readObject();
        // hash password
    }

    public static void persist(){
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try{
            fos = new FileOutputStream("Users.txt");
            out = new ObjectOutputStream(fos);
            out.writeObject(instance);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
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

    public void addStudent(Student student) throws Exception {
        HashMap<String, Student> students = new HashMap<>();
        for (AbstractUser user : loginInformation.values()) {
            if (user instanceof Student) {
                Student s = (Student) user;
                students.put(s.getMatricNumber(), s);
            }
        }

        if (students.containsKey(student.getMatricNumber())) {
            throw new Exception();
        } else {
            loginInformation.put(student.getUsername(), student);
            persist();
        }
    }

    public void addAdmin(Staff staff) throws Exception {
        if (loginInformation.containsKey(staff.getUsername())) {
            throw new Exception();
        } else {
            loginInformation.put(staff.getUsername(), staff);
            persist();
        }
    }

    public UserType authenticate(String username, String password) {
        AbstractUser user = loginInformation.get(username);

        if (user != null) {
            if (password.equals(user.getPassword())) {
                return user.getUserType();
            }
        }
        System.out.println("wrong username/password");
        return null;
    }
}
