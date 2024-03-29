package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseController;
import pl.edu.agh.managementlibrarysystem.event.BorderPaneReadyEvent;
import pl.edu.agh.managementlibrarysystem.event.OpenWindowEvent;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;
import pl.edu.agh.managementlibrarysystem.session.UserSession;
import pl.edu.agh.managementlibrarysystem.utils.ControlsUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Controller
public class MainStageController extends BaseController implements Initializable {

    public BorderPane pane;

    public UserSession session;
    @FXML
    public MFXButton statistics;
    @FXML
    private MFXButton editUser;

    @FXML
    private BorderPane borderpane;
    @FXML
    private VBox homeButtonsPanel;
    @FXML
    private MFXButton back;
    @FXML
    private MFXButton home;
    @FXML
    private MFXButton books;
    @FXML
    private MFXButton users;
    @FXML
    private MFXButton issuedBooks;
    @FXML
    private MFXButton returnBooks;
    @FXML
    private MFXButton allIssuedBooks;
    @FXML
    private MFXButton announcement;
    @FXML
    private MFXButton closeButton;
    @FXML
    private MFXButton settings;
    @FXML
    private MFXButton addUser;
    @FXML
    private MFXButton recommendedBooks;


    public MainStageController(ApplicationContext applicationContext, UserSession session) {
        super(applicationContext);
        this.session = session;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pane = borderpane;
        initializeStageOptions();
        back.setTooltip(new Tooltip("Logout"));
        applicationContext.publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource("fxml/home.fxml")));

    }

    private void initializeStageOptions() {
        if (session.getLoggedUser() == null) {
            return;
        }
        User u = session.getLoggedUser();
        Permission uPermission = u.getPermission();

        if (uPermission == Permission.NORMAL_USER) {
            users.setText("Profile");
            ControlsUtils.changeControlVisibility(addUser, false);
            ControlsUtils.changeControlVisibility(settings, false);

        }
        if (uPermission != Permission.ADMIN) {
            ControlsUtils.changeControlVisibility(editUser, false);
        }

        if (uPermission != Permission.NORMAL_USER) {
            ControlsUtils.changeControlVisibility(recommendedBooks, false);
        }


    }

    @FXML
    private void logout(ActionEvent actionEvent) {
        session.setLoggedUser(null);
        applicationContext
                .publishEvent(new OpenWindowEvent((Stage) ((Node) actionEvent.getSource()).getScene().getWindow(),
                        new ClassPathResource("fxml/login.fxml")));
    }

    @FXML
    private void loadHomePanel(ActionEvent actionEvent) {
        applicationContext.publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource("fxml/home.fxml")));
    }

    @FXML
    private void loadBooksPanel(ActionEvent actionEvent){
        applicationContext.publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource("fxml/books.fxml")));
    }

    @FXML
    private void loadRecommendedBooks(ActionEvent actionEvent) {
        applicationContext.publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource("fxml/recommendedBooks.fxml")));
    }

    @FXML
    private void loadUserPanel(ActionEvent actionEvent) {
        if (session.getLoggedUser() == null) {
            return;
        }
        User u = session.getLoggedUser();
        if (u.getPermission() == Permission.NORMAL_USER) {
            applicationContext.publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource("fxml/profile.fxml")));
        } else {
            applicationContext.publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource("fxml/usersAdmin.fxml")));
        }
    }

    @FXML
    private void loadIssueBooksPanel(ActionEvent actionEvent) {
        applicationContext.publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource("fxml/issueBook.fxml")));
    }

    @FXML
    private void loadReturnBooksPanel(ActionEvent actionEvent) {
        applicationContext.publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource("fxml/returnBook.fxml")));
    }

    @FXML
    private void viewAllIssuedBooks(ActionEvent actionEvent) {
        applicationContext.publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource("fxml/issuedBooks.fxml")));
    }

    @FXML
    private void loadSendAnnouncementsPanel(ActionEvent actionEvent) {
        applicationContext.publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource("fxml/notifications.fxml")));
    }
    @FXML
    private void loadStatisticsPanel(ActionEvent actionEvent) {
        String s;
        if(session.getLoggedUser().getPermission() == Permission.NORMAL_USER){
            s = "fxml/statisticsUser.fxml";
        }
        else{
            s = "fxml/statisticsAdmin.fxml";
        }
        applicationContext.publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource(s)));
    }
    @FXML
    private void loadSettingsPanel(ActionEvent actionEvent) {
        if (this.session.getLoggedUser().getPermission() == Permission.ADMIN) {
            applicationContext.publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource("fxml/adminSettings.fxml")));
        } else {
            applicationContext.publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource("fxml/settings.fxml")));
        }

    }

    @FXML
    private void closeApp(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to close the application ?");
        Optional<ButtonType> option = alert.showAndWait();
        if (option.isPresent() && option.get() == ButtonType.OK) {
            ((ConfigurableApplicationContext) applicationContext).close();
            Platform.exit();
        }
    }

    public void addUserPanel(ActionEvent actionEvent) {
        applicationContext
                .publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource("fxml/addOtherUser.fxml")));
    }

    @FXML
    public void editUserPanel(ActionEvent actionEvent) {
        if (session.getLoggedUser() == null) {
            return;
        }
        User u = session.getLoggedUser();
        if (u.getPermission() == Permission.ADMIN) {
            applicationContext.publishEvent(new BorderPaneReadyEvent(pane, new ClassPathResource("fxml/editUsers.fxml")));
        }
    }


}
