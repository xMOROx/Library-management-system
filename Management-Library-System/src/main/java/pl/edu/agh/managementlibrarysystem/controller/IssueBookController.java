package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.UserDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ResizeableBaseController;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.service.UserService;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class IssueBookController extends ResizeableBaseController implements Initializable {
    private final BookService bookService;
    private final UserService userService;

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

    private BookDTO book;
    private UserDTO user;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tooltipInitializer();
        this.issueBook.setDisable(true);
    }

    @FXML
    private void searchBook(KeyEvent keyEvent) {
        String bookISBN = bookSearchField.getText();
        if (bookISBN.isEmpty()) {
            Alerts.showInformationAlert("Field validation", "Book ISBN field is empty!");
            return;
        }

        book = bookService.findByISBN(bookISBN);

        if (book == null) {
            Alerts.showErrorAlert("Book not found", "Book with given ISBN does not exist!");
            return;
        }

        if (book.getAvailability().equals("Not available")) {
            Alerts.showErrorAlert("Book not available", "Book with given ISBN is not available!");
            return;
        }


        this.bookTitle.setText(book.getTitle());
        this.bookAuthor.setText(book.getAuthor());
        this.bookPublisher.setText(book.getPublisher());
        this.availability.setText(book.getAvailability());

        if (user != null && !numberOfDaysTextField.getText().isEmpty()) {
            this.issueBook.setDisable(false);
        }
    }

    @FXML
    private void searchUser(KeyEvent keyEvent) {
        String userId = userSearchTextField.getText();
        if (userId.isEmpty()) {
            Alerts.showInformationAlert("Field validation", "User ID field is empty!");
            return;
        }

        user = userService.findById(Long.parseLong(userId));

        if (user == null) {
            Alerts.showErrorAlert("User not found", "User with given ID does not exist!");
            return;
        }

        this.userFullName.setText(user.getFullname());
        this.userEmail.setText(user.getEmail());

        if (book != null && !numberOfDaysTextField.getText().isEmpty()) {
            this.issueBook.setDisable(false);
        }
    }

    @FXML
    private void enterNumberOfDays(KeyEvent keyEvent) {
        String numberOfDays = numberOfDaysTextField.getText();
        if (numberOfDays.isEmpty()) {
            Alerts.showInformationAlert("Field validation", "Number of days field is empty!");
            return;
        }

        try {
            Integer.parseInt(numberOfDays);
        } catch (NumberFormatException e) {
            Alerts.showErrorAlert("Field validation", "Number of days field must be a number!");
        }

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
        if (this.bookService.checkIfUserHasBook(user)) {
            Alerts.showErrorAlert("User already has book", "User with given id " + user.getId() + " already has book");
            return;
        }

        Integer days = Integer.parseInt(numberOfDaysTextField.getText());
        this.bookService.issueBook(book, user, days);

        Alerts.showInformationAlert("Book issued", "Book has been issued successfully!");
        this.clearFields();
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
