package pl.edu.agh.managementlibrarysystem.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
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
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.models.Book;
import pl.edu.agh.managementlibrarysystem.services.BookService;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class BookController implements Initializable {
    public static HBox box;

    private final ApplicationContext applicationContext;

    private final BookService bookService;
    @FXML
    private FontIcon searchIcon;
    @FXML
    private BorderPane borderpane;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private MFXButton allStudents;
    @FXML
    private Label minimise;
    @FXML
    private Label fullscreen;
    @FXML
    private Label unfullscreen;
    @FXML
    private Label close;
    @FXML
    private ImageView spinner;
    @FXML
    private Label label1;
    @FXML
    private Label allBooks;
    @FXML
    private Label label2;
    @FXML
    private Label rBooks;
    @FXML
    private TextField searchTextField;
    @FXML
    private TableView<Book> tableView;
    @FXML
    private CheckBox checkAll;
    @FXML
    private TableColumn check;
    @FXML
    private TableColumn booKID;
    @FXML
    private TableColumn bookName;
    @FXML
    private TableColumn bookAuthor;
    @FXML
    private TableColumn bookPublisher;
    @FXML
    private TableColumn edition;
    @FXML
    private TableColumn quantity;
    @FXML
    private TableColumn remainingBooks;
    @FXML
    private TableColumn sectionCol;
    @FXML
    private TableColumn availability;
    @FXML
    private TableColumn description;

    @FXML
    private HBox controlBox;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        box = controlBox;
//        CommonActions.tooltipInitializer(close, minimise);

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
        minimise.setGraphic(new ImageView(minimizeImage));
    }

    @FXML
    public void minimize(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    public void fullscreen(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        if (!stage.isFullScreen()) {
            stage.setFullScreen(true);
        }
    }

    @FXML
    public void unfullscreen(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
        }
    }

    @FXML
    public void close(MouseEvent mouseEvent) {
        ((ConfigurableApplicationContext) applicationContext).close();
        Platform.exit();
    }

    @FXML
    public void searchBook(KeyEvent keyEvent) {

    }

    @FXML
    public void loadUpdateBook(ActionEvent actionEvent) {

    }

    @FXML
    public void deleteBook(ActionEvent actionEvent) {

    }

    @FXML
    public void deleteSelectedBooks(ActionEvent actionEvent) {

    }

    @FXML
    public void loadBookDataentry(ActionEvent actionEvent) {

    }
}
