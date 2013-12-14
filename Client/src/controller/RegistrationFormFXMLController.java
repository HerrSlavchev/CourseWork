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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import properties.Properties;
import services.BindingConsts;
import services.RemoteServices;
import services.server.UserDAIF;
import utils.Utils;

/**
 * FXML Controller class
 *
 * @author root
 */
public class RegistrationFormFXMLController implements Initializable {

    
    @FXML
    TextField eMailTF;
    @FXML
    PasswordField passwordTF;
    @FXML
    PasswordField confirmTF;
    @FXML
    TextField fNameTF;
    @FXML
    TextField sNameTF;
    @FXML
    TextField lNameTF;
    @FXML
    TextArea descriptionTF;

    private User readFromForm() {
        User u = new User(0);
        u.description = descriptionTF.getText();
        u.eMail = eMailTF.getText();
        u.password = passwordTF.getText();
        u.fName = fNameTF.getText();
        u.sName = sNameTF.getText();
        u.lName = lNameTF.getText();
        
        return u;
    }

    @FXML
    private void handleRegistrationAction(ActionEvent event) {

        UserDAIF stub = (UserDAIF) RemoteServices.getStub(BindingConsts.USER_DA);
        Throwable exc = null;
        try {
            User u = readFromForm();
            List<User> lst = new ArrayList();
            lst.add(u);
            Result<User> res = stub.insertUser(lst, Properties.getSession());
            if (res.getException() != null) {
                exc = res.getException();
            } else {
                Utils.showMessage("SUCCESS!", "Successful registration!", Client.getMainPageStage());
            }
        } catch (Exception e) {
            exc = e;
        }
        
        if (exc != null) {
            Utils.showError(exc.getMessage(), Client.getMainPageStage());
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
