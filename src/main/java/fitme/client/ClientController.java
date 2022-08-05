package fitme.client;

import fitme.dbUtil.dbConnection;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

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
    @FXML
    private Label dailyCalsAteLabel;
    @FXML
    private Label dailyCalsBurnedLabel;
    @FXML
    private TextField workoutExcerciseField;
    @FXML
    private TextField workoutTimeField;
    @FXML
    private TextField workoutCaloriesField;
    @FXML
    private TextField workoutSetsField;
    @FXML
    private TextField workoutRepsField;
    @FXML
    private TextField workoutWeightField;
    @FXML
    private DatePicker workoutDateField;
    @FXML
    private Label workoutErrorLabel;
    @FXML
    private DatePicker viewExcerciseDatePicker;
    @FXML
    private TableView<ExcerciseData> workoutTable;
    @FXML
    private TableColumn<ExcerciseData, String> excerciseColumn;
    @FXML
    private TableColumn<ExcerciseData, String> timeColumn;
    @FXML
    private TableColumn<ExcerciseData, String> calsBurnedColumn;
    @FXML
    private TableColumn<ExcerciseData, String> setsColumn;
    @FXML
    private TableColumn<ExcerciseData, String> repsColumn;
    @FXML
    private TableColumn<ExcerciseData, String> weightColumn;
    @FXML
    private Button foodDelete;
    @FXML
    private Button workoutDelete;
    @FXML
    private TableColumn<FoodData, String> foodIdColumn;
    @FXML
    private TableColumn<ExcerciseData, String> workoutIdColumn;
    @FXML
    private Label workoutRemovalLabel;
    @FXML
    private Label foodRemovalLabel;
    @FXML
    private Label yearlyCalsAteLabel;
    @FXML
    private Label yearlyCalsBurnedLabel;


    private String selectedFoodEntry = null;
    private String selectedWorkoutEntry = null;
    private ObservableList<FoodData> foodData;
    private ObservableList<ExcerciseData> excerciseData;


    public void initialize(URL url, ResourceBundle rb){
        foodDelete.setDisable(true);
        workoutDelete.setDisable(true);
        this.workoutErrorLabel.setWrapText(true);

        foodTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            try{
                foodDelete.setDisable(false);
                selectedFoodEntry = newSelection.getEntry_id();
            }
            catch(Exception e){
//                e.printStackTrace();
            }
        });
        workoutTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            try{
                workoutDelete.setDisable(false);
                selectedWorkoutEntry = newSelection.getId();
            }
            catch(Exception e){

            }
        });
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
            populateHome();
            numericalInput();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
    }

    private void populateHome(){
        Date dt = new Date();
        int currYr = dt.getYear();
        String today = String.valueOf(java.time.LocalDate.now());

        double yrCalsAte = 0;
        int yrCalsBurned = 0;
        double dailyCalsAte = 0;
        int dailyCalsBurned = 0;

        String startYear = String.valueOf(currYr+1900)+"-01-01";
        String endYear = String.valueOf(currYr+1900)+"-12-31";
        String calsAteYrSql = "SELECT user_food_entry.food_name, user_food_entry.servings, user_food_entry.date_entered, user_food_entry.user_entered, food_macro.name, food_macro.calories FROM user_food_entry INNER JOIN food_macro ON user_food_entry.food_name=food_macro.name WHERE date_entered BETWEEN ? AND ? AND user_food_entry.user_entered=?";
        String calsBurnedYrSql = "SELECT cals_burned, date_performed, user_entered FROM user_workout_entry WHERE date_performed BETWEEN ? AND ? AND user_entered=?";

        String calsAteDailySql = "SELECT user_food_entry.food_name, user_food_entry.servings, user_food_entry.date_entered, user_food_entry.user_entered, food_macro.name, food_macro.calories FROM user_food_entry INNER JOIN food_macro ON user_food_entry.food_name=food_macro.name WHERE date_entered=? AND user_food_entry.user_entered=?";
        String calsBurnedDailySql = "SELECT cals_burned, date_performed, user_entered FROM user_workout_entry WHERE date_performed=? AND user_entered=?";
        try{

            Connection conn = dbConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(calsAteYrSql);
            ps.setString(1,startYear);
            ps.setString(2,endYear);
            ps.setString(3, this.currUser);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                double servings = Double.valueOf(rs.getString(2));
                double calories = Double.valueOf(rs.getString(6));

                yrCalsAte += servings*calories;
            }

            ps = conn.prepareStatement(calsBurnedYrSql);
            ps.setString(1,startYear);
            ps.setString(2, endYear);
            ps.setString(3, this.currUser);
            rs = ps.executeQuery();
            while(rs.next()){
                yrCalsBurned += Integer.valueOf(rs.getString(1));
            }

            ps = conn.prepareStatement(calsAteDailySql);
            ps.setString(1,today);
            ps.setString(2,this.currUser);
            rs = ps.executeQuery();
            while(rs.next()){
                double servings = Double.valueOf(rs.getString(2));
                double calories = Double.valueOf(rs.getString(6));

                dailyCalsAte += servings*calories;
            }

            ps = conn.prepareStatement(calsBurnedDailySql);
            ps.setString(1,today);
            ps.setString(2,this.currUser);
            rs = ps.executeQuery();
            while(rs.next()){
                dailyCalsBurned += Integer.valueOf(rs.getString(1));
            }

            ps.close();
            rs.close();
            conn.close();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }

        this.dailyCalsAteLabel.setText(String.valueOf(dailyCalsAte));
        this.dailyCalsBurnedLabel.setText(String.valueOf(dailyCalsBurned));
        this.yearlyCalsAteLabel.setText(String.valueOf(yrCalsAte));
        this.yearlyCalsBurnedLabel.setText(String.valueOf(yrCalsBurned));
    }

    private void populateAboutMe(){
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

    private void numericalInput(){
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();

            if (text.matches("[0-9]*")) {
                return change;
            }

            return null;
        };
        TextFormatter<String> tf1, tf2,tf3,tf4,tf5,tf6,tf7,tf8,tf9,tf10,tf11;
        tf1=new TextFormatter<>(filter);tf2=new TextFormatter<>(filter);tf3=new TextFormatter<>(filter);tf4=new TextFormatter<>(filter);tf5=new TextFormatter<>(filter);tf6=new TextFormatter<>(filter);tf7=new TextFormatter<>(filter);tf8=new TextFormatter<>(filter);tf9=new TextFormatter<>(filter);tf10=new TextFormatter<>(filter);tf11=new TextFormatter<>(filter);
        macroEntryCaloriesField.setTextFormatter(tf1);
        macroEntryProteinField.setTextFormatter(tf2);
        macroEntryFiberField.setTextFormatter(tf3);
        macroEntryCarbohydratesField.setTextFormatter(tf4);
        macroEntryFatsField.setTextFormatter(tf5);
        recordServingsField.setTextFormatter(tf6);
        workoutTimeField.setTextFormatter(tf7);
        workoutCaloriesField.setTextFormatter(tf8);
        workoutSetsField.setTextFormatter(tf9);
        workoutRepsField.setTextFormatter(tf10);
        workoutWeightField.setTextFormatter(tf11);
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
                PreparedStatement ps = conn.prepareStatement(sqlCheck);

                ps.setString(1, recordFoodField.getText());
                ResultSet rs = ps.executeQuery();
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
        loadFoodData();
        populateHome();
    }

    @FXML
    public void loadFoodData(){
        String sqlGetServ = "SELECT * FROM user_food_entry WHERE user_entered=? AND date_entered=?";
        String sqlGetMacro = "SELECT * FROM food_macro WHERE name=?";

        try{
            Connection conn = dbConnection.getConnection();
            this.foodData = FXCollections.observableArrayList();

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

                this.foodData.add(new FoodData(userRs.getString(1),macroRs.getString(2),userRs.getString(3),macroRs.getString(3),macroRs.getString(4),macroRs.getString(5),macroRs.getString(6),macroRs.getString(7)));
            }
            conn.close();
        }
        catch(SQLException ex){
            System.err.println(ex);
        }

        this.foodIdColumn.setCellValueFactory(new PropertyValueFactory<FoodData, String>("entry_id"));
        this.foodColumn.setCellValueFactory(new PropertyValueFactory<FoodData, String>("name"));
        this.servingsColumn.setCellValueFactory(new PropertyValueFactory<FoodData, String>("servings"));
        this.caloriesColumn.setCellValueFactory(new PropertyValueFactory<FoodData, String>("calories"));
        this.proteinColumn.setCellValueFactory(new PropertyValueFactory<FoodData, String>("protein"));
        this.fibersColumn.setCellValueFactory(new PropertyValueFactory<FoodData, String>("fibers"));
        this.carbsColumn.setCellValueFactory(new PropertyValueFactory<FoodData, String>("carbohydrates"));
        this.fatsColumn.setCellValueFactory(new PropertyValueFactory<FoodData, String>("fats"));
        this.foodTable.setItems(null);
        this.foodTable.setItems(this.foodData);

    }

    @FXML
    public void remFoodEntry(ActionEvent event){
        String sqlRem = "DELETE FROM user_food_entry WHERE entry_id=?";
        try{
            Connection conn = dbConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sqlRem);
            ps.setString(1,this.selectedFoodEntry);
            ps.execute();
            ps.close();
            conn.close();
        }
        catch(SQLException ex){
            System.err.println(ex);
        }
        loadFoodData();
        foodRemovalLabel.setText("Removed successfully.");
    }

    @FXML
    public void addExcerciseRecord(ActionEvent event){

        String sqlInsert = "INSERT INTO user_workout_entry(excercise, time, cals_burned, sets, reps, weight, date_performed, user_entered) VALUES(?,?,?,?,?,?,?,?)";
        try{
            if(workoutExcerciseField.getText().equals("")||workoutCaloriesField.getText().equals("")||workoutDateField.getValue()==null){
                workoutErrorLabel.setText("Error: Excercise, calories, and date cannot be null.");
            }
            else{
                Connection conn = dbConnection.getConnection();

                PreparedStatement ps = conn.prepareStatement(sqlInsert);
                ps.setString(1,workoutExcerciseField.getText());
                ps.setString(2, workoutTimeField.getText());
                ps.setString(3, workoutCaloriesField.getText());
                ps.setString(4, workoutSetsField.getText());
                ps.setString(5,workoutRepsField.getText());
                ps.setString(6,workoutWeightField.getText());
                ps.setString(7, String.valueOf(workoutDateField.getValue()));
                ps.setString(8,this.currUser);

                ps.execute();
                conn.close();
                ps.close();

                workoutExcerciseField.setText("");
                workoutTimeField.setText("");
                workoutCaloriesField.setText("");
                workoutSetsField.setText("");
                workoutRepsField.setText("");
                workoutWeightField.setText("");
                workoutDateField.setValue(null);
                workoutErrorLabel.setText("Workout Entry Added");
            }
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        loadExcerciseData();
        populateHome();
    }

    @FXML
    public void loadExcerciseData(){
        String sqlGet = "SELECT * FROM user_workout_entry WHERE user_entered=? AND date_performed=?";

        try{
            Connection conn = dbConnection.getConnection();
            this.excerciseData = FXCollections.observableArrayList();

            PreparedStatement ps = null;
            ps = conn.prepareStatement(sqlGet);
            ps.setString(1,this.currUser);
            ps.setString(2, String.valueOf(this.viewExcerciseDatePicker.getValue()));
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                this.excerciseData.add(new ExcerciseData(rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5),rs.getString(6),rs.getString(7)));
            }
            conn.close();
        }
        catch(SQLException ex){
            System.err.println(ex);
        }

        this.workoutIdColumn.setCellValueFactory(new PropertyValueFactory<ExcerciseData, String>("id"));
        this.excerciseColumn.setCellValueFactory(new PropertyValueFactory<ExcerciseData, String>("excercise"));
        this.timeColumn.setCellValueFactory(new PropertyValueFactory<ExcerciseData, String>("time"));
        this.calsBurnedColumn.setCellValueFactory(new PropertyValueFactory<ExcerciseData, String>("cals_burned"));
        this.setsColumn.setCellValueFactory(new PropertyValueFactory<ExcerciseData, String>("sets"));
        this.repsColumn.setCellValueFactory(new PropertyValueFactory<ExcerciseData, String>("reps"));
        this.weightColumn.setCellValueFactory(new PropertyValueFactory<ExcerciseData, String>("weight"));
        this.workoutTable.setItems(null);
        this.workoutTable.setItems(this.excerciseData);
    }

    @FXML
    public void remExcerciseEntry(ActionEvent event){
        String sqlRem = "DELETE FROM user_workout_entry WHERE id=?";
        try{
            Connection conn = dbConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(sqlRem);
            ps.setString(1,this.selectedWorkoutEntry);
            ps.execute();
            ps.close();
            conn.close();
        }
        catch(SQLException ex){
            System.err.println(ex);
        }
        loadExcerciseData();
        workoutRemovalLabel.setText("Removed Successfully.");
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
