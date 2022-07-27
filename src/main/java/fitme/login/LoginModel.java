package fitme.login;

import fitme.dbUtil.dbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {

    Connection connection;

    public LoginModel(){
        try{
            this.connection = dbConnection.getConnection();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        if(this.connection==null)
            System.exit(1);
    }

    public boolean isDataBaseConnected(){
        return this.connection!=null;
    }

    public void endConn() throws SQLException {
        this.connection.close();
    }

    public boolean isLoggedIn(String user, String pswd) throws Exception{
        PreparedStatement checkPr = null;
        ResultSet checkRs = null;

        PreparedStatement loggedPr = null;
        String dateNow = String.valueOf(java.time.LocalDate.now());
        String sql = "SELECT * FROM login WHERE username = ? AND password = ?";
        String sqlLastLoggedin = "UPDATE login SET last_login_date = \""+ dateNow + "\" WHERE username = ?";

        try{
            checkPr = this.connection.prepareStatement(sql);
            checkPr.setString(1,user);
            checkPr.setString(2, pswd);

            checkRs = checkPr.executeQuery();
            if(checkRs.next()){
                loggedPr = this.connection.prepareStatement(sqlLastLoggedin);
                loggedPr.setString(1, user);
                loggedPr.execute();
                loggedPr.close();
                return true;
            }
            return false;

        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally{
            checkPr.close();
            checkRs.close();
        }
    }

}
