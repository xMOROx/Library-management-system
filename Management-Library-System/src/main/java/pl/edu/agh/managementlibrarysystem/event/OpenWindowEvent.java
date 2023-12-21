package pl.edu.agh.managementlibrarysystem.event;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.io.Resource;

public class OpenWindowEvent extends ApplicationEvent {
    private final Resource fxml;

    public Stage getStage() {
        return (Stage) getSource();
    }
    public Resource getFxml(){
        return fxml;
    }
    public OpenWindowEvent(Stage source, Resource fxml) {
        super(source);
        this.fxml = fxml;
    }
}
