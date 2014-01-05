/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dto.domain.PersistedDTO;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import utils.ChoiceDelegatorIF;
import utils.Utils;

/**
 * FXML Controller class
 *
 * @author root
 */
public class TableChoiceFXMLController implements Initializable {

    @FXML
    Label label;
    @FXML
    TableView table;

    
    private ObservableList<PersistedDTO> data = FXCollections.observableArrayList();

    private ChoiceDelegatorIF<? extends PersistedDTO> interf;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setText(String text) {
        label.setText(text);
    }

    public void setInterface(ChoiceDelegatorIF<? extends PersistedDTO> interf){
        this.interf = interf;
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void prepareTable(String[] colNames, String[] fields, double[] widths, SelectionMode slm) {
        ControllerUtils.prepareTable(table, colNames, fields, widths);
        table.setItems(data);
        table.getSelectionModel().setSelectionMode(slm);
    }

    public void setData(List<? extends PersistedDTO> items) {
        data.clear();
        data.addAll(items);
    }


    @FXML
    private void handleOKAction(ActionEvent event) {
        List selected = table.getSelectionModel().getSelectedItems();
        if (selected == null || selected.isEmpty()) {
            Utils.showError("Nothing selected!", stage);
            return;
        }
        
        interf.processSelected(selected);
        stage.close();
    }

    @FXML
    private void handleCancelAction(ActionEvent event) {
        stage.close();
    }

}
