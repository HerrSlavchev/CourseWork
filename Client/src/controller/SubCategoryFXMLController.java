/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import dto.Result;
import dto.domain.Category;
import dto.domain.Event;
import dto.domain.PersistedDTO;
import dto.domain.Region;
import dto.domain.SubCategory;
import dto.domain.Town;
import dto.domain.User;
import dto.filters.CategoryFilter;
import dto.filters.RegionFilter;
import dto.filters.SubCategoryFilter;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import properties.SessionProperties;
import services.BindingConsts;
import services.RemoteServices;
import services.server.CategoryDAIF;
import services.server.RegionDAIF;
import services.server.SubCategoryDAIF;
import services.server.TownDAIF;
import utils.Utils;
import view.Client;

/**
 * FXML Controller class
 *
 * @author Lubomir
 */
public class SubCategoryFXMLController implements Initializable, SessionAwareIF {
    
    @FXML
    TableView table;
    //==Info/Enput fields
    @FXML
    TextField name;
    @FXML
    ChoiceBox<Category> choiceBoxCat;
    //==Buttons
    @FXML
    Button insertButton;
    @FXML
    Button updateButton;
    @FXML
    Button clearButton;
    //==Table for descriptions
    @FXML
    TextArea descriptionArea;
    
    SubCategoryDAIF stub = (SubCategoryDAIF) RemoteServices.getStub(BindingConsts.SUBCATEGORY_DA);
    private ObservableList<SubCategory> data = FXCollections.observableArrayList();
    private ObservableList<Category> dataCategory = FXCollections.observableArrayList();
    //current selected record, top-level info, eager fetch
    private SubCategory currentItem;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //I: Prepare the main table
        {
            final String[] colNames = {
                "Name",
                "Categories",
                "Interests"
            };

            final String[] fields = {
                "name",
                "category",
                "interestCount"
            };

            final double[] widths = {
                3,
                2,
                2
            };

            ControllerUtils.prepareTable(table, colNames, fields, widths);
        }
        
        //II: Add listener (when the user clicks on a row, show info about it
        {
            table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<SubCategory>() {

                @Override
                public void changed(ObservableValue<? extends SubCategory> observable,
                        SubCategory oldValue, SubCategory newValue) {
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
    }
    
    private void prepareControls() throws Throwable {
        choiceBoxCat.setItems(dataCategory);

        CategoryDAIF regStub = (CategoryDAIF) RemoteServices.getStub(BindingConsts.CATEGORY_DA);
        Result<Category> res = regStub.fetchCategories(new CategoryFilter());
        if (res.getException() != null) {
            throw res.getException();
        }

        if (res.getResult() != null) {
            dataCategory.clear();
            dataCategory.addAll(res.getResult());
        }

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
        descriptionArea.setEditable(isAdmin);
        choiceBoxCat.setDisable(!isAdmin);
    }
    
    //###=COMMON METHODS=###
    @FXML
    //reset input fields
    private void handleClearAction(ActionEvent event) {
        setCurrentItem(null);
    }
    
    private boolean checkInput(SubCategory input) {

        if (input.name.isEmpty()) {
            Utils.showError("Name cannot be empty!", Client.getMainPageStage());
            return false;
        }
        
        if (input.category == null) {
            Utils.showError("Must choose region!", Client.getMainPageStage());
            return false;
        }
        
        for (SubCategory cmp : data) {
            if (cmp.name.equals(input.name)
                    && cmp.getID() != input.getID()) {
                Utils.showError("The category exists.", Client.getMainPageStage());
                return false;
            }
        }

        return true;
    }
    
    private void setCurrentItem(SubCategory subCategory) {
        //I: set currentItem to point to the chosen one
        currentItem = subCategory;

        if (currentItem == null) {
            //reset info
            name.setText("");
            choiceBoxCat.getSelectionModel().clearSelection();
            descriptionArea.setText("");
        } else {
            Throwable exc = null;
            SubCategory resItem = null;
            try {
                //II: prepare filter
                SubCategoryFilter filter = new SubCategoryFilter();
                //pass ID of chosen item
                filter.ids.add(currentItem.getID());
                //raise appropriate fetch flags for extra info (if needed)
                filter.fetchInterests = true;

                //III: Fetch info and process result
                Result<SubCategory> res = stub.fetchSubCategories(filter);
                //check for errors and mark them
                exc = res.getException();
                //if ok, try and mark the fetched item
                if (exc == null) {
                    List<SubCategory> lst = res.getResult();
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
                descriptionArea.setText(currentItem.description);
                //show info
                name.setText(currentItem.name);
                for (Category cmp : choiceBoxCat.getItems()) {
                    //System.out.println(cmp.getID() + "|" + currentItem.region.getID());
                    if (cmp.getID() == currentItem.category.getID()) {
                        choiceBoxCat.setValue(cmp);
                        break;
                    }
                }
            }
        }
    }
    
    //prepares an item according to user input and validates it
    private SubCategory processInput() {

        //I: if we have a chosen item, copy ID and timeupd (if any) from it
        int id = 0;
        if (currentItem != null) {
            id = currentItem.getID();
        }
        //same with timeupd when exists

        SubCategory subCategory = new SubCategory(id);

        //II: read data from input controls
        subCategory.name = name.getText();
        subCategory.category = choiceBoxCat.getValue();
        subCategory.description = descriptionArea.getText();

        //III: validate inputs
        if (false == checkInput(subCategory)) {
            return null;
        }

        return subCategory;
    }
    
    //###=CRU(D) METHODS=###
    //fetch and show info from server
    private void refreshData() throws Throwable {
        Result<SubCategory> res = stub.fetchSubCategories(new SubCategoryFilter());
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
            SubCategory subCategory = processInput();
            if (subCategory == null) {
                return;
            }

            //II: if ok, continue and wrap it for RMI
            List<SubCategory> lst = new ArrayList<SubCategory>();
            lst.add(subCategory);
            //III: try to insert it and process result
            Result<SubCategory> res = stub.insertSubCategory(lst, SessionProperties.getSession());
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
            SubCategory subCategory = processInput();
            if (subCategory == null) {
                return;
            }
            List<SubCategory> lst = new ArrayList<SubCategory>();
            lst.add(subCategory);
            Result<SubCategory> res = stub.updateSubCategory(lst, SessionProperties.getSession());
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
