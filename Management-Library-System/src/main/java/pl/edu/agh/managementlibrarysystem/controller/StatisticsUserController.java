package pl.edu.agh.managementlibrarysystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ResizeableBaseController;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.StatisticsController;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;
import pl.edu.agh.managementlibrarysystem.service.StatisticsService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;

import java.net.URL;
import java.util.ResourceBundle;
@Controller
public class StatisticsUserController extends StatisticsController {

    @FXML
    public ListView<BookDTO> readBooks;



    public StatisticsUserController(ApplicationContext applicationContext,
                                    StatisticsService statisticsService,
                                    UserSession session) {
        super(applicationContext,statisticsService,session);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tooltipInitializer();
        initializeStageOptions();
        this.createNewTask(50, 20);
    }

    protected void initializeStageOptions() {
        if (session.getLoggedUser() == null) {
            return;
        }
        User u = session.getLoggedUser();
        if (u.getPermission() == Permission.NORMAL_USER) {

        }
    }
}
