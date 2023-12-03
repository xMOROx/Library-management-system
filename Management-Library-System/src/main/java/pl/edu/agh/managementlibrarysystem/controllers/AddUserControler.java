package pl.edu.agh.managementlibrarysystem.controllers;

import io.github.palexdev.materialfx.controls.MFXButton;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import pl.edu.agh.managementlibrarysystem.config.events.OpenWindowEvent;

import java.net.URL;
import java.util.List;
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
    private Pattern patternEmail;

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
    @FXML
    public VBox errorVbox;

    public AddUserControler(@Value("classpath:/fxml/login.fxml") Resource backWindow,
                           ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        this.backWindow = backWindow;
        this.patternEmail = Pattern.compile(".+@.+\\..+", Pattern.CASE_INSENSITIVE);
    }
    @FXML
    public void addClicked(MouseEvent mouseEvent){
        System.out.println("Adding works");
    }

    @FXML
    public void cancelClicked(MouseEvent mouseEvent){
        applicationContext.publishEvent(new OpenWindowEvent((Stage)((Node)mouseEvent.getSource()).getScene().getWindow(),backWindow));
    }
    @FXML
    public void keyTyped(){
        updateErrorList();
    }
    public void updateErrorList() {
        ObservableList<Node> list = this.errorVbox.getChildren();
        list.clear();
        Color c = Color.color(1,0,0);
        if(!nameBool.get()){
            Label l = new Label();
            l.setText("Problem with name");
            l.setTextFill(c);
            list.add(l);
        }
        if(!surnameBool.get()){
            Label l = new Label();
            l.setText("Problem with surname");
            l.setTextFill(c);
            list.add(l);
        }
        if(!emailBool.get()){
            Label l = new Label();
            l.setText("Problem with email");
            l.setTextFill(c);
            list.add(l);
        }
        if(!passwordBool.get()){
            Label l = new Label();
            l.setText("Problem with password");
            l.setTextFill(c);
            list.add(l);
        }
        if(!repeatPasswordBool.get()){
            Label l = new Label();
            l.setText("Passwords aren't equal");
            l.setTextFill(c);
            list.add(l);
        }
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
        name.focusedProperty().addListener((observable,oldValue,newValue)->{
            if(!newValue){

            }
        });
    }
}
