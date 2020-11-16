import javax.xml.stream.FactoryConfigurationError;
import java.util.Scanner;

public class LoginControl {
    private final Scanner _scanner;
    UserType userType;

    public LoginControl(Scanner scanner) {
        this._scanner = scanner;
        userType = null;
    }

    public UserType login(UserDatabase userDatabase) {
        do {
            System.out.print("enter username: ");
            String username = _scanner.nextLine();

            System.out.print("enter password: ");
            String password = _scanner.nextLine();

            userType = userDatabase.authenticate(username, password);

        } while (userType == null);

        return userType;
    }
}
