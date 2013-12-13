/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dto.Result;
import dto.domain.User;
import javafx.geometry.Insets;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
            }
        } catch (Exception e) {
            exc = e;
        }
        
        if (exc != null) {
            Utils.showMessage(exc.getMessage());
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
