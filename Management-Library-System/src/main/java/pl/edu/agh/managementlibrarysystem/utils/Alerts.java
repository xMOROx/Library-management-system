package pl.edu.agh.managementlibrarysystem.utils;

import javafx.scene.control.Alert;

public class Alerts extends Alert {

    public Alerts(AlertType type, String title, String headerText, String contentText) {
        super(type);
        this.setTitle(title);
        this.setHeaderText(headerText);
        this.setContentText(contentText);
    }

    public static void showAddingAlert(String title, String headerText, String contentText) {
        Alert alert = new Alerts(AlertType.INFORMATION, title, headerText, contentText);
        alert.showAndWait();
    }

    public static void showErrorAlert(String title, String content) {
        Alert alert = new Alerts(AlertType.ERROR, title, null, content);
        alert.showAndWait();
    }

    public static void showInformationAlert(String title, String s) {
        Alert alert = new Alerts(AlertType.INFORMATION, title, null, s);
        alert.showAndWait();
    }

    public static void showSuccessAlert(String title, String content) {
        Alert alert = new Alerts(AlertType.INFORMATION, title, null, content);
        alert.showAndWait();
    }
}
