package pl.edu.agh.managementlibrarysystem.config.events;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

public class MainAppReadyEvent extends ApplicationEvent {
    public Stage getStage() {
        return (Stage) getSource();
    }

    public MainAppReadyEvent(Stage source) {
        super(source);
    }
}
