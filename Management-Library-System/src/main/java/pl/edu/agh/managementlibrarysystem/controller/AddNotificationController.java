package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.UserDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseDataEntryController;
import pl.edu.agh.managementlibrarysystem.event.fxml.LeavingBorderPaneEvent;
import pl.edu.agh.managementlibrarysystem.model.util.Type;
import pl.edu.agh.managementlibrarysystem.service.*;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;

@Controller
public class AddNotificationController extends BaseDataEntryController<ActionEvent> implements Initializable {
    private final ApplicationContext applicationContext;
    private final BookService bookService;
    private final NotificationService notificationService;
    private final UserService userService;
    private BookDTO searchedBook;
    private UserDTO searchedUser;

    @FXML
    private CheckBox isResolved;
    @FXML
    private ComboBox<String> type;
    @FXML
    private MFXTextField bookSearchField;
    @FXML
    private Text bookTitle;
    @FXML
    private Text bookAuthor;
    @FXML
    private Text bookPublisher;
    @FXML
    private Text availability;
    @FXML
    private MFXTextField userSearchTextField;
    @FXML
    private Text userFullName;
    @FXML
    private Text userEmail;
    @FXML
    private MFXTextField dateTextField;
    @FXML
    private MFXButton makeNotification;
    @FXML
    private Label close;
    @FXML
    private MFXButton backBtn;
    private final ObservableList<String> types = Type.getObservableList();

    public AddNotificationController(ApplicationContext applicationContext,
                                     BookService bookService,
                                     NotificationService notificationService,
                                     UserService userService) {
        this.userService = userService;
        this.bookService = bookService;
        this.notificationService = notificationService;
        this.applicationContext = applicationContext;
    }

    @FXML
    public void back(ActionEvent actionEvent){
        this.root.fireEvent(new LeavingBorderPaneEvent(LeavingBorderPaneEvent.LEAVING));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeNotification.disabledProperty();
        type.setItems(types);
        this.makeNotification.disableProperty().bind(this.userSearchTextField.textProperty().isEmpty()
                .or(this.bookSearchField.textProperty().isEmpty())
                .or(this.dateTextField.textProperty().isEmpty()));
    }
    @FXML
    @Override
    protected void save(ActionEvent event) {

    }

    private boolean verifyData() {
        String dateRegex = "^\\d{4}-\\d{2}-\\d{2}$";
        String numberRegex = ".*\\d.*";
        String isbn13and10Regex = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
        if (!this.bookSearchField.getText().matches(isbn13and10Regex)) {
            Alerts.showErrorAlert("Wrong input", "Provided input is not a ISBN 10 or 13");
            return false;
        }
        if(!userSearchTextField.getText().matches(numberRegex)){
            Alerts.showErrorAlert("Wrong input", "User ID must be a number");
            return false;
        }
        if (!dateTextField.getText().matches(dateRegex)) {
            Alerts.showErrorAlert("Wrong input", "enter date as: YYYY-MM-DD");
            return false;
        }
        if(type.getValue().equals("Choose Type")){
            Alerts.showErrorAlert("Wrong input", "You must select a notification type");
            return false;
        }
        return true;
    }

    @FXML
    @Override
    protected void cancel(ActionEvent event) {
        this.clearFields();
    }

    @Override
    protected void clearFields() {
        this.userSearchTextField.clear();
        this.dateTextField.clear();
        this.bookSearchField.clear();
        this.bookTitle.setText("Book Title");
        this.bookAuthor.setText("Book Author");
        this.bookPublisher.setText("Book Publisher");
        this.availability.setText("Availability");
        this.userFullName.setText("User Full Name");
        this.userEmail.setText("Email Address");
        this.type.setValue("Choose Type");
        this.isResolved.setSelected(false);
    }

    public void close(MouseEvent mouseEvent) {
    }

    @FXML
    private void searchBook(KeyEvent keyEvent) {
        String bookISBN = bookSearchField.getText();
        if (bookISBN.isEmpty()) {
            Alerts.showInformationAlert("Field validation", "Book ISBN field is empty!");
            return;
        }

        searchedBook = bookService.findByISBN(bookISBN);

        if (searchedBook == null) {
            Alerts.showErrorAlert("Book not found", "Book with given ISBN does not exist!");
            return;
        }

        if (searchedBook.getAvailability().equals("Not available")) {
            Alerts.showErrorAlert("Book not available", "Book with given ISBN is not available!");
            return;
        }


        this.bookTitle.setText(searchedBook.getTitle());
        this.bookAuthor.setText(searchedBook.getAuthor());
        this.bookPublisher.setText(searchedBook.getPublisher());
        this.availability.setText(searchedBook.getAvailability());
    }
    @FXML
    private void searchUser(KeyEvent keyEvent) {
        String userId = userSearchTextField.getText();
        if (userId.isEmpty()) {
            Alerts.showInformationAlert("Field validation", "User ID field is empty!");
            return;
        }

        searchedUser = userService.findById(Long.parseLong(userId));

        if (searchedUser == null) {
            Alerts.showErrorAlert("User not found", "User with given ID does not exist!");
            return;
        }

        this.userFullName.setText(searchedUser.getFullname());
        this.userEmail.setText(searchedUser.getEmail());
    }

    @FXML
    private void enterDate(KeyEvent keyEvent) {
        String date = dateTextField.getText();
        if (date.isEmpty()) {
            Alerts.showInformationAlert("Field validation", "date field is empty!");
            return;
        }

        try {
            Date.valueOf(date);
        } catch (IllegalArgumentException e) {
            Alerts.showErrorAlert("Field validation", "date must be in format YYYY-MM-DD!");
        }
    }
    @FXML
    private void makeNotification(){
        if(!this.verifyData()){
            return;
        }
        String date = dateTextField.getText();
        Type type1 = null;
        if(type.getValue().equals("Books are due")){
            type1 = Type.BOOKS_ARE_DUE;
        }
        else if(type.getValue().equals("Return date is approaching"))
        {
            type1=Type.RETURN_DATE_IS_CLOSE;
        }
        String msg = notificationService.makeNewNotification(
                searchedBook.getIsbn(),
                searchedUser.getEmail(),
                Date.valueOf(date),
                type1,
                isResolved.isSelected()
        );

        Alerts.showInformationAlert("Notification notification",msg);
    }

}
