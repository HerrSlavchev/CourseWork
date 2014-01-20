/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package controller;

import dto.Result;
import dto.domain.Category;
import dto.filters.CategoryFilter;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import properties.SessionProperties;
import services.BindingConsts;
import services.RemoteServices;
import services.server.CategoryDAIF;
import utils.Utils;
import view.Client;

/**
 * FXML Controller class
 *
 * @author Lubomir
 */
public class CategoryFXMLController implements Initializable, SessionAwareIF {

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
    //==Table for descriptions
    @FXML
    TextArea descriptionArea;
    //###=COMMON DATA=###
    CategoryDAIF stub = (CategoryDAIF) RemoteServices.getStub(BindingConsts.CATEGORY_DA);
    private ObservableList<Category> data = FXCollections.observableArrayList();
    //current selected record, top-level info, eager fetch
    private Category currentItem;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //I: Prepare the main table
        {
            final String[] colNames = {
                "Name",
                "Subcategories",
                "Interests"
            };

            final String[] fields = {
                "name",
                "subCategoryCount",
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
            table.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Category>() {

                @Override
                public void changed(ObservableValue<? extends Category> observable,
                        Category oldValue, Category newValue) {
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
    }
    
    //###=COMMON METHODS=###
    @FXML
    //reset input fields
    private void handleClearAction(ActionEvent event) {
        setCurrentItem(null);
    }

    private boolean checkInput(Category input) {

        if (input.getName().isEmpty()) {
            Utils.showError("Name cannot be empty!", Client.getMainPageStage());
            return false;
        }

        for (Category cmp : data) {
            if (cmp.getName().equals(input.getName())
                    && cmp.getID() != input.getID()) {
                Utils.showError("The category exists.", Client.getMainPageStage());
                return false;
            }
        }

        return true;
    }
    
    //Sets a chosen item as current; pass null to reset
    private void setCurrentItem(Category category) {
        //I: set currentItem to point to the chosen one
        currentItem = category;

        if (currentItem == null) {
            //reset info
            name.setText("");
            descriptionArea.setText("");
            
        } else {
            Throwable exc = null;
            Category resItem = null;
            try {
                //II: prepare filter
                CategoryFilter filter = new CategoryFilter();
                //pass ID of chosen item
                filter.ids.add(currentItem.getID());
                //raise appropriate fetch flags for extra info (if needed)
                filter.fetchInterests = true;
                filter.fetchSubCategories = true;
                
                //III: Fetch info and process result
                Result<Category> res = stub.fetchCategories(filter);
                //check for errors and mark them
                exc = res.getException();
                //if ok, try and mark the fetched item
                if (exc == null) {
                    List<Category> lst = res.getResult();
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
                name.setText(currentItem.getName());
                descriptionArea.setText(currentItem.getDescription());
            }
        }
    }
    
    //prepares an item according to user input and validates it
    private Category processInput() {

        //I: if we have a chosen item, copy ID and timeupd (if any) from it
        int id = 0;
        if (currentItem != null) {
            id = currentItem.getID();
        }
        //same with timeupd when exists
        
        Category cat = new Category(id);

        //II: read data from input controls
        cat.setName(name.getText());
        cat.setDescription(descriptionArea.getText());
        //III: validate inputs
        if (false == checkInput(cat)) {
            return null;
        }

        return cat;
    }
    
    //###=CRU(D) METHODS=###
    //fetch and show info from server
    private void refreshData() throws Throwable {
        Result<Category> cat = stub.fetchCategories(new CategoryFilter());
        if (cat.getException() != null) {
            throw cat.getException();
        }
        
        data.clear();
        if (cat.getResult() != null) {
            data.addAll(cat.getResult());
        }
        
        handleClearAction(null);
    }
    
    @FXML
    //Create
    private void handleInsertAction(ActionEvent event) {
        Throwable exc = null;
        try {
            //I: read input
            Category cat = processInput();
            if (cat == null) {
                return;
            }
            
            //II: if ok, continue and wrap it for RMI
            List<Category> lst = new ArrayList<Category>();
            lst.add(cat);
            //III: try to insert it and process result
            Result<Category> res = stub.insertCategory(lst, SessionProperties.getSession());
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
            Category cat = processInput();
            if (cat == null) {
                return;
            }
            List<Category> lst = new ArrayList<Category>();
            lst.add(cat);
            Result<Category> res = stub.updateCategory(lst, SessionProperties.getSession());
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
