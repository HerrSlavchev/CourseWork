/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import view.Client;
import images.ImageViewFactory;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import properties.SessionProperties;
import services.BindingConsts;
import services.RemoteServices;
import services.server.ClientManagerIF;
import utils.ConfirmationDelegatorIF;
import utils.Utils;

/**
 * FXML Controller class
 *
 * @author root
 */
public class MainPageFXMLController implements Initializable, SessionAwareIF {

    @FXML
    BorderPane mainPane;
    @FXML
    MenuButton loginB;
    @FXML
    VBox personalVB;
    
    private Initializable centerController;

    private ImageView loggedIV;
    private ImageView guestIV;
    private MenuItem loginMI;
    private MenuItem registerMI;
    private MenuItem editMI;
    private MenuItem logoutMI;

    private boolean loggingOut = false;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //Client.getMainPageStage().setTitle("GUEST");
        loggedIV = ImageViewFactory.getImageView("user_64_in.png", loginB.getMinHeight());
        guestIV = ImageViewFactory.getImageView("user_64.png", loginB.getMinHeight());
        loginB.setGraphic(guestIV);

        loginMI = new MenuItem("Login");
        loginMI.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                handleLoginAction();
            }
        });

        registerMI = new MenuItem("Register");
        registerMI.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                handleRegisterAction();
            }
        });

        editMI = new MenuItem("Profile");
        editMI.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                handleEditAction();
            }
        });

        logoutMI = new MenuItem("Logout");
        logoutMI.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
                handleLogoutAction();
            }
        });

        loginB.getItems().clear();
        loginB.getItems().addAll(loginMI, registerMI);

        setPersonalTab("PersonalTabFXML.fxml");
    }

    @FXML
    private void handleInterestAction(ActionEvent event) {

    }

    @FXML
    private void handleGroupAction(ActionEvent event) {

    }

    @FXML
    private void handleEventAction(ActionEvent event) {

    }

    @FXML
    private void handleUserAction(ActionEvent event) {

    }

    @FXML
    private void handleCategoryAction(ActionEvent event) {
        setCenterScene("CategoryFXML.fxml");
    }

    @FXML
    private void handleSubCategoryAction(ActionEvent event) {
        setCenterScene("SubCategoryFXML.fxml");
    }

    @FXML
    private void handleRegionAction(ActionEvent event) {
        setCenterScene("RegionFXML.fxml");
    }

    @FXML
    private void handleTownAction(ActionEvent event) {
        setCenterScene("TownFXML.fxml");
    }

    private void handleLoginAction() {
        try {
            FXMLLoader loginLoader = new FXMLLoader();
            loginLoader.setLocation(Client.class.getResource("LoginDialogFXML.fxml"));
            loginLoader.load();

            Parent root = loginLoader.getRoot();

            Stage loginDialog = new Stage(StageStyle.DECORATED);
            loginDialog.setTitle("LOGIN");
            loginDialog.initModality(Modality.APPLICATION_MODAL);
            loginDialog.initOwner(Client.getMainPageStage());
            Scene scene = new Scene(root);

            LoginDialogFXMLController loginController = loginLoader.getController();
            loginController.setStage(loginDialog);

            loginDialog.setScene(scene);
            loginDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRegisterAction() {
        setCenterScene("RegistrationFormFXML.fxml");
    }

    private void handleLogoutAction() {
        Utils.showConfirmation("Are you sure you want to logout?", Client.getMainPageStage(), new ConfirmationDelegatorIF() {

            @Override
            public void ok() {
                try {
                    ClientManagerIF clm = (ClientManagerIF) RemoteServices.getStub(BindingConsts.CLIENT_MANAGER);
                    clm.removeClient(SessionProperties.getSession());
                } catch (RemoteException re) {
                    Utils.showError("Error connecting to server", Client.getMainPageStage());
                }
            }

            @Override
            public void cancel() {
            }
        });
    }

    private void handleEditAction() {
        setCenterScene("RegistrationFormFXML.fxml");
    }

    public boolean isLoggingOut(){
        return loggingOut;
    }
    
    public void setLogged(boolean b) {
        if (false == b) {
            loggingOut = true;
            SessionProperties.killSession();
            SessionProperties.user = null;
        }
        refreshGUI();
        loggingOut = false;
    }

    @FXML
    private void handleLogoutAction(ActionEvent event) {
        Utils.showConfirmation("Are you sure you want to exit?", Client.getMainPageStage(), new ConfirmationDelegatorIF() {

            @Override
            public void ok() {
                setLogged(false);
            }

            @Override
            public void cancel() {
            }
        });
    }

    @Override
    public void refreshGUI() {
        if (false == SessionProperties.isLogged()) {
            loginB.setGraphic(guestIV);
            loginB.getItems().clear();
            loginB.getItems().addAll(loginMI, registerMI);
            Client.getMainPageStage().setTitle("GUEST");
        } else {
            loginB.setGraphic(loggedIV);
            loginB.getItems().clear();
            loginB.getItems().addAll(editMI, logoutMI);
            Client.getMainPageStage().setTitle(SessionProperties.user.lName + ", " + SessionProperties.user.fName);
        }
        if (centerController != null && centerController instanceof SessionAwareIF) {
            ((SessionAwareIF) centerController).refreshGUI();
        }
        if (Client.getPersonalTabController() != null) {
            Client.getPersonalTabController().refreshGUI();
        }
    }

    public void setCenterScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Client.class.getResource(fxmlFile));
            Pane pane = (Pane) loader.load();

            centerController = loader.getController();

            mainPane.setCenter(pane);
            if (centerController instanceof SessionAwareIF) {
                ((SessionAwareIF) centerController).refreshGUI();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPersonalTab(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Client.class.getResource(fxmlFile));
            Pane pane = (Pane) loader.load();
            
            Client.setPersonalTabController((PersonalTabFXMLController)loader.getController());

            personalVB.getChildren().clear();
            personalVB.getChildren().add(pane);
            Client.getPersonalTabController().refreshGUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
