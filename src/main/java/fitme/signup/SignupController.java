package fitme.signup;

import fitme.dbUtil.dbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    @FXML
    private TextField signupUsernameField;
    @FXML
    private TextField signupPasswordField;
    @FXML
    private Button createAccountButton;

    private dbConnection conn;

    public void initialize(URL url, ResourceBundle rb){

    }

    @FXML
    public void createAccount(ActionEvent event){

        try{

            System.out.println("account created");
            System.out.println(signupUsernameField.getText());
            System.out.println(signupPasswordField.getText());
        }
        catch(Exception localException){
            localException.printStackTrace();
        }
    }

}
