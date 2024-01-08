package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.ReviewBookDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ControllerWithTableView;
import pl.edu.agh.managementlibrarysystem.event.BorderPaneReadyEvent;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.service.NotificationService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;
import pl.edu.agh.managementlibrarysystem.utils.TaskFactory;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class ProfileController extends ControllerWithTableView<ReviewBookDTO> implements Initializable {
    private final UserSession userSession;
    private final NotificationService notificationService;
    private final BookService bookService;

    @FXML
    public MFXButton editButton;
    @FXML
    public BorderPane borderpane;
    public TableColumn<ReviewBookDTO, String> bookISBN;
    public TableColumn<ReviewBookDTO, String> bookTitle;
    public TableColumn<ReviewBookDTO, String> bookAuthors;
    public TableColumn<ReviewBookDTO, String> bookGenres;
    public TableColumn<ReviewBookDTO, Integer> bookEdition;
    public TableColumn<ReviewBookDTO, Integer> bookRating;
    @FXML
    private Label totalFees;
    @FXML
    private Label email;
    @FXML
    private Label lastName;
    @FXML
    private Label firstName;
    @FXML
    private Label notifications;

    @Autowired
    public ProfileController(ApplicationContext applicationContext,
                             UserSession userSession,
                             NotificationService notificationService,
                             BookService bookService) {
        super(applicationContext);
        this.userSession = userSession;
        this.notificationService = notificationService;
        this.bookService = bookService;
    }


    public void initialize(URL location, ResourceBundle resources) {
        this.tooltipInitializer();
        this.initializeColumns();
        this.createNewTask();
    }

    @Override
    protected void createNewTask() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                spinner.setVisible(true);
                initData();
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            spinner.setVisible(false);
        });

        TaskFactory.startTask(task);
    }

    @Override
    protected void initializeColumns() {
        this.bookISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        this.bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.bookAuthors.setCellValueFactory(new PropertyValueFactory<>("authors"));
        this.bookGenres.setCellValueFactory(new PropertyValueFactory<>("genres"));
        this.bookEdition.setCellValueFactory(new PropertyValueFactory<>("edition"));
        this.bookRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
    }

    @Override
    protected void searchData(KeyEvent keyEvent) {

    }

    @Override
    protected void initData() {
        User currUser = userSession.getLoggedUser();
        Platform.runLater(
                () -> {
                    this.firstName.setText(currUser.getFirstname());
                    this.lastName.setText(currUser.getLastname());
                    this.email.setText(currUser.getEmail());
                    this.totalFees.setText(String.valueOf(this.bookService.getTotalFeesByUserId(currUser.getId())));
                    this.notifications.setText(String.valueOf(notificationService.getAmount(currUser.getEmail())));
                }
        );
        data = FXCollections.observableArrayList(this.bookService.getAllReadBookForUser(currUser));
        this.tableView.getItems().clear();
        this.tableView.getItems().addAll(data);
    }

    public void editUser(ActionEvent actionEvent) {

        if (userSession.getLoggedUser() == null) {
            return;
        }
        applicationContext.publishEvent(new BorderPaneReadyEvent((BorderPane) borderpane.getParent(), new ClassPathResource("fxml/editUsers.fxml")));

    }
}
