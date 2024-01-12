package pl.edu.agh.managementlibrarysystem.controller;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.BookStatsDTO;
import pl.edu.agh.managementlibrarysystem.DTO.ReadBookDTO;
import pl.edu.agh.managementlibrarysystem.DTO.UserStatsDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.StatisticsController;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;
import pl.edu.agh.managementlibrarysystem.service.StatisticsService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;
import pl.edu.agh.managementlibrarysystem.utils.TaskFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class StatisticsAdminController extends StatisticsController {

    @FXML
    public TableView<UserStatsDTO> userView;
    @FXML
    public TableColumn<UserStatsDTO,Integer> userId;
    @FXML
    public TableColumn<UserStatsDTO,String> userFirst;
    @FXML
    public TableColumn<UserStatsDTO,String> userLast;
    @FXML
    public TableColumn<UserStatsDTO,Integer> userBooks;
    @FXML
    public TableColumn<UserStatsDTO,Integer> userReviews;

    @FXML
    public TableView<BookStatsDTO> bookView;
    @FXML
    public TableColumn<BookStatsDTO,String>  bookISBN;
    @FXML
    public TableColumn<BookStatsDTO,String>  bookTitle;
    @FXML
    public TableColumn<BookStatsDTO,String>  bookAuthors;
    @FXML
    public TableColumn<BookStatsDTO,Integer>  bookReviews;
    @FXML
    public TableColumn<BookStatsDTO,Integer>  bookTimes;
    @FXML
    public ListView<String> statisticsList;

    private List<UserStatsDTO> usersData;
    private List<BookStatsDTO> booksData;
    private List<String> otherStatistics;


    public StatisticsAdminController(ApplicationContext applicationContext,
                                     StatisticsService statisticsService,
                                     UserSession session) {
        super(applicationContext,statisticsService,session);
        this.otherStatistics = new ArrayList<>();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tooltipInitializer();
        this.initializeColumns();
        this.createNewTask();
    }

    @Override
    protected void initializeStageOptions() {

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
        this.usersData = FXCollections.observableArrayList(statisticsService.getAllUsersStats());
        this.userView.getItems().clear();
        this.userView.getItems().addAll(usersData);

        this.booksData =  FXCollections.observableArrayList(statisticsService.getAllBookStats());
        this.bookView.getItems().clear();
        this.bookView.getItems().addAll(booksData);
    }
    public void initStatistics() {
        List<String> stats = statisticsService.getAllUsersStatistics();
        otherStatistics.add("All read books:                    "+ stats.get(0));
        otherStatistics.add("All ratings given:                 "+ stats.get(1));
        otherStatistics.add("All reviews given:                 "+stats.get(2));
        otherStatistics.add("Total number of normal users: "+stats.get(3));
        otherStatistics.add("Total number of books:           "+stats.get(4));
        this.statisticsList.setItems(FXCollections.observableArrayList(otherStatistics));
    }

    protected void initializeColumns() {
        this.bookISBN.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        this.bookTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.bookAuthors.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        this.bookReviews.setCellValueFactory(new PropertyValueFactory<>("reviews"));
        this.bookTimes.setCellValueFactory(new PropertyValueFactory<>("readTimes"));

        this.userId.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.userFirst.setCellValueFactory(new PropertyValueFactory<>("first"));
        this.userLast.setCellValueFactory(new PropertyValueFactory<>("last"));
        this.userBooks.setCellValueFactory(new PropertyValueFactory<>("books"));
        this.userReviews.setCellValueFactory(new PropertyValueFactory<>("reviews"));
    }
}
