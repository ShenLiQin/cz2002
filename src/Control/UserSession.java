package Control;

import ValueObject.AbstractUser;
import ValueObject.Student;

import java.util.Scanner;

public class UserSession implements ISession{
    private final Scanner _scanner;
    private boolean loggedIn = false;
    private Student _user;

    public UserSession(Scanner scanner, AbstractUser user) {
        _scanner = scanner;
        _user = (Student) user;
    }

    @Override
    public boolean logout() {
        return loggedIn;
    }

    @Override
    public void exit() {
        System.exit(0);
    }

    @Override
    public void run() {
        int choice;

        System.out.println("_______User Dashboard_______");
        System.out.println("1. Add ValueObject.Course");
        System.out.println("2. Drop ValueObject.Course");
        System.out.println("3. Check/Print Courses Registered");
        System.out.println("4. Check Vacancies Available");
        System.out.println("5. Change ValueObject.Index Number of ValueObject.Course");
        System.out.println("6. Swap ValueObject.Index Number with Another ValueObject.Student");
        System.out.println("7. Log out");
        System.out.println("8. Exit");

        choice = _scanner.nextInt();
        _scanner.nextLine();

        switch (choice) {
            case 7 -> loggedIn = false;
            case 8 -> exit();
        }
    }
}
