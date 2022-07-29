package fitme.client;

import fitme.dbUtil.dbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
    private TextField recordServingsField;
    @FXML
    private Label macroErrorLabel;
    @FXML
    private Label recordFoodErrorLabel;
    @FXML
    private Label accountInfoJoinedDateLabel;
    @FXML
    private Label accountInfoUsernameLabel;
    @FXML
    private TextField updatePasswordField;
    @FXML
    private TextField updateFirstNameField;
    @FXML
    private TextField updateLastNameField;
    @FXML
    private DatePicker updateBirthdateField;
    @FXML
    private TableView<FoodData> foodTable;
    @FXML
    private TableColumn<FoodData, String> foodColumn;
    @FXML
    private TableColumn<FoodData, String> servingsColumn;
    @FXML
    private TableColumn<FoodData, String> caloriesColumn;
    @FXML
    private TableColumn<FoodData, String> proteinColumn;
    @FXML
    private TableColumn<FoodData, String> fibersColumn;
    @FXML
    private TableColumn<FoodData, String> carbsColumn;
    @FXML
    private TableColumn<FoodData, String> fatsColumn;
    @FXML
    private DatePicker viewFoodDatepicker;

    private ObservableList<FoodData> data;


    public void initialize(URL url, ResourceBundle rb){}

    @FXML
    private void loadFoodData(){
        String sqlGetServ = "SELECT * FROM user_food_entry WHERE user_entered=? AND date_entered=?";
        String sqlGetMacro = "SELECT * FROM food_macro WHERE name=?";

        try{
            Connection conn = dbConnection.getConnection();
            this.data = FXCollections.observableArrayList();

            PreparedStatement userPs = null;
            userPs = conn.prepareStatement(sqlGetServ);
            userPs.setString(1,this.currUser);
            userPs.setString(2,String.valueOf(this.viewFoodDatepicker.getValue()));
            ResultSet userRs = userPs.executeQuery();

            PreparedStatement macroPs = null;
            ResultSet macroRs = null;
            macroPs = conn.prepareStatement(sqlGetMacro);

            while(userRs.next()){

                macroPs.setString(1,userRs.getString(2));
                macroRs = macroPs.executeQuery();

                this.data.add(new FoodData(macroRs.getString(2),userRs.getString(3),macroRs.getString(3),macroRs.getString(4),macroRs.getString(5),macroRs.getString(6),macroRs.getString(7)));
            }
            conn.close();
        }
        catch(SQLException ex){
            System.err.println(ex);
        }

        this.foodColumn.setCellValueFactory(new PropertyValueFactory<FoodData, String>("name"));
        this.servingsColumn.setCellValueFactory(new PropertyValueFactory<FoodData, String>("servings"));
        this.caloriesColumn.setCellValueFactory(new PropertyValueFactory<FoodData, String>("calories"));
        this.proteinColumn.setCellValueFactory(new PropertyValueFactory<FoodData, String>("protein"));
        this.fibersColumn.setCellValueFactory(new PropertyValueFactory<FoodData, String>("fibers"));
        this.carbsColumn.setCellValueFactory(new PropertyValueFactory<FoodData, String>("carbohydrates"));
        this.fatsColumn.setCellValueFactory(new PropertyValueFactory<FoodData, String>("fats"));

        this.foodTable.setItems(null);
        this.foodTable.setItems(this.data);

    }

    public void initUser(String user){
        String sqlLastLoggedIn = "SELECT * FROM login WHERE username=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            this.currUser = user;
            Connection loginConn = dbConnection.getConnection();

            ps = loginConn.prepareStatement(sqlLastLoggedIn);
            ps.setString(1,user);
            rs = ps.executeQuery();

            this.greetingText.setText("Hey " + user+"!");
            this.lastLoggedInText.setText("Last Logged In: " + rs.getString("last_login_date"));

            loginConn.close();
            populateAboutMe();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    public void populateAboutMe(){
        String sqlGetUser = "SELECT * FROM login WHERE username=?";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            Connection conn = dbConnection.getConnection();
            ps = conn.prepareStatement(sqlGetUser);
            ps.setString(1, this.currUser);
            rs = ps.executeQuery();

            String userPassword = rs.getString("password");
            String userFirstName = rs.getString("firstname");
            String userLastName = rs.getString("lastname");
            String userBirthDate = rs.getString("birth_date");
            String userCreateDate = rs.getString("create_date");

            accountInfoJoinedDateLabel.setText(userCreateDate);
            accountInfoUsernameLabel.setText(this.currUser);
            updatePasswordField.setText(userPassword);
            updateFirstNameField.setText(userFirstName);
            updateLastNameField.setText(userLastName);
            updateBirthdateField.setValue(LocalDate.parse(userBirthDate));

            ps.close();
            rs.close();
            conn.close();
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

        String sqlInsert = "INSERT INTO food_macro(name, calories, protein, fibers, carbohydrates, fats) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlCheck = "SELECT * FROM food_macro WHERE name=?";
        try{

            if(macroEntryNameField.getText().equals("")||macroEntryCaloriesField.getText().equals("")||macroEntryProteinField.getText().equals("")||macroEntryFiberField.getText().equals("")||macroEntryCarbohydratesField.getText().equals("")||macroEntryFatsField.getText().equals("")){
                macroErrorLabel.setText("Error: Fields cannot be null.");
            }
            else {
                Connection conn = dbConnection.getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;

                ps = conn.prepareStatement(sqlCheck);
                ps.setString(1, macroEntryNameField.getText());
                rs = ps.executeQuery();
                if (rs.next()) {
                    macroErrorLabel.setText("Error: Food already exists.");
                } else {
                    ps = conn.prepareStatement(sqlInsert);
                    ps.setString(1, macroEntryNameField.getText());
                    ps.setString(2, macroEntryCaloriesField.getText());
                    ps.setString(3, macroEntryProteinField.getText());
                    ps.setString(4, macroEntryFiberField.getText());
                    ps.setString(5, macroEntryCarbohydratesField.getText());
                    ps.setString(6, macroEntryFatsField.getText());

                    ps.execute();

                    macroEntryNameField.setText("");
                    macroEntryCaloriesField.setText("");
                    macroEntryProteinField.setText("");
                    macroEntryFiberField.setText("");
                    macroEntryCarbohydratesField.setText("");
                    macroEntryFatsField.setText("");
                    macroErrorLabel.setText("Entered Successfully.");
                }
                ps.close();
                rs.close();
                conn.close();
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }

    }

    @FXML
    public void addFoodRecord(ActionEvent event){
        String sqlInsert = "INSERT INTO user_food_entry (food_name, servings, date_entered, user_entered) VALUES (? ,?, ?, ?)";
        String sqlCheck = "SELECT * FROM food_macro WHERE name=?";
        try {
            if(recordFoodField.getText().equals("")||recordServingsField.getText().equals("")||recordDatePicker.getValue()==null){
                recordFoodErrorLabel.setText("Error: Fields cannot be null.");
            }
            else {
                Connection conn = dbConnection.getConnection();
                PreparedStatement ps = null;
                ResultSet rs = null;

                ps = conn.prepareStatement(sqlCheck);
                ps.setString(1, recordFoodField.getText());
                rs = ps.executeQuery();
                if (rs.next()) {
                    ps = conn.prepareStatement(sqlInsert);
                    ps.setString(1, recordFoodField.getText());
                    ps.setString(2, recordServingsField.getText());
                    ps.setString(3, String.valueOf(recordDatePicker.getValue()));
                    ps.setString(4, this.currUser);
                    ps.execute();

                    recordFoodField.setText("");
                    recordServingsField.setText("");
                    recordDatePicker.setValue(null);
                    recordFoodErrorLabel.setText("Entered Successfully.");
                } else {
                    recordFoodErrorLabel.setText("Error: Food doesn't exist");
                }

                ps.close();
                rs.close();
                conn.close();
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }


    @FXML
    public void updateUserInfo(){

//        finish update user logic and db update
        String sqlUpdate = "UPDATE login SET password = ?, firstname=?, lastname=?,birth_date=? WHERE username=\""+this.currUser+"\"";

        try{
            Connection conn = dbConnection.getConnection();
            PreparedStatement ps = null;
            ps = conn.prepareStatement(sqlUpdate);

            ps.setString(1,this.updatePasswordField.getText());
            ps.setString(2,this.updateFirstNameField.getText());
            ps.setString(3,this.updateLastNameField.getText());
            ps.setString(4,String.valueOf(this.updateBirthdateField.getValue()));
            ps.execute();
            ps.close();
            conn.close();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

}
