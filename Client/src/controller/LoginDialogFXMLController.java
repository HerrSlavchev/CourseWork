/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dto.Result;
import dto.domain.User;
import example.Client;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.BindingConsts;
import services.RemoteServices;
import services.client.NotifiableIF;
import services.implementations.ServiceExposer;
import services.server.ClientManagerIF;
import utils.Utils;

/**
 * FXML Controller class
 *
 * @author root
 */
public class LoginDialogFXMLController implements Initializable {

    private Stage stage = null;
    
    @FXML
    TextField eMailTF;
    @FXML
    PasswordField passwordTF;

    @FXML
    private void handleLoginAction(ActionEvent event) {
        String eMail = eMailTF.getText();
        String password = passwordTF.getText();

        ClientManagerIF stub = (ClientManagerIF) RemoteServices.getStub(BindingConsts.CLIENT_MANAGER);
        User user = null;
        Throwable exc = null;
        try {
            User u = new User(0);
            u.eMail = eMailTF.getText();
            u.password = passwordTF.getText();
            NotifiableIF client = ServiceExposer.client;
            Result<User> res = stub.registerClient(client, u);
            if (res.getException() != null){
                exc = res.getException();
            } else {
                user = res.getResult().get(0);
            }
        } catch (Exception e){
            exc = e;
        }
        
        if(exc != null) {
            errorPrompt(exc);
        } else {
            successfulLogin(user);
        }
    }

    private void successfulLogin(User user){
        Utils.showMessage("SUCCESS!", "Successful login!", stage);
        Client.getMainPageController().setLogged(true);
        stage.close();
    }
    
    private void errorPrompt(Throwable exc){
        Utils.showError(exc.getMessage(), stage);
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
