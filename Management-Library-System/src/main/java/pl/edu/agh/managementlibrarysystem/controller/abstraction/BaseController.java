package pl.edu.agh.managementlibrarysystem.controller.abstraction;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseController {
    protected ApplicationContext applicationContext;
    @FXML
    protected Label close;
    @FXML
    protected Label minimize;

    public BaseController(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    protected void tooltipInitializer() {
        Tooltip closeApp = new Tooltip("Close");
        closeApp.setStyle("-fx-font-size:11");
        closeApp.setMinSize(20, 20);
        close.setTooltip(closeApp);
        Tooltip minimizeApp = new Tooltip("Minimize");
        minimizeApp.setStyle("-fx-font-size:11");
        minimizeApp.setMinSize(20, 20);
        minimize.setTooltip(minimizeApp);
    }

    @FXML
    protected void minimize(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    protected void close(MouseEvent event) {
        ((ConfigurableApplicationContext) applicationContext).close();
        Platform.exit();
    }

}
