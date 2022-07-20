package fitme.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.IOException;

public class loginApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("loginApp.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("loginStyle.css").toExternalForm());
        stage.setScene(scene);
        stage.setTitle("FitMe Login");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args){
        launch();
    }

}
