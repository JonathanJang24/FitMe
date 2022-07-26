package fitme.client;

import fitme.dbUtil.foodDbConnection;
import fitme.dbUtil.loginDbConnection;
import fitme.login.LoginController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    @FXML
    private DatePicker recordDatePicker;
    @FXML
    private TextField recordFoodField;
    @FXML
    private Button logoutButton;
    @FXML
    private Label userNameLabel;

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

            this.greetingText.setText("Hey " + user+"!");
            this.lastLoggedInText.setText("Last Logged In: " + rs.getString("last_login_date"));
            this.userNameLabel.setText(rs.getString("firstname")+" " +rs.getString("lastname"));
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void logout(ActionEvent event){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Logout?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if(alert.getResult()==ButtonType.YES){
            Stage currStage = (Stage)logoutButton.getScene().getWindow();
            currStage.close();
        }
    }

    @FXML
    public void addFoodMacro(ActionEvent event){

        System.out.println("clicked");

    }

    @FXML
    public void addFoodRecord(ActionEvent event){
        String sqlInsert = "INSERT INTO "+this.currUser+" (food_name, date_entered) VALUES (? ,?)";

        try {
            Connection conn = foodDbConnection.getConnection();

            PreparedStatement ps = null;

            ps = conn.prepareStatement(sqlInsert);
            ps.setString(1,recordFoodField.getText());
            ps.setString(2, String.valueOf(recordDatePicker.getValue()));

            ps.execute();
            conn.close();
            recordFoodField.setText("");
            recordDatePicker.setValue(null);
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }

    }



}
