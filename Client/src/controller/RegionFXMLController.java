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
import properties.SessionProperties;
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

    //###=COMMON CONTROLS=###
    @FXML
    TableView table;
    //==Info/Enput fields
    @FXML
    TextField name;
    //==Buttons
    @FXML
    Button insertButton;
    @FXML
    Button updateButton;
    @FXML
    Button clearButton;
    @FXML
    Button exitButton;

    //###=EXTRAS
    @FXML
    TableView detailsTable;
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

    //###=COMMON DATA=###
    RegionDAIF stub = (RegionDAIF) RemoteServices.getStub(BindingConsts.REGION_DA);
    private ObservableList<Region> data = FXCollections.observableArrayList();
    //current selected record, top-level info, eager fetch
    private Region currentItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        //I: Prepare the main table
        {
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

            ControllerUtils.prepareTable(table, colNames, fields, widths);
        }

        //II: Add listener (when the user clicks on a row, show info about it
        {
            table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Region>() {

                @Override
                public void changed(ObservableValue<? extends Region> observable,
                        Region oldValue, Region newValue) {
                    setCurrentItem(newValue);
                }
            });
        }

        //III: Bind data with the table and load data from server
        {
            table.setItems(data);
            try {
                refreshData();
            } catch (Throwable exc) {
                Utils.showError(exc.getMessage(), Client.getMainPageStage());
            }
        }
        
        detailsTable.setVisible(false);
    }

    //###=SESSION AWARENESS=###
    @Override
    //sets visibility/editability of controllers according to user and session properties
    public void refreshGUI() {

        //check for role
        boolean isAdmin = SessionProperties.hasRole(Role.ADMIN);

        //show/hide buttons
        insertButton.setVisible(isAdmin);
        updateButton.setVisible(isAdmin);
        clearButton.setVisible(isAdmin);

        //enable/disable inputs
        name.setEditable(isAdmin);
    }

    //###=COMMON METHODS=###
    @FXML
    //reset input fields
    private void handleClearAction(ActionEvent event) {
        setCurrentItem(null);
    }

    //run some checks to validate a prepared object
    private boolean checkInput(Region input) {
        for (Region cmp : data) {
            if (cmp.name.equals(input.name) && cmp.getID() != input.getID()) {
                Utils.showError("A region with the specified name already exists!", Client.getMainPageStage());
                return false;
            }
        }
        
        if(input.name.isEmpty()){
            Utils.showError("Name cannot be empty!", Client.getMainPageStage());
                return false;
        }
        
        return true;
    }

    //Sets a chosen item as current; pass null to reset
    private void setCurrentItem(Region region) {
        //I: set currentItem to point to the chosen one
        currentItem = region;

        if (currentItem == null) {
            //reset info
            name.setText("");
            //reset extra
            tCount.setText("N/A");
            uCount.setText("N/A");
            eCount.setText("N/A");
            
            prepareDetailsTable();
        } else {
            Throwable exc = null;
            Region resItem = null;
            try {
                //II: prepare filter
                RegionFilter filter = new RegionFilter();
                //pass ID of chosen item
                filter.ids.add(currentItem.getID());
                //raise appropriate fetch flags for extra info (if needed)
                filter.fetchEvents = true;
                filter.fetchTowns = true;
                filter.fetchUsers = true;

                //III: Fetch info and process result
                Result<Region> res = stub.fetchRegions(filter);
                //check for errors and mark them
                exc = res.getException();
                //if ok, try and mark the fetched item
                if (exc == null) {
                    List<Region> lst = res.getResult();
                    if (lst == null || lst.size() == 0) {
                        exc = new Exception("No data found!");
                    } else {
                        resItem = lst.get(0);
                    }
                }
            } catch (Exception e) {
                exc = e;
            }

            //IV: Show error OR display fetched data
            if (exc != null) {
                Utils.showError(exc.getMessage(), Client.getMainPageStage());
            } else {
                //set currentItem
                currentItem = resItem;
                //show info
                name.setText(currentItem.name);
                //extras
                tCount.setText("" + currentItem.townCount);
                uCount.setText("" + currentItem.userCount);
                eCount.setText("" + currentItem.eventCount);
                prepareDetailsTable();
            }
        }
    }

    //prepares an item according to user input and validates it
    private Region processInput() {

        //I: if we have a chosen item, copy ID and timeupd (if any) from it
        int id = 0;
        if (currentItem != null) {
            id = currentItem.getID();
        }
        //same with timeupd when exists
        
        Region reg = new Region(id);

        //II: read data from input controls
        reg.name = name.getText();

        //III: validate inputs
        if (false == checkInput(reg)) {
            return null;
        }

        return reg;
    }

    //###=CRU(D) METHODS=###
    //fetch and show info from server
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

    @FXML
    //Create
    private void handleInsertAction(ActionEvent event) {
        Throwable exc = null;
        try {
            //I: read input
            Region reg = processInput();
            if (reg == null) {
                return;
            }
            
            //II: if ok, continue and wrap it for RMI
            List<Region> lst = new ArrayList<Region>();
            lst.add(reg);
            //III: try to insert it and process result
            Result<Region> res = stub.insertRegion(lst, SessionProperties.getSession());
            exc = res.getException();
            if (exc == null) {
                //IV: if ok, refresh data
                refreshData();
            }
        } catch (Throwable e) {
            exc = e;
        }
        //V: Show any errors
        if (exc != null) {
            Utils.showError(exc.getMessage(), Client.getMainPageStage());
        }
    }

    @FXML
    //Update
    private void handleUpdateAction(ActionEvent event) {

        Throwable exc = null;
        try {
            Region reg = processInput();
            if (reg == null) {
                return;
            }
            List<Region> lst = new ArrayList<Region>();
            lst.add(reg);
            Result<Region> res = stub.updateRegion(lst, SessionProperties.getSession());
            exc = res.getException();
            if (exc == null) {
                refreshData();
            }
        } catch (Throwable e) {
            exc = e;
        }
        
        if (exc != null) {
            Utils.showError(exc.getMessage(), Client.getMainPageStage());
        }
    }

    //###=EXTRA METHODS=###
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

        if (detailsData.size() == 0) {
            detailsTable.setVisible(false);
        } else {
            ControllerUtils.prepareTable(detailsTable, colNames, fields, widths);
            detailsTable.setItems(detailsData);
            detailsTable.setVisible(true);
        }
    }
}
