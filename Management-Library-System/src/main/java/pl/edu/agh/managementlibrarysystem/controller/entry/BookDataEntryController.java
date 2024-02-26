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
import org.controlsfx.control.SearchableComboBox;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseDataEntryController;
import pl.edu.agh.managementlibrarysystem.utils.enums.CoverType;
import pl.edu.agh.managementlibrarysystem.event.BorderPaneReadyEvent;
import pl.edu.agh.managementlibrarysystem.event.fxml.LeavingBorderPaneEvent;
import pl.edu.agh.managementlibrarysystem.event.fxml.NewItemAddedEvent;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;
import pl.edu.agh.managementlibrarysystem.service.AuthorService;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.service.GenresService;
import pl.edu.agh.managementlibrarysystem.service.PublisherService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;
import pl.edu.agh.managementlibrarysystem.utils.ControlsUtils;
import pl.edu.agh.managementlibrarysystem.utils.FxmlPropertyFactory;

import java.io.Serial;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class BookDataEntryController extends BaseDataEntryController<ActionEvent> implements Initializable {
    private final ApplicationContext applicationContext;
    private final BookService bookService;
    private final AuthorService authorService;
    private final PublisherService publisherService;
    private final GenresService genresService;
    private final ObservableList<String> coverTypes = CoverType.getObservableList();
    private final UserSession session;
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
    private SearchableComboBox<String> authorSelection;
    @FXML
    private SearchableComboBox<String> publisherSelection;
    @FXML
    private SearchableComboBox<String> genresSelection;
    @FXML
    private MFXTextField tableOfContent;
    @FXML
    private MFXTextField imageURL;
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
                                   GenresService genresService,
                                   UserSession session
    ) {
        this.applicationContext = applicationContext;
        this.bookService = bookService;
        this.authorService = authorService;
        this.publisherService = publisherService;
        this.genresService = genresService;
        this.session = session;

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
        if (session.getSelectedBook() != null) {
            BookDTO bookDTO = session.getSelectedBook();
            setFields(bookDTO);
        }
        initializeStageOptions();
    }

    private void initializeStageOptions() {
        if (session.getLoggedUser() == null) {
            return;
        }
        User u = session.getLoggedUser();
        if (u.getPermission() == Permission.NORMAL_USER) {
            ControlsUtils.changeControlVisibility(cancel, false);
            ControlsUtils.changeControlVisibility(save, false);
            ControlsUtils.changeControlVisibility(addAuthorButton, false);
            ControlsUtils.changeControlVisibility(addGenresButton, false);
            ControlsUtils.changeControlVisibility(addPublisherButton, false);
            title.setDisable(true);
            isbn.setDisable(true);
            edition.setDisable(true);
            quantity.setDisable(true);
            description.setDisable(true);
            tableOfContent.setDisable(true);
            coverType.setDisable(true);
            selectAuthor.setDisable(true);
            selectPublisher.setDisable(true);
            selectGenres.setDisable(true);
            availability.setDisable(true);
        }
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
                .title(FxmlPropertyFactory.stringProperty(this.title.getText()))
                .isbn(FxmlPropertyFactory.stringProperty(this.isbn.getText().trim()))
                .edition(!this.edition.getText().isEmpty() ? FxmlPropertyFactory.integerProperty(Integer.parseInt(this.edition.getText())) : FxmlPropertyFactory.integerProperty(1))
                .quantity(FxmlPropertyFactory.integerProperty(bookQuantity))
                .remainingBooks(FxmlPropertyFactory.integerProperty(bookQuantity))
                .description(!this.description.getText().isEmpty() ? FxmlPropertyFactory.stringProperty(this.description.getText()) : null)
                .availability(this.availability.isSelected() ? FxmlPropertyFactory.stringProperty("available") : FxmlPropertyFactory.stringProperty("unavailable"))
                .cover(FxmlPropertyFactory.stringProperty(this.coverType.getValue()))
                .image(!this.imageURL.getText().isEmpty() ? FxmlPropertyFactory.stringProperty(this.imageURL.getText()) : null)
                .tableOfContent(!this.tableOfContent.getText().isEmpty() ? FxmlPropertyFactory.stringProperty(this.tableOfContent.getText()) : null)
                .build();

        String authorName = this.authorSelection.getValue().split(" ")[0];
        String authorLastname = this.authorSelection.getValue().split(" ")[1];
        String publisherName = this.publisherSelection.getValue();
        String genreType = this.genresSelection.getValue();

        if (this.session.getSelectedBook() != null) {
            if (this.bookService.updateBook(book, authorName, authorLastname, publisherName, genreType)) {
                Alerts.showAddingAlert("Book modified", "Book has been modified successfully", this.title.getText());
                this.clearFields();
                this.session.setSelectedBook(null);
                this.cancel(actionEvent);

            }
        } else if (this.bookService.saveBook(book, authorName, authorLastname, publisherName, genreType)) {
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

    protected void setFields(BookDTO bookDTO) {
        this.title.setText(bookDTO.getTitle().getValue());
        this.isbn.setText(bookDTO.getIsbn().getValue());
        this.edition.setText(bookDTO.getEdition().toString());
        this.quantity.setText(bookDTO.getQuantity().toString());
        this.description.setText(bookDTO.getDescription().getValue());
        this.tableOfContent.setText(bookDTO.getTableOfContent().getValue());
        this.availability.setSelected(Boolean.parseBoolean(bookDTO.getAvailability().getValue()));
        this.coverType.setValue(bookDTO.getCover().getValue());
        this.authorSelection.setValue(bookDTO.getAuthors().getValue());
        this.publisherSelection.setValue(bookDTO.getPublisher().getValue());
        this.genresSelection.setValue(bookDTO.getMainGenre().getValue());
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

    public void deleteBook(ActionEvent actionEvent) {
        bookService.deleteByISBN(session.getSelectedBook().getIsbn().getValue());
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
