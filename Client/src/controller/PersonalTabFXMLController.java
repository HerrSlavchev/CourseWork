/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dto.Result;
import dto.domain.Interest;
import dto.domain.User;
import dto.filters.InterestFilter;
import images.ImageViewFactory;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import properties.SessionProperties;
import services.BindingConsts;
import services.RemoteServices;
import services.server.InterestDAIF;
import utils.Utils;
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleNewInterestAction(ActionEvent event) {
        Client.getMainPageController().setCenterScene("InterestFXML.fxml");
    }

    public void loadInterests() {
        interests = new ArrayList<>();
        if (SessionProperties.isLogged()) {
            User user = SessionProperties.user;
            InterestFilter filter = new InterestFilter();
            filter.users.add(user);
            filter.fetchUsers = true;
            Throwable exc = null;
            try {
                Result<Interest> res = stubInterest.fetchInterests(filter);
                exc = res.getException();
                if (exc == null) {
                    interests = res.getResult();
                }
            } catch (Exception e) {
                exc = e;
            }
            if (exc != null) {
                exc.printStackTrace();
                Utils.showError(exc.getMessage(), Client.getMainPageStage());
            }
        }
        showInterests();
    }

    private void updateInterest(List<Interest> lst) {

        Throwable exc = null;
        try {
            Result<Interest> res = stubInterest.updateInterest(lst, SessionProperties.getSession());
            exc = res.getException();
        } catch (Exception e) {
            exc = e;
        }
        if (exc != null) {
            Utils.showError(exc.getMessage(), Client.getMainPageStage());
        } else {
            loadInterests();
        }
    }

    private void showInterests() {
        interestsBox.getChildren().clear();

        for (final Interest intr : interests) {
            HBox hb = new HBox(3);

            Button leaveButton = new Button();
            leaveButton.setPrefSize(22, 22);
            leaveButton.setMinSize(22, 22);
            leaveButton.setMaxSize(22, 22);
            leaveButton.setGraphic(ImageViewFactory.getImageView("x_sign.png", 22));
            leaveButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    intr.users.removeChild(SessionProperties.user);
                    List<Interest> lst = new ArrayList<>();
                    lst.add(intr);
                    updateInterest(lst);
                }
            });
            hb.getChildren().add(leaveButton);

            final boolean isNotified = intr.users.getOldChildren().get(0).notify;
            Button manageNotification = new Button();
            manageNotification.setPrefSize(22, 22);
            manageNotification.setMinSize(22, 22);
            manageNotification.setMaxSize(22, 22);
            String img = "";
            if (isNotified) {
                img = "eye_open_checkmark.png";
            } else {
                img = "eye_closed_cross.png";
            }
            manageNotification.setGraphic(ImageViewFactory.getImageView(img, 22));
            manageNotification.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    User usr = intr.users.getOldChildren().get(0);
                    usr.modified = true;
                    usr.notify = !isNotified;
                    List<Interest> lst = new ArrayList<>();
                    lst.add(intr);
                    updateInterest(lst);
                }
            });
            hb.getChildren().add(manageNotification);

            Label l = new Label(intr.name);
            l.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent t) {
                    MainPageFXMLController mpc = Client.getMainPageController();
                    mpc.setCenterScene("InterestFXML.fxml");
                    ((InterestFXMLController) mpc.getCenterController()).loadItem(intr.getID());
                }
            });
            hb.getChildren().add(l);
            
            interestsBox.getChildren().add(hb);
        }
    }

    @Override
    public void refreshGUI() {
        if (false == SessionProperties.isLogged()) {

            mainPane.setVisible(false);
        } else {

            mainPane.setVisible(true);
        }

        loadInterests();
    }

    private void showTables() {

    }
}
