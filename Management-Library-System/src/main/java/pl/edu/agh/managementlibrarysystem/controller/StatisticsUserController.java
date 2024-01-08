package pl.edu.agh.managementlibrarysystem.controller;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.ReadBookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.ReturnedBookDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ResizeableBaseController;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.StatisticsController;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.service.StatisticsService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;
import pl.edu.agh.managementlibrarysystem.utils.TaskFactory;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
@Controller
public class StatisticsUserController extends StatisticsController {


    public TableView<ReturnedBookDTO> tableView;
    public TableColumn<ReturnedBookDTO, String> bookISBN;
    public TableColumn<ReturnedBookDTO, String> bookTitle;
    public TableColumn<ReturnedBookDTO, String> bookAuthors;
    public TableColumn<ReturnedBookDTO, String> bookGenres;
    public TableColumn<ReturnedBookDTO, Integer> bookEdition;
    public TableColumn<ReturnedBookDTO, Integer> bookDays;
    public TableColumn<ReturnedBookDTO, Date> bookIssued;
    public TableColumn<ReturnedBookDTO, Date> bookReturned;
    private final BookService bookService;

    private List<ReturnedBookDTO> data;


    public StatisticsUserController(ApplicationContext applicationContext,
                                    StatisticsService statisticsService,
                                    BookService bookService,
                                    UserSession session) {
        super(applicationContext,statisticsService,session);
        this.bookService = bookService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tooltipInitializer();
        initializeStageOptions();
        this.initializeColumns();
        this.createNewTask();
    }
    protected void createNewTask() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                spinner.setVisible(true);
                initData();
                return null;
            }
        };

        task.setOnSucceeded(event -> {
            spinner.setVisible(false);
        });

        TaskFactory.startTask(task);
    }
    protected void initData() {
        User currUser = session.getLoggedUser();
        data = FXCollections.observableArrayList(statisticsService.getBooksReturnedByUser(session.getLoggedUser()));
        this.tableView.getItems().clear();
        this.tableView.getItems().addAll(data);
    }
//    public void initializeStatistics() {
//        String description = book.getDescription() == null ? "No description" : book.getDescription();
//        String tableOfContent = book.getTableOfContent() == null ? "No table of content" : book.getTableOfContent();
//
//        data.add("Book ISBN:              " + book.getIsbn());
//        data.add("Book Title:              " + book.getTitle());
//        data.add("Authors:          " + book.getAuthors());
//        data.add("Publisher:          " + book.getPublisher());
//        data.add("Genres:          " + book.getGenres());
//        data.add("Edition:          " + book.getEdition());
//        data.add("Quantity:          " + book.getQuantity());
//        data.add("Remaining amount:          " + book.getRemainingBooks());
//        data.add("Cover Type:          " + book.getCover());
//        data.add("Description:          " + description);
//        data.add("Table of contents:          " + tableOfContent);
//        data.add("Available:          " + (book.getAvailability().equalsIgnoreCase("AVAILABLE") ? "YES" : "NO"));
//
//        this.bookListView.setItems(data);
//    }
    protected void initializeStageOptions() {
        if (session.getLoggedUser() == null) {
            return;
        }
        User u = session.getLoggedUser();
        if (u.getPermission() == Permission.NORMAL_USER) {

        }
    }
    protected void initializeColumns() {
        this.bookISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        this.bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.bookAuthors.setCellValueFactory(new PropertyValueFactory<>("authors"));
        this.bookGenres.setCellValueFactory(new PropertyValueFactory<>("genres"));
        this.bookEdition.setCellValueFactory(new PropertyValueFactory<>("edition"));
        this.bookDays.setCellValueFactory(new PropertyValueFactory<>("days"));
        this.bookIssued.setCellValueFactory(new PropertyValueFactory<>("issued"));
        this.bookReturned.setCellValueFactory(new PropertyValueFactory<>("returned"));
    }
}
