/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import utils.ConfirmationDelegatorIF;

/**
 * FXML Controller class
 *
 * @author root
 */
public class ConfirmationFXMLController implements Initializable {

    @FXML
    Label label;

    private ConfirmationDelegatorIF interf;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setInterface(ConfirmationDelegatorIF interf) {
        this.interf = interf;
    }

    public void setText(String text) {
        label.setText(text);
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void handleOKAction(ActionEvent event) {
        interf.ok();
        stage.close();
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        interf.cancel();
        stage.close();
    }

}
