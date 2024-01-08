package pl.edu.agh.managementlibrarysystem.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
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


    public StatisticsUserController(ApplicationContext applicationContext,
                                    StatisticsService statisticsService,
                                    UserSession session) {
        super(applicationContext,statisticsService,session);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tooltipInitializer();
        initializeStageOptions();
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
}
