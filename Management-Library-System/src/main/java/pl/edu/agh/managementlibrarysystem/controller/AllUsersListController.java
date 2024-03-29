package pl.edu.agh.managementlibrarysystem.controller;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.UserDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ControllerWithTableView;
import pl.edu.agh.managementlibrarysystem.service.UserService;
import pl.edu.agh.managementlibrarysystem.utils.TaskFactory;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class AllUsersListController extends ControllerWithTableView<UserDTO> implements Initializable {
    private final UserService userService;

    @FXML
    private TableColumn<UserDTO, Long> userId;
    @FXML
    private TableColumn<UserDTO, String> fullName;
    @FXML
    private TableColumn<UserDTO, String> email;
    @FXML
    private TableColumn<UserDTO, String> permission;

    public AllUsersListController(ApplicationContext applicationContext, UserService userService) {
        super(applicationContext);
        this.userService = userService;
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
                progressBar.setVisible(true);
                initData();
                return null;
            }
        };
        task.setOnSucceeded(event -> {
            spinner.setVisible(false);
            progressBar.setVisible(false);
        });

        TaskFactory.startTask(task);
    }

    @Override
    protected void initializeColumns() {
        userId.setCellValueFactory(new PropertyValueFactory<>("id"));
        fullName.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        email.setCellValueFactory(new PropertyValueFactory<>("email"));
        permission.setCellValueFactory(new PropertyValueFactory<>("permission"));
    }

    @Override
    protected void searchData(KeyEvent keyEvent) {

    }

    @Override
    protected void initData() {
        data = FXCollections.observableArrayList(this.userService.getAllUsers());
        this.tableView.getItems().clear();
        this.tableView.getItems().addAll(data);
    }

    @FXML
    private void deleteUser(MouseEvent mouseEvent) {
        this.data.remove(tableView.getSelectionModel().getSelectedItem());

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                userService.deleteByUserId(tableView.getSelectionModel().getSelectedItem().getId());
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
}
