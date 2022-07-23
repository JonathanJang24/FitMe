package fitme.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    private Stage currStage;
    private String currUser;
    @FXML
    private Label greetingText;

    public void initialize(URL url, ResourceBundle rb){}

    public void initUser(String user){
        this.currUser = user;
        this.greetingText.setText("Hello "  + user);
    }




}
