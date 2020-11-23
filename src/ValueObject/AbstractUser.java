package ValueObject;

import Helper.PasswordStorage;

import java.io.Serializable;
import java.time.Year;
import java.util.Random;

public abstract class AbstractUser implements Serializable {
    private String username;
    private String hash;
    private String name;
    private Gender gender;
    private Nationality nationality;
    private School school;
    private UserType userType;
    private String email;
    private static final long serialVersionUID = 1L;

    public UserType getUserType() {
        return userType;
    }

    public AbstractUser(String name, School school, Gender gender, Nationality nationality, UserType userType) throws PasswordStorage.CannotPerformOperationException {
        this.hash = PasswordStorage.createHash(name.strip().toLowerCase() +
                Year.now().toString());
        this.username = name.strip().toLowerCase().substring(name.length() - 3) +
                school.toString().toLowerCase() +
                String.format("%04d", new Random().nextInt(1000));
        this.gender = gender;
        this.nationality = nationality;
        this.name = name;
        this.school = school;
        this.userType = userType;
        this.email = name.strip().toLowerCase().substring(name.length() - 3) +
                String.format("%04d", hash.hashCode()) +
                school.toString().toLowerCase() +
                '.' +
                userType.toString().toLowerCase() +
                "@ntu.edu.sg";
        System.out.println(username + ": " + name.strip().toLowerCase() +
                Year.now().toString());
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Nationality getNationality() {
        return nationality;
    }

    public void setNationality(Nationality nationality) {
        this.nationality = nationality;
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

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
