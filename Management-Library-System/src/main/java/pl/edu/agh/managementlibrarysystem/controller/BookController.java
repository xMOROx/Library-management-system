package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
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
import pl.edu.agh.managementlibrarysystem.controller.popups.BookDetailsController;
import pl.edu.agh.managementlibrarysystem.enums.BorderpaneFields;
import pl.edu.agh.managementlibrarysystem.event.BorderPaneReadyEvent;
import pl.edu.agh.managementlibrarysystem.event.OpenNewBookWindowEvent;
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
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class BookController extends ControllerWithTableView<BookDTO> {

    private final BookService bookService;
    private final UserSession session;
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
    private TableColumn<BookDTO, Number> edition;
    @FXML
    private TableColumn<BookDTO, Number> quantity;
    @FXML
    private TableColumn<BookDTO, Number> remainingBooks;
    @FXML
    private TableColumn<BookDTO, String> availability;


    public BookController(ApplicationContext applicationContext, BookService bookService, UserSession session) {
        super(applicationContext);
        this.bookService = bookService;
        this.session = session;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        initializeStageOptions();


        this.loadDataEntryButton.disableProperty().setValue(true);


        this.borderPane.addEventHandler(
                LeavingBorderPaneEvent.LEAVING,
                event -> {
                    this.applicationContext.publishEvent(
                            new SetItemToBorderPaneEvent<>(this.tableView, this.borderPane, BorderpaneFields.CENTER));
                    this.createNewTask();
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

        this.createNewTask();

    }

    private void initializeStageOptions() {
        if (session.getLoggedUser() == null) {
            return;
        }
        User u = session.getLoggedUser();
        if (u.getPermission() == Permission.NORMAL_USER) {
            ControlsUtils.changeControlVisibility(loadDataEntryButton, false);
            for (int i = 0; i < 2; i++) {
                this.contextMenu.getItems().remove(1);
            }
        }
    }

    @Override
    protected void initializeColumns() {
        bookISBN.setCellValueFactory(cell -> cell.getValue().getIsbn());
        bookTitle.setCellValueFactory(cell -> cell.getValue().getTitle());
        bookAuthors.setCellValueFactory(cell -> cell.getValue().getAuthors());
        bookPublisher.setCellValueFactory(cell -> cell.getValue().getPublisher());
        mainGenre.setCellValueFactory(cell -> cell.getValue().getMainGenre());
        edition.setCellValueFactory(cell -> cell.getValue().getEdition());
        quantity.setCellValueFactory(cell -> cell.getValue().getQuantity());
        remainingBooks.setCellValueFactory(cell -> cell.getValue().getRemainingBooks());
        availability.setCellValueFactory(cell -> cell.getValue().getAvailability());
    }

    @Override
    protected void createNewTask() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                loadDataEntryButton.disableProperty().setValue(true);
                progressBar.setVisible(true);
                spinner.setVisible(true);
                initData();
                Platform.runLater(() -> allBooksAndRemainingBooks());
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            spinner.setVisible(false);
            this.progressBar.setVisible(false);
            this.loadDataEntryButton.disableProperty().setValue(false);
        });

        TaskFactory.startTask(task);
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
        List<BookDTO> temp = this.session.getLoggedUser().getPermission() == Permission.NORMAL_USER ?
                this.bookService.getAllAvailableBooks() : this.bookService.getAllBooks();

        data = FXCollections.observableArrayList(temp);
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

                        if (book.getIsbn().getValue().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getTitle().getValue().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getAuthors().getValue().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getPublisher().getValue().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getMainGenre().getValue().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getEdition().toString().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getQuantity().toString().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getRemainingBooks().toString().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        } else if (book.getAvailability().getValue().toLowerCase().contains(lowerCaseFilter)) {
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
        this.createNewTask();
    }
    @FXML
    private void deleteBook(ActionEvent actionEvent) {
        BookDTO bookDTO = this.tableView.getSelectionModel().getSelectedItem();
        if (bookDTO == null) {
            Alerts.showErrorAlert("No book selected", "Please select book to delete");
            return;
        }

        this.data.remove(bookDTO);
        this.booksAmount.setText(String.valueOf(Integer.parseInt(this.booksAmount.getText()) - bookDTO.getQuantity().getValue()));
        this.remainingBooksAmount.setText(String.valueOf(Integer.parseInt(this.remainingBooksAmount.getText()) - bookDTO.getRemainingBooks().getValue()));
        Alerts.showSuccessAlert("Book deleted", "Book has been deleted");

        tableView.refresh();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                bookService.deleteBookByIsbn(bookDTO.getIsbn().getValue());
                return null;
            }
        };

        TaskFactory.startTask(task);
    }

    @FXML
    private void deleteSelectedBooks(ActionEvent actionEvent) {
        this.tableView.getSelectionModel().getSelectedItems().forEach(bookDTO -> {
            this.data.remove(bookDTO);
            this.booksAmount.setText(String.valueOf(Integer.parseInt(this.booksAmount.getText()) - bookDTO.getQuantity().getValue()));
            this.remainingBooksAmount.setText(String.valueOf(Integer.parseInt(this.remainingBooksAmount.getText()) - bookDTO.getRemainingBooks().getValue()));

            tableView.refresh();

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    bookService.deleteBookByIsbn(bookDTO.getIsbn().getValue());
                    return null;
                }
            };

            Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
        });


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
                new OpenNewBookWindowEvent<>(new ClassPathResource("fxml/bookDetails.fxml"), bookDTO, (book, controller) -> {
                    BookDetailsController bookDetailsController = (BookDetailsController) controller;
                    bookDetailsController.setBookISBN(((BookDTO) book).getIsbn().getValue());
                }, "Book details"));

    }

    @FXML
    private void loadReadBookAvailableToReview(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(new ClassPathResource("fxml/readBookTable.fxml").getURL());
        fxmlLoader.setControllerFactory(this.applicationContext::getBean);

        this.borderPane.setCenter(fxmlLoader.load());
        this.changeVisibilityOfControls(false);

    }

    @FXML
    private void loadAllData(ActionEvent actionEvent) {
        this.borderPane.setCenter(this.tableView);
        this.changeVisibilityOfControls(true);
    }
    @FXML
    private void changeAvailability(ActionEvent actionEvent) {
        BookDTO bookDTO = this.tableView.getSelectionModel().getSelectedItem();
        if (bookDTO == null) {
            Alerts.showErrorAlert("No book selected", "Please select book to change availability");
            return;
        }

        if (bookDTO.getAvailability().getValue().equals("available")) {
            bookDTO.setAvailability(new SimpleStringProperty("unavailable"));
            this.remainingBooksAmount.setText(String.valueOf(Integer.parseInt(this.remainingBooksAmount.getText()) - bookDTO.getRemainingBooks().getValue()));
            Alerts.showSuccessAlert("Book unavailable", "Book is now unavailable");
        } else {
            bookDTO.setAvailability(new SimpleStringProperty("available"));
            this.remainingBooksAmount.setText(String.valueOf(Integer.parseInt(this.remainingBooksAmount.getText()) + bookDTO.getRemainingBooks().getValue()));
            Alerts.showSuccessAlert("Book available", "Book is now available");
        }

        tableView.refresh();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                bookService.updateBook(bookDTO.getIsbn().toString());
                return null;
            }
        };

        TaskFactory.startTask(task);
    }
    private void changeVisibilityOfControls(Boolean visible) {
        this.readBooksButton.setVisible(visible);
        this.allBooksButton.setVisible(!visible);
        this.bookDetailsButton.setVisible(visible);

        if (visible) {
            this.delete.setVisible(this.session.getLoggedUser().getPermission() != Permission.NORMAL_USER);
        } else {
            this.delete.setVisible(false);
        }

        this.arrow.setVisible(visible);
        this.checkAllCheckbox.setVisible(visible);
        this.remainingBooksAmount.setVisible(visible);
        this.remainingBooksLabel.setVisible(visible);
        this.booksAmount.setVisible(visible);
        this.booksLabel.setVisible(visible);
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
