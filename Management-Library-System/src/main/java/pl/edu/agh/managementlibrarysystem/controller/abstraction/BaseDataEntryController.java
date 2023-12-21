package pl.edu.agh.managementlibrarysystem.controller.abstraction;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public abstract class BaseDataEntryController<E> {
    @FXML
    protected BorderPane root;

    @FXML
    protected MFXButton save;

    @FXML
    protected MFXButton cancel;

    @FXML
    protected abstract void save(E event);
    @FXML
    protected abstract void cancel(E event);
}
