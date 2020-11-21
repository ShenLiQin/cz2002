package Control;

import DataAccessObject.IUserDataAccessObject;
import Helper.PasswordStorage;
import ValueObject.AbstractUser;
import ValueObject.Gender;
import jline.Terminal;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;
import org.beryx.textio.system.SystemTextTerminal;

import java.io.Console;
import java.time.Month;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Logger;

public class LoginControl {
    private final Scanner _scanner;
    AbstractUser user;
    private final String welcome =
            "   ___    ___    _  _   ___    ___    _      ___     ___   _____     _     ___   ___ \n" +
            "  / __|  / _ \\  | \\| | / __|  / _ \\  | |    | __|   / __| |_   _|   /_\\   | _ \\ / __|\n" +
            " | (__  | (_) | | .` | \\__ \\ | (_) | | |__  | _|    \\__ \\   | |    / _ \\  |   / \\__ \\\n" +
            "  \\___|  \\___/  |_|\\_| |___/  \\___/  |____| |___|   |___/   |_|   /_/ \\_\\ |_|_\\ |___/\n" +
            "                                                                                     ";
    private final TextIO textIO;
    private final TextTerminal terminal;

    public LoginControl(Scanner scanner) {
        this._scanner = scanner;
        user = null;
//        SystemTextTerminal sysTerminal = new SystemTextTerminal();
//        textIO = new TextIO(sysTerminal);
//        terminal = textIO.getTextTerminal();
        textIO = TextIoFactory.getTextIO();
        terminal = TextIoFactory.getTextTerminal();
        terminal.getProperties().setPromptColor("white");
        terminal.setBookmark("clear");
        terminal.println(welcome);
        terminal.setBookmark("beginning");
    }

    public AbstractUser login(IUserDataAccessObject userDataAccessObject) {
        terminal.resetToBookmark("beginning");
        try {
//            terminal.resetToBookmark("welcome");
//            terminal.setBookmark("welcome");
            do {
//                System.out.println(welcome);
                String username = textIO.newStringInputReader()
                        .read("enter Username:");

//                System.out.print("enter username: ");
//                String username = _scanner.nextLine();

//                System.out.println("Enter peer password:");
//                String passwordArray = _scanner.nextLine();
                String password = textIO.newStringInputReader()
                        .withMinLength(6)
                        .withInputMasking(true)
                        .read("Enter Password: ");
//                String password;
//                Console console = System.console();
//                if (console == null) {
////                    System.out.println("Couldn't get Console instance");
//                    password = _scanner.nextLine();
//                } else {
//                    password = Arrays.toString(console.readPassword("Enter your password: "));
//                }
                user = userDataAccessObject.authenticate(username, password);
                terminal.resetToBookmark("beginning");
                if (user == null) {
                    terminal.getProperties().setPromptColor("red");
                    terminal.println("wrong username/password");
                    terminal.getProperties().setPromptColor("white");
//                    System.out.println("wrong username/password");
                }
            } while (user == null);
        } catch (PasswordStorage.CannotPerformOperationException e) {
            terminal.getProperties().setPromptColor("red");
            terminal.println("error validating password");
            terminal.getProperties().setPromptColor("white");
        } catch (PasswordStorage.InvalidHashException e) {
            terminal.getProperties().setPromptColor("red");
            System.out.println("error unhashing password");
            terminal.getProperties().setPromptColor("white");
        }
        return user;
    }
}