package pl.edu.agh.managementlibrarysystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.ReadBookAvailableToVoteDTO;
import pl.edu.agh.managementlibrarysystem.controller.popups.ReviewBookController;
import pl.edu.agh.managementlibrarysystem.event.OpenNewBookWindowEvent;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class ReadBookAvailableToReviewController implements Initializable {
    private final ApplicationContext applicationContext;
    private final BookService bookService;
    private final UserSession session;

    private List<ReadBookAvailableToVoteDTO> data;
    @FXML
    private ContextMenu contextMenu;
    // READ BOOKS
    @FXML
    private TableView<ReadBookAvailableToVoteDTO> tableViewForReadBook;
    @FXML
    private TableColumn<ReadBookAvailableToVoteDTO, String> bookISBN;
    @FXML
    private TableColumn<ReadBookAvailableToVoteDTO, String> bookTitle;
    @FXML
    private TableColumn<ReadBookAvailableToVoteDTO, String> bookAuthors;
    @FXML
    private TableColumn<ReadBookAvailableToVoteDTO, String> bookPublisher;
    @FXML
    private TableColumn<ReadBookAvailableToVoteDTO, String> bookGenres;
    @FXML
    private TableColumn<ReadBookAvailableToVoteDTO, String> returnedDate;
    @FXML
    private TableColumn<ReadBookAvailableToVoteDTO, Number> bookEdition;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeColumns();
        initData();
    }

    private void initializeColumns() {
        bookISBN.setCellValueFactory(cell -> cell.getValue().getIsbn());
        bookTitle.setCellValueFactory(cell -> cell.getValue().getTitle());
        bookAuthors.setCellValueFactory(cell -> cell.getValue().getAuthors());
        bookPublisher.setCellValueFactory(cell -> cell.getValue().getPublisher());
        bookGenres.setCellValueFactory(cell -> cell.getValue().getGenres());
        bookEdition.setCellValueFactory(cell -> cell.getValue().getEdition());
        returnedDate.setCellValueFactory(cell -> cell.getValue().getReturnedDate());
    }

    private void initData() {
        data = this.bookService.getAllReadBookAvailableToVoteByUserId(this.session.getLoggedUser());
        this.tableViewForReadBook.getItems().clear();
        this.tableViewForReadBook.getItems().addAll(data);
    }

    @FXML
    private void refreshList(ActionEvent actionEvent) {
        initData();
    }

    @FXML
    private void rateBook(ActionEvent actionEvent) {
        ReadBookAvailableToVoteDTO bookDTO = this.tableViewForReadBook.getSelectionModel().getSelectedItem();
        tableViewForReadBook.refresh();
        this.applicationContext.publishEvent(
                new OpenNewBookWindowEvent<>(new ClassPathResource("fxml/reviewBook.fxml"), bookDTO, (book, controller) -> {
                    ReviewBookController reviewBookController = (ReviewBookController) controller;
                    reviewBookController.setBook((ReadBookAvailableToVoteDTO) book);
                }, "Book review"));
    }
}
