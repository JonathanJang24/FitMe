package fitme.dbUtil;

import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {

    private static final String CONN = "jdbc:sqlite:/Users/jonathanjang/Documents/Code/FitMe/src/main/resources/users.db";

    public static java.sql.Connection getConnection() throws SQLException{
        try{

            Class.forName("org.sqlite.JDBC");
            return DriverManager.getConnection(CONN);

        }
        catch(ClassNotFoundException ex){
            ex.printStackTrace();
        }
        return null;
    }

}
