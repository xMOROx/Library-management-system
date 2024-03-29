package pl.edu.agh.managementlibrarysystem.event;

import javafx.scene.layout.BorderPane;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.io.Resource;

import java.util.List;

public class BorderPaneReadyEvent extends ApplicationEvent {
    public BorderPaneReadyEvent(BorderPane source, Resource fxml) {
        super(List.of(source, fxml).toArray());
    }

    public Object[] getBorderPane() {
        return (Object[]) getSource();
    }
}
