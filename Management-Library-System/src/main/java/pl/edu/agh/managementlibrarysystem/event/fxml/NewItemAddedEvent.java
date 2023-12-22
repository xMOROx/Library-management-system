package pl.edu.agh.managementlibrarysystem.event.fxml;

import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;

import java.io.Serial;

public class NewItemAddedEvent extends Event {
    public static final EventType<NewItemAddedEvent> NEW_ITEM_ADDED =
            new EventType<>(Event.ANY, "NEW_ITEM_ADDED");

    public static final EventType<NewItemAddedEvent> NEW_AUTHOR =
            new EventType<>(Event.ANY, "NEW_AUTHOR");
    public static final EventType<NewItemAddedEvent> NEW_PUBLISHER =
            new EventType<>(Event.ANY, "NEW_PUBLISHER");
    public static final EventType<NewItemAddedEvent> NEW_GENRE =
            new EventType<>(Event.ANY, "NEW_GENRE");
    @Serial
    private static final long serialVersionUID = 1546543L;

    public NewItemAddedEvent(EventType<? extends NewItemAddedEvent> type) {
        super(type);
    }

    public NewItemAddedEvent(Object source, EventTarget target, EventType<? extends NewItemAddedEvent> type) {
        super(source, target, NEW_ITEM_ADDED);
    }

    @Override
    public NewItemAddedEvent copyFor(Object newSource, EventTarget newTarget) {
        return (NewItemAddedEvent) super.copyFor(newSource, newTarget);
    }

    @Override
    public EventType<? extends NewItemAddedEvent> getEventType() {
        return (EventType<? extends NewItemAddedEvent>) super.getEventType();
    }
}
