package pl.edu.agh.managementlibrarysystem.event.listeners;

import javafx.scene.Node;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import lombok.NoArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import pl.edu.agh.managementlibrarysystem.enums.BorderpaneFields;
import pl.edu.agh.managementlibrarysystem.event.SetItemToBorderPaneEvent;

import java.util.logging.Level;
import java.util.logging.Logger;

@Component
@NoArgsConstructor
public class SetItemToBorderPaneListener implements ApplicationListener<SetItemToBorderPaneEvent<? extends Node>> {

    @Override
    public void onApplicationEvent(SetItemToBorderPaneEvent event) {
        try {
            Object[] items = event.getItems();
            Node item = (Node) items[0];
            BorderPane borderPane = (BorderPane) items[1];

            switch ((BorderpaneFields)items[2]) {
                case CENTER:
                    borderPane.setCenter(item);
                    break;
                case TOP:
                    borderPane.setTop(item);
                    break;
                case BOTTOM:
                    borderPane.setBottom(item);
                    break;
                case LEFT:
                    borderPane.setLeft(item);
                    break;
                case RIGHT:
                    borderPane.setRight(item);
                    break;
            }
        } catch (Exception ex) {
            Logger.getLogger(SetItemToBorderPaneListener.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
