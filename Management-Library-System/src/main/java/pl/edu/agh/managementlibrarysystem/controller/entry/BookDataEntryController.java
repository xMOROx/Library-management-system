package pl.edu.agh.managementlibrarysystem.controller.entry;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventTarget;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseDataEntryController;
import pl.edu.agh.managementlibrarysystem.enums.CoverType;
import pl.edu.agh.managementlibrarysystem.event.BorderPaneReadyEvent;
import pl.edu.agh.managementlibrarysystem.event.fxml.LeavingBorderPaneEvent;
import pl.edu.agh.managementlibrarysystem.event.fxml.NewItemAddedEvent;
import pl.edu.agh.managementlibrarysystem.model.Book;
import pl.edu.agh.managementlibrarysystem.service.AuthorService;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.service.GenresService;
import pl.edu.agh.managementlibrarysystem.service.PublisherService;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

import java.io.Serial;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

@Controller
public class BookDataEntryController extends BaseDataEntryController<ActionEvent> implements Initializable {
    private final ApplicationContext applicationContext;
    private final BookService bookService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final GenresService genresService;
    private final ObservableList<String> coverTypes = CoverType.getObservableList();
    private ObservableList<String> authors;
    private ObservableList<String> publishers;
    private ObservableList<String> genres;

    @FXML
    private MFXTextField title;
    @FXML
    private MFXTextField isbn;
    @FXML
    private MFXTextField edition;
    @FXML
    private MFXTextField quantity;
    @FXML
    private MFXTextField description;
    @FXML
    private CheckBox availability;
    @FXML
    private ComboBox<String> coverType;
    @FXML
    private ComboBox<String> authorSelection;
    @FXML
    private ComboBox<String> publisherSelection;
    @FXML
    private ComboBox<String> genresSelection;
    @FXML
    private MFXTextField tableOfContent;
    @FXML
    private MFXButton selectAuthor;
    @FXML
    private MFXButton selectPublisher;
    @FXML
    private MFXButton selectGenres;
    @FXML
    private MFXButton addAuthorButton;
    @FXML
    private MFXButton addPublisherButton;
    @FXML
    private MFXButton addGenresButton;

    @FXML
    private BorderPane helperView;

    public BookDataEntryController(ApplicationContext applicationContext,
                                   BookService bookService,
                                   AuthorService authorService,
                                   PublisherService publisherService,
                                   GenresService genresService
    ) {
        this.applicationContext = applicationContext;
        this.bookService = bookService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.genresService = genresService;

        this.authors = this.authorService.getAllAuthors();
        this.publishers = this.publisherService.getAllPublishers();
        this.genres = this.genresService.getAllGenres();

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        save.disabledProperty();
        coverType.setItems(coverTypes);
        clearFields();

        this.helperView.addEventHandler(EntryHelperEmptyEvent.HELPER_EMPTY, event ->
                {
                    this.helperView.setCenter(null);
                    this.addAuthorButton.setDisable(false);
                    this.addPublisherButton.setDisable(false);
                    this.addGenresButton.setDisable(false);
                }
        );

        this.authorSelection.setItems(this.authors);
        this.authorSelection.setVisibleRowCount(5);

        this.publisherSelection.setItems(this.publishers);
        this.authorSelection.setVisibleRowCount(5);

        this.genresSelection.setItems(this.genres);
        this.genresSelection.setVisibleRowCount(5);

        this.helperView.addEventHandler(
                NewItemAddedEvent.NEW_AUTHOR,
                event -> {
                    this.authors = this.authorService.getAllAuthors();
                    this.authorSelection.setItems(this.authors);

                }
        );

        this.helperView.addEventHandler(
                NewItemAddedEvent.NEW_PUBLISHER,
                event -> {
                    this.publishers = this.publisherService.getAllPublishers();
                    this.publisherSelection.setItems(this.publishers);
                }
        );

        this.helperView.addEventHandler(
                NewItemAddedEvent.NEW_GENRE,
                event -> {
                    this.genres = this.genresService.getAllGenres();
                    this.genresSelection.setItems(this.genres);

                }
        );
        this.save.disableProperty().bind(
                this.title.textProperty().isEmpty()
                        .or(this.isbn.textProperty().isEmpty())
                        .or(this.quantity.textProperty().isEmpty())
                        .or(this.coverType.valueProperty().isNull())
        );

    }

    @FXML
    private void back(ActionEvent actionEvent) {
        this.root.fireEvent(new LeavingBorderPaneEvent(LeavingBorderPaneEvent.LEAVING));
    }

    @FXML
    @Override
    protected void save(ActionEvent actionEvent) {
        if (!this.verifyData()) {
            return;
        }


        int bookQuantity = Integer.parseInt(this.quantity.getText());
        BookDTO book = BookDTO.builder()
                .title(this.title.getText())
                .isbn(this.isbn.getText())
                .edition(!this.edition.getText().isEmpty() ? Integer.parseInt(this.edition.getText()) : 1)
                .quantity(bookQuantity)
                .remainingBooks(bookQuantity)
                .description(!this.description.getText().isEmpty() ? this.description.getText() : null)
                .availability(this.availability.isSelected() ? "available" : "unavailable")
                .cover(this.coverType.getValue())
                .tableOfContent(!this.tableOfContent.getText().isEmpty() ? this.tableOfContent.getText() : null)
                .build();

        String authorName = this.authorSelection.getValue().split(" ")[0];
        String authorLastname = this.authorSelection.getValue().split(" ")[1];
        String publisherName = this.publisherSelection.getValue();
        String genreType = this.genresSelection.getValue();


        if (this.bookService.saveBook(book, authorName, authorLastname, publisherName, genreType)) {
            Alerts.showAddingAlert("Book added", "Book has been added successfully", this.title.getText());
            this.clearFields();
            this.cancel(actionEvent);
        }

    }

    @FXML
    @Override
    protected void cancel(ActionEvent actionEvent) {
        this.root.fireEvent(new LeavingBorderPaneEvent(LeavingBorderPaneEvent.LEAVING));
        this.clearFields();
    }

    @Override
    protected void clearFields() {
        this.title.clear();
        this.isbn.clear();
        this.edition.clear();
        this.quantity.clear();
        this.description.clear();
        this.tableOfContent.clear();
        this.availability.setSelected(false);
        this.coverType.setValue("Select cover type");
        this.authorSelection.setValue("Select author");
        this.publisherSelection.setValue("Select publisher");
        this.genresSelection.setValue("Select genres");

    }

    private boolean verifyData() {
        String numberRegex = ".*\\d.*";
        String isbn13and10Regex = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";

        if (!isbn.getText().matches(numberRegex)) {
            Alerts.showErrorAlert("Wrong input", "Provided input is not a ISBN 10 or 13");
            return false;
        }

        if (!edition.getText().matches(numberRegex) && !edition.getText().isEmpty()) {
            Alerts.showErrorAlert("Wrong input", "Edition must be a number");
            return false;
        }

        if (!quantity.getText().matches(numberRegex)) {
            Alerts.showErrorAlert("Wrong input", "Quantity must be a number");
            return false;
        }

        if (coverType.getValue().equals("Select cover type")) {
            Alerts.showErrorAlert("Wrong input", "You must select cover type");
            return false;
        }

        return true;
    }

    @FXML
    private void loadAuthorView(ActionEvent actionEvent) {
        this.addAuthorButton.setDisable(true);
        this.enableOtherButtons(this.addAuthorButton);
        this.applicationContext.publishEvent(new BorderPaneReadyEvent(this.helperView, new ClassPathResource("fxml/authorDataEntry.fxml")));
    }

    @FXML
    private void loadPublisherView(ActionEvent actionEvent) {
        this.addPublisherButton.setDisable(true);
        this.enableOtherButtons(this.addPublisherButton);
        this.applicationContext.publishEvent(new BorderPaneReadyEvent(this.helperView, new ClassPathResource("fxml/publisherDataEntry.fxml")));
    }

    @FXML
    private void loadGenresView(ActionEvent actionEvent) {
        this.addGenresButton.setDisable(true);
        this.enableOtherButtons(this.addGenresButton);
        this.applicationContext.publishEvent(new BorderPaneReadyEvent(this.helperView, new ClassPathResource("fxml/genresDataEntry.fxml")));
    }

    private void enableOtherButtons(MFXButton button) {
        if (button == this.addAuthorButton) {
            this.addPublisherButton.setDisable(false);
            this.addGenresButton.setDisable(false);
        } else if (button == this.addPublisherButton) {
            this.addAuthorButton.setDisable(false);
            this.addGenresButton.setDisable(false);
        } else if (button == this.addGenresButton) {
            this.addAuthorButton.setDisable(false);
            this.addPublisherButton.setDisable(false);
        }
    }

    public static class EntryHelperEmptyEvent extends Event {
        public static final EventType<EntryHelperEmptyEvent> HELPER_EMPTY =
                new EventType<>(Event.ANY, "HELPER_EMPTY");
        @Serial
        private static final long serialVersionUID = 20121107L;

        public EntryHelperEmptyEvent() {
            super(HELPER_EMPTY);
        }

        public EntryHelperEmptyEvent(Object source, EventTarget target) {
            super(source, target, HELPER_EMPTY);
        }

        @Override
        public EntryHelperEmptyEvent copyFor(Object newSource, EventTarget newTarget) {
            return (EntryHelperEmptyEvent) super.copyFor(newSource, newTarget);
        }

        @Override
        public EventType<? extends EntryHelperEmptyEvent> getEventType() {
            return (EventType<? extends EntryHelperEmptyEvent>) super.getEventType();
        }
    }

}
