package fitme.dbUtil;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class excerciseDbConnection {

    private static final String CONN = "jdbc:sqlite:/Users/jonathanjang/Documents/Code/FitMe/src/main/resources/userExcercise.db";

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
