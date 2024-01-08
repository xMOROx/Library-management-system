package pl.edu.agh.managementlibrarysystem.controller;


import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.IssuedBookDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ResizeableBaseController;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;
import pl.edu.agh.managementlibrarysystem.utils.TaskFactory;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class ReadBookController extends ResizeableBaseController implements Initializable {

    private final BookService bookService;
    private final ObservableList<String> data = FXCollections.observableArrayList();
    @FXML
    private MFXButton submit;
    @FXML
    private MFXButton renew;
    @FXML
    private MFXTextField issuedIdInput;
    @FXML
    private MFXTextField numberOfDaysToRenewInput;
    @FXML
    private ListView<String> listView;
    private int numberOfDaysToRenew;
    private String issuedId;
    private Long userId, bookId;


    public ReadBookController(ApplicationContext applicationContext, BookService bookService) {
        super(applicationContext);
        this.bookService = bookService;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tooltipInitializer();
        this.createNewTask(80, 20);
    }

    private void updateFee() {
        this.bookService.updateFee();
    }

    protected void createNewTask(int maxIterations, int sleepTime) {
        Task<Integer> task = TaskFactory.countingTask(maxIterations, sleepTime);

        task.setOnRunning(event -> updateFee());

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @FXML
    private void loadIssuedBookDetails(KeyEvent keyEvent) {
        if (keyEvent.isControlDown() || keyEvent.isShiftDown() || keyEvent.isAltDown() || keyEvent.isMetaDown() ||
                keyEvent.getCode().isArrowKey() || keyEvent.getCode().isFunctionKey() || keyEvent.getCode().isMediaKey() ||
                keyEvent.getCode().isModifierKey() || keyEvent.getCode().isNavigationKey() || keyEvent.getCode().isKeypadKey() ||
                keyEvent.getCode().isLetterKey() || keyEvent.getCode().isDigitKey()) {
            return;
        }


        String text = issuedIdInput.getText();
        String bookIdString;
        String userIdString;
        data.clear();

        if (text.isEmpty()) {
            Alerts.showErrorAlert("Invalid issued id", "Issued id cannot be empty");
            return;
        }

        if (!text.startsWith("ISSUED-")) {
            Alerts.showErrorAlert("Invalid issued id", "Issued id should start with ISSUED-");
            return;
        }

        try {
            String[] split = text.split("-");
            bookIdString = split[1];
            userIdString = split[2];
        } catch (ArrayIndexOutOfBoundsException e) {
            Alerts.showErrorAlert("Invalid issued id", "Issued id should be in format ISSUED-<bookId>-<userId>");
            return;
        }


        try {
            this.bookId = Long.parseLong(bookIdString);
        } catch (NumberFormatException e) {
            Alerts.showErrorAlert("Invalid issued id", "Book id part of issuedId should be a number");
            return;
        }

        try {
            this.userId = Long.parseLong(userIdString);
        } catch (NumberFormatException e) {
            Alerts.showErrorAlert("Invalid issued id", "User id part of issuedId should be a number");
            return;
        }


        IssuedBookDTO issuedBookDTO = bookService.getIssuedBookById(bookId, userId);

        if (issuedBookDTO == null) {
            Alerts.showErrorAlert("Invalid issued id", "There is no issued book with given id");
            return;
        }


        data.add("Issued ID               : " + issuedBookDTO.getIssuedID());
        data.add("Fee                         : " + issuedBookDTO.getFee());
        data.add("Days                       : " + issuedBookDTO.getDays());
        data.add("\nISSUED BOOK INFORMATION");
        data.add("Book ISBN             : " + issuedBookDTO.getBookISBN());
        data.add("Book Title              : " + issuedBookDTO.getBookTitle());
        data.add("Issued Time           : " + issuedBookDTO.getIssuedDate());
        data.add("Return Time          : " + issuedBookDTO.getReturnedDate());
        data.add("Taken                 : " + issuedBookDTO.getIsTaken());
        data.add("\nUSER INFORMATION");
        data.add("User ID                 : " + issuedBookDTO.getUserID());
        data.add("User full name       : " + issuedBookDTO.getUserFullName());
        data.add("Student Email        : " + issuedBookDTO.getUserEmail());

        listView.setItems(data);
        this.issuedId = text;
        this.submit.setDisable(false);

    }

    @FXML
    private void inputNumberOfDays(KeyEvent keyEvent) {
        String text = numberOfDaysToRenewInput.getText();

        if (text.isEmpty()) {
            Alerts.showErrorAlert("Invalid number of days", "Number of days cannot be empty if you want to renew book");
            return;
        }

        try {
            this.numberOfDaysToRenew = Integer.parseInt(text);
        } catch (NumberFormatException ex) {
            Alerts.showErrorAlert("Invalid number of days", "Number of days have to be a number");
            return;
        }

        if (numberOfDaysToRenew < 0) {
            Alerts.showErrorAlert("Invalid number of days", "Number of days cannot be negative");
            return;
        }

        if (numberOfDaysToRenew > 30) {
            Alerts.showErrorAlert("Invalid number of days", "Number of days cannot be greater than 30");
            return;
        }

        if (numberOfDaysToRenew == 0) {
            Alerts.showErrorAlert("Invalid number of days", "Number of days cannot be 0");
            return;
        }

        if (this.issuedId != null) {
            this.renew.setDisable(false);
        }
    }

    @FXML
    private void submitBook(ActionEvent actionEvent) {
        if (!bookService.checkIfBookIsIssuedByUser(this.bookId, this.userId)) {
            Alerts.showErrorAlert("Book not returned", "Book has not been taken yet");
            return;
        }

        if (!bookService.returnBook(this.bookId, this.userId)) {
            Alerts.showErrorAlert("Book not returned", "Book has not been taken yet");
            return;
        }

        if (!bookService.deleteIssuedBook(this.bookId, this.userId)) {
            Alerts.showErrorAlert("Book not returned", "Book has not been returned");
            return;
        }
        Alerts.showSuccessAlert("Book returned", "Book has been returned successfully");
        this.clearInputs();
    }

    @FXML
    private void renewBook(ActionEvent actionEvent) {
        if (bookService.renewBook(this.bookId, this.userId, this.numberOfDaysToRenew)) {
            Alerts.showSuccessAlert("Book renewed", "Book has been renewed successfully");
            this.clearInputs();
            return;
        }
        Alerts.showErrorAlert("Book not renewed", "Book has not been renewed");

    }

    private void clearInputs() {
        this.issuedIdInput.clear();
        this.numberOfDaysToRenewInput.clear();
        this.renew.setDisable(true);
        this.submit.setDisable(true);
        this.listView.getItems().clear();
    }
}
