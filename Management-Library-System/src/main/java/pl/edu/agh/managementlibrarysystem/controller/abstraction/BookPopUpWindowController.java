package pl.edu.agh.managementlibrarysystem.controller.abstraction;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.utils.ImageLoader;

@Controller
public abstract class BookPopUpWindowController<T> extends ResizeableBaseController implements Initializable {
    protected final BookService bookService;
    protected final ImageLoader imageLoader;
    protected final ObservableList<String> data = FXCollections.observableArrayList();
    protected T book;
    @FXML
    protected Pane mainContainer;
    @FXML
    protected ListView<String> bookListView;


    public BookPopUpWindowController(ApplicationContext applicationContext, BookService bookService, ImageLoader imageLoader) {
        super(applicationContext);
        this.bookService = bookService;
        this.imageLoader = imageLoader;
    }

    @Override
    protected void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    protected void initializeBookDetails() {
        data.clear();
    }
}
