package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
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
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

@Controller
public class IssueBookController extends ResizeableBaseController implements Initializable {
    private final BookService bookService;
    private final UserService userService;
    private final UserSession session;
    @FXML
    public DatePicker datePicker;
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
    private Label errorISBNLabel;
    @FXML
    private Label errorUserLabel;

    private BookDTO book;
    private UserDTO user;

    private StringProperty errorISBNMessage;
    private StringProperty errorUserMessage;

    private Long userId;
    private LocalDate date;
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
        LocalDate minDate = LocalDate.now();
        datePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isBefore(minDate));
                    }});

        datePicker.valueProperty().addListener((obs,oldDate,newDate)->{
            if(newDate!=null){
                dateEntered();
            }
        });
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

        if (book.getAvailability().getValue().equalsIgnoreCase("unavailable")) {
            errorISBNMessage.setValue("Book with given ISBN is not available!");
            return;
        }
        errorISBNMessage.setValue("");

        this.bookTitle.setText(book.getTitle().getValue());
        this.bookAuthor.setText(book.getAuthors().getValue());
        this.bookPublisher.setText(book.getPublisher().getValue());
        this.availability.setText(book.getAvailability().getValue());
        if (user != null && date!=null) {
            this.issueBook.setDisable(false);
        }
    }

    private void setUserControls(UserDTO user) {
        this.userFullName.setText(user.getFullname());
        this.userEmail.setText(user.getEmail());
        if (book != null && date!=null) {
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
        System.out.println("lol");
        if (user == null) {
            errorUserMessage.setValue("User with given ID does not exist!");
            return;
        }
        setUserControls(user);

    }

    private void dateEntered(){
        this.date = datePicker.getValue();
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
        if(!this.bookService.checkIfBookIsAvailable(book)){
            Alerts.showErrorAlert("Book is not available", "Book with given ISBN is not available");
            return;
        }


        Integer days = (int) ChronoUnit.DAYS.between(LocalDate.now(), date);
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
        datePicker.setValue(null);
        this.datePicker.setDisable(true);
        this.bookTitle.setText("Book Title");
        this.bookAuthor.setText("Book Author");
        this.bookPublisher.setText("Book Publisher");
        this.availability.setText("Availability");
        this.userFullName.setText("User Full Name");
        this.userEmail.setText("Email Address");
    }
}
