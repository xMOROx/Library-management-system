package pl.edu.agh.managementlibrarysystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.ReadBookAvailableToVoteDTO;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class ReadBookAvailableToReviewController implements Initializable {
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
    private TableColumn<ReadBookAvailableToVoteDTO, Integer> bookEdition;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeColumns();
        initData();
        this.tableViewForReadBook.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    private void initializeColumns() {
        bookISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        bookAuthors.setCellValueFactory(new PropertyValueFactory<>("authors"));
        bookPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        bookGenres.setCellValueFactory(new PropertyValueFactory<>("genres"));
        bookEdition.setCellValueFactory(new PropertyValueFactory<>("edition"));
        returnedDate.setCellValueFactory(new PropertyValueFactory<>("returnedDate"));
    }

    private void initData() {
        data = this.bookService.getAllReadBookAvailableToVoteByUserId(this.session.getLoggedUser());
        this.tableViewForReadBook.getItems().clear();
        this.tableViewForReadBook.getItems().addAll(data);
    }

    @FXML
    private void refreshList(ActionEvent actionEvent) {

    }

    @FXML
    private void rateBook(ActionEvent actionEvent) {

    }
}
