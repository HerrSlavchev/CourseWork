/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dto.Result;
import dto.domain.Region;
import dto.domain.User;
import dto.filters.RegionFilter;
import dto.rolemanagement.Role;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import properties.Properties;
import services.BindingConsts;
import services.RemoteServices;
import services.server.RegionDAIF;
import utils.Utils;
import view.Client;

/**
 * FXML Controller class
 *
 * @author Lubomir
 */
public class RegionFXMLController implements Initializable, SessionAwareIF {

    @FXML
    TableView table;
    @FXML
    Button insertButton;
    @FXML
    Button updateButton;
    @FXML
    Button clearButton;
    @FXML
    Button exitButton;
    @FXML
    TextField name;

    RegionDAIF stub = (RegionDAIF) RemoteServices.getStub(BindingConsts.REGION_DA);

    private ObservableList<Region> data = FXCollections.observableArrayList();
    private Region currentItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        final String[] colNames = {
            "Name",
            "Towns",
            "Users",
            "Events"
        };
        
        final String[] fields = {
            "name",
            "townCount",
            "userCount",
            "eventCount"
        };
        
        final double[] widths = {
            3,
            1,
            1,
            1
        };
        final Class[] propTypes = {
            String.class,
            Integer.class,
            Integer.class,
            Integer.class
        };
        
        
        
        ControllerUtils.setUpTable(table, colNames, fields, widths);
        ControllerUtils.setUpTable(table, colNames, fields, widths);
        table.setItems(data);
        try {
            refreshData();
        } catch(Throwable exc){
            Utils.showError(exc.getMessage(), Client.getMainPageStage());
        }
    }

    private boolean checkInformation(Region reg) {
        return true;
    }

    private Region getFromForm() {

        int id = 0;
        if (currentItem != null) {
            id = currentItem.getID();
        }

        Region reg = new Region(id);
        reg.name = name.getText();

        if (false == checkInformation(reg)) {
            return null;
        }
        return reg;
    }

    @FXML
    private void handleInsertAction(ActionEvent event) {
        RegionDAIF stub = (RegionDAIF) RemoteServices.getStub(BindingConsts.REGION_DA);
        Throwable exc = null;
        try {
            Region reg = getFromForm();
            if (reg == null) {
                return;
            }
            List<Region> lst = new ArrayList<Region>();
            lst.add(reg);
            Result<Region> res = stub.insertRegion(lst, Properties.getSession());
            exc = res.getException();
            refreshData();
        } catch (Throwable e) {
            exc = e;
        }
        if (exc != null) {
            Utils.showError(exc.getMessage(), Client.getMainPageStage());
        }
    }

    @FXML
    private void handleUpdateAction(ActionEvent event) {

        Throwable exc = null;
        try {
            Region reg = getFromForm();
            if (reg == null) {
                return;
            }
            List<Region> lst = new ArrayList<Region>();
            lst.add(reg);
            Result<Region> res = stub.updateRegion(lst, Properties.getSession());
            exc = res.getException();
            refreshData();
        } catch (Throwable e) {
            exc = e;
        }
        if (exc != null) {
            Utils.showError(exc.getMessage(), Client.getMainPageStage());
        } else {
            Utils.showMessage("SUCCESS!", "Successful action!", Client.getMainPageStage());
        }
    }

    @FXML
    private void handleClearAction(ActionEvent event) {
        currentItem = null;
        name.setText("");
    }

    @Override
    public void refreshGUI() {
        User user = null;
        if (Properties.isLogged()) {
            user = Properties.user;
        }

        boolean isAdmin = (user != null && user.role.compareTo(Role.ADMIN) == 0);

        insertButton.setVisible(isAdmin);
        updateButton.setVisible(isAdmin);
        clearButton.setVisible(isAdmin);

        name.setEditable(isAdmin);
    }

    private void refreshData() throws Throwable {
        Result<Region> res = stub.fetchRegions(new RegionFilter());
        if (res.getException() != null) {
            throw res.getException();
        }
        data.clear();
        if (res.getResult() != null) {
            data.addAll(res.getResult());
        }
        handleClearAction(null);
    }
}
