package pl.edu.agh.managementlibrarysystem.config.events.listeners;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.config.events.StageReadyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class StageListener implements ApplicationListener<StageReadyEvent> {
    private final String applicationTitle;
    private final Resource fxml;
    private final ApplicationContext applicationContext;

    public StageListener(@Value("${spring.application.iu.title}") String applicationTitle,
                         @Value("classpath:/fxml/main-stage.fxml") Resource fxml, ApplicationContext applicationContext) {
        this.applicationTitle = applicationTitle;
        this.fxml = fxml;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {
            AtomicReference<Double> xOffset = new AtomicReference<>((double) 0);
            AtomicReference<Double> yOffset = new AtomicReference<>((double) 0);

            Stage stage = event.getStage();
            URL url = this.fxml.getURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(this.applicationContext::getBean);

            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root, 800, 600);
            stage.initStyle(StageStyle.UNDECORATED);

            stage.setResizable(true);

            root.setOnMousePressed(mouseEvent -> {
                xOffset.set(mouseEvent.getSceneX());
                yOffset.set(mouseEvent.getSceneY());
            });

            root.setOnMouseDragged(mouseEvent -> {
                stage.setX(mouseEvent.getScreenX() - xOffset.get());
                stage.setY(mouseEvent.getScreenY() - yOffset.get());
            });

            stage.setScene(scene);
            stage.setTitle(this.applicationTitle);
            stage.show();
        } catch (IOException ignored) {
        }

    }
}
