package DataAccessObject;

import Exception.ExistingUserException;
import Exception.NonExistentUserException;
import Helper.PasswordStorage;
import ValueObject.AbstractUser;
import ValueObject.Staff;
import ValueObject.Student;

import java.util.HashMap;
import java.util.List;

public interface IUserDataAccessObject {
    void updateStudent(Student student) throws NonExistentUserException;

    void addStudent(Student student) throws ExistingUserException;

    void addAdmin(Staff staff) throws ExistingUserException;

    AbstractUser authenticate(String username, String password) throws PasswordStorage.InvalidHashException, PasswordStorage.CannotPerformOperationException;

    Student getStudent(String matricNumber);

    HashMap<String, Student> getAllStudents();
}
