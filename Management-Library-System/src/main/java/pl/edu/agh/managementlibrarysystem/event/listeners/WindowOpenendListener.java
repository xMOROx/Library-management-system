package pl.edu.agh.managementlibrarysystem.event.listeners;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.config.CommonEventActions;
import pl.edu.agh.managementlibrarysystem.event.OpenWindowEvent;

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

            CommonEventActions.enableDrag(stage, root);
            stage.centerOnScreen();
            stage.setScene(scene);
        } catch (IOException e) {
            Logger.getLogger(LoadingStageListener.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
