package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.BookDetailsDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ResizeableBaseController;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.recommender.CollaborativeFilteringRecommender;
import pl.edu.agh.managementlibrarysystem.recommender.RatingLookUp;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;
import pl.edu.agh.managementlibrarysystem.utils.ImageLoader;
import pl.edu.agh.managementlibrarysystem.utils.TaskFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class RecommendedBooksControllers extends ResizeableBaseController implements Initializable {
    protected final ObservableList<String> data = FXCollections.observableArrayList();
    private final CollaborativeFilteringRecommender collaborativeFilteringRecommender;
    private final BookService bookService;
    private final ImageLoader imageLoader;
    private final UserSession userSession;
    private final ObservableList<BookDetailsDTO> bookDetailsDTOS = FXCollections.observableArrayList();

    @FXML
    private BorderPane borderPane;
    @FXML
    private AnchorPane imageContainer;
    @FXML
    private ImageView currentImage;
    @FXML
    private ImageView spinner;
    @FXML
    private ListView<String> bookInformationContainer;
    @FXML
    private Label totalNumberOfBooks;
    @FXML
    private Label currentNumberOfBooks;
    @FXML
    private MFXButton previous;
    @FXML
    private MFXButton next;
    private User user;
    private List<RatingLookUp> recommendedBooks;
    private int currentBookIndex = 0;

    public RecommendedBooksControllers(ApplicationContext applicationContext,
                                       CollaborativeFilteringRecommender collaborativeFilteringRecommender, BookService bookService,
                                       ImageLoader imageLoader, UserSession userSession) {
        super(applicationContext);
        this.collaborativeFilteringRecommender = collaborativeFilteringRecommender;
        this.bookService = bookService;
        this.imageLoader = imageLoader;
        this.userSession = userSession;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tooltipInitializer();
        this.user = userSession.getLoggedUser();

        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                spinner.setVisible(true);
                recommendedBooks = collaborativeFilteringRecommender.recommend(user);
                Platform.runLater(
                        () -> {
                            totalNumberOfBooks.setText(String.valueOf(recommendedBooks.size()));
                            currentNumberOfBooks.setText("1");
                            for (RatingLookUp ratingLookUp : recommendedBooks) {
                                bookDetailsDTOS.add(bookService.getBookDetails(ratingLookUp.id()));
                            }
                            initializeBookDetails(bookDetailsDTOS.get(0));
                            currentImage.setImage(imageLoader.getImage(bookDetailsDTOS.get(0).getImage()));
                            currentImage.setFitHeight(imageContainer.getHeight());
                            currentImage.setFitWidth(imageContainer.getWidth());
                        }
                );

                return null;
            }
        };

        task.setOnSucceeded(event -> {
                    next.setDisable(false);
                    previous.setDisable(false);
                    spinner.setVisible(false);
                }
        );

        TaskFactory.startTask(task);
    }

    @FXML
    private void previous(ActionEvent actionEvent) {
        if (currentBookIndex > 0) {
            currentBookIndex--;
            currentNumberOfBooks.setText(String.valueOf(currentBookIndex + 1));
            initializeBookDetails(bookDetailsDTOS.get(currentBookIndex));
            currentImage.setImage(imageLoader.getImage(bookDetailsDTOS.get(currentBookIndex).getImage()));
            currentImage.setFitHeight(imageContainer.getHeight());
            currentImage.setFitWidth(imageContainer.getWidth());
        }
    }

    @FXML
    private void next(ActionEvent actionEvent) {
        if (currentBookIndex < recommendedBooks.size() - 1) {
            currentBookIndex++;
            currentNumberOfBooks.setText(String.valueOf(currentBookIndex + 1));
            initializeBookDetails(bookDetailsDTOS.get(currentBookIndex));
            currentImage.setImage(imageLoader.getImage(bookDetailsDTOS.get(currentBookIndex).getImage()));
            currentImage.setFitHeight(imageContainer.getHeight());
            currentImage.setFitWidth(imageContainer.getWidth());
        }
    }

    private void initializeBookDetails(BookDetailsDTO book) {
        data.clear();
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

        this.bookInformationContainer.setItems(data);
    }

}

