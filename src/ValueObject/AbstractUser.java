package ValueObject;

import Helper.PasswordStorage;

import java.io.Serializable;

public abstract class AbstractUser implements Serializable {
    private String username;
    private String hash;
    private String name;
    private String school;
    private UserType userType;
    private static final long serialVersionUID = 1L;

    public UserType getUserType() {
        return userType;
    }

    public AbstractUser(String name, String school, UserType userType) throws PasswordStorage.CannotPerformOperationException {
        this.username = name+school+"2020";
        this.hash = PasswordStorage.createHash(name+"2020");
        this.name = name;
        this.school = school;
        this.userType = userType;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
