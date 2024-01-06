package pl.edu.agh.managementlibrarysystem.controller;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.DTO.UserDTO;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.BaseController;
import pl.edu.agh.managementlibrarysystem.controller.abstraction.ResizeableBaseController;
import pl.edu.agh.managementlibrarysystem.event.fxml.LeavingBorderPaneEvent;
import pl.edu.agh.managementlibrarysystem.model.User;
import pl.edu.agh.managementlibrarysystem.model.util.Permission;
import pl.edu.agh.managementlibrarysystem.repository.UserRepository;
import pl.edu.agh.managementlibrarysystem.service.UserService;
import pl.edu.agh.managementlibrarysystem.session.UserSession;
import pl.edu.agh.managementlibrarysystem.utils.Alerts;
import pl.edu.agh.managementlibrarysystem.utils.ControlsUtils;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Controller

public class EditUserController extends ResizeableBaseController implements Initializable {
    private final UserService userService;
    private final Pattern patternEmail;
    private final ObservableList<String> data = FXCollections.observableArrayList();
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserSession session;
    private UserDTO currUserDTO;
    @FXML
    private TextField surname;
    @FXML
    private TextField name;
    @FXML
    private TextField email;
    @FXML
    private MFXButton saveAsUs;
    @FXML
    private MFXButton saveAsLib;
    @FXML
    private MFXButton saveAsAdmin;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField repeatPassword;
    @FXML
    private VBox errorVbox;
    @FXML
    private ComboBox<String> chooseUser;
    private BooleanProperty nameBool;
    private BooleanProperty surnameBool;
    private BooleanProperty emailBool;
    private BooleanProperty passwordBool;
    private BooleanProperty repeatPasswordBool;
    public EditUserController(ApplicationContext applicationContext, UserService userService, PasswordEncoder passwordEncoder, UserRepository userRepository,UserSession session) {
        super(applicationContext);
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.patternEmail = Pattern.compile(".+@.+\\..+", Pattern.CASE_INSENSITIVE);
        this.userService = userService;
        this.session = session;
    }
    private static void errorLabel(String error_message, Color c, ObservableList<Node> list) {
        Label l = new Label();
        l.setText(error_message);
        l.setTextFill(c);
        list.add(l);
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.tooltipInitializer();
        this.loadUserDropbox();
        this.nameBool = new SimpleBooleanProperty(false);
        this.surnameBool = new SimpleBooleanProperty(false);
        this.emailBool = new SimpleBooleanProperty(false);
        this.passwordBool = new SimpleBooleanProperty(false);
        this.repeatPasswordBool = new SimpleBooleanProperty(false);

        this.saveAsUs.disableProperty().bind(Bindings.createBooleanBinding(() -> !(nameBool.get() && surnameBool.get() && emailBool.get() && passwordBool.get() && repeatPasswordBool.get())
                , nameBool, surnameBool, emailBool, passwordBool, repeatPasswordBool));
        this.saveAsLib.disableProperty().bind(Bindings.createBooleanBinding(() -> !(nameBool.get() && surnameBool.get() && emailBool.get() && passwordBool.get() && repeatPasswordBool.get())
                , nameBool, surnameBool, emailBool, passwordBool, repeatPasswordBool));
        this.saveAsAdmin.disableProperty().bind(Bindings.createBooleanBinding(() -> !(nameBool.get() && surnameBool.get() && emailBool.get() && passwordBool.get() && repeatPasswordBool.get())
                , nameBool, surnameBool, emailBool, passwordBool, repeatPasswordBool));

        validateName(nameBool, name);

        validateName(surnameBool, surname);

        validateEmail();

        validateNewPassword();

        initializeStageOptions();

    }
    private void initializeStageOptions() {
        if (session.getLoggedUser() == null) {
            return;
        }
        User u = session.getLoggedUser();
        if (u.getPermission() == Permission.NORMAL_USER) {
            ControlsUtils.changeControlVisibility(chooseUser, false);
            ControlsUtils.changeControlVisibility(saveAsAdmin, false);
            ControlsUtils.changeControlVisibility(saveAsLib, false);
            chooseUser.setValue(u.getId().toString());
            userHasBeenChosen();

        }
    }
    private void validateEmail() {
        emailBool.bind(Bindings.createBooleanBinding(() -> {
            String text = email.textProperty().get();
            Matcher matcher = this.patternEmail.matcher(text);
            return matcher.find();
        }, email.textProperty()));
    }

    private void validateName(BooleanProperty nameBool, TextField name) {
        nameBool.bind(Bindings.createBooleanBinding(() -> {
            String text = name.textProperty().get();
            if (text.length() < 2) {
                return false;
            } else return text.toUpperCase().charAt(0) == text.charAt(0);

        }, name.textProperty()));
    }

    private void validatePassword() {
        if(currUserDTO==null){
            return;
        }
        passwordBool.set(userRepository.findByEmail(currUserDTO.getEmail()).isPresent() && passwordEncoder.matches(password.getText(), userRepository.findByEmail(currUserDTO.getEmail()).get().getPassword()));
    }
    private void validateNewPassword(){

        repeatPasswordBool.bind(Bindings.createBooleanBinding(() -> password.getText().length()>=8, repeatPassword.textProperty(), password.textProperty()));
        name.focusedProperty().addListener((observable, oldValue, newValue) -> {
        });
    }
    private void loadUserDropbox(){
        this.data.clear();
        List<UserDTO> users = userService.getAllUsers();
        for(UserDTO userDTO : users){
            data.add(userDTO.getId().toString());
        }
        this.chooseUser.setItems(data);
    }
    @FXML
    private void userHasBeenChosen(){
        if(chooseUser.getValue().equals("Choose user")){
           return;
        }
        Long id =0L;
        try{
            id = Long.parseLong(chooseUser.getValue());
        }
        catch (NumberFormatException e){
            e.printStackTrace();
            return;
        }
        currUserDTO = userService.findById(Long.parseLong(chooseUser.getValue()));
        try{
            this.name.setText(currUserDTO.getFullname().split(" ")[0]);
            this.surname.setText(currUserDTO.getFullname().split(" ")[1]);
        }
        catch (IndexOutOfBoundsException e){
            e.printStackTrace();
            return;
        }
        this.email.setText(currUserDTO.getEmail());
        updateErrorList();
    }

    @FXML
    private void keyTyped(KeyEvent keyEvent) {
        updateErrorList();
    }
    private void updateErrorList() {
        ObservableList<Node> list = this.errorVbox.getChildren();
        list.clear();
        Color c = Color.color(1, 0, 0);
        if (!nameBool.get()) {
            errorLabel("Problem with name", c, list);
        }
        if (!surnameBool.get()) {
            errorLabel("Problem with surname", c, list);
        }
        if (!emailBool.get()) {
            errorLabel("Problem with email", c, list);
        }
        if (!passwordBool.get()) {
            errorLabel("Password doesn't match user", c, list);
        }
        if (!repeatPasswordBool.get()) {
            errorLabel("New password is too short", c, list);
        }
    }
    @FXML
    private void addUserClicked(MouseEvent mouseEvent) {
        updateUser(Permission.NORMAL_USER);
    }

    @FXML
    private void addLibrarianClicked(MouseEvent mouseEvent) {
        updateUser(Permission.LIBRARIAN);
    }

    @FXML
    private void addAdminClicked(MouseEvent mouseEvent) {
        updateUser(Permission.ADMIN);
    }
    private void updateUser(Permission permission){
        userService.updateUser(this.currUserDTO.getId(),this.name.getText(),this.surname.getText(),this.email.getText(),passwordEncoder.encode(this.repeatPassword.getText()),permission);
        Alerts.showInformationAlert("Update User attempt","user has been updated");
    }

    @FXML
    private void passwordKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            validatePassword();
        }

    }
}
