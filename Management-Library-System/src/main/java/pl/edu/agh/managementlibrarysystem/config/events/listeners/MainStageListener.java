package pl.edu.agh.managementlibrarysystem.config.events.listeners;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.config.events.CommonActions;
import pl.edu.agh.managementlibrarysystem.config.events.MainAppReadyEvent;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class MainStageListener implements ApplicationListener<MainAppReadyEvent> {

    private final Resource fxml;
    private final ApplicationContext applicationContext;

    public MainStageListener(@Value("classpath:/fxml/main-stage.fxml") Resource fxml,
                             ApplicationContext applicationContext) {
        this.fxml = fxml;
        this.applicationContext = applicationContext;
    }


    @Override
    public void onApplicationEvent(MainAppReadyEvent event) {
        try {
            Stage stage = event.getStage();
            URL url = this.fxml.getURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(this.applicationContext::getBean);

            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            CommonActions.enableDrag(stage, root);
            stage.centerOnScreen();
            stage.setScene(scene);

        } catch (IOException e) {
            Logger.getLogger(MainStageListener.class.getName()).log(Level.SEVERE, null, e);
        }
    }


}
