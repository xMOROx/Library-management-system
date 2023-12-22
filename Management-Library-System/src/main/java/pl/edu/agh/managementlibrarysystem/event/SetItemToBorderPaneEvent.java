package pl.edu.agh.managementlibrarysystem.event;

import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import org.springframework.context.ApplicationEvent;
import pl.edu.agh.managementlibrarysystem.enums.BorderpaneFields;

import java.util.List;

public class SetItemToBorderPaneEvent<T extends Node> extends ApplicationEvent {
    public Object[] getItems() {
        return (Object[]) getSource();
    }
    public SetItemToBorderPaneEvent(T  source, BorderPane target, BorderpaneFields field) {
        super(List.of(source, target, field).toArray());
    }
}
