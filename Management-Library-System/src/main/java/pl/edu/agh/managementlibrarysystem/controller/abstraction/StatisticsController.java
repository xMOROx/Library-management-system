package pl.edu.agh.managementlibrarysystem.controller.abstraction;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import pl.edu.agh.managementlibrarysystem.event.OpenChartsWindowEvent;
import pl.edu.agh.managementlibrarysystem.event.OpenWindowEvent;
import pl.edu.agh.managementlibrarysystem.service.StatisticsService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public abstract class StatisticsController extends ResizeableBaseController implements Initializable {
    protected final StatisticsService statisticsService;

    protected final UserSession session;
    @FXML
    public ImageView spinner;
    @FXML
    public ProgressBar progressBar;

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

    }
    @FXML
    public void openCharts(MouseEvent mouseEvent){
        applicationContext.publishEvent(new OpenChartsWindowEvent(new ClassPathResource("fxml/charts.fxml")));
    }
    abstract protected void initializeStageOptions();


}
