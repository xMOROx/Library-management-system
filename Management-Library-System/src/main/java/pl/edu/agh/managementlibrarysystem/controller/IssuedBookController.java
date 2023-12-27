package pl.edu.agh.managementlibrarysystem.controller;

import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import pl.edu.agh.managementlibrarysystem.utils.TaskFactory;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class IssuedBookController extends ControllerWithTableView<IssuedBookDTO> implements Initializable {

    private final BookService bookService;
    private final UserSession session;

    @FXML
    private TableColumn<IssuedBookDTO, String> issuedID;
    @FXML
    private TableColumn<IssuedBookDTO, String> bookISBN;
    @FXML
    private TableColumn<IssuedBookDTO, String> bookTitle;
    @FXML
    private TableColumn<IssuedBookDTO, Integer> userID;
    @FXML
    private TableColumn<IssuedBookDTO, String> userName;
    @FXML
    private TableColumn<IssuedBookDTO, String> issuedDate;
    @FXML
    private TableColumn<IssuedBookDTO, String> returnedDate; //TODO: when date passed, change color
    @FXML
    private TableColumn<IssuedBookDTO, Integer> days;
    @FXML
    private TableColumn<IssuedBookDTO, Integer> fee;

    @Autowired
    public IssuedBookController(ApplicationContext applicationContext, BookService bookService, UserSession session) {
        super(applicationContext);
        this.bookService = bookService;
        this.session = session;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.createNewTask(80, 20);

        this.tableView.setPlaceholder(new Text("No issued books"));

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

                        if (book.getBookISBN().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getBookTitle().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getUserID().toString().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getUserFullName().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getIssuedDate().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getReturnedDate().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getDays().toString().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getFee().toString().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getIssuedID().toLowerCase().contains(lowerCaseFilter)) {
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
    protected void createNewTask(int maxIterations, int sleepTime) {
        Task<Integer> task = TaskFactory.countingTaskForProgressBar(maxIterations, sleepTime, progressBar);

        task.setOnRunning(event -> updateFee());

        task.setOnSucceeded(event -> {
            spinner.setVisible(false);
            loadData();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    protected void loadData() {
        if(session.getLoggedUser().getPermission() == Permission.NORMAL_USER){
            data = this.bookService.getIssuedBooksByUserId(session.getLoggedUser().getId());
        }
        else{
            data = this.bookService.getIssuedBooks();
        }

        this.tableView.getItems().clear();
        this.tableView.getItems().addAll(data);
    }

    @Override
    protected void initializeColumns() {
        issuedID.setCellValueFactory(new PropertyValueFactory<>("issuedID"));
        bookISBN.setCellValueFactory(new PropertyValueFactory<>("bookISBN"));
        bookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        userID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        userName.setCellValueFactory(new PropertyValueFactory<>("userFullName"));
        issuedDate.setCellValueFactory(new PropertyValueFactory<>("issuedDate"));
        returnedDate.setCellValueFactory(new PropertyValueFactory<>("returnedDate"));
        days.setCellValueFactory(new PropertyValueFactory<>("days"));
        fee.setCellValueFactory(new PropertyValueFactory<>("fee"));
    }
}
