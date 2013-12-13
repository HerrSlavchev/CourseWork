/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author root
 */
public class LoginDialogFXMLController implements Initializable {

    private Stage parentStage = null;
    private Stage stage = null;
    
    @FXML
    TextField eMailTF;
    @FXML
    PasswordField passwordTF;

    @FXML
    private void handleLoginAction(ActionEvent event) {
        String eMail = eMailTF.getText();
        String password = passwordTF.getText();

        int rnd = (int) (Math.random() * 2);
        
        if(rnd == 1){
            successfulLogin();
        } else {
            errorPrompt();
        }
    }

    private void successfulLogin(){
        System.out.println("Success");
        stage.close();
    }
    
    private void errorPrompt(){
        
    }
    
    public void setParent(Stage parentStage){
        this.parentStage = parentStage;
    }
    
    public void setStage(Stage stage){
        this.stage = stage;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   

}
