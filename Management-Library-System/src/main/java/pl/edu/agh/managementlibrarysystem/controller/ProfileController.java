package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
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
import pl.edu.agh.managementlibrarysystem.DTO.UserProfileDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ControllerWithTableView;
import pl.edu.agh.managementlibrarysystem.event.BorderPaneReadyEvent;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.service.NotificationService;
import pl.edu.agh.managementlibrarysystem.service.ProfileService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class ProfileController extends ControllerWithTableView<UserProfileDTO> implements Initializable {
    private final UserSession userSession;
    private final NotificationService notificationService;
    private final ProfileService profileService;
    private final BookService bookService;

    @FXML
    public MFXButton editButton;
    @FXML
    public BorderPane borderpane;
    @FXML
    private Label totalFees;
    @FXML
    private Label email;
    @FXML
    private Label lastName;
    @FXML
    private Label firstName;
    @FXML
    private TableColumn<UserProfileDTO, String> tableBookTitle;
    @FXML
    private TableColumn<UserProfileDTO, String> tableBorrowDate;
    @FXML
    private TableColumn<UserProfileDTO, String> tableDueDate;
    @FXML
    private TableColumn<UserProfileDTO, Double> tableDueFees;
    @FXML
    private TableColumn<UserProfileDTO, String> tableCurrentlyBorrowed;
    @FXML
    private Label notifications;

    @Autowired
    public ProfileController(ApplicationContext applicationContext, UserSession userSession, NotificationService notificationService , ProfileService profileService, BookService bookService) {
        super(applicationContext);
        this.userSession = userSession;
        this.notificationService = notificationService;
        this.profileService = profileService;
        this.bookService = bookService;
    }

    
    public void initialize(URL location, ResourceBundle resources){
        this.tooltipInitializer();
        this.initializeColumns();
        this.initData();
    }
    @Override
    protected void createNewTask(int maxIterations, int sleepTime) {
        
    }

    @Override
    protected void initializeColumns() {
        tableBookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        tableBorrowDate.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));
        tableDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        tableDueFees.setCellValueFactory(new PropertyValueFactory<>("dueFees"));
        tableCurrentlyBorrowed.setCellValueFactory(new PropertyValueFactory<>("currentlyBorrowed"));
    }

    @Override
    protected void searchData(KeyEvent keyEvent) {

    }

    @Override
    protected void initData() {
        User currUser = userSession.getLoggedUser();
        this.firstName.setText(currUser.getFirstname());
        this.lastName.setText(currUser.getLastname());
        this.email.setText(currUser.getEmail());
        this.totalFees.setText(String.valueOf(this.bookService.getTotalFeesByUserId(currUser.getId())));
        this.notifications.setText(String.valueOf(notificationService.getAmount(currUser.getEmail())));
        data = this.profileService.getListOfUserProfileDTOofUser(currUser);
        this.tableView.getItems().clear();
        this.tableView.getItems().addAll(data);
    }

    public void editUser(ActionEvent actionEvent) {

        if (userSession.getLoggedUser() == null) {
            return;
        }
        User u = userSession.getLoggedUser();
        applicationContext.publishEvent(new BorderPaneReadyEvent((BorderPane) borderpane.getParent(), new ClassPathResource("fxml/editUsers.fxml")));

    }
}
