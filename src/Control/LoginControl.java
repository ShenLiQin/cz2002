package Control;

import DataAccessObject.IUserDataAccessObject;
import Helper.PasswordStorage;
import ValueObject.AbstractUser;

import java.io.Console;
import java.util.Arrays;
import java.util.Scanner;

public class LoginControl {
    private final Scanner _scanner;
    AbstractUser user;

    public LoginControl(Scanner scanner) {
        this._scanner = scanner;
        user = null;
    }

    public AbstractUser login(IUserDataAccessObject userDataAccessObject) {
        try {
            do {
                System.out.print("enter username: ");
                String username = _scanner.nextLine();

//                System.out.println("Enter peer password:");
//                String passwordArray = _scanner.nextLine();

                Console console = System.console();
                if (console == null) {
                    System.out.println("Couldn't get Console instance");
                    System.exit(0);
                }
                String passwordArray = Arrays.toString(console.readPassword("Enter your password: "));
                user = userDataAccessObject.authenticate(username, passwordArray);
                if (user == null) {
                    System.out.println("wrong username/password");
                }
            } while (user == null);
        } catch (PasswordStorage.CannotPerformOperationException e) {
            System.out.println("error validating password");
        } catch (PasswordStorage.InvalidHashException e) {
            System.out.println("error unhashing password");
        }
        return user;
    }
}
