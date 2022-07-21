module fitme.login {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens fitme.login to javafx.fxml;
    exports fitme.login;
    opens fitme.signup to javafx.fxml;
    exports fitme.signup;
}
