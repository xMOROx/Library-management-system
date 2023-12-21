package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseController;
import pl.edu.agh.managementlibrarysystem.event.BorderPaneReadyEvent;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.service.BookService;

import java.net.URL;
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
    private CheckBox checkAll;
    @FXML
    private TableView<Book> tableView;
    @FXML
    private TableColumn<Book, String> bookISBN;
    @FXML
    private TableColumn<Book, String> bookTitle;
    @FXML
    private TableColumn<Book, String> bookAuthor; //TODO: concatenate name and surname
    @FXML
    private TableColumn<Book, String> bookPublisher;
    @FXML
    private TableColumn<Book, String> mainGenre;
    @FXML
    private TableColumn<Book, Integer> edition;
    @FXML
    private TableColumn<Book, Integer> quantity;
    @FXML
    private TableColumn<Book, Integer> remainingBooks;
    @FXML
    private TableColumn<Book, String> sectionCol;
    @FXML
    private TableColumn<Book, String> availability;

    @FXML
    private HBox controlBox;
    public static HBox box;


    @Autowired
    public BookController(ApplicationContext applicationContext, BookService bookService) {
        this.applicationContext = applicationContext;
        this.bookService = bookService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        box = controlBox;
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
    }

}
