package pl.edu.agh.managementlibrarysystem.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.ReadBookDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.StatisticsController;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;
import pl.edu.agh.managementlibrarysystem.service.BookService;
import pl.edu.agh.managementlibrarysystem.service.StatisticsService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;
import pl.edu.agh.managementlibrarysystem.utils.TaskFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
@Controller
public class StatisticsUserController extends StatisticsController {

    @FXML
    public TableView<ReadBookDTO> tableView;
    @FXML
    public TableColumn<ReadBookDTO, String> bookISBN;
    @FXML
    public TableColumn<ReadBookDTO, String> bookTitle;
    @FXML
    public TableColumn<ReadBookDTO, String> bookAuthors;
    @FXML
    public TableColumn<ReadBookDTO, String> bookPublisher;
    @FXML
    public TableColumn<ReadBookDTO, String> bookGenres;
    @FXML
    public TableColumn<ReadBookDTO, Date> bookIssued;
    @FXML
    public TableColumn<ReadBookDTO, Date> bookReturned;
    @FXML
    public TableColumn<ReadBookDTO, Integer> bookDays;
    @FXML
    public TableColumn<ReadBookDTO, Double> bookFee;
    @FXML
    public TableColumn<ReadBookDTO, Integer> bookEdition;
    @FXML
    public ListView<String> statisitcsList;


    private List<ReadBookDTO> data;
    private List<String> otherStatistics;


    public StatisticsUserController(ApplicationContext applicationContext,
                                    StatisticsService statisticsService,
                                    UserSession session) {
        super(applicationContext,statisticsService,session);
        this.otherStatistics = new ArrayList<>();
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
                initStatistics();
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
    public void initStatistics() {
        List<String> stats = statisticsService.getBasicUserStatistics(session.getLoggedUser());
        otherStatistics.add("Read books:        "+ stats.get(0));
        otherStatistics.add("Ratings given:     "+ stats.get(1));
        otherStatistics.add("Reviews given:    "+stats.get(2));
        otherStatistics.add("Total fees:         "+stats.get(3));
        otherStatistics.add("Ratings average: "+stats.get(4));
        this.statisitcsList.setItems(FXCollections.observableArrayList(otherStatistics));
    }
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
        this.bookPublisher.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        this.bookGenres.setCellValueFactory(new PropertyValueFactory<>("genres"));
        this.bookIssued.setCellValueFactory(new PropertyValueFactory<>("issuedDate"));
        this.bookReturned.setCellValueFactory(new PropertyValueFactory<>("returnedDate"));
        this.bookDays.setCellValueFactory(new PropertyValueFactory<>("days"));
        this.bookFee.setCellValueFactory(new PropertyValueFactory<>("fee"));
        this.bookEdition.setCellValueFactory(new PropertyValueFactory<>("edition"));

        ;
    }
}
