/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import com.sun.glass.ui.Cursor;
import dto.domain.Interest;
import dto.filters.InterestFilter;
import dto.filters.UserFilter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import properties.SessionProperties;
import services.BindingConsts;
import services.RemoteServices;
import services.server.InterestDAIF;
import view.Client;

/**
 * FXML Controller class
 *
 * @author root
 */
public class PersonalTabFXMLController implements Initializable, SessionAwareIF {

    @FXML
    VBox conversationsBox;
    @FXML
    VBox interestsBox;
    @FXML
    VBox groupsBox;
    @FXML
    VBox eventsBox;
    
    @FXML
    GridPane mainPane;
    
    private List<Interest> interests = new ArrayList<Interest>();
    private InterestDAIF stubInterest;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        try {
            stubInterest = (InterestDAIF) RemoteServices.getStub(BindingConsts.INTEREST_DA);
        } catch (Exception e){
            e.printStackTrace();
        }
    }    
    
    @FXML
    private void handleNewInterestAction(ActionEvent event) {
        Client.getMainPageController().setCenterScene("InterestFXML.fxml");
    }

    @Override
    public void refreshGUI() {
        if(false == SessionProperties.isLogged()){
            interests = new ArrayList<>();
            mainPane.setVisible(false);
        } else {
            
            mainPane.setVisible(true);
        }
        
        showTables();
    }
    
    private void fetchInterests(){
        
    }
    
    private void showTables(){
        
    }
}
