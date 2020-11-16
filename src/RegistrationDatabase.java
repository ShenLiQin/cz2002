import java.io.*;
import java.util.Date;
import java.util.TreeMap;

public class RegistrationDatabase implements Serializable{
    private final TreeMap<RegistrationKey, Long> registrations = new TreeMap<>();
    private RegistrationPeriod registrationPeriod = null;
    private static RegistrationDatabase instance = null;

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
        if (newRegistrationPeriod.equals(newRegistrationPeriod)) {
            throw new Exception();
        } else {
            registrationPeriod = newRegistrationPeriod;
        }
    }

    public void addRegistration(RegistrationKey registrationKey) throws Exception {
        if (registrations.containsKey(registrationKey)) {
            throw new Exception();
        } else {
            registrations.put(registrationKey, new Date().getTime());
        }
    }

    public static void save(){
        FileOutputStream fos = null;
        ObjectOutputStream out = null;
        try{
            fos = new FileOutputStream("Registrations.txt");
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
}
