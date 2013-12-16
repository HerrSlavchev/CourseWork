/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import controller.ConfirmationFXMLController;
import example.Client;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

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
        dialogStage.setResizable(false);
        dialogStage.setScene(new Scene(VBoxBuilder.create().
                children(new Text(text), new Text("\n"), okButton
                ).
                alignment(Pos.CENTER).padding(new Insets(5)).build()));
        dialogStage.show();
    }

    public static void showConfirmation(String text, Stage owner, ConfirmationDelegatorIF interf) {
        showConfirmation("WARNING!", text, owner, interf);
    }

    public static void showConfirmation(String title, String text, Stage owner, ConfirmationDelegatorIF interf) {

        try {
            FXMLLoader confirmationLoader = new FXMLLoader();
            confirmationLoader.setLocation(Client.class.getResource("ConfirmationFXML.fxml"));
            confirmationLoader.load();

            Parent root = confirmationLoader.getRoot();
            Stage dialogStage = new Stage(StageStyle.DECORATED);
            dialogStage.setTitle(title);
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(owner);
            Scene scene = new Scene(root);

            ConfirmationFXMLController confirmationController = confirmationLoader.getController();
            confirmationController.setStage(dialogStage);
            confirmationController.setInterface(interf);
            confirmationController.setText(text);
            
            dialogStage.setScene(scene);
            dialogStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
