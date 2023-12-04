package pl.edu.agh.managementlibrarysystem.config.events;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicReference;

@Component
public class CommonActions {
    public static void enableDrag(Stage stage, Parent root) {
        AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
        AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);

        root.setOnMousePressed(mouseEvent -> {
            xOffset.set(mouseEvent.getSceneX());
            yOffset.set(mouseEvent.getSceneY());
        });

        root.setOnMouseDragged(mouseEvent -> {
            stage.setX(mouseEvent.getScreenX() - xOffset.get());
            stage.setY(mouseEvent.getScreenY() - yOffset.get());
        });
    }

    public static void createPrimaryStage(Stage stage, Parent root, String applicationTitle) {
        Scene scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image("/images/books.png"));

        stage.setResizable(true);

        CommonActions.enableDrag(stage, root);

        stage.setScene(scene);
        stage.setTitle(applicationTitle);

    }

    public static void tooltipInitializer(Label close, Label minimize) {
        Tooltip closeApp = new Tooltip("Close");
        closeApp.setStyle("-fx-font-size:11");
        closeApp.setMinSize(20, 20);
        close.setTooltip(closeApp);
        Tooltip minimizeApp = new Tooltip("Minimize");
        minimizeApp.setStyle("-fx-font-size:11");
        minimizeApp.setMinSize(20, 20);
        minimize.setTooltip(minimizeApp);
    }
}
