package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.UserDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseDataEntryController;
import pl.edu.agh.managementlibrarysystem.model.util.Type;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.service.EmailService;
import pl.edu.agh.managementlibrarysystem.service.NotificationService;
import pl.edu.agh.managementlibrarysystem.service.UserService;
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
    private final ObservableList<String> types = Type.getObservableList();
    private final EmailService emailService;
    private BookDTO searchedBook;
    private UserDTO searchedUser;
    @FXML
    private TextArea emailTextArea;
    @FXML
    private MFXButton editEmail;
    @FXML
    private VBox toHide2;
    @FXML
    private VBox toHide1;
    @FXML
    private CheckBox sendEmailCheckbox;
    @FXML
    private CheckBox isResolved;
    @FXML
    private ComboBox<String> type;
    @FXML
    private MFXTextField bookSearchField;
    @FXML
    private Text bookTitle;
    @FXML
    private Text bookAuthors;
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
    private MFXButton makeNotification;

    public AddNotificationController(ApplicationContext applicationContext,
                                     BookService bookService,
                                     NotificationService notificationService,
                                     UserService userService, EmailService emailService) {
        this.userService = userService;
        this.bookService = bookService;
        this.notificationService = notificationService;
        this.applicationContext = applicationContext;
        this.emailService = emailService;
    }



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        makeNotification.disabledProperty();
        changeEmailEditionVisibility(false);
        type.setItems(types);
        this.type.setValue("Choose Type");
        this.makeNotification.disableProperty().bind(this.userSearchTextField.textProperty().isEmpty());
        this.editEmail.disableProperty().bind(this.userSearchTextField.textProperty().isEmpty());
        this.emailTextArea.setText(null);
    }

    @FXML
    @Override
    protected void save(ActionEvent event) {

    }

    private boolean verifyData() {
        if (type.getValue().equals("Choose Type")) {
            Alerts.showErrorAlert("Wrong input", "You must select a notification type");
            return false;
        }
        String numberRegex = ".*\\d.*";
        if(type.getValue().equals("Books are due") || type.getValue().equals("Return date is approaching")){
            String isbn13and10Regex = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
            if (!this.bookSearchField.getText().matches(isbn13and10Regex)) {
                Alerts.showErrorAlert("Wrong input", "Provided input is not a ISBN 10 or 13");
                return false;
            }
        }
        else{
            if(!isResolved.isSelected()){
                Alerts.showErrorAlert("Wrong input", "With these notifications Resolved checkbox must be checked!");
                return false;
            }
        }
        if (!userSearchTextField.getText().matches(numberRegex)) {
            Alerts.showErrorAlert("Wrong input", "User ID must be a number");
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
        this.bookSearchField.clear();
        this.bookTitle.setText("Book Title");
        this.bookAuthors.setText("Book Author");
        this.bookPublisher.setText("Book Publisher");
        this.availability.setText("Availability");
        this.userFullName.setText("User Full Name");
        this.userEmail.setText("Email Address");
        this.type.setValue("Choose Type");
        this.isResolved.setSelected(false);
        this.searchedBook = null;
        this.searchedUser = null;
        this.sendEmailCheckbox.setSelected(false);
        this.emailTextArea.setText(null);
        this.changeEmailEditionVisibility(false);
    }

    public void close(MouseEvent mouseEvent) {
    }

    @FXML
    private void searchBook(KeyEvent keyEvent) {
        if (keyEvent.getCode() != KeyCode.ENTER) {
            return;
        }
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
        this.bookAuthors.setText(searchedBook.getAuthors());
        this.bookPublisher.setText(searchedBook.getPublisher());
        this.availability.setText(searchedBook.getAvailability());
    }

    @FXML
    private void searchUser(KeyEvent keyEvent) {
        if (keyEvent.getCode() != KeyCode.ENTER) {
            return;
        }
        String userId = userSearchTextField.getText();
        if (userId.isEmpty()) {
            Alerts.showInformationAlert("Field validation", "User ID field is empty!");
            return;
        }
        String numberRegex = ".*\\d.*";
        if (!userId.matches(numberRegex)) {
            Alerts.showInformationAlert("Field validation", "User ID must be a number!");
            return;
        }
        try {
            searchedUser = userService.findById(Long.parseLong(userId));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        if (searchedUser == null) {
            Alerts.showErrorAlert("User not found", "User with given ID does not exist!");
            return;
        }

        this.userFullName.setText(searchedUser.getFullname());
        this.userEmail.setText(searchedUser.getEmail());
    }

    @FXML
    private void makeNotification() {
        if (!this.verifyData()) {
            return;
        }
        Date date = new Date(System.currentTimeMillis());
        Type type1 = null;
        if (type.getValue().equals("Books are due")) {
            type1 = Type.BOOKS_ARE_DUE;
        } else if (type.getValue().equals("Return date is approaching")) {
            type1 = Type.RETURN_DATE_IS_CLOSE;
        }else if (type.getValue().equals("New books are available")) {
            type1 = Type.NEW_BOOKS_ARE_AVAILABLE;
        }else if (type.getValue().equals("No recent activity")) {
            type1 = Type.NO_RECENT_ACTIVITY;
        }
        String bookIsbn;
        if(searchedBook==null){
            bookIsbn=null;
        }
        else{
            bookIsbn=searchedBook.getIsbn();
        }
        String msg = notificationService.makeNewNotification(
                bookIsbn,
                searchedUser.getEmail(),
                date,
                type1,
                isResolved.isSelected()
        );

        Alerts.showInformationAlert("Notification notification", msg);
        if(msg.split(" ")[0].equals("Error")){
            return;
        }
        if(sendEmailCheckbox.isSelected()){
            sendEmail();
        }
    }

    @FXML
    private void editEmailText(ActionEvent actionEvent) {
        if(!toHide1.isVisible() && emailTextArea.getText()==null){
            if(searchedBook==null){
                this.emailTextArea.setText(EmailService.getTypicalText(type.getValue(),searchedUser.getFullname(),null));
            }
            else{
                this.emailTextArea.setText(EmailService.getTypicalText(type.getValue(),searchedUser.getFullname(),searchedBook.getTitle()));
            }
                changeEmailEditionVisibility(true);
        }
        else if(toHide1.isVisible()){
            this.emailTextArea.setText(null);
            changeEmailEditionVisibility(false);
        }
    }
    private void changeEmailEditionVisibility(boolean visibility){
        toHide1.setVisible(visibility);
        toHide2.setVisible(visibility);
    }
    private void sendEmail(){
        if(sendEmailCheckbox.isSelected()){
            if(searchedUser==null || searchedUser.getEmail()==null){
                Alerts.showErrorAlert("Sending Email","User doesn't exist or is incorrect");
                return;
            }
            if(emailTextArea.getText()==null){
                if(searchedBook==null){
                    emailTextArea.setText(EmailService.getTypicalText(type.getValue(),searchedUser.getFullname(),null));
                }
                else{
                    emailTextArea.setText(EmailService.getTypicalText(type.getValue(),searchedUser.getFullname(),searchedBook.getTitle()));
                }
            }
            String msg = this.emailService.send(type.getValue(),emailTextArea.getText(),searchedUser.getEmail());
            if(msg.split(" ")[0].equals("Error:")){
                Alerts.showErrorAlert("Sending Email", msg);
            }
            else{
                Alerts.showSuccessAlert("Sending Email", msg);
            }
        }
        return;
    }
}
