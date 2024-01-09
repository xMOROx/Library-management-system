package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ResizeableBaseController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
@Slf4j
public class AdminSettingsController extends ResizeableBaseController implements Initializable {

    @FXML
    private BorderPane borderPane;
    @FXML
    private MFXButton adminInformationButton;
    @FXML
    private MFXButton feeButton;
    @FXML
    private MFXButton mailServerSettings;
    @FXML
    private MFXButton signInOptions;

    public AdminSettingsController(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tooltipInitializer();

    }

    @FXML
    private void loadAdminInfo(ActionEvent actionEvent) {
    }

    @FXML
    private void loadChangeFeePanel(ActionEvent actionEvent) {
        this.LoadView("changeFeePerDay");
    }

    @FXML
    private void loadMailServerSettingsPanel(ActionEvent actionEvent) {
        this.LoadView("configureEmailServer");
    }

    @FXML
    private void loadSignInOptions(ActionEvent actionEvent) {
        this.LoadView("changeAdminPassword");
    }

    private void LoadView(String viewName) {
        try {
            FXMLLoader loader = new FXMLLoader(new ClassPathResource("fxml/" + viewName + ".fxml").getURL());
            loader.setControllerFactory(applicationContext::getBean);
            borderPane.setCenter(loader.load());
        } catch (IOException e) {
            log.error("Error while loading view: " + viewName, e);
        }

    }
}
