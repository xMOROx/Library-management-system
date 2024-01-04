package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ControllerWithTableView;
import pl.edu.agh.managementlibrarysystem.enums.BorderpaneFields;
import pl.edu.agh.managementlibrarysystem.event.BorderPaneReadyEvent;
import pl.edu.agh.managementlibrarysystem.event.OpenBookDetailsEvent;
import pl.edu.agh.managementlibrarysystem.event.SetItemToBorderPaneEvent;
import pl.edu.agh.managementlibrarysystem.event.fxml.LeavingBorderPaneEvent;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;
import pl.edu.agh.managementlibrarysystem.utils.ControlsUtils;
import pl.edu.agh.managementlibrarysystem.utils.TaskFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class BookController extends ControllerWithTableView<BookDTO> {

    private final BookService bookService;
    private final UserSession session;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private FontIcon searchIcon;
    @FXML
    private MFXButton loadDataEntryButton;
    @FXML
    private MFXButton bookDetailsButton;
    @FXML
    private MFXButton allBooksButton;
    @FXML
    private MFXButton readBooksButton;
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
    private TableColumn<BookDTO, String> bookAuthors;
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


    public BookController(ApplicationContext applicationContext, BookService bookService, UserSession session) throws IOException {
        super(applicationContext);
        this.bookService = bookService;
        this.session = session;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        initializeStageOptions();


        this.createNewTask(50, 20);
        this.loadDataEntryButton.disableProperty().setValue(true);


        this.borderPane.addEventHandler(
                LeavingBorderPaneEvent.LEAVING,
                event -> {
                    this.applicationContext.publishEvent(
                            new SetItemToBorderPaneEvent<>(this.tableView, this.borderPane, BorderpaneFields.CENTER));
                    this.initData();
                    this.changeFieldsVisibility(true);
                });

        this.tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        this.checkAllCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                this.tableView.getSelectionModel().selectAll();
            } else {
                this.tableView.getSelectionModel().clearSelection();
            }
        });

        if (this.session.getLoggedUser().getPermission() == Permission.NORMAL_USER) {
            this.delete.setVisible(false);
            this.availability.setVisible(false);
        } else {
            this.readBooksButton.setVisible(false);
        }


        this.bookDetailsButton.disableProperty()
                .bind(this.tableView.getSelectionModel().selectedItemProperty().isNull());

    }

    private void initializeStageOptions() {
        if (session.getLoggedUser() == null) {
            return;
        }
        User u = session.getLoggedUser();
        if (u.getPermission() == Permission.NORMAL_USER) {
            ControlsUtils.changeControlVisibility(loadDataEntryButton, false);
            this.contextMenu.getItems().remove(1);
        }
    }

    @Override
    protected void initializeColumns() {
        bookISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthors.setCellValueFactory(new PropertyValueFactory<>("authors"));
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
            this.initData();
            this.allBooksAndRemainingBooks();
            this.loadDataEntryButton.disableProperty().setValue(false);
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void allBooksAndRemainingBooks() {
        int amountOfAllBooks = this.session.getLoggedUser().getPermission() == Permission.NORMAL_USER ?
                this.bookService.getSumOfAllAvailableBooks() : this.bookService.getSumOfAllBooks();
        int amountOfAllRemainingBooks = this.session.getLoggedUser().getPermission() == Permission.NORMAL_USER ?
                this.bookService.getSumOfAllRemainingAvailableBooks() : this.bookService.getSumOfAllRemainingBooks();
        this.booksAmount.setText(String.valueOf(amountOfAllBooks));
        this.remainingBooksAmount.setText(String.valueOf(amountOfAllRemainingBooks));
    }

    @Override
    protected void initData() {
        data = this.session.getLoggedUser().getPermission() == Permission.NORMAL_USER ?
                this.bookService.getAllAvailableBooks() : this.bookService.getAllBooks();
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
                        } else if (book.getAuthors().toLowerCase().contains(lowerCaseFilter)) {
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
                });
    }

    @FXML
    private void refreshList(ActionEvent actionEvent) {
        this.initData();
        this.allBooksAndRemainingBooks();
    }

    @FXML
    private void deleteBook(ActionEvent actionEvent) {
        BookDTO bookDTO = this.tableView.getSelectionModel().getSelectedItem();
        if (bookDTO == null) {
            Alerts.showErrorAlert("No book selected", "Please select book to delete");
            return;
        }


        this.bookService.deleteBookByIsbn(bookDTO.getIsbn());
        this.initData();
        this.allBooksAndRemainingBooks();
        Alerts.showSuccessAlert("Book deleted", "Book has been deleted");
    }

    @FXML
    private void deleteSelectedBooks(ActionEvent actionEvent) {
        this.tableView.getSelectionModel().getSelectedItems().forEach(bookDTO -> {
            this.bookService.deleteBookByIsbn(bookDTO.getIsbn());
        });
        this.initData();
        this.allBooksAndRemainingBooks();
        Alerts.showSuccessAlert("Books deleted", "All selected books have been deleted");
    }

    @FXML
    private void loadDataBookEntry(ActionEvent actionEvent) {
        changeFieldsVisibility(false);
        this.applicationContext.publishEvent(
                new BorderPaneReadyEvent(this.borderPane, new ClassPathResource("fxml/bookDataEntry.fxml")));
    }

    @FXML
    private void loadBookDetails(ActionEvent actionEvent) {
        if (this.tableView.getSelectionModel().getSelectedItems().size() > 1) {
            Alerts.showErrorAlert("Too many books selected", "Please select only one book to see details");
            return;
        }

        BookDTO bookDTO = this.tableView.getSelectionModel().getSelectedItem();

        this.applicationContext.publishEvent(
                new OpenBookDetailsEvent(new ClassPathResource("fxml/bookDetails.fxml"), bookDTO));

    }

    @FXML
    private void loadReadBookAvailableToReview(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(new ClassPathResource("fxml/readBookTable.fxml").getURL());
        fxmlLoader.setControllerFactory(this.applicationContext::getBean);

        this.readBooksButton.setVisible(false);
        this.allBooksButton.setVisible(true);

        this.borderPane.setCenter(fxmlLoader.load());

        this.delete.setVisible(false);
        this.arrow.setVisible(false);
        this.checkAllCheckbox.setVisible(false);
        this.remainingBooksAmount.setVisible(false);
        this.remainingBooksLabel.setVisible(false);
        this.booksAmount.setVisible(false);
        this.booksLabel.setVisible(false);

    }

    @FXML
    private void loadAllData(ActionEvent actionEvent) {
        this.readBooksButton.setVisible(true);
        this.allBooksButton.setVisible(false);
        this.borderPane.setCenter(this.tableView);

        this.delete.setVisible(true);
        this.arrow.setVisible(true);
        this.checkAllCheckbox.setVisible(true);
        this.remainingBooksAmount.setVisible(true);
        this.remainingBooksLabel.setVisible(true);
        this.booksAmount.setVisible(true);
        this.booksLabel.setVisible(true);
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
        this.bookDetailsButton.setVisible(visible);
    }


}
