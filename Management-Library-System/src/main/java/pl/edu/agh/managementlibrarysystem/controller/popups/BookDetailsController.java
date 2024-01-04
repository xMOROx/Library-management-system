package pl.edu.agh.managementlibrarysystem.controller.popups;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.BookDetailsDTO;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.utils.ImageLoader;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class BookDetailsController extends BookPopUpWindowController<BookDetailsDTO> implements Initializable {

    @FXML
    private ImageView bookCover;


    public BookDetailsController(ApplicationContext applicationContext,
                                 ImageLoader imageLoader,
                                 BookService bookService) {
        super(applicationContext, bookService, imageLoader);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tooltipInitializer();
    }

    public void setBookISBN(String bookISBN) {
        this.book = bookService.getBookDetails(bookISBN);
        this.initializeBookDetails();
        this.initializeBookCover();
    }

    public void initializeBookDetails() {
        super.initializeBookDetails();
        String description = book.getDescription() == null ? "No description" : book.getDescription();
        String tableOfContent = book.getTableOfContent() == null ? "No table of content" : book.getTableOfContent();

        data.add("Book ISBN:              " + book.getIsbn());
        data.add("Book Title:              " + book.getTitle());
        data.add("Authors:          " + book.getAuthors());
        data.add("Publisher:          " + book.getPublisher());
        data.add("Genres:          " + book.getGenres());
        data.add("Edition:          " + book.getEdition());
        data.add("Quantity:          " + book.getQuantity());
        data.add("Remaining amount:          " + book.getRemainingBooks());
        data.add("Cover Type:          " + book.getCover());
        data.add("Description:          " + description);
        data.add("Table of contents:          " + tableOfContent);
        data.add("Available:          " + (book.getAvailability().equalsIgnoreCase("AVAILABLE") ? "YES" : "NO"));

        this.bookListView.setItems(data);
    }

    public void initializeBookCover() {
        this.bookCover.setImage(this.imageLoader.getImage(book.getImage()));
    }

}
