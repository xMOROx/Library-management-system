package pl.edu.agh.managementlibrarysystem.controller.abstraction;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import org.springframework.context.ApplicationContext;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;
import pl.edu.agh.managementlibrarysystem.service.StatisticsService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;
import pl.edu.agh.managementlibrarysystem.utils.TaskFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public abstract class StatisticsController extends ResizeableBaseController implements Initializable {
    protected final StatisticsService statisticsService;

    protected final UserSession session;
    @FXML
    public BarChart<String,Number> popularBooks;
    @FXML
    public BarChart<String,Number> popularAuthors;
    @FXML
    public BarChart<String,Number> popularGenres;
    @FXML
    public ImageView spinner;
    @FXML
    public ProgressBar progressBar;
    private List<Object[]> booksData;
    private List<Object[]> authorsData;
    private List<Object[]> genresData;
    public StatisticsController(ApplicationContext applicationContext,
                                    StatisticsService statisticsService,
                                    UserSession session) {
        super(applicationContext);
        this.statisticsService = statisticsService;
        this.session = session;

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tooltipInitializer();
        initializeStageOptions();
        this.createNewTask(50, 20);
    }
    protected void createNewTask(int maxIterations, int sleepTime) {
        Task<Integer> task = TaskFactory.countingTaskForProgressBar(maxIterations, sleepTime, progressBar);

        task.setOnSucceeded(event -> {
            spinner.setVisible(false);
            this.initData();
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }
    private void initData(){
        booksData = statisticsService.getPopularBooks(5);
        authorsData = statisticsService.getPopularAuthors(5);
        genresData = statisticsService.getPopularGenres(5);
        fillBarChart(popularBooks,booksData);
        fillBarChart(popularAuthors,authorsData);
        fillBarChart(popularGenres,genresData);
    }
    abstract protected void initializeStageOptions();

    private void fillBarChart(BarChart<String,Number> chart,List<Object[]> data){
        XYChart.Series series = new XYChart.Series();
        for(Object[] row:data){
            series.getData().add(new XYChart.Data<String,Number>((String) row[0],(Number) row[1]));
        }
        chart.getData().add(series);
    }
}
