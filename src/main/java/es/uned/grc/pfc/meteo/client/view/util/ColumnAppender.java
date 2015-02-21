package es.uned.grc.pfc.meteo.client.view.util;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.view.client.SelectionModel;

public class ColumnAppender <C, E extends Object> {

   public interface GetValue <C, E extends Object> {
      C getValue (E entity);
   }

   public Header <Boolean> getSelectAllHeader (final CellTable <E> cellTable, final SelectionModel <E> selectionModel) {
      CheckboxCell headerCheckbox = new CheckboxCell (true, false);
      
      Header <Boolean> selectHeader = new Header <Boolean> (headerCheckbox) {
         @Override
         public Boolean getValue () {
            for (E item : cellTable.getVisibleItems ()) {
               if (!selectionModel.isSelected (item)) {
                  return false;
               }
            }
            return cellTable.getVisibleItems ().size () > 0;
         }
      };
      selectHeader.setUpdater (new ValueUpdater <Boolean> () {
         @Override
         public void update (Boolean value) {
            for (E item : cellTable.getVisibleItems ()) {
               selectionModel.setSelected (item, value);
            }
         }
      });
      
      return selectHeader;
   }
   
   /**
    * Add a column with some properties
    */
   public Column <E, C> addColumn (CellTable <E> cellTable,
                                   Cell <C> cell, 
                                   Header <?> header, 
                                   String columnServerName, 
                                   final GetValue <C, E> getter, 
                                   FieldUpdater <E, C> fieldUpdater, 
                                   boolean sortable, 
                                   Integer widthPercentage) {
      return addColumn (cellTable,
            cell, 
            null,
            header,
            columnServerName, 
            getter, 
            fieldUpdater, 
            sortable, 
            widthPercentage);
   }
   
   /**
    * Add a column with some properties
    */
   public Column <E, C> addColumn (CellTable <E> cellTable,
                                   Cell <C> cell, 
                                   String headerText, 
                                   String columnServerName, 
                                   final GetValue <C, E> getter, 
                                   FieldUpdater <E, C> fieldUpdater, 
                                   boolean sortable, 
                                   Integer widthPercentage) {
      return addColumn (cellTable,
                        cell, 
                        headerText,
                        null,
                        columnServerName, 
                        getter, 
                        fieldUpdater, 
                        sortable, 
                        widthPercentage);
   }
   
   /**
    * Add a column with some properties
    */
   private Column <E, C> addColumn (CellTable <E> cellTable,
                                   Cell <C> cell, 
                                   String headerText, 
                                   Header <?> header,
                                   String columnServerName, 
                                   final GetValue <C, E> getter, 
                                   FieldUpdater <E, C> fieldUpdater, 
                                   boolean sortable, 
                                   Integer widthPercentage) {
      
      Column <E, C> column = new Column <E, C> (cell) {
         @Override
         public C getValue (E entity) {
            return getter.getValue (entity);
         }
      };
      if (columnServerName != null) {
         column.setDataStoreName (columnServerName);
      }
      column.setFieldUpdater (fieldUpdater);
      column.setSortable (sortable);
      if (header == null) {
         cellTable.addColumn (column, headerText);
      } else {
         cellTable.addColumn (column, header);
      }
      if (widthPercentage != null) {
         cellTable.setColumnWidth (column, widthPercentage, Unit.PCT);
      }
      
      return column;
   }
   
}
