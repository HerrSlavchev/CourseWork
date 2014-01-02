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
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TitledPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
import services.server.UserDAIF;
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

    private SessionAwareIF centerController;

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

    }
    
    @FXML
    private void handleRegionAction(ActionEvent event) {
        setCenterScene("RegionFXML.fxml");
    }
    
    @FXML
    private void handleTownAction(ActionEvent event) {
        setCenterScene("TownFXML.fxml");
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
        try {
            FXMLLoader loader = new FXMLLoader(Client.class.getResource("RegistrationFormFXML.fxml"));
            AnchorPane ap = (AnchorPane) loader.load();
            mainPane.setCenter(ap);
        } catch (Exception e) {

        }
    }

    public void setLogged(boolean b) {
        if (false == b) {
            SessionProperties.killSession();
            SessionProperties.user = null;
        }
        refreshGUI();
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
            personalAcc.setVisible(false);
        } else {
            loginB.setGraphic(loggedIV);
            loginB.getItems().clear();
            loginB.getItems().addAll(editMI, logoutMI);
            Client.getMainPageStage().setTitle(SessionProperties.user.lName + ", " + SessionProperties.user.fName);
            personalAcc.setVisible(true);
        }
        if (centerController != null){
            centerController.refreshGUI();
        }
    }

    private void setCenterScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Client.class.getResource(fxmlFile));
            Pane pane = (Pane) loader.load();
            Object obj = loader.getController();
            if (obj instanceof SessionAwareIF) {
                SessionAwareIF sessionAwareIF = (SessionAwareIF) obj;
                centerController = sessionAwareIF;
                centerController.refreshGUI();
            } else {
                centerController = null;
            }
            mainPane.setCenter(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
