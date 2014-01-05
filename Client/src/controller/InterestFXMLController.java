/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dto.Result;
import dto.domain.ChildrenManager;
import dto.domain.Interest;
import dto.domain.SubCategory;
import dto.filters.InterestFilter;
import dto.filters.SubCategoryFilter;
import dto.rolemanagement.Role;
import images.ImageViewFactory;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import properties.SessionProperties;
import services.BindingConsts;
import services.RemoteServices;
import services.server.InterestDAIF;
import services.server.SubCategoryDAIF;
import utils.ChoiceDelegatorIF;
import utils.Utils;
import view.Client;

/**
 * FXML Controller class
 *
 * @author Lubomir
 */
public class InterestFXMLController implements Initializable, SessionAwareIF {

    //==Info/Input fields
    @FXML
    TextField name;
    //==Description area
    @FXML
    TextArea descriptionArea;
    //==The scroll pane/VBox
    @FXML
    VBox vboxSubCategories;
    //==Buttons
    @FXML
    Button insertButton;
    @FXML
    Button updateButton;
    @FXML
    Button clearButton;
    @FXML
    Button addSubCategoryButton;

    //###=COMMON DATA=###
    InterestDAIF stub = (InterestDAIF) RemoteServices.getStub(BindingConsts.INTEREST_DA);

    SubCategoryDAIF stubSubCategory = (SubCategoryDAIF) RemoteServices.getStub(BindingConsts.SUBCATEGORY_DA);
    private List<SubCategory> dataSubCategories = new ArrayList<>();

    //current selected record, top-level info, eager fetch
    private Interest currentItem = new Interest(0);
    private ChildrenManager<SubCategory> chm = new ChildrenManager<>();

    private ImageView plusSign;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        plusSign = ImageViewFactory.getImageView("plus_sign.png", 22);
        addSubCategoryButton.setGraphic(plusSign);
        try {
            loadData();
        } catch (Throwable exc) {
            Utils.showError(exc.getMessage(), Client.getMainPageStage());
        }
    }

    //load secondary data like subcategories we can choose from
    private void loadData() throws Throwable {

        Throwable exc = null;
        Result<SubCategory> resSBC = stubSubCategory.fetchSubCategories(new SubCategoryFilter());

        exc = resSBC.getException();
        if (exc == null) {
            dataSubCategories = resSBC.getResult();
            if (dataSubCategories == null || dataSubCategories.isEmpty()) {
                exc = new Throwable("No subcategories found!");
            }
        } else {
            throw exc;
        }
    }

    /**
     * Sets the current item to this object if we are using the screen to show
     * an existing record;
     *
     * @param currentItem - an existing object
     */
    public void setCurrentItem(Interest item) {
        //check if the item 
        if (item == null || item.getID() == 0) {
            currentItem = new Interest(0);
        } else {
            InterestFilter filter = new InterestFilter();
            filter.ids.add(item.getID());

            Throwable exc = null;
            try {
                Result<Interest> res = stub.fetchInterests(filter);
                exc = res.getException();
                if (exc == null) {
                    List<Interest> lst = res.getResult();
                    if (lst == null || lst.isEmpty()) {
                        throw new Exception("No data found!");
                    }
                    currentItem = lst.get(0);
                }
            } catch (Exception e) {
                exc = e;
            }
            if (exc != null) {
                Utils.showError(exc.getMessage(), Client.getMainPageStage());
            }
        }

        showCurrentItem();
    }

    private void showCurrentItem() {
        name.setText(currentItem.name);
        descriptionArea.setText(currentItem.description);
        chm = new ChildrenManager<>(currentItem.subCategories.getOldChildren());

        showSubCategories();
    }

    private void showSubCategories() {

        vboxSubCategories.getChildren().clear();

        final ChildrenManager<SubCategory> twin = chm;

        List<SubCategory> allCats = twin.getCurrentChildren();
        for (final SubCategory child : allCats) {
            HBox hb = new HBox(3);
            if (addSubCategoryButton.isVisible()) {
                Button b = new Button();
                b.setPrefSize(22, 22);
                b.setMinSize(22, 22);
                b.setMaxSize(22, 22);
                b.setGraphic(ImageViewFactory.getImageView("x_sign.png", 22));
                b.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        twin.removeChild(child);
                        showSubCategories();
                    }
                });
                hb.getChildren().add(b);
            }
            Label l = new Label(child.name);
            hb.getChildren().add(l);
            vboxSubCategories.getChildren().add(hb);
        }
    }

    //###=SESSION AWARENESS=###
    @Override
    //sets visibility/editability of controllers according to user and session properties
    public void refreshGUI() {

        //check for role
        boolean isAdmin = SessionProperties.hasRole(Role.ADMIN);
        boolean isLogged = SessionProperties.isLogged();
        boolean isOwner = SessionProperties.isOwner(currentItem);

        //show/hide buttons
        insertButton.setVisible(isLogged && currentItem.getID() == 0); //inserting new item
        updateButton.setVisible(currentItem.getID() != 0 && (isAdmin || isOwner)); //updating old record
        clearButton.setVisible(insertButton.isVisible());

        boolean isActive = insertButton.isVisible() || updateButton.isVisible(); //are we able to insert or update anything
        addSubCategoryButton.setVisible(isActive);

        //enable/disable inputs
        name.setEditable(isActive);
        descriptionArea.setEditable(isActive);
        showSubCategories();
    }

    //###=COMMON METHODS=###
    @FXML
    //reset input fields
    private void handleClearAction(ActionEvent event) {
        setCurrentItem(new Interest(0));
    }

    private boolean checkInput(Interest input) {

        if (input.name.isEmpty()) {
            Utils.showError("Name cannot be empty!", Client.getMainPageStage());
            return false;
        }

        if (input.subCategories.getCurrentChildren().isEmpty()) {
            Utils.showError("There must be atleast one subcategory!", Client.getMainPageStage());
            return false;
        }

        return true;
    }

    private Interest processInput() {

        //I: if we have a chosen item, copy ID and timeupd (if any) from it
        int id = 0;
        Timestamp timeupd = null;
        if (currentItem != null) {
            id = currentItem.getID();
            timeupd = currentItem.getTimeUpd();
        }

        Interest intr = new Interest(id, null, timeupd);

        //II: read data from input controls
        intr.name = name.getText();
        intr.description = descriptionArea.getText();
        intr.subCategories = chm;

        //III: validate inputs
        if (false == checkInput(intr)) {
            return null;
        }

        return intr;
    }

    @FXML
    //Create
    private void handleInsertAction(ActionEvent event) {

        Throwable exc = null;
        try {
            //I: read input
            Interest intr = processInput();
            if (intr == null) {
                return;
            }
            //II: if ok, continue and wrap it for RMI
            List<Interest> lst = new ArrayList<>();
            lst.add(intr);
            //III: try to insert it and process result
            Result<Interest> res = stub.insertInterest(lst, SessionProperties.getSession());
            exc = res.getException();
            if (exc == null) {
                //IV: if ok, refresh data
                currentItem = res.getResult().get(0);
                showCurrentItem();
                refreshGUI();
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
            Interest intr = processInput();
            if (intr == null) {
                return;
            }

            //II: if ok, continue and wrap it for RMI
            List<Interest> lst = new ArrayList<>();
            lst.add(intr);
            //III: try to insert it and process result
            Result<Interest> res = stub.updateInterest(lst, SessionProperties.getSession());
            exc = res.getException();
            if (exc == null) {
                //IV: if ok, refresh data
                currentItem = res.getResult().get(0);
                showCurrentItem();
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
    //Show choice dialog
    private void handleAddSubCategoryAction(ActionEvent event) {
        
        //prepary only subcategories we haven't included yet
        List<SubCategory> sbcs = chm.filterList(dataSubCategories);
        final String[] colNames = {
            "Name",
            "Category",
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
        
        Utils.showTableChoice("SUBCATEGORIES", Client.getMainPageStage(), sbcs, colNames, fields,
                widths, SelectionMode.SINGLE, new ChoiceDelegatorIF<SubCategory>() {

            @Override
            public void processSelected(List<SubCategory> selected) {
                for(SubCategory sbc : selected){
                    chm.addChild(sbc);
                }
                showSubCategories();
            }
        });
    }
}
