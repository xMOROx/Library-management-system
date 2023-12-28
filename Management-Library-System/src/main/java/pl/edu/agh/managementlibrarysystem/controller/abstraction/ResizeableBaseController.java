package pl.edu.agh.managementlibrarysystem.controller.abstraction;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public abstract class ResizeableBaseController extends BaseController {

    @FXML
    protected Label fullscreen;
    @FXML
    protected Label unfullscreen;

    public ResizeableBaseController(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    protected void tooltipInitializer() {
        Tooltip fullScreen = new Tooltip("fullscreen");
        fullScreen.setStyle("-fx-font-size:11");
        fullScreen.setMinSize(20, 20);
        fullscreen.setTooltip(fullScreen);

        Tooltip exitFullScreen = new Tooltip("Exit full screen");
        exitFullScreen.setStyle("-fx-font-size:11");
        exitFullScreen.setMinSize(20, 20);
        unfullscreen.setTooltip(exitFullScreen);
        Image closeImage = new Image("/images/close.png");
        close.setGraphic(new ImageView(closeImage));
        Image unfullscreenImage = new Image("/images/unfullscreen.png");
        unfullscreen.setGraphic(new ImageView(unfullscreenImage));
        Image fullscreenImage = new Image("/images/fullscreen.png");
        fullscreen.setGraphic(new ImageView(fullscreenImage));
        Image minimizeImage = new Image("/images/minimize.png");
        minimize.setGraphic(new ImageView(minimizeImage));
    }

    @FXML
    protected void fullscreen(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        if (!stage.isFullScreen()) {
            stage.setFullScreen(true);
        }
    }

    @FXML
    protected void unfullscreen(MouseEvent mouseEvent) {
        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        if (stage.isFullScreen()) {
            stage.setFullScreen(false);
        }
    }

}
