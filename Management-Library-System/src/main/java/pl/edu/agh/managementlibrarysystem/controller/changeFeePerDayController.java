package pl.edu.agh.managementlibrarysystem.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseDataEntryController;
import pl.edu.agh.managementlibrarysystem.service.SettingsService;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
@RequiredArgsConstructor
public class changeFeePerDayController extends BaseDataEntryController<ActionEvent> implements Initializable {

    private final SettingsService settingsService;

    @FXML
    private TextField currentFee;
    @FXML
    private TextField newFee;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentFee.setText(String.valueOf(settingsService.getFeePerDay()));
        newFee.textProperty().addListener((observable, oldValue, newValue) -> {
            save.setDisable(newValue.isEmpty());
        });
    }


    @FXML
    @Override
    protected void save(ActionEvent event) {
        try {
            Double.parseDouble(newFee.getText());
        } catch (NumberFormatException e) {
            Alerts.showAlert("Error", "Invalid fee", "Fee must be a number");
            return;
        }

        if (Double.parseDouble(newFee.getText()) < 0) {
            Alerts.showAlert("Error", "Invalid fee", "Fee must be a positive number");
            return;
        }

        settingsService.updateFeePerDay(Double.parseDouble(newFee.getText()));
        Alerts.showAlert("Success", "Fee updated", "Fee updated successfully");
        currentFee.setText(String.valueOf(settingsService.getFeePerDay()));
        clearFields();
    }

    @FXML
    @Override
    protected void cancel(ActionEvent event) {
        clearFields();
    }

    @Override
    protected void clearFields() {
        this.newFee.clear();
    }
}
