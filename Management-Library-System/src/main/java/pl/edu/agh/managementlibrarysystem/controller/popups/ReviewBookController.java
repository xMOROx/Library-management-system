package pl.edu.agh.managementlibrarysystem.controller.popups;


import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.controlsfx.control.Rating;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.ReadBookAvailableToVoteDTO;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;
import pl.edu.agh.managementlibrarysystem.utils.ImageLoader;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class ReviewBookController extends BookPopUpWindowController<ReadBookAvailableToVoteDTO> implements Initializable {

    @FXML
    private MFXButton submitButton;
    @FXML
    private TextArea reviewTextArea;
    @FXML
    private Rating rating;


    public ReviewBookController(ApplicationContext applicationContext,
                                ImageLoader imageLoader,
                                BookService bookService) {
        super(applicationContext, bookService, imageLoader);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tooltipInitializer();

        this.rating.setRating(0);
        this.rating.setUpdateOnHover(true);
        this.rating.setPartialRating(true);

        this.submitButton.disableProperty().bind(this.rating.ratingProperty().isEqualTo(0));
    }

    public void setBook(ReadBookAvailableToVoteDTO book) {
        this.book = book;
        this.initializeBookDetails();
    }

    public void initializeBookDetails() {
        super.initializeBookDetails();

        data.add("Book ISBN:              " + book.getIsbn());
        data.add("Book Title:              " + book.getTitle());
        data.add("Authors:          " + book.getAuthors());
        data.add("Publisher:          " + book.getPublisher());
        data.add("Genres:          " + book.getGenres());
        data.add("Edition:          " + book.getEdition());
        data.add("Return date:          " + book.getReturnedDate());

        this.bookListView.setItems(data);
    }

    @FXML
    private void submit(ActionEvent actionEvent) {
        if (!this.bookService.reviewBook(this.book.getBookId(), this.book.getUserId(), this.reviewTextArea.getText(), this.rating.getRating())) {
            Alerts.showErrorAlert("Error", "Error while reviewing book");
            return;
        }
        Alerts.showSuccessAlert("Success", "Book reviewed successfully");
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }
}
