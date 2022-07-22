package fitme.login;

import fitme.dbUtil.loginDbConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {

    Connection connection;

    public LoginModel(){
        try{
            this.connection = loginDbConnection.getConnection();
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

    public boolean isLoggedIn(String user, String pswd) throws Exception{
        PreparedStatement pr = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM login WHERE username = ? AND password = ?";


        try{
            pr = this.connection.prepareStatement(sql);
            pr.setString(1,user);
            pr.setString(2, pswd);

            rs = pr.executeQuery();
            return rs.next();

        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        finally{
            pr.close();
            rs.close();
        }
    }

}
