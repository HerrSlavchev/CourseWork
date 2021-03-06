/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package view;

import dao.ConnectionProvider;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import services.implementations.ServiceExposer;

/**
 *
 * @author root
 */
public class Server extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        try {
            ServiceExposer.exposeAll();
            ConnectionProvider.init();
        } catch(Exception e){
            e.printStackTrace();
            return;
        }
        
        Parent root = FXMLLoader.load(getClass().getResource("MainPageFXML.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("Server control");
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
