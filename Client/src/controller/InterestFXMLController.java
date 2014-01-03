/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import dto.domain.Category;
import dto.domain.Interest;
import dto.rolemanagement.Role;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import properties.SessionProperties;
import services.BindingConsts;
import services.RemoteServices;
import services.server.InterestDAIF;

/**
 * FXML Controller class
 *
 * @author Lubomir
 */
public class InterestFXMLController implements Initializable, SessionAwareIF {
    
    //==Info/Input fields
    @FXML
    TextField name;
    //==Description area
    @FXML
    TextArea descriptionArea;
    //==The scroll pane/VBox
    @FXML
    VBox vboxSubCategories;
    //==Buttons
    @FXML
    Button insertButton;
    @FXML
    Button updateButton;
    @FXML
    Button clearButton;
    @FXML
    Button addSubCategoryButton;
    
    //###=COMMON DATA=###
    InterestDAIF stub = (InterestDAIF) RemoteServices.getStub(BindingConsts.INTEREST_DA);
    private ObservableList<Interest> data = FXCollections.observableArrayList();
    //current selected record, top-level info, eager fetch
    private Interest currentItem;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    //###=SESSION AWARENESS=###
    @Override
    //sets visibility/editability of controllers according to user and session properties
    public void refreshGUI() {

        //check for role
        boolean isAdmin = SessionProperties.hasRole(Role.ADMIN);
        boolean hasRights = SessionProperties.isLogged();
        boolean isOwner = SessionProperties.isOwner(currentItem);

        //show/hide buttons
        insertButton.setVisible(hasRights);
        updateButton.setVisible(isAdmin || isOwner);
        clearButton.setVisible(hasRights);
        addSubCategoryButton.setVisible(hasRights);

        //enable/disable inputs
        name.setEditable(hasRights);
        descriptionArea.setEditable(hasRights);
    }
    
}
