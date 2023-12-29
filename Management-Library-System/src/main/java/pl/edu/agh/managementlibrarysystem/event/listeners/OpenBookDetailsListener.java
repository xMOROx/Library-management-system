package pl.edu.agh.managementlibrarysystem.event.listeners;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.config.CommonEventActions;
import pl.edu.agh.managementlibrarysystem.controller.BookDetailsController;
import pl.edu.agh.managementlibrarysystem.event.OpenBookDetailsEvent;

import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class OpenBookDetailsListener implements ApplicationListener<OpenBookDetailsEvent> {

    private final ApplicationContext applicationContext;

    public OpenBookDetailsListener(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(OpenBookDetailsEvent event) {
        try {

            URL url = ((Resource) event.getSource()).getURL();
            BookDTO book = event.getBook();

            FXMLLoader fxmlLoader = new FXMLLoader(url);
            fxmlLoader.setControllerFactory(this.applicationContext::getBean);

            Stage stage = new Stage();
            Parent loaded = fxmlLoader.load();
            BookDetailsController controller = fxmlLoader.getController();

            controller.setBookISBN(book.getIsbn());

            CommonEventActions.createPrimaryStage(stage, loaded, "Book details");

            stage.show();

        } catch (Exception ex) {
            Logger.getLogger(OpenBookDetailsListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
