package pl.edu.agh.managementlibrarysystem.event.listeners;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.config.CommonEventActions;
import pl.edu.agh.managementlibrarysystem.event.OpenNewBookWindowEvent;

import java.net.URL;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class OpenNewBookWindowListener implements ApplicationListener<OpenNewBookWindowEvent<?>> {

    private final ApplicationContext applicationContext;

    public OpenNewBookWindowListener(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(OpenNewBookWindowEvent event) {
        try {

            URL url = ((Resource) event.getSource()).getURL();


            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(this.applicationContext::getBean);

            Stage stage = new Stage();
            Parent loaded = fxmlLoader.load();
            Initializable controller = fxmlLoader.getController();

            BiConsumer<Object, Initializable> consumer = event.getConsumer();
            consumer.accept(event.getBook(), controller);


            CommonEventActions.createPrimaryStage(stage, loaded, "Book details");

            stage.show();

        } catch (Exception ex) {
            Logger.getLogger(OpenNewBookWindowListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
