package pl.edu.agh.managementlibrarysystem.controllers;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.config.events.CommonActions;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class LoadController implements Initializable {

    private final ApplicationContext applicationContext;

    @FXML
    private Label text;
    @FXML
    private Label developer;
    @FXML
    private Label close;
    @FXML
    private Label minimize;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CommonActions.tooltipInitializer(close, minimize);
        FadeTransition fadeinText = new FadeTransition(Duration.seconds(1), text);
        fadeinText.setToValue(1);

        TranslateTransition moveText = new TranslateTransition(Duration.seconds(1), text);
        moveText.setToX(80);
        moveText.play();
        moveText.setOnFinished((e) -> {
            fadeinText.play();
        });

    }

    @FXML
    private void close(MouseEvent event) {
        ((ConfigurableApplicationContext)applicationContext).close();
        Platform.exit();
    }

    @FXML
    private void minimize(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }
}
