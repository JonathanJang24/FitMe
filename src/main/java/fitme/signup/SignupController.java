package fitme.signup;

import fitme.dbUtil.excerciseDbConnection;
import fitme.dbUtil.foodDbConnection;
import fitme.dbUtil.loginDbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SignupController implements Initializable {

    @FXML
    private TextField signupUsernameField;
    @FXML
    private TextField signupPasswordField;
    @FXML
    private TextField signupFirstNameField;
    @FXML
    private TextField signupLastNameField;
    @FXML
    private DatePicker signupBirthdatePicker;
    @FXML
    private Button createAccountButton;
    @FXML
    private Label signupStatus;


    public void initialize(URL url, ResourceBundle rb){

    }

    @FXML
    public void createAccount(ActionEvent event){


            String sqlInsert = "INSERT INTO login (username, password, firstname, lastname, birth_day, birth_month, birth_year, create_date,last_login_date) VALUES (?, ?, ?, ?, ?,?,?,?,?)";

            String sqlCheck = "SELECT * FROM login WHERE username = ?";

            String sqlCreateFood = "CREATE TABLE "+this.signupUsernameField.getText()+" (entry_id INTEGER PRIMARY KEY, food_name TEXT, date_entered TEXT)";

            try{
                Connection conn = loginDbConnection.getConnection();
                Connection foodConn = foodDbConnection.getConnection();

                PreparedStatement ps = null;
                ResultSet rs = null;
                ps = conn.prepareStatement(sqlCheck);
                ps.setString(1, this.signupUsernameField.getText());
                rs = ps.executeQuery();
                if(rs.next()){
                    signupStatus.setText("User Already Exists");
                }
                else{
                    PreparedStatement stat = conn.prepareStatement(sqlInsert);

                    stat.setString(1, this.signupUsernameField.getText());
                    stat.setString(2, this.signupPasswordField.getText());
                    stat.setString(3, this.signupFirstNameField.getText());
                    stat.setString(4, this.signupLastNameField.getText());
                    stat.setString(5, String.valueOf(this.signupBirthdatePicker.getValue().getDayOfMonth()));
                    stat.setString(6, String.valueOf(this.signupBirthdatePicker.getValue().getMonthValue()));
                    stat.setString(7, String.valueOf(this.signupBirthdatePicker.getValue().getYear()));
                    stat.setString(8, String.valueOf(java.time.LocalDate.now()));
                    stat.setString(9, String.valueOf(java.time.LocalDate.now()));

                    stat.execute();
                    conn.close();

//                  initialize user specific excercise and food databases
                    PreparedStatement foodPs = foodConn.prepareStatement(sqlCreateFood);
                    foodPs.execute();
                    foodConn.close();

                    Stage stage = (Stage)this.createAccountButton.getScene().getWindow();
                    stage.close();
                    stat.close();
                }
            }
            catch(SQLException ex){
                throw new RuntimeException(ex);
            }

    }

}
