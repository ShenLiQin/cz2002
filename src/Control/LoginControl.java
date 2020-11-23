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
            "   _____                                 _             _____   _                        \n" +
            "  / ____|                               | |           / ____| | |                       \n" +
            " | |        ___    _ __    ___    ___   | |   ___    | (___   | |_    __ _   _ __   ___ \n" +
            " | |       / _ \\  | '_ \\  / __|  / _ \\  | |  / _ \\    \\___ \\  | __|  / _` | | '__| / __|\n" +
            " | |____  | (_) | | | | | \\__ \\ | (_) | | | |  __/    ____) | | |_  | (_| | | |    \\__ \\\n" +
            "  \\_____|  \\___/  |_| |_| |___/  \\___/  |_|  \\___|   |_____/   \\__|  \\__,_| |_|    |___/\n" +
            "                                                                                        \n" +
            "                                                                                        ";

    public LoginControl(TextIO textIO, TextTerminal terminal) {
        user = null;
        _textIO = textIO;
        _terminal = terminal;
        _terminal.getProperties().setPromptColor("white");
    }

    public AbstractUser login(IUserDataAccessObject userDataAccessObject) {
        _terminal.setBookmark("clear");
        try {
            _terminal.println(welcome);
            _terminal.setBookmark("prompt");
            do {
                String username = _textIO.newStringInputReader()
                        .read("Enter Username:");
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