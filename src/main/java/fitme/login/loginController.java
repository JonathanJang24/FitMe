package fitme.login;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class loginController {

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;

    public void initialize(URL url, ResourceBundle rb){
        System.out.println("initalized");
    }

    @FXML
    public void login(ActionEvent event){
        try{
            System.out.println("login");

        }
        catch(Exception localException){
            localException.printStackTrace();
        }
    }

    @FXML
    public void signup(ActionEvent event){

        try{
            System.out.println("signup");
        }
        catch(Exception localException){
            localException.printStackTrace();
        }

    }



}
