/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dto.Result;
import dto.domain.Event;
import dto.domain.PersistedDTO;
import dto.domain.Region;
import dto.domain.Town;
import dto.domain.User;
import dto.filters.RegionFilter;
import dto.rolemanagement.Role;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    TableView detailsTable;
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

    @FXML
    Label tCount;
    @FXML
    Label uCount;
    @FXML
    Label eCount;

    private static final int DETAILS_NONE = -1;
    private static final int DETAILS_TOWN = 1;
    private static final int DETAILS_USER = 2;
    private static final int DETAILS_EVENT = 3;

    private int detailsMode = DETAILS_NONE;

    RegionDAIF stub = (RegionDAIF) RemoteServices.getStub(BindingConsts.REGION_DA);

    private ObservableList<Region> data = FXCollections.observableArrayList();

    //current selected record, top-level info, eager fetch
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

        ControllerUtils.setUpTable(table, colNames, fields, widths);
        ControllerUtils.setUpTable(table, colNames, fields, widths);

        table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Region>() {

            @Override
            public void changed(ObservableValue<? extends Region> observable,
                    Region oldValue, Region newValue) {
                setCurrentItem(newValue);
            }
        });
        table.setItems(data);
        try {
            refreshData();
        } catch (Throwable exc) {
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
    private void handleTownAction(ActionEvent event) {
        detailsMode = DETAILS_TOWN;
        prepareDetailsTable();
    }

    @FXML
    private void handleUserAction(ActionEvent event) {
        detailsMode = DETAILS_USER;
        prepareDetailsTable();
    }

    @FXML
    private void handleEventAction(ActionEvent event) {
        detailsMode = DETAILS_EVENT;
        prepareDetailsTable();
    }

    private void prepareDetailsTable() {
        
        if (detailsMode == DETAILS_NONE || currentItem == null) {
            detailsTable.setVisible(false);
            return;
        }

        ObservableList detailsData = FXCollections.observableArrayList();

        String[] colNames = new String[0];
        String[] fields = new String[0];
        double[] widths = new double[0];

        //town details
        String[] colNamesTown = {
            "Name",};

        String[] fieldsTown = {
            "name",};

        double[] widthsTown = {
            1
        };

        //user details
        String[] colNamesUser = {
            "First Name",
            "Last Name"
        };

        String[] fieldsUser = {
            "f_name",
            "l_name"
        };

        double[] widthsUser = {
            1,
            1
        };

        //event details
        String[] colNamesEvent = {
            "Name",
            "From",
            "To"
        };

        String[] fieldsEvent = {
            "name",
            "dateFrom",
            "dateTo"
        };

        double[] widthsEvent = {
            3,
            1,
            1
        };

        switch (detailsMode) {
            case DETAILS_TOWN:
                detailsData.addAll(currentItem.towns);
                colNames = colNamesTown;
                fields = fieldsTown;
                widths = widthsTown;
                break;
            case DETAILS_USER:
                detailsData.addAll(currentItem.users);
                colNames = colNamesUser;
                fields = fieldsUser;
                widths = widthsUser;
                break;
            case DETAILS_EVENT:
                detailsData.addAll(currentItem.events);
                colNames = colNamesEvent;
                fields = fieldsEvent;
                widths = widthsEvent;
                break;
            default:
                detailsTable.setVisible(false);
                return;
        }
        
        if(detailsData.size() == 0){
            detailsTable.setVisible(false);
        } else {
            ControllerUtils.setUpTable(detailsTable, colNames, fields, widths);
            detailsTable.setItems(detailsData);
        }
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
        }
    }

    @FXML
    private void handleClearAction(ActionEvent event) {
        setCurrentItem(null);
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

    private void setCurrentItem(Region region) {
        currentItem = region;

        if (currentItem == null) {
            name.setText("");
            tCount.setText("N/A");
            uCount.setText("N/A");
            eCount.setText("N/A");
            detailsMode = DETAILS_NONE;
        } else {
            Throwable exc = null;
            try {
                RegionFilter filter = new RegionFilter();
                filter.ids.add(currentItem.getID());
                filter.fetchEvents = true;
                filter.fetchTowns = true;
                filter.fetchUsers = true;
                Result<Region> res = stub.fetchRegions(filter);
                List<Region> lst = res.getResult();
                if (lst == null || lst.size() == 0){
                    exc = new Exception("No data found!");
                } else {
                    currentItem = lst.get(0);
                }
                exc = res.getException();
            } catch (Exception e){
                exc = e;
            }
            
            name.setText(currentItem.name);
            tCount.setText("" + currentItem.townCount);
            uCount.setText("" + currentItem.userCount);
            eCount.setText("" + currentItem.eventCount);
        }
        prepareDetailsTable();
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
        setCurrentItem(null);
    }
}
