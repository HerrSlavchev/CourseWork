/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import dto.domain.PersistedDTO;
import dto.domain.Region;
import java.lang.reflect.Type;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author root
 */
public class ControllerUtils {

    public static void setUpTable(TableView table, String[] colNames, String[] fields, double[] widths) {
        
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        double sum = 0;
        for (double d : widths){
            sum += d;
        }
        
        double rem = 1;
        double rat = 0;
        TableColumn[] cols = new TableColumn[colNames.length];
        for (int i = 0; i < colNames.length; i++) {
            TableColumn col = new TableColumn(colNames[i]);
            col.setCellValueFactory(
                    new PropertyValueFactory(fields[i])
            );
            double d = widths[i];
            rat = d/sum;
            rem -= rat;
            
            col.prefWidthProperty().bind(table.widthProperty().multiply(rat));
            cols[i] = col;
        }
        rem += rat;
        cols[cols.length - 1].prefWidthProperty().bind(table.widthProperty().multiply(rem*0.9));
        
        table.getColumns().clear();
        table.getColumns().addAll(cols);
    }
}
