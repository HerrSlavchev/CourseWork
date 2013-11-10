/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dto.Result;
import dto.domain.Region;
import dto.filters.RegionFilter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import services.BindingConsts;
import services.RemoteServices;
import services.server.RegionsDAIF;

/**
 *
 * @author root
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Label label;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");

        RegionsDAIF stub = (RegionsDAIF) RemoteServices.getStub(BindingConsts.REGION_DA);
        try {
            Result<Region> res = stub.fetchRegions(new RegionFilter());
            System.out.println("result size: " + res.getResult().size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
