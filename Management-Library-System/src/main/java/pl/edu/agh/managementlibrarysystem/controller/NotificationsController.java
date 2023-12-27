package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXCheckbox;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.NotificationDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ControllerWithTableView;
import pl.edu.agh.managementlibrarysystem.enums.BorderpaneFields;
import pl.edu.agh.managementlibrarysystem.event.BorderPaneReadyEvent;
import pl.edu.agh.managementlibrarysystem.event.SetItemToBorderPaneEvent;
import pl.edu.agh.managementlibrarysystem.event.fxml.LeavingBorderPaneEvent;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.service.NotificationService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;
import pl.edu.agh.managementlibrarysystem.utils.TaskFactory;

import java.net.URL;
import java.sql.Date;
import java.util.ResourceBundle;
@Controller
@RequiredArgsConstructor
public class NotificationsController extends ControllerWithTableView<NotificationDTO> implements Initializable {
    @FXML
    private MFXCheckbox ignoreResolved;
    @FXML
    private ContextMenu contextMenu;
    @FXML
    private HBox toKill1;
    @FXML
    private HBox toKill2;
    private NotificationService notificationService;
    private UserSession userSession;
    @FXML
    private TableView<NotificationDTO> tableView;
    @FXML
    private BorderPane borderPane;
    @FXML
    private User currUser;

    @FXML
    private MFXButton addNotification;
    @FXML
    private TableColumn<NotificationDTO,Long> userID;
    @FXML
    private TableColumn<NotificationDTO, String> bookISBN;
    @FXML
    private TableColumn<NotificationDTO, String> typeOfNotification;
    @FXML
    private TableColumn<NotificationDTO, String> bookTitle;
    @FXML
    private TableColumn<NotificationDTO, Date> dateOfAdmition;
    @FXML
    private TableColumn<NotificationDTO, Boolean> status;


    @Autowired
    public NotificationsController(UserSession userSession, NotificationService notificationService, ApplicationContext applicationContext) {
        this.userSession = userSession;
        this.notificationService = notificationService;
        this.applicationContext = applicationContext;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources){
        currUser = userSession.getLoggedUser();
        if(currUser.getPermission().toString().equalsIgnoreCase("normal_user")){
            addNotification.setVisible(false);
        }
        this.tooltipInitializer();
        this.initializeColumns();
        this.createNewTask(50, 20);
        this.borderPane.addEventHandler(
                LeavingBorderPaneEvent.LEAVING,
                event -> {
                    this.applicationContext.publishEvent(new SetItemToBorderPaneEvent<>(this.tableView, this.borderPane, BorderpaneFields.CENTER));
                    this.loadData();
                    setVisibility(true);
                }
        );
    }
    @Override
    protected void createNewTask(int maxIterations, int sleepTime) {
        Task<Integer> task = TaskFactory.countingTaskForProgressBar(maxIterations, sleepTime, progressBar);

        task.setOnSucceeded(event -> {
            spinner.setVisible(false);
            this.loadData();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    @Override
    protected void initializeColumns() {
        userID.setCellValueFactory(new PropertyValueFactory<>("userID"));
        bookISBN.setCellValueFactory(new PropertyValueFactory<>("bookISBN"));
        bookTitle.setCellValueFactory(new PropertyValueFactory<>("bookTitle"));
        typeOfNotification.setCellValueFactory(new PropertyValueFactory<>("typeOfNotification"));
        dateOfAdmition.setCellValueFactory(new PropertyValueFactory<>("dateOfAdmition"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    @Override
    protected void searchData(KeyEvent keyEvent) {
    }

    @Override
    protected void loadData() {
        data = this.notificationService.getNotifications(currUser,ignoreResolved.isSelected());
        this.tableView.getItems().clear();
        this.tableView.getItems().addAll(data);
    }
    @FXML
    private void addNotification(ActionEvent actionEvent) {
        setVisibility(false);
        this.applicationContext.publishEvent(new BorderPaneReadyEvent(this.borderPane, new ClassPathResource("fxml/addNotification.fxml")));
    }
    private void setVisibility(boolean visibility){
        toKill1.setVisible(visibility);
        toKill2.setVisible(visibility);
        ignoreResolved.setVisible(visibility);
    }
    @FXML
    private void deleteNotification(ActionEvent actionEvent) {
        NotificationDTO notificationDTO = tableView.getSelectionModel().getSelectedItem();
        String msg = notificationService.deleteNotification(notificationDTO);
        Alerts.showInformationAlert("Notification notification",msg);
        loadData();
    }
    @FXML
    private void resolveNotification(ActionEvent actionEvent) {
        NotificationDTO notificationDTO = tableView.getSelectionModel().getSelectedItem();
        String msg = notificationService.resolveNotifications(notificationDTO);
        Alerts.showInformationAlert("Notification notification",msg);
        loadData();
    }
    @FXML
    private void ignoreResolved(ActionEvent actionEvent) {
        this.ignoreResolved.setSelected(!ignoreResolved.isSelected());
    }
}
