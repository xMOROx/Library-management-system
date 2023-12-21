package pl.edu.agh.managementlibrarysystem.event.listeners;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.event.BorderPaneReadyEvent;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class BorderPaneListener implements ApplicationListener<BorderPaneReadyEvent> {

    private final ApplicationContext applicationContext;

    public BorderPaneListener(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(BorderPaneReadyEvent event) {
        try {
            Object[] source = event.getBorderPane();

            BorderPane borderPane = (BorderPane) source[0];
            URL url = ((Resource) source[1]).getURL();

            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(this.applicationContext::getBean);

            borderPane.setCenter(fxmlLoader.load());
        } catch (Exception ex) {
            Logger.getLogger(BorderPaneListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
