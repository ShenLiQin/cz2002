package ValueObject;

import Helper.PasswordStorage;

import java.io.Serializable;

public class Staff extends AbstractUser implements Serializable {
    public Staff(String name, School school, Gender gender, Nationality nationality) throws PasswordStorage.CannotPerformOperationException {
        super(name, school, gender, nationality, UserType.ADMIN);
    }
}
