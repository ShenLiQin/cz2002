import java.io.Serializable;

public class Staff extends AbstractUser implements Serializable {
    public Staff(String name, String school) throws PasswordStorage.CannotPerformOperationException {
        super(name, school, UserType.ADMIN);
    }
}
