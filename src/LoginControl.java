import javax.xml.stream.FactoryConfigurationError;
import java.util.Scanner;

public class LoginControl {
    private final Scanner _scanner;
    AbstractUser user;

    public LoginControl(Scanner scanner) {
        this._scanner = scanner;
        user = null;
    }

    public AbstractUser login(UserDatabase userDatabase) {
        do {
            System.out.print("enter username: ");
            String username = _scanner.nextLine();

            System.out.print("enter password: ");
            String password = _scanner.nextLine();

            user = userDatabase.authenticate(username, password);
            if (user == null) {
                System.out.println("wrong username/password");
            }

        } while (user == null);

        return user;
    }
}
