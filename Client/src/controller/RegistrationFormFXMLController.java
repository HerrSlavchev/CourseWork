/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dto.Result;
import dto.domain.User;
import view.Client;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import properties.SessionProperties;
import services.BindingConsts;
import services.RemoteServices;
import services.server.UserDAIF;
import utils.Utils;

/**
 * FXML Controller class
 *
 * @author root
 */
public class RegistrationFormFXMLController implements Initializable, SessionAwareIF {

    @FXML
    GridPane mainPane;

    @FXML
    TextField eMailTF;
    @FXML
    Label passwordL;
    @FXML
    PasswordField passwordTF;
    @FXML
    Label confirmL;
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
        User u = SessionProperties.user;
        if (u != null) {
            tmp = new User(u.getID(), u.getTimeIns(), u.getTimeUpd(), null);
            tmp.setRole(u.getRole());
        }

        tmp.setDescription(descriptionTF.getText());
        tmp.setE_Mail(eMailTF.getText());
        tmp.setPassword(confirmTF.getText());
        tmp.setFirstName(fNameTF.getText());
        tmp.setSurName(sNameTF.getText());
        tmp.setLastName(lNameTF.getText());

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
            Result<User> res = stub.insertUser(lst, SessionProperties.getSession());
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
            Result<User> res = stub.updateUser(lst, SessionProperties.getSession());
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

    }

    @Override
    public void refreshGUI() {
        if (false == SessionProperties.isLogged()) {
            editB.setVisible(false);

            if (Client.getMainPageController().isLoggingOut()) {
                mainPane.setVisible(false);
            }
        } else {
            User u = SessionProperties.user;
            eMailTF.setText(u.getE_Mail());
            eMailTF.setEditable(false);

            passwordL.setVisible(false);
            passwordTF.setVisible(false);
            confirmL.setVisible(false);
            confirmTF.setVisible(false);

            //confirmTF.setEditable(false);
            fNameTF.setText(u.getFirstName());
            sNameTF.setText(u.getSurName());
            lNameTF.setText(u.getLastName());
            descriptionTF.setText(u.getDescription());
            registerB.setVisible(false);
            editB.setVisible(true);
            mainPane.setVisible(true);
        }
    }

}
