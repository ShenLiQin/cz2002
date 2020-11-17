package Control;

import DataAccessObject.IUserDataAccessObject;
import ValueObject.AbstractUser;

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

//            Console console = System.console();
//            String password = Arrays.toString(console.readPassword());
                System.out.print("enter password: ");
                String password = _scanner.nextLine();

                user = userDataAccessObject.authenticate(username, password);
                if (user == null) {
                    System.out.println("wrong username/password");
                }
            } while (user == null);

        } catch (Exception e) {
        System.out.println("error");
        }
        return user;
    }
}
