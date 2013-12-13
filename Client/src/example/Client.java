/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package example;

import dto.session.Session;
import java.rmi.server.UnicastRemoteObject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.BindingConsts;
import services.RemoteServices;
import services.client.NotifiableIF;
import services.server.ClientManagerIF;

/**
 *
 * @author root
 */
public class Client extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //huc huc
        try {
            RemoteServices.init();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        Parent root = FXMLLoader.load(getClass().getResource("MainPageFXML.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
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
    
}
