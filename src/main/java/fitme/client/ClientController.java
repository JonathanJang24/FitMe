package fitme.client;

import fitme.dbUtil.loginDbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    private String currUser;
    @FXML
    private Label greetingText;
    @FXML
    private TextField macroEntryNameField;
    @FXML
    private TextField macroEntryCaloriesField;
    @FXML
    private TextField macroEntryProteinField;
    @FXML
    private TextField macroEntryFiberField;
    @FXML
    private TextField macroEntryCarbohydratesField;
    @FXML
    private TextField macroEntryFatsField;
    @FXML
    private Label lastLoggedInText;

    public void initialize(URL url, ResourceBundle rb){}

    public void initUser(String user){
        String sqlLastLoggedIn = "SELECT * FROM login WHERE username=?";
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            this.currUser = user;

            Connection loginConn = loginDbConnection.getConnection();

            ps = loginConn.prepareStatement(sqlLastLoggedIn);
            ps.setString(1,user);
            rs = ps.executeQuery();

            this.greetingText.setText("Hello " + user);
            this.lastLoggedInText.setText("Last Logged In: " + rs.getString("last_login_date"));
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void addFoodMacro(ActionEvent event){

        System.out.println("clicked");

    }

    @FXML
    public void addFoodRecord(ActionEvent event){
        System.out.println("clicked");
    }



}
