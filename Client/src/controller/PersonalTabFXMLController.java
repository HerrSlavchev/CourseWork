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

    private void removeInterest(List<Interest> lst) {

        Throwable exc = null;
        try {
            Result<Interest> res = stubInterest.deleteInterest(lst, SessionProperties.getSession());
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

            Button b = new Button();
            b.setPrefSize(22, 22);
            b.setMinSize(22, 22);
            b.setMaxSize(22, 22);
            b.setGraphic(ImageViewFactory.getImageView("x_sign.png", 22));
            b.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    List<Interest> lst = new ArrayList<Interest>();
                    lst.add(intr);
                    removeInterest(lst);
                }
            });
            hb.getChildren().add(b);

            Label l = new Label(intr.name);
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
