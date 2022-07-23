package fitme.login;

import fitme.client.ClientController;
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
        Parent clientRoot;
        try{
            if(this.loginModel.isLoggedIn(this.usernameField.getText(),this.passwordField.getText())){
                Stage stage = (Stage)this.loginButton.getScene().getWindow();
                stage.close();
                Stage clientStage = new Stage();
                FXMLLoader clientLoader = new FXMLLoader();
                clientRoot = clientLoader.load(getClass().getResource("/fitme.client/clientApp.fxml"));
                Scene clientScene = new Scene(clientRoot);
                clientScene.getStylesheets().add(getClass().getResource("/fitme.client/clientStyle.css").toExternalForm());
                ClientController clientController = clientLoader.getController();

                clientStage.setTitle("FitMe");
                clientStage.setScene(clientScene);
                clientStage.setResizable(false);
                clientStage.show();
            }
            else{
                loginStatus.setText("Incorrect Login");
            }
        }
        catch(Exception localException){
            localException.printStackTrace();
        }
    }

    @FXML
    public void signup(ActionEvent event){
        Parent signupRoot;
        try{
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
