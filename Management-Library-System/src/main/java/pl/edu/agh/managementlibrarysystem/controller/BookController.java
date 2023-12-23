package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseController;
import pl.edu.agh.managementlibrarysystem.enums.BorderpaneFields;
import pl.edu.agh.managementlibrarysystem.event.BorderPaneReadyEvent;
import pl.edu.agh.managementlibrarysystem.event.SetItemToBorderPaneEvent;
import pl.edu.agh.managementlibrarysystem.event.fxml.LeavingBorderPaneEvent;
import pl.edu.agh.managementlibrarysystem.service.BookService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class BookController extends BaseController implements Initializable {

    private final BookService bookService;

    @FXML
    private FontIcon searchIcon;
    @FXML
    private BorderPane borderpane;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private MFXButton loadDataEntryButton;
    @FXML
    private Label fullscreen;
    @FXML
    private Label unfullscreen;
    @FXML
    private ImageView spinner;
    @FXML
    private Label booksAmount;
    @FXML
    private Label booksLabel;
    @FXML
    private Label remainingBooksAmount;
    @FXML
    private Label remainingBooksLabel;
    @FXML
    private TextField searchTextField;
    @FXML
    private ImageView arrow;
    @FXML
    private CheckBox checkAllCheckbox;
    @FXML
    private Hyperlink delete;

    @FXML
    private TableView<BookDTO> tableView;
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

    @FXML
    private HBox controlBox;

    private List<BookDTO> books;

    @Autowired
    public BookController(ApplicationContext applicationContext, BookService bookService) {
        this.applicationContext = applicationContext;
        this.bookService = bookService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tooltipInitializer();

        Tooltip fullScreen = new Tooltip("fullscreen");
        fullScreen.setStyle("-fx-font-size:11");
        fullScreen.setMinSize(20, 20);
        fullscreen.setTooltip(fullScreen);

        Tooltip exitFullScreen = new Tooltip("Exit full screen");
        exitFullScreen.setStyle("-fx-font-size:11");
        exitFullScreen.setMinSize(20, 20);
        unfullscreen.setTooltip(exitFullScreen);
        Image closeImage = new Image("/images/close.png");
        close.setGraphic(new ImageView(closeImage));
        Image unfullscreenImage = new Image("/images/unfullscreen.png");
        unfullscreen.setGraphic(new ImageView(unfullscreenImage));
        Image fullscreenImage = new Image("/images/fullscreen.png");
        fullscreen.setGraphic(new ImageView(fullscreenImage));
        Image minimizeImage = new Image("/images/minimize.png");
        minimize.setGraphic(new ImageView(minimizeImage));

        this.borderpane.addEventHandler(
                LeavingBorderPaneEvent.LEAVING,
                event -> {
                    this.applicationContext.publishEvent(new SetItemToBorderPaneEvent<>(this.tableView, this.borderpane, BorderpaneFields.CENTER));
                    this.changeFieldsVisibility(true);
                }
        );

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                this.tableView.getItems().setAll(books);
            }
        });

        this.initializeColumns();
        this.createNewTask(50, 20);

        this.tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.checkAllCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.tableView.getSelectionModel().selectAll();
            } else {
                this.tableView.getSelectionModel().clearSelection();
            }
        });
    }

    private void initializeColumns() {
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

    private void createNewTask(int maxIterations, int sleepTime) {
        Task<Integer> task = new Task<>() {
            @Override
            protected Integer call() throws Exception {
                for (int i = 0; i <= maxIterations; i++) {
                    if (isCancelled()) {
                        break;
                    }
                    updateProgress(i, maxIterations);
                    Thread.sleep(sleepTime);
                }
                return maxIterations;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());
        task.setOnSucceeded(event -> {
            spinner.setVisible(false);
            this.loadData();
            this.allBooksAndRemainingBooks();
        });
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void allBooksAndRemainingBooks() {
        this.booksAmount.setText(String.valueOf(this.bookService.getSumOfAllBooks()));
        this.remainingBooksAmount.setText(String.valueOf(this.bookService.getSumOfAllRemainingBooks()));
    }

    private void loadData() {
        books = this.bookService.getBooks();
        this.tableView.getItems().clear();
        this.tableView.getItems().addAll(books);
    }

    @FXML
    private void fullscreen(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        if (!stage.isFullScreen()) {
            stage.setFullScreen(true);
        }
    }

    @FXML
    private void unfullscreen(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
        }
    }

    @FXML
    private void searchBook(KeyEvent keyEvent) {
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
