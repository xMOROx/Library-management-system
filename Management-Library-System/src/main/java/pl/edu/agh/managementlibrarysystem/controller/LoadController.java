package pl.edu.agh.managementlibrarysystem.controller;

import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.Duration;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseController;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class LoadController extends BaseController implements Initializable {

    @FXML
    private Label text;
    @FXML
    private Label developer;

    public LoadController(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.tooltipInitializer();

        FadeTransition fadeinText = new FadeTransition(Duration.seconds(1), text);
        fadeinText.setToValue(1);

        TranslateTransition moveText = new TranslateTransition(Duration.seconds(1), text);
        moveText.setToX(80);
        moveText.play();
        moveText.setOnFinished((e) -> {
            fadeinText.play();
        });

    }
}
