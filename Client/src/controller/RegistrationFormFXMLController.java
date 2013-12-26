/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dto.Result;
import dto.domain.User;
import dto.session.Session;
import view.Client;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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

    @FXML
    Button registerB;
    @FXML
    Button editB;

    private User readFromForm() {
        
        User tmp = new User(0);
        
        //if editing, take some info from logged in user
        User u = Properties.user;
        if (u != null) {
            tmp = new User(u.getID(), u.getTimeIns(), u.getTimeUpd(), null);
            tmp.role = u.role;
        }
        
        tmp.description = descriptionTF.getText();
        tmp.eMail = eMailTF.getText();
        tmp.password = passwordTF.getText();
        tmp.fName = fNameTF.getText();
        tmp.sName = sNameTF.getText();
        tmp.lName = lNameTF.getText();

        return tmp;
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

    @FXML
    private void handleEditAction(ActionEvent event) {
        UserDAIF stub = (UserDAIF) RemoteServices.getStub(BindingConsts.USER_DA);
        
        Throwable exc = null;
        try {
            User u = readFromForm();
            List<User> lst = new ArrayList();
            lst.add(u);
            Result<User> res = stub.updateUser(lst, Properties.getSession());
            if (res.getException() != null) {
                exc = res.getException();
            } else {
                Utils.showMessage("SUCCESS!", "Successful edit!", Client.getMainPageStage());
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
        Session s = Properties.getSession();
        User u = Properties.user;
        if (s == null) {
            editB.setVisible(false);
        } else {
            eMailTF.setText(u.eMail);
            eMailTF.setEditable(false);
            passwordTF.setText("");
            passwordTF.setEditable(false);
            confirmTF.setText("");
            confirmTF.setEditable(false);
            fNameTF.setText(u.fName);
            sNameTF.setText(u.sName);
            lNameTF.setText(u.lName);
            descriptionTF.setText(u.description);
            registerB.setVisible(false);
        }

    }

}
