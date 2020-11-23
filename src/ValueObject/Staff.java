package ValueObject;

import Helper.PasswordStorage;

import java.io.Serializable;

/**
 * Class representing admin staff.
 *
 */
public class Staff extends AbstractUser implements Serializable {
    /**
     *Creates staff user containing staff specific information.
     *
     * @param name A String that represents the staff name
     * @param school An Enum value of the school
     * @param gender An Enum value representing gender of staff
     * @param nationality An Enum value of the nationality of the staff
     * @throws PasswordStorage.CannotPerformOperationException
     * @see ValueObject.School
     * @see ValueObject.Gender
     * @see ValueObject.Nationality
     */
    public Staff(String name, School school, Gender gender, Nationality nationality) throws PasswordStorage.CannotPerformOperationException {
        super(name, school, gender, nationality, UserType.ADMIN);
    }
}
