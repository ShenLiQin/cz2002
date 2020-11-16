import java.io.Serializable;

public class Staff extends AbstractUser implements Serializable {
    public Staff(String name, String school) {
        super(name, school, UserType.ADMIN);
    }
}
