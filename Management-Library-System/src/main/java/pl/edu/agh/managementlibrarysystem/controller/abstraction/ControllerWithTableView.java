package pl.edu.agh.managementlibrarysystem.controller.abstraction;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Component
public abstract class ControllerWithTableView<T> extends ResizeableBaseController implements Initializable {

    @FXML
    protected BorderPane borderPane;

    @FXML
    protected TextField searchTextField;
    @FXML
    protected ProgressBar progressBar;
    @FXML
    protected ImageView spinner;

    @FXML
    protected TableView<T> tableView;

    protected List<T> data;

    public ControllerWithTableView(ApplicationContext applicationContext) {
        super(applicationContext);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tooltipInitializer();
        this.initializeColumns();

        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                this.tableView.getItems().setAll(data);
            }
        });
    }

    protected abstract void createNewTask(int maxIterations, int sleepTime);

    protected abstract void initializeColumns();


    @FXML
    protected abstract void searchData(KeyEvent keyEvent);

    protected abstract void loadData();
}
