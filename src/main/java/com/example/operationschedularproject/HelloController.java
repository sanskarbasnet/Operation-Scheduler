package com.example.operationschedularproject;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloController {
    @FXML
    private Label message;
    @FXML
    TextField username;
    @FXML
    PasswordField password;
    Parent root;
    Scene scene;
    Stage stage;

    @FXML
    public void onLogin(ActionEvent e) throws IOException {
        String Username = "1234";
        String Password = "1234";

        String inUserName = username.getText();
        String inPass = password.getText();
        if(inUserName.length() == 0 ){
            message.setText("Enter a valid Username");
        } else if(inPass.length() == 0 ){
            message.setText("Enter a valid Password");
        } else if (!Username.equals(inUserName) || !Password.equals(inPass)){
            message.setText("Username or Password not correct");
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScreen.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            String css = this.getClass().getResource("style.css").toExternalForm();
            scene.getStylesheets().add(css);
            stage.setScene(scene);
            stage.sizeToScene();
            stage.show();

        }

    }
}