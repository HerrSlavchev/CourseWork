/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import example.Client;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

/**
 * FXML Controller class
 *
 * @author root
 */
public class MainPageFXMLController implements Initializable {

    @FXML
    BorderPane mainPane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void handleButton1Action(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("CategoryFXML.fxml"));
            AnchorPane ap = (AnchorPane) loader.load();
            mainPane.setCenter(ap);
            
        }catch(Exception e){
            
        }
    }
    
    @FXML
    private void handleButton2Action(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("RegionFXML.fxml"));
            AnchorPane ap = (AnchorPane) loader.load();
            mainPane.setCenter(ap);
            
        }catch(Exception e){
            
        }
    }
}
