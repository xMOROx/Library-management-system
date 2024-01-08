package pl.edu.agh.managementlibrarysystem.controller.abstraction;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
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
    protected ContextMenu contextMenu;
    @FXML
    protected MenuItem copyItem;

    @FXML
    protected TableView<T> tableView;

    protected ObservableList<T> data;

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

    @FXML
    protected void copy(ActionEvent actionEvent) {
        ObservableList<TablePosition> posList = tableView.getSelectionModel().getSelectedCells();
        int old_r = -1;
        StringBuilder clipboardString = new StringBuilder();
        for (TablePosition p : posList) {
            int r = p.getRow();
            int c = p.getColumn();
            Object cell = tableView.getColumns().get(c).getCellData(r);
            if (cell == null)
                cell = "";
            if (old_r == r)
                clipboardString.append('\t');
            else if (old_r != -1)
                clipboardString.append('\n');
            clipboardString.append(cell);
            old_r = r;
        }
        ClipboardContent content = new ClipboardContent();
        content.putString(clipboardString.toString());
        Clipboard.getSystemClipboard().setContent(content);
    }

    protected abstract void createNewTask();

    protected abstract void initializeColumns();

    @FXML
    protected abstract void searchData(KeyEvent keyEvent);

    protected abstract void initData();
}
