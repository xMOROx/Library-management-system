package pl.edu.agh.managementlibrarysystem.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.kordamp.ikonli.javafx.FontIcon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ResizeableBaseController;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.recommender.CollaborativeFilteringRecommender;
import pl.edu.agh.managementlibrarysystem.service.NotificationService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;

import java.net.URL;
import java.util.ResourceBundle;


@Controller
public class HomeController extends ResizeableBaseController implements Initializable {
    private final UserSession userSession;
    private final NotificationService notificationService;
    private final CollaborativeFilteringRecommender collaborativeFilteringRecommender;
    @FXML
    private Label numberOfNotifications;
    @FXML
    private Label notificationText1;
    @FXML
    private Label notificationText2;
    @FXML
    private FontIcon notificationIcon;
    @FXML
    private Label userName;
    @FXML
    private Label userEmail;
    @FXML
    private Label userRole;

    @Autowired
    public HomeController(UserSession userSession, NotificationService notificationService, ApplicationContext applicationContext, CollaborativeFilteringRecommender collaborativeFilteringRecommender) {
        super(applicationContext);
        this.userSession = userSession;
        this.notificationService = notificationService;
        this.collaborativeFilteringRecommender = collaborativeFilteringRecommender;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tooltipInitializer();
        User user = userSession.getLoggedUser();

        userRole.setText(user.getPermission().toString().toLowerCase());
        userEmail.setText(user.getEmail());
        userName.setText(user.getFirstname());
        if (user.getPermission().toString().equalsIgnoreCase("normal_user")) {
            Integer notificationAmount = notificationService.getAmount(user.getEmail());
            if (notificationAmount <= 9) {
                numberOfNotifications.setText(notificationAmount.toString());
            } else {
                numberOfNotifications.setText("9+");
            }
        } else {
            notificationIcon.setVisible(false);
            notificationText1.setVisible(false);
            notificationText2.setVisible(false);
            numberOfNotifications.setVisible(false);
        }
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                collaborativeFilteringRecommender.recommend(user);
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

}
