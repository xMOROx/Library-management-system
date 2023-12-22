package pl.edu.agh.managementlibrarysystem.event.fxml;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

import java.io.Serial;

public class LeavingBorderPaneEvent extends Event {

    public static final EventType<LeavingBorderPaneEvent> LEAVING =
            new EventType<>(Event.ANY, "LEAVING");

    @Serial
    private static final long serialVersionUID = 65689876L;

    public LeavingBorderPaneEvent(EventType<? extends LeavingBorderPaneEvent> type) {
        super(type);
    }

    @Override
    public LeavingBorderPaneEvent copyFor(Object newSource, EventTarget newTarget) {
        return (LeavingBorderPaneEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends LeavingBorderPaneEvent> getEventType() {
        return (EventType<? extends LeavingBorderPaneEvent>) super.getEventType();
    }
}
