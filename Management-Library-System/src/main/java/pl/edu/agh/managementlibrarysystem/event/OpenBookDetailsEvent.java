package pl.edu.agh.managementlibrarysystem.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.io.Resource;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;

@Getter
public class OpenBookDetailsEvent extends ApplicationEvent {

    private final BookDTO book;

    public OpenBookDetailsEvent(Resource fxml, BookDTO book) {
        super(fxml);
        this.book = book;
    }

}
