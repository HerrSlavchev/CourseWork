/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import example.Client;
import images.ImageViewFactory;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.ConfirmationDelegatorIF;
import utils.Utils;

/**
 * FXML Controller class
 *
 * @author root
 */
public class MainPageFXMLController implements Initializable {

    @FXML
    BorderPane mainPane;
    
    @FXML
    Button loginB;
    
    private ImageView logged;
    private ImageView guest;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        logged = ImageViewFactory.getImageView("user_64_in.png", loginB.getMinHeight());
        guest =  ImageViewFactory.getImageView("user_64.png", loginB.getMinHeight());
        loginB.setGraphicTextGap(0);
        loginB.setGraphic(guest);
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
    
    @FXML
    private void handleRegisterAction(ActionEvent event) {
        try{
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("RegistrationFormFXML.fxml"));
            AnchorPane ap = (AnchorPane) loader.load();
            mainPane.setCenter(ap);
        }catch(Exception e){
            
        }
    }
    
    @FXML
    private void handleLoginAction(ActionEvent event) {
        
        try{
            FXMLLoader loginLoader = new FXMLLoader();
            loginLoader.setLocation(Client.class.getResource("LoginDialogFXML.fxml"));
            loginLoader.load();
            
            Parent root = loginLoader.getRoot();
            
            Stage loginDialog = new Stage(StageStyle.DECORATED);
            loginDialog.initModality(Modality.APPLICATION_MODAL);
            loginDialog.initOwner(Client.getMainPageStage());
            Scene scene = new Scene(root);
            
            LoginDialogFXMLController loginController = loginLoader.getController();
            loginController.setStage(loginDialog);
            
            loginDialog.setScene(scene);
            loginDialog.show();
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    public void setLogged(boolean b){
        if (b){
            loginB.setGraphic(logged);
        } else {
            loginB.setGraphic(guest);
        }
    }
    
    @FXML
    private void handleLogoutAction(ActionEvent event) {
        Utils.showConfirmation("Are you sure you want to exit?", Client.getMainPageStage(), new ConfirmationDelegatorIF() {

        @Override
        public void ok() {
            logout();
        }

        @Override
        public void cancel() {
            //no need to do anything
        }
        
    });
    }
    
    
    public void logout(){
        
    }
}
