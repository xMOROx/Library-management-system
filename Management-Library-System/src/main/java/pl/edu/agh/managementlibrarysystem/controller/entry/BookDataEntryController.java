package pl.edu.agh.managementlibrarysystem.controller.entry;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseDataEntryController;
import pl.edu.agh.managementlibrarysystem.enums.CoverType;
import pl.edu.agh.managementlibrarysystem.event.BorderPaneReadyEvent;
import pl.edu.agh.managementlibrarysystem.service.BookService;

import java.io.Serial;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class BookDataEntryController extends BaseDataEntryController<ActionEvent> implements Initializable {
    private final ApplicationContext applicationContext;
    private final BookService bookService;
    private final ObservableList<String> coverTypes = CoverType.getObservableList();

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

    public BookDataEntryController(ApplicationContext applicationContext, BookService  bookService) {
        this.applicationContext = applicationContext;
        this.bookService = bookService;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        save.disabledProperty();
        coverType.setItems(coverTypes);
        coverType.setValue("Select cover type");
        authorSelection.setValue("Select author");
        publisherSelection.setValue("Select publisher");
        genresSelection.setValue("Select genres");

        this.helperView.addEventHandler(EntryHelperEmptyEvent.HELPER_EMPTY, event ->
                {
                    this.helperView.setCenter(null);
                    this.addAuthorButton.setDisable(false);
                    this.addPublisherButton.setDisable(false);
                    this.addGenresButton.setDisable(false);
                }
        );

    }

    @FXML
    private void back(ActionEvent actionEvent) {

    }

    @FXML
    @Override
    protected void save(ActionEvent actionEvent) {

    }

    @FXML
    @Override
    protected void cancel(ActionEvent actionEvent) {

    }

    @FXML
    private void loadAuthorView(ActionEvent actionEvent) {
    }

    @FXML
    private void loadPublisherView(ActionEvent actionEvent) {
    }

    @FXML
    private void loadGenresView(ActionEvent actionEvent) {
    }

    @FXML
    private void addAuthor(ActionEvent actionEvent) {
        this.addAuthorButton.setDisable(true);
        this.enableOtherButtons(this.addAuthorButton);
        this.applicationContext.publishEvent(new BorderPaneReadyEvent(this.helperView, new ClassPathResource("fxml/authorDataEntry.fxml")));
    }

    @FXML
    private void addPublisher(ActionEvent actionEvent) {
        this.addPublisherButton.setDisable(true);
        this.enableOtherButtons(this.addPublisherButton);
        this.applicationContext.publishEvent(new BorderPaneReadyEvent(this.helperView, new ClassPathResource("fxml/publisherDataEntry.fxml")));
    }

    @FXML
    private void addGenres(ActionEvent actionEvent) {
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
