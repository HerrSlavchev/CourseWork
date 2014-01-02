/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dto.Result;
import dto.domain.Region;
import dto.domain.Town;
import dto.filters.RegionFilter;
import dto.filters.TownFilter;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import properties.SessionProperties;
import services.BindingConsts;
import services.RemoteServices;
import services.server.RegionDAIF;
import services.server.TownDAIF;
import utils.Utils;
import view.Client;

/**
 * FXML Controller class
 *
 * @author Lubomir
 */
public class TownFXMLController implements Initializable, SessionAwareIF {

    @FXML
    TableView table;

    @FXML
    TextField name;
    @FXML
    ChoiceBox<Region> choiceBoxReg;

    @FXML
    Button insertButton;
    @FXML
    Button updateButton;
    @FXML
    Button clearButton;

    TownDAIF stub = (TownDAIF) RemoteServices.getStub(BindingConsts.TOWN_DA);
    private ObservableList<Town> data = FXCollections.observableArrayList();
    private ObservableList<Region> dataRegion = FXCollections.observableArrayList();
    //current selected record, top-level info, eager fetch
    private Town currentItem;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        {
            final String[] colNames = {
                "Name",
                "Region",
                "Users",
                "Events"
            };

            final String[] fields = {
                "name",
                "region",
                "userCount",
                "eventCount"
            };

            final double[] widths = {
                3,
                3,
                1,
                1
            };

            ControllerUtils.prepareTable(table, colNames, fields, widths);
        }
        //II: Add listener (when the user clicks on a row, show info about it
        {
            table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Town>() {

                @Override
                public void changed(ObservableValue<? extends Town> observable,
                        Town oldValue, Town newValue) {
                    setCurrentItem(newValue);
                }
            });
        }
        {
            table.setItems(data);
            try {
                refreshData();
                prepareControls();
            } catch (Throwable exc) {
                Utils.showError(exc.getMessage(), Client.getMainPageStage());
            }
        }

    }

    private void prepareControls() throws Throwable {
        choiceBoxReg.setItems(dataRegion);

        RegionDAIF regStub = (RegionDAIF) RemoteServices.getStub(BindingConsts.REGION_DA);
        Result<Region> res = regStub.fetchRegions(new RegionFilter());
        if (res.getException() != null) {
            throw res.getException();
        }

        if (res.getResult() != null) {
            dataRegion.clear();
            dataRegion.addAll(res.getResult());
        }

    }

    @Override
    public void refreshGUI() {
        boolean isAdmin = SessionProperties.hasRole(Role.ADMIN);

        insertButton.setVisible(isAdmin);
        updateButton.setVisible(isAdmin);
        clearButton.setVisible(isAdmin);

        name.setEditable(isAdmin);
        choiceBoxReg.setDisable(!isAdmin);
    }

    @FXML
    //reset input fields
    private void handleClearAction(ActionEvent event) {
        setCurrentItem(null);
    }

    private boolean checkInput(Town input) {

        if (input.name.isEmpty()) {
            Utils.showError("Name cannot be empty!", Client.getMainPageStage());
            return false;
        }

        if (input.region == null) {
            Utils.showError("Must choose region!", Client.getMainPageStage());
            return false;
        }

        for (Town cmp : data) {
            if (cmp.name.equals(input.name)
                    && cmp.getID() != input.getID()
                    && cmp.region.getID() == input.region.getID()) {
                Utils.showError("A town with the specified name already exists in this region!", Client.getMainPageStage());
                return false;
            }
        }

        return true;
    }

    private void setCurrentItem(Town town) {
        //I: set currentItem to point to the chosen one
        currentItem = town;

        if (currentItem == null) {
            //reset info
            name.setText("");

        } else {
            Throwable exc = null;
            Town resItem = null;
            try {
                //II: prepare filter
                TownFilter filter = new TownFilter();
                //pass ID of chosen item
                filter.ids.add(currentItem.getID());
                //raise appropriate fetch flags for extra info (if needed)
                filter.fetchEvents = true;
                filter.fetchUsers = true;

                //III: Fetch info and process result
                Result<Town> res = stub.fetchTown(filter);
                //check for errors and mark them
                exc = res.getException();
                //if ok, try and mark the fetched item
                if (exc == null) {
                    List<Town> lst = res.getResult();
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
                for (Region cmp : choiceBoxReg.getItems()) {
                    //System.out.println(cmp.getID() + "|" + currentItem.region.getID());
                    if (cmp.getID() == currentItem.region.getID()) {
                        choiceBoxReg.setValue(cmp);
                        break;
                    }
                }
            }
        }
    }

    //prepares an item according to user input and validates it
    private Town processInput() {

        //I: if we have a chosen item, copy ID and timeupd (if any) from it
        int id = 0;
        if (currentItem != null) {
            id = currentItem.getID();
        }
        //same with timeupd when exists

        Town town = new Town(id);

        //II: read data from input controls
        town.name = name.getText();
        town.region = choiceBoxReg.getValue();

        //III: validate inputs
        if (false == checkInput(town)) {
            return null;
        }

        return town;
    }

    private void refreshData() throws Throwable {
        Result<Town> res = stub.fetchTown(new TownFilter());
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
            Town town = processInput();
            if (town == null) {
                return;
            }

            //II: if ok, continue and wrap it for RMI
            List<Town> lst = new ArrayList<Town>();
            lst.add(town);
            //III: try to insert it and process result
            Result<Town> res = stub.insertTown(lst, SessionProperties.getSession());
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
            Town reg = processInput();
            if (reg == null) {
                return;
            }
            List<Town> lst = new ArrayList<Town>();
            lst.add(reg);
            Result<Town> res = stub.updateTown(lst, SessionProperties.getSession());
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
}
