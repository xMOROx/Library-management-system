package pl.edu.agh.managementlibrarysystem.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.config.events.OpenWindowEvent;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Controller

public class AddUserControler implements Initializable {

    private final ApplicationContext applicationContext;

    private final Resource backWindow;
    private BooleanProperty nameBool;
    private BooleanProperty surnameBool;
    private BooleanProperty emailBool;
    private BooleanProperty passwordBool;
    private BooleanProperty repeatPasswordBool;
    private Pattern patternEmail = Pattern.compile(".+@.+\\..+", Pattern.CASE_INSENSITIVE);

    @FXML
    public TextField name;
    @FXML
    public TextField surname;
    @FXML
    public TextField email;
    @FXML
    public MFXButton addUser;
    @FXML
    public Label error;
    @FXML
    public PasswordField password;
    @FXML
    public PasswordField repeatPassword;
    @FXML
    public MFXButton cancel;

    public AddUserControler(@Value("classpath:/fxml/login.fxml") Resource backWindow,
                           ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.backWindow = backWindow;
    }
    @FXML
    public void addClicked(MouseEvent mouseEvent){
        System.out.println("Adding works");
    }

    @FXML
    public void cancelClicked(MouseEvent mouseEvent){
        applicationContext.publishEvent(new OpenWindowEvent((Stage)((Node)mouseEvent.getSource()).getScene().getWindow(),backWindow));
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.nameBool = new SimpleBooleanProperty(false);
        this.surnameBool = new SimpleBooleanProperty(false);
        this.emailBool = new SimpleBooleanProperty(false);
        this.passwordBool = new SimpleBooleanProperty(false);
        this.repeatPasswordBool = new SimpleBooleanProperty(false);
        this.addUser.disableProperty().bind(Bindings.createBooleanBinding(() -> {
            return !(nameBool.get() && surnameBool.get() && emailBool.get() && passwordBool.get() && repeatPasswordBool.get());
        }
        ,nameBool,surnameBool,emailBool,passwordBool,repeatPasswordBool));
        nameBool.bind(Bindings.createBooleanBinding(() -> {
            String text = name.textProperty().get();
            if (text.length()<2) {
                return false;
            }
            else return text.toUpperCase().charAt(0) == text.charAt(0);

        }, name.textProperty()));

        surnameBool.bind(Bindings.createBooleanBinding(() -> {
            String text = surname.textProperty().get();
            if (text.length()<2) {
                return false;
            }
            else return text.toUpperCase().charAt(0) == text.charAt(0);
        }, surname.textProperty()));

        emailBool.bind(Bindings.createBooleanBinding(() -> {
            String text = email.textProperty().get();
            Matcher matcher = this.patternEmail.matcher(text);
            return matcher.find();
        }, email.textProperty()));

        passwordBool.bind(Bindings.createBooleanBinding(() -> {
            return password.getText().length()>=6;
        }, password.textProperty()));

        repeatPasswordBool.bind(Bindings.createBooleanBinding(() -> {
            return password.getText().equals(repeatPassword.getText());
        }, repeatPassword.textProperty(), password.textProperty()));
    }
}
