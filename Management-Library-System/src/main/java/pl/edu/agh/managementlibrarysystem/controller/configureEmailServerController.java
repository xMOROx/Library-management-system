package pl.edu.agh.managementlibrarysystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseDataEntryController;

@Controller
public class configureEmailServerController extends BaseDataEntryController<ActionEvent> implements Initializable {

    @FXML
    private CheckBox authentication;
    @FXML
    private CheckBox tls;
    @FXML
    private TextField serverName;
    @FXML
    private TextField serverPort;
    @FXML
    private TextField systemEmail;
    @FXML
    private PasswordField emailPassword;


    @Override
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
    }

    @FXML
    @Override
    protected void save(ActionEvent event) {

    }

    @FXML
    @Override
    protected void cancel(ActionEvent event) {

    }

    @Override
    protected void clearFields() {

    }
}
