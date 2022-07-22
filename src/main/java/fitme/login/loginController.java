package fitme.login;

import fitme.signup.SignupController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class loginController implements Initializable {

    LoginModel loginModel = new LoginModel();

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private Button loginButton;
    @FXML
    private Button signUpButton;
    @FXML
    private Label loginStatus;

    public void initialize(URL url, ResourceBundle rb){
        System.out.println("initalized");
    }

    @FXML
    public void login(ActionEvent event){
        try{

            if(this.loginModel.isLoggedIn(this.usernameField.getText(),this.passwordField.getText())){
                Stage stage = (Stage)this.loginButton.getScene().getWindow();
                stage.close();
                System.out.println("User Logged in!");
            }
            else{
                loginStatus.setText("Incorrect Login");
            }
        }
        catch(Exception localException){
            localException.printStackTrace();
        }
    }

//    Add feature to test validity of signup, can't be already existing username
    @FXML
    public void signup(ActionEvent event){
        Parent signupRoot;
        try{
            System.out.println("signup");
            Stage signupStage = new Stage();
            FXMLLoader signupLoader = new FXMLLoader();
            signupRoot = signupLoader.load(getClass().getResource("/fitme.signup/signupApp.fxml").openStream());
            Scene signupScene = new Scene(signupRoot);
            signupScene.getStylesheets().add(getClass().getResource("/fitme.signup/signupStyle.css").toExternalForm());
            SignupController signupController = signupLoader.getController();

            signupStage.setTitle("Sign Up");
            signupStage.setScene(signupScene);
            signupStage.setResizable(false);
            signupStage.show();

        }
        catch(Exception localException){
            localException.printStackTrace();
        }

    }



}
