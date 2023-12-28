package pl.edu.agh.managementlibrarysystem.utils;

import javafx.scene.control.Control;

public class ControlsUtils {
    public static void changeControlVisibility(Control control, boolean visibility){
        control.managedProperty().set(visibility);
        control.visibleProperty().set(visibility);
    }
}
