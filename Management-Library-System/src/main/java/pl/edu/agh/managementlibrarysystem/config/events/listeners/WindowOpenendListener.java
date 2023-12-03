package pl.edu.agh.managementlibrarysystem.config.events.listeners;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.config.events.CommonActions;
import pl.edu.agh.managementlibrarysystem.config.events.OpenWindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class WindowOpenendListener implements ApplicationListener<OpenWindowEvent> {

    private final ApplicationContext applicationContext;

    @Override
    public void onApplicationEvent(OpenWindowEvent event) {
        try {
            Stage stage = event.getStage();
            URL url = null;
            url = event.getFxml().getURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(this.applicationContext::getBean);

            Parent root = fxmlLoader.load();
            Scene scene = new Scene(root);

            CommonActions.enableDrag(stage, root);
            stage.centerOnScreen();
            stage.setScene(scene);
        } catch (IOException e) {
            Logger.getLogger(LoadingStageListener.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
