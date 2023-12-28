package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.UserDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ResizeableBaseController;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.service.UserService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;
import pl.edu.agh.managementlibrarysystem.utils.ControlsUtils;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class IssueBookController extends ResizeableBaseController implements Initializable {
    private final BookService bookService;
    private final UserService userService;
    private final UserSession session;

    @FXML
    private Text bookTitle;
    @FXML
    private Text bookAuthor;
    @FXML
    private Text bookPublisher;
    @FXML
    private Text availability;
    @FXML
    private Text userFullName;
    @FXML
    private Text userEmail;
    @FXML
    private MFXButton issueBook;
    @FXML
    private MFXTextField userSearchTextField;
    @FXML
    private MFXTextField bookSearchField;
    @FXML
    private MFXTextField numberOfDaysTextField;
    @FXML
    private Label errorISBNLabel;
    @FXML
    private Label errorUserLabel;

    private BookDTO book;
    private UserDTO user;

    private StringProperty errorISBNMessage;
    private StringProperty errorUserMessage;

    private Long userId;
    private boolean logged = false;

    public IssueBookController(ApplicationContext applicationContext, BookService bookService,
                               UserService userService, UserSession session) {
        super(applicationContext);
        this.bookService = bookService;
        this.userService = userService;
        this.session = session;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tooltipInitializer();
        errorISBNMessage = new SimpleStringProperty();
        errorISBNLabel.textProperty().bind(errorISBNMessage);
        errorUserMessage = new SimpleStringProperty();
        errorUserLabel.textProperty().bind(errorUserMessage);
        initializeStageOptions();
        this.issueBook.setDisable(true);
    }

    private void initializeStageOptions() {
        if (session.getLoggedUser() == null) {
            return;
        }
        User u = session.getLoggedUser();
        if (u.getPermission() == Permission.NORMAL_USER) {
            userId = session.getLoggedUser().getId();
            ControlsUtils.changeControlVisibility(userSearchTextField, false);
            user = userService.findById(this.userId);
            logged = true;
            setUserControls(user);
        }
    }

    @FXML
    private void searchBook(KeyEvent keyEvent) {
        if (!keyEvent.getCode().toString().equals("ENTER")) {
            return;
        }


        String bookISBN = bookSearchField.getText();
        if (bookISBN.isEmpty()) {
            errorISBNMessage.setValue("Book ISBN field is empty!");
            return;
        }

        book = bookService.findByISBN(bookISBN);

        if (book == null) {
            errorISBNMessage.setValue("Book with given ISBN does not exist!");
            return;
        }

        if (book.getAvailability().equals("Not available")) {
            errorISBNMessage.setValue("Book with given ISBN is not available!");
            return;
        }
        errorISBNMessage.setValue("");

        this.bookTitle.setText(book.getTitle());
        this.bookAuthor.setText(book.getAuthor());
        this.bookPublisher.setText(book.getPublisher());
        this.availability.setText(book.getAvailability());

        if (user != null && !numberOfDaysTextField.getText().isEmpty()) {
            this.issueBook.setDisable(false);
        }
    }

    private void setUserControls(UserDTO user) {
        this.userFullName.setText(user.getFullname());
        this.userEmail.setText(user.getEmail());
        if (book != null && !numberOfDaysTextField.getText().isEmpty()) {
            this.issueBook.setDisable(false);
        }
    }

    @FXML
    private void searchUser(KeyEvent keyEvent) {
        String userId = userSearchTextField.getText();
        if (userId.isEmpty()) {
            errorUserMessage.setValue("User ID field is empty!");
            return;
        }
        this.userId = Long.parseLong(userId);
        user = userService.findById(this.userId);

        if (user == null) {
            errorUserMessage.setValue("User with given ID does not exist!");
            return;
        }
        setUserControls(user);

    }

    @FXML
    private void enterNumberOfDays(KeyEvent keyEvent) {
        String numberOfDays = numberOfDaysTextField.getText();
        if (numberOfDays.isEmpty()) {
            errorUserMessage.setValue("Number of days field is empty!");
            return;
        }

        try {
            Integer.parseInt(numberOfDays);
        } catch (NumberFormatException e) {
            errorUserMessage.setValue("Number of days field must be a number!");
        }

        if (Integer.parseInt(numberOfDays) < 0) {
            errorUserMessage.setValue("Number of days field must be a positive number!");
            return;
        }

        errorUserMessage.setValue("");


        if (book != null && user != null) {
            this.issueBook.setDisable(false);
        }
    }

    @FXML
    private void cancel(ActionEvent actionEvent) {
        this.clearFields();
    }

    @FXML
    private void issueBook(ActionEvent actionEvent) {
        if (this.bookService.checkIfUserHasGivenBook(user, book)) {
            Alerts.showErrorAlert("User already has book", "User with given id " + user.getId() + " already has book");
            return;
        }

        Integer days = Integer.parseInt(numberOfDaysTextField.getText());
        this.bookService.issueBook(book, user, days, !logged);

        Alerts.showInformationAlert("Book issued", "Book has been issued successfully!");
        this.clearFields();
        if (logged) {
            this.setUserControls(user);
        }

    }

    private void clearFields() {
        bookSearchField.clear();
        userSearchTextField.clear();
        numberOfDaysTextField.clear();
        this.bookTitle.setText("Book Title");
        this.bookAuthor.setText("Book Author");
        this.bookPublisher.setText("Book Publisher");
        this.availability.setText("Availability");
        this.userFullName.setText("User Full Name");
        this.userEmail.setText("Email Address");
    }
}
