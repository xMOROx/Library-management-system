package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseDataEntryController;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.service.UserService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class changeAdminPasswordController extends BaseDataEntryController<ActionEvent> implements Initializable {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserSession userSession;
    private User user;

    @FXML
    private MFXButton change;
    @FXML
    private PasswordField currentPassword;
    @FXML
    private PasswordField newPassword;
    @FXML
    private PasswordField newPasswordRepeated;
    @FXML
    private VBox errorBox;
    private BooleanProperty passwordBool;
    private BooleanProperty newPasswordBool;
    private BooleanProperty passwordMatchBool;
    private BooleanProperty repeatPasswordBool;

    public changeAdminPasswordController(PasswordEncoder passwordEncoder, UserService userService, UserSession userSession) {
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.userSession = userSession;
    }

    private static void errorLabel(String error_message, Color c, ObservableList<Node> list) {
        Label l = new Label();
        l.setText(error_message);
        l.setTextFill(c);
        list.add(l);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.initializeStageOptions();
        newPassword.setTooltip(new Tooltip("The password must be at least 8 characters long"));
        newPasswordRepeated.setTooltip(new Tooltip("The password must be at least 8 characters long"));


        this.passwordBool = new SimpleBooleanProperty(false);
        this.newPasswordBool = new SimpleBooleanProperty(false);
        this.repeatPasswordBool = new SimpleBooleanProperty(false);
        this.passwordMatchBool = new SimpleBooleanProperty(false);

        validateNewPassword();
    }

    private void initializeStageOptions() {
        if (userSession.getLoggedUser() == null) {
            return;
        }
        user = userSession.getLoggedUser();
    }

    @FXML
    @Override
    protected void save(ActionEvent event) {
        if (passwordBool.get() && newPasswordBool.get() && repeatPasswordBool.get() && passwordMatchBool.get()) {
            user.setPassword(passwordEncoder.encode(newPassword.getText()));
            userService.changePassword(user);
            change.setDisable(false);
            clearFields();
            disablePasswordFieldsAndButtons();
            Alerts.showSuccessAlert("Success", "Password changed successfully");
        }
    }

    @FXML
    @Override
    protected void cancel(ActionEvent event) {
        change.setDisable(false);
        clearFields();
        disablePasswordFieldsAndButtons();
    }

    @FXML
    @Override
    protected void clearFields() {
        this.currentPassword.clear();
        this.newPassword.clear();
        this.newPasswordRepeated.clear();
    }

    @FXML
    private void enablePasswordFieldsAndButtons(ActionEvent actionEvent) {
        change.setDisable(true);
        currentPassword.setEditable(true);
        newPassword.setEditable(true);
        newPasswordRepeated.setEditable(true);
        save.setDisable(false);
        cancel.setDisable(false);
    }

    private void disablePasswordFieldsAndButtons() {
        currentPassword.setEditable(false);
        newPassword.setEditable(false);
        newPasswordRepeated.setEditable(false);
        save.setDisable(true);
        cancel.setDisable(true);
    }

    private void validateCurrentPassword() {
        passwordBool.set(userService.findByEmail(user.getEmail()).isPresent() && passwordEncoder.matches(currentPassword.getText(), userService.findByEmail(user.getEmail()).get().getPassword()));
    }

    private void validateNewPassword() {
        passwordMatchBool.bind(Bindings.createBooleanBinding(() -> newPassword.getText().equals(newPasswordRepeated.getText()), newPasswordRepeated.textProperty(), newPassword.textProperty()));
        newPasswordBool.bind(Bindings.createBooleanBinding(() -> newPassword.getText().length() >= 8, newPasswordRepeated.textProperty(), newPassword.textProperty()));
        repeatPasswordBool.bind(Bindings.createBooleanBinding(() -> newPasswordRepeated.getText().length() >= 8, newPasswordRepeated.textProperty(), newPassword.textProperty()));
    }

    @FXML
    private void passwordKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            validateCurrentPassword();
        }
    }

    @FXML
    private void keyTyped(KeyEvent keyEvent) {
        updateErrorList();
    }

    private void updateErrorList() {
        ObservableList<Node> list = this.errorBox.getChildren();
        list.clear();
        Color c = Color.color(1, 0, 0);

        if (!passwordBool.get()) {
            errorLabel("Password doesn't match user", c, list);
        }
        if (!passwordMatchBool.get()) {
            errorLabel("Passwords don't match", c, list);
        }

        if (!newPasswordBool.get()) {
            errorLabel("First new password is too short", c, list);
        }

        if (!repeatPasswordBool.get()) {
            errorLabel("Second new password is too short", c, list);
        }
    }
}
