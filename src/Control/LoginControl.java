package Control;

import DataAccessObject.IUserDataAccessObject;
import Helper.PasswordStorage;
import ValueObject.AbstractUser;
import org.beryx.textio.TextIO;
import org.beryx.textio.TextIoFactory;
import org.beryx.textio.TextTerminal;

import java.util.Scanner;

public class LoginControl {
    AbstractUser user;
    private final TextIO _textIO;
    private final TextTerminal _terminal;
    private final String welcome =
            "   ___    ___    _  _   ___    ___    _      ___     ___   _____     _     ___   ___ \n" +
            "  / __|  / _ \\  | \\| | / __|  / _ \\  | |    | __|   / __| |_   _|   /_\\   | _ \\ / __|\n" +
            " | (__  | (_) | | .` | \\__ \\ | (_) | | |__  | _|    \\__ \\   | |    / _ \\  |   / \\__ \\\n" +
            "  \\___|  \\___/  |_|\\_| |___/  \\___/  |____| |___|   |___/   |_|   /_/ \\_\\ |_|_\\ |___/\n" +
            "                                                                                     ";

    public LoginControl(TextIO textIO, TextTerminal terminal) {
        user = null;
        _textIO = textIO;
        _terminal = terminal;
        _terminal.getProperties().setPromptColor("white");
        _terminal.setBookmark("clear");
    }

    public AbstractUser login(IUserDataAccessObject userDataAccessObject) {
        _terminal.resetToBookmark("clear");
        try {
            _terminal.println(welcome);
            _terminal.setBookmark("prompt");
            do {
                String username = _textIO.newStringInputReader()
                        .read("enter Username:");
                String password = _textIO.newStringInputReader()
                        .withMinLength(6)
                        .withInputMasking(true)
                        .read("Enter Password: ");
                user = userDataAccessObject.authenticate(username, password);
                _terminal.resetToBookmark("prompt");
                if (user == null) {
                    _terminal.getProperties().setPromptColor("red");
                    _terminal.println("wrong username/password");
                    _terminal.getProperties().setPromptColor("white");
                }
            } while (user == null);
        } catch (PasswordStorage.CannotPerformOperationException e) {
            _terminal.getProperties().setPromptColor("red");
            _terminal.println("error validating password");
        } catch (PasswordStorage.InvalidHashException e) {
            _terminal.getProperties().setPromptColor("red");
            System.out.println("error unhashing password");
        } finally {
            _terminal.getProperties().setPromptColor("white");
        }
        return user;
    }
}