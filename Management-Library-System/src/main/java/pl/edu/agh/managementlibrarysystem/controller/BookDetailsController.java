package pl.edu.agh.managementlibrarysystem.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.BookDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ResizeableBaseController;
import pl.edu.agh.managementlibrarysystem.service.BookService;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class BookDetailsController extends ResizeableBaseController implements Initializable {

    @FXML
    private Pane mainContainer;
    @FXML
    private ListView<String> bookDetailsView;
    @FXML
    private ImageView bookCover;
    @Setter
    private BookDTO book;

    public BookDetailsController(ApplicationContext applicationContext, BookService bookService) {
        super(applicationContext);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void back(ActionEvent actionEvent) {

    }
}
