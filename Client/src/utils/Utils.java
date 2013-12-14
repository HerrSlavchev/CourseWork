/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import controller.MainPageFXMLController;
import java.awt.Desktop.Action;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.xml.ws.Response;

/**
 *
 * @author root
 */
public class Utils {

    public static void showError(String text, Stage owner) {
        showMessage("ERROR!", text, owner);
    }

    public static void showMessage(String title, String text, Stage owner) {
        final Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        
        Button okButton = new Button("OK");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                dialogStage.close();
            }
        });
        dialogStage.initOwner(owner);
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setScene(new Scene(VBoxBuilder.create().
                children(new Text(text), new Text("\n"), okButton
                ).
                alignment(Pos.CENTER).padding(new Insets(5)).build()));
        dialogStage.show();
    }
}
