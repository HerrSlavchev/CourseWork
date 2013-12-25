/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dto.filters.UserFilter;
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
import javafx.scene.control.Accordion;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import properties.Properties;
import services.BindingConsts;
import services.RemoteServices;
import services.implementations.ServiceExposer;
import services.server.ClientManagerIF;
import services.server.UserDAIF;
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
    MenuButton loginB;
    @FXML
    Accordion personalAcc;
    @FXML
    TitledPane conversationsPane;
    @FXML
    VBox conversationsBox;
    @FXML
    TitledPane interestsPane;
    @FXML
    VBox interestsBox;
    @FXML
    TitledPane groupsPane;
    @FXML
    VBox groupsBox;
    @FXML
    TitledPane eventsPane;
    @FXML
    VBox eventsBox;
    
    private ImageView loggedIV;
    private ImageView guestIV;
    private MenuItem loginMI;
    private MenuItem registerMI;
    private MenuItem editMI;
    private MenuItem logoutMI;

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
        
        personalAcc.setVisible(false);
    }

    @FXML
    private void handleTestAction(ActionEvent event) {
        try {
            UserDAIF intf = (UserDAIF) RemoteServices.getStub(BindingConsts.USER_DA);
            UserFilter fil = new UserFilter();
            fil.fetchConversations = true;
            fil.fetchEvents = true;
            fil.fetchFullPersonalData = true;
            fil.fetchGroups = true;
            fil.fetchInterests = true;
            fil.fetchTowns = true;
            intf.fetchUsers(fil);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleButton1Action(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("CategoryFXML.fxml"));
            AnchorPane ap = (AnchorPane) loader.load();
            mainPane.setCenter(ap);

        } catch (Exception e) {

        }
    }

    @FXML
    private void handleButton2Action(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("RegionFXML.fxml"));
            AnchorPane ap = (AnchorPane) loader.load();
            mainPane.setCenter(ap);

        } catch (Exception e) {

        }
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
        try {
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("RegistrationFormFXML.fxml"));
            AnchorPane ap = (AnchorPane) loader.load();
            mainPane.setCenter(ap);
        } catch (Exception e) {

        }
    }

    private void handleLogoutAction() {
        Utils.showConfirmation("Are you sure you want to logout?", Client.getMainPageStage(), new ConfirmationDelegatorIF() {

            @Override
            public void ok() {
                try {
                ClientManagerIF clm = (ClientManagerIF) RemoteServices.getStub(BindingConsts.CLIENT_MANAGER);
                clm.removeClient(Properties.getSession());
                } catch (RemoteException re){
                    Utils.showError("Error connecting to server", Client.getMainPageStage());
                }
            }

            @Override
            public void cancel() {
            }
        });
    }

    private void handleEditAction() {
        try {
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("RegistrationFormFXML.fxml"));
            AnchorPane ap = (AnchorPane) loader.load();
            mainPane.setCenter(ap);
        } catch (Exception e) {

        }
    }

    public void setLogged(boolean b) {
        if (b) {
            login();
        } else {
            logout();
        }
    }

    private void logout() {
        Properties.killSession();
        loginB.setGraphic(guestIV);
        loginB.getItems().clear();
        loginB.getItems().addAll(loginMI, registerMI);
        Client.getMainPageStage().setTitle("GUEST");
        Properties.user = null;
        
        personalAcc.setVisible(false);
    }

    private void login() {
        loginB.setGraphic(loggedIV);
        loginB.getItems().clear();
        loginB.getItems().addAll(editMI, logoutMI);
        Client.getMainPageStage().setTitle(Properties.user.lName + ", " + Properties.user.fName);
        
        personalAcc.setVisible(true);
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
                //no need to do anything
            }
        });
    }

}
