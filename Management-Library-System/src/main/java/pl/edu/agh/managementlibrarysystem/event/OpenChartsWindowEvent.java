package pl.edu.agh.managementlibrarysystem.event;

import javafx.fxml.Initializable;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.io.Resource;

import java.util.function.BiConsumer;

@Getter
public class OpenChartsWindowEvent extends ApplicationEvent {

    public OpenChartsWindowEvent(Resource fxml) {
        super(fxml);
    }

}