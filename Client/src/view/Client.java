/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.MainPageFXMLController;
import controller.PersonalTabFXMLController;
import controller.SessionAwareIF;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.RemoteServices;

/**
 *
 * @author root
 */
public class Client extends Application {

    private static Stage mainPageStage = null;
    private static MainPageFXMLController mainPageController = null;
    private static PersonalTabFXMLController personalTabController = null;
    
    private boolean askBeforeExit = true;

    public void setAskBeforeExit(boolean b) {
        askBeforeExit = b;
    }

    public static Stage getMainPageStage() {
        return mainPageStage;
    }

    public static MainPageFXMLController getMainPageController() {
        return mainPageController;
    }
    
    public static void setPersonalTabController(PersonalTabFXMLController controller){
        personalTabController = controller;
    }
    
    public static PersonalTabFXMLController getPersonalTabController() {
        return personalTabController;
    }

    @Override
    public void start(Stage stage) throws Exception {
        
        try {
            RemoteServices.init();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        FXMLLoader fl = new FXMLLoader();
        fl.setLocation(getClass().getResource("MainPageFXML.fxml"));
        fl.load();
        Parent root = fl.getRoot();
        mainPageController = (MainPageFXMLController) fl.getController();
        mainPageStage = stage;
        stage.setTitle("GUEST");
        Scene scene = new Scene(root);
        stage.setScene(scene);
        //mainPageController.setLogged(false);
        stage.show();
        
        if (stage instanceof SessionAwareIF) {
            SessionAwareIF sessionAwareIF = (SessionAwareIF) stage;
            sessionAwareIF.refreshGUI();
        }
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        if (askBeforeExit) {
            //prompt user and return
        }
    }
}
