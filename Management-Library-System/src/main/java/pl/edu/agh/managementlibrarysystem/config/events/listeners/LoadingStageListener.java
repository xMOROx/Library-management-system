package pl.edu.agh.managementlibrarysystem.config.events.listeners;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
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
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class LoadingStageListener implements ApplicationListener<StageReadyEvent> {
    private final String applicationTitle;
    private final Resource fxmlLoadingScreen;
    private final Resource fxmlLoginScreen;
    private final ApplicationContext applicationContext;

    public LoadingStageListener(@Value("${spring.application.iu.title}") String applicationTitle,
                                @Value("classpath:/fxml/load.fxml") Resource fxmlLoadingScreen,
                                @Value("classpath:/fxml/login.fxml") Resource fxmlLoginScreen,
                                ApplicationContext applicationContext) {
        this.applicationTitle = applicationTitle;
        this.fxmlLoadingScreen = fxmlLoadingScreen;
        this.fxmlLoginScreen = fxmlLoginScreen;
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        try {

            Stage stage = event.getStage();
            URL url = this.fxmlLoadingScreen.getURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(this.applicationContext::getBean);

            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.getIcons().add(new Image("/images/books.png"));

            stage.setResizable(true);

            enableDrag(stage, root);

            stage.setScene(scene);
            stage.setTitle(this.applicationTitle);
            stage.show();

            Thread thread = getThreadLoginScreen(stage);
            thread.start();
        } catch (IOException e) {
            Logger.getLogger(LoadingStageListener.class.getName()).log(Level.SEVERE, null, e);
        }

    }

    private void enableDrag(Stage stage, Parent root) {
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

    private Thread getThreadLoginScreen(Stage stage) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(3000);
                Platform.runLater(() -> {
                    try {
                        URL url1 = fxmlLoginScreen.getURL();
                        FXMLLoader fxmlLoader1 = new FXMLLoader(url1);
                        fxmlLoader1.setControllerFactory(applicationContext::getBean);

                        Parent root2 = fxmlLoader1.load();

                        enableDrag(stage, root2);

                        Scene scene2 = new Scene(root2);

                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(LoadingStageListener.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        stage.setScene(scene2);
                        stage.show();
                    } catch (IOException ex) {
                        Logger.getLogger(LoadingStageListener.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                return null;
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        return thread;
    }
}
