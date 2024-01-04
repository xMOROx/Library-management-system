package pl.edu.agh.managementlibrarysystem.event;

import javafx.fxml.Initializable;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.io.Resource;

import java.util.function.BiConsumer;

@Getter
public class OpenNewBookWindowEvent<T> extends ApplicationEvent {

    private final T book;
    private final BiConsumer<Object, Initializable> consumer;
    private final String title;

    public OpenNewBookWindowEvent(Resource fxml, T book, BiConsumer<Object, Initializable> consumer, String title) {
        super(fxml);
        this.book = book;
        this.consumer = consumer;
        this.title = title;
    }

    public OpenNewBookWindowEvent(Resource fxml, T book, BiConsumer<Object, Initializable> consumer) {
        super(fxml);
        this.book = book;
        this.consumer = consumer;
        this.title = "Pop up window";
    }

}
