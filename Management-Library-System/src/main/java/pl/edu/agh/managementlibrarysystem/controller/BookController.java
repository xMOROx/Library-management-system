package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import lombok.RequiredArgsConstructor;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ControllerWithTableView;
import pl.edu.agh.managementlibrarysystem.enums.BorderpaneFields;
import pl.edu.agh.managementlibrarysystem.event.BorderPaneReadyEvent;
import pl.edu.agh.managementlibrarysystem.event.SetItemToBorderPaneEvent;
import pl.edu.agh.managementlibrarysystem.event.fxml.LeavingBorderPaneEvent;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.utils.TaskFactory;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class BookController extends ControllerWithTableView<BookDTO> implements Initializable {

    private final BookService bookService;

    @FXML
    private FontIcon searchIcon;
    @FXML
    private BorderPane borderpane;
    @FXML
    private MFXButton loadDataEntryButton;
    @FXML
    private Label booksAmount;
    @FXML
    private Label booksLabel;
    @FXML
    private Label remainingBooksAmount;
    @FXML
    private Label remainingBooksLabel;
    @FXML
    private ImageView arrow;
    @FXML
    private CheckBox checkAllCheckbox;
    @FXML
    private Hyperlink delete;

    @FXML
    private TableColumn<BookDTO, String> bookISBN;
    @FXML
    private TableColumn<BookDTO, String> bookTitle;
    @FXML
    private TableColumn<BookDTO, String> bookAuthor;
    @FXML
    private TableColumn<BookDTO, String> bookPublisher;
    @FXML
    private TableColumn<BookDTO, String> mainGenre;
    @FXML
    private TableColumn<BookDTO, Integer> edition;
    @FXML
    private TableColumn<BookDTO, Integer> quantity;
    @FXML
    private TableColumn<BookDTO, Integer> remainingBooks;
    @FXML
    private TableColumn<BookDTO, String> availability;


    @Autowired
    public BookController(ApplicationContext applicationContext, BookService bookService) {
        this.applicationContext = applicationContext;
        this.bookService = bookService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.createNewTask(50, 20);
        this.loadDataEntryButton.disableProperty().setValue(true);


        this.borderpane.addEventHandler(
                LeavingBorderPaneEvent.LEAVING,
                event -> {
                    this.applicationContext.publishEvent(new SetItemToBorderPaneEvent<>(this.tableView, this.borderpane, BorderpaneFields.CENTER));
                    this.loadData();
                    this.changeFieldsVisibility(true);
                }
        );


        this.tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.checkAllCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.tableView.getSelectionModel().selectAll();
            } else {
                this.tableView.getSelectionModel().clearSelection();
            }
        });
    }

    @Override
    protected void initializeColumns() {
        bookISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        bookPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        mainGenre.setCellValueFactory(new PropertyValueFactory<>("mainGenre"));
        edition.setCellValueFactory(new PropertyValueFactory<>("edition"));
        quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        remainingBooks.setCellValueFactory(new PropertyValueFactory<>("remainingBooks"));
        availability.setCellValueFactory(new PropertyValueFactory<>("availability"));
    }

    @Override
    protected void createNewTask(int maxIterations, int sleepTime) {
        Task<Integer> task = TaskFactory.countingTaskForProgressBar(maxIterations, sleepTime, progressBar);

        task.setOnSucceeded(event -> {
            spinner.setVisible(false);
            this.loadData();
            this.allBooksAndRemainingBooks();
            this.loadDataEntryButton.disableProperty().setValue(false);
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void allBooksAndRemainingBooks() {
        this.booksAmount.setText(String.valueOf(this.bookService.getSumOfAllBooks()));
        this.remainingBooksAmount.setText(String.valueOf(this.bookService.getSumOfAllRemainingBooks()));
    }

    @Override
    protected void loadData() {
        data = this.bookService.getBooks();
        this.tableView.getItems().clear();
        this.tableView.getItems().addAll(data);
    }

    @Override
    protected void searchData(KeyEvent keyEvent) {
        FilteredList<BookDTO> filteredList = new FilteredList<>(this.tableView.getItems(), p -> true);

        searchTextField.textProperty().addListener(
                (ObservableValue, oldValue, newValue) -> {
                    filteredList.setPredicate(book -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();

                        if (book.getIsbn().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getTitle().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getAuthor().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getPublisher().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getMainGenre().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getEdition().toString().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getQuantity().toString().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getRemainingBooks().toString().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getAvailability().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        tableView.setPlaceholder(new Text("No record match your search"));
                        return false;
                    });
                    SortedList<BookDTO> sortedList = new SortedList<>(filteredList);
                    sortedList.comparatorProperty().bind(tableView.comparatorProperty());
                    tableView.getItems().setAll(sortedList);
                }
        );
    }

    @FXML
    private void loadUpdateBook(ActionEvent actionEvent) {

    }

    @FXML
    private void deleteBook(ActionEvent actionEvent) {

    }

    @FXML
    private void deleteSelectedBooks(ActionEvent actionEvent) {

    }

    @FXML
    private void loadDataBookEntry(ActionEvent actionEvent) {
        changeFieldsVisibility(false);
        this.applicationContext.publishEvent(new BorderPaneReadyEvent(this.borderpane, new ClassPathResource("fxml/bookDataEntry.fxml")));
    }

    private void changeFieldsVisibility(Boolean visible) {
        this.searchTextField.setVisible(visible);
        this.booksLabel.setVisible(visible);
        this.remainingBooksLabel.setVisible(visible);
        this.booksAmount.setVisible(visible);
        this.remainingBooksAmount.setVisible(visible);
        this.searchIcon.setVisible(visible);
        this.loadDataEntryButton.setVisible(visible);
        this.arrow.setVisible(visible);
        this.checkAllCheckbox.setVisible(visible);
        this.delete.setVisible(visible);
    }

}
