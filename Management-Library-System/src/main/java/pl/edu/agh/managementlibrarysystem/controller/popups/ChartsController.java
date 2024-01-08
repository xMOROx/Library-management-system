package pl.edu.agh.managementlibrarysystem.controller.popups;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ResizeableBaseController;
import pl.edu.agh.managementlibrarysystem.service.StatisticsService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;
import pl.edu.agh.managementlibrarysystem.utils.TaskFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Controller
public class ChartsController  extends ResizeableBaseController implements Initializable {
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
    public ChartsController(ApplicationContext applicationContext,
                                StatisticsService statisticsService,
                                UserSession session) {
        super(applicationContext);
        this.statisticsService = statisticsService;
        this.session = session;

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tooltipInitializer();
        this.createNewTask(50, 20);
    }
    protected void createNewTask(int maxIterations, int sleepTime) {
        Task<Integer> task = TaskFactory.countingTask(maxIterations, sleepTime);

        task.setOnSucceeded(event -> {
            spinner.setVisible(false);
            Platform.runLater(() -> initData());
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

    private void fillBarChart(BarChart<String,Number> chart, List<Object[]> data){
        NumberAxis yAxis = (NumberAxis)chart.getYAxis();
        XYChart.Series series = new XYChart.Series();
        for(Object[] row:data){
            series.getData().add(new XYChart.Data<String,Number>((String) row[0],(Number) row[1]));
        }
        chart.getData().add(series);
        yAxis.setMinorTickCount(0);
        yAxis.setMinorTickVisible(false);

    }
    @Override
    protected void close(MouseEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

}
