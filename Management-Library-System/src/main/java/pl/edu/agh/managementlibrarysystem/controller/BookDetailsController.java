package pl.edu.agh.managementlibrarysystem.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.BookDetailsDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ResizeableBaseController;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.utils.ImageLoader;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class BookDetailsController extends ResizeableBaseController implements Initializable {
    private final BookService bookService;
    private final ImageLoader imageLoader;
    private final ObservableList<String> data = FXCollections.observableArrayList();
    private BookDetailsDTO book;
    private String bookISBN;
    @FXML
    private Pane mainContainer;
    @FXML
    private ListView<String> bookDetailsView;
    @FXML
    private ImageView bookCover;


    public BookDetailsController(ApplicationContext applicationContext,
                                 ImageLoader imageLoader,
                                 BookService bookService) {
        super(applicationContext);
        this.bookService = bookService;
        this.imageLoader = imageLoader;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tooltipInitializer();
    }

    public void setBookISBN(String bookISBN) {
        this.bookISBN = bookISBN;
        this.book = bookService.getBookDetails(bookISBN);
        this.initializeBookDetails();
        this.initializeBookCover();
    }

    public void initializeBookDetails() {
        data.clear();

        data.add("Book ISBN:              " + book.getIsbn());
        data.add("Book Title:              " + book.getTitle());
        data.add("Authors:          " + book.getAuthors());
        data.add("Publisher:          " + book.getPublisher());
        data.add("Genres:          " + book.getGenres());
        data.add("Edition:          " + book.getEdition());
        data.add("Quantity:          " + book.getQuantity());
        data.add("Remaining amount:          " + book.getRemainingBooks());
        data.add("Cover Type:          " + book.getCover());
        data.add("Description:          " + book.getDescription());
        data.add("Table of contents:          " + book.getTableOfContent());
        data.add("Available:          " + (book.getAvailability().equalsIgnoreCase("AVAILABLE") ? "YES" : "NO"));

        this.bookDetailsView.setItems(data);
    }

    public void initializeBookCover() {
        this.bookCover.setImage(this.imageLoader.getImage(book.getImage()));
    }

    @Override
    protected void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}
