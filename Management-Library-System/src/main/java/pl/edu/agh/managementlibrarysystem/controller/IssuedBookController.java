package pl.edu.agh.managementlibrarysystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.IssuedBookDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ControllerWithTableView;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;
import pl.edu.agh.managementlibrarysystem.utils.FxmlPropertyFactory;
import pl.edu.agh.managementlibrarysystem.utils.TaskFactory;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class IssuedBookController extends ControllerWithTableView<IssuedBookDTO> implements Initializable {

    private final BookService bookService;
    private final UserSession session;

    @FXML
    private ContextMenu contextMenu;
    @FXML
    private TableColumn<IssuedBookDTO, String> issuedID;
    @FXML
    private TableColumn<IssuedBookDTO, String> bookISBN;
    @FXML
    private TableColumn<IssuedBookDTO, String> bookTitle;
    @FXML
    private TableColumn<IssuedBookDTO, Number> userID;
    @FXML
    private TableColumn<IssuedBookDTO, String> userName;
    @FXML
    private TableColumn<IssuedBookDTO, String> issuedDate;
    @FXML
    private TableColumn<IssuedBookDTO, String> returnedDate; //TODO: when date passed, change color
    @FXML
    private TableColumn<IssuedBookDTO, Number> days;
    @FXML
    private TableColumn<IssuedBookDTO, Number> fee;
    @FXML
    private TableColumn<IssuedBookDTO, String> isTaken;

    @Autowired
    public IssuedBookController(ApplicationContext applicationContext, BookService bookService, UserSession session) {
        super(applicationContext);
        this.bookService = bookService;
        this.session = session;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.createNewTask();

        this.tableView.setPlaceholder(new Text("No issued books"));

        if (session.getLoggedUser().getPermission() == Permission.NORMAL_USER) {
            this.contextMenu.getItems().remove(0);
        }
    }

    private void updateFee() {
        this.bookService.updateFee();
    }

    @Override
    protected void searchData(KeyEvent keyEvent) {
        FilteredList<IssuedBookDTO> filteredList = new FilteredList<>(this.tableView.getItems(), p -> true);


        searchTextField.textProperty().addListener(
                (ObservableValue, oldValue, newValue) -> {
                    filteredList.setPredicate(book -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (book.getBookISBN().getValue().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getBookTitle().getValue().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getUserID().toString().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getUserFullName().getValue().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getIssuedDate().getValue().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getReturnedDate().getValue().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getDays().toString().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getFee().toString().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getIssuedID().getValue().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getIsTaken().getValue().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }

                        tableView.setPlaceholder(new Text("No record match your search"));
                        return false;
                    });
                    SortedList<IssuedBookDTO> sortedList = new SortedList<>(filteredList);
                    sortedList.comparatorProperty().bind(tableView.comparatorProperty());
                    tableView.getItems().setAll(sortedList);
                }
        );
    }

    @Override
    protected void createNewTask() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                spinner.setVisible(true);
                progressBar.setVisible(true);
                initData();
                return null;
            }
        };


        task.setOnRunning(event -> updateFee());
        task.setOnSucceeded(event -> {
            spinner.setVisible(false);
            progressBar.setVisible(false);
        });

        TaskFactory.startTask(task);
    }

    @Override
    protected void initData() {
        if (session.getLoggedUser().getPermission() == Permission.NORMAL_USER) {
            data = FXCollections.observableArrayList(this.bookService.getIssuedBooksByUserId(session.getLoggedUser().getId()));
        } else {
            data = FXCollections.observableArrayList(this.bookService.getIssuedBooks());
        }

        this.tableView.getItems().clear();
        this.tableView.getItems().addAll(data);
    }

    @Override
    protected void initializeColumns() {
        issuedID.setCellValueFactory(cell -> cell.getValue().getIssuedID());
        bookISBN.setCellValueFactory(cell -> cell.getValue().getBookISBN());
        bookTitle.setCellValueFactory(cell -> cell.getValue().getBookTitle());
        userID.setCellValueFactory(cell -> cell.getValue().getUserID());
        userName.setCellValueFactory(cell -> cell.getValue().getUserFullName());
        issuedDate.setCellValueFactory(cell -> cell.getValue().getIssuedDate());
        returnedDate.setCellValueFactory(cell -> cell.getValue().getReturnedDate());
        days.setCellValueFactory(cell -> cell.getValue().getDays());
        fee.setCellValueFactory(cell -> cell.getValue().getFee());
        isTaken.setCellValueFactory(cell -> cell.getValue().getIsTaken());
    }

    public void issueBookToUser(ActionEvent actionEvent) {
        IssuedBookDTO issuedBookDTO = this.tableView.getSelectionModel().getSelectedItem();
        if (issuedBookDTO.getIsTaken().getValue().equalsIgnoreCase("yes")) {
            Alerts.showAlert("Book already issued", "Book is already issued to user", "Please choose another book");
            return;
        }

        issuedBookDTO.setIsTaken(FxmlPropertyFactory.stringProperty("Yes"));
        tableView.refresh();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                bookService.issueBookToUser(issuedBookDTO);
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            Alerts.showAlert("Book issued", "Book issued to user", "Book issued to user successfully");
        });

        TaskFactory.startTask(task);

    }
}
