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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import services.BindingConsts;
import services.RemoteServices;
import services.server.RegionDAIF;

/**
 * FXML Controller class
 *
 * @author Lubomir
 */
public class CategoryFXMLController implements Initializable {

    @FXML
    TableView resultTable;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void handleButtonAction(ActionEvent event) {
        
        RegionDAIF stub = (RegionDAIF) RemoteServices.getStub(BindingConsts.REGION_DA);
        try {
            Result<Region> res = stub.fetchRegions(new RegionFilter());
            //System.out.println("result size: " + res.getResult().size());
            
            final ObservableList<Region> data = FXCollections.observableArrayList(res.getResult()); //convert result from server to "table model"
            //System.out.println("ds: " + data.size());
            resultTable.getColumns().clear();
            TableColumn idCol = new TableColumn("ID");
            TableColumn nameCol = new TableColumn("Name");
            
            idCol.setCellValueFactory(
                    new PropertyValueFactory<Region,Integer>("ID")
            );
            nameCol.setCellValueFactory(
                    new PropertyValueFactory<Region,String>("name")
            );
            
            resultTable.setItems(data);
            resultTable.getColumns().addAll(idCol, nameCol);
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Hallo Welt
        
    }
    
}
