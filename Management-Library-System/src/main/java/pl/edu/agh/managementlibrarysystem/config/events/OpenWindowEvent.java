package pl.edu.agh.managementlibrarysystem.config.events;

import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.io.Resource;

public class OpenWindowEvent extends ApplicationEvent {
    private Resource fxml;

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
