import net.jards.core.Document;
import net.jards.core.DocumentList;
import net.jards.errors.JsonFormatException;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * Created by jDzama on 20.3.2017.
 */
public class TasksTableModel extends AbstractTableModel {

    private DocumentList tasks;

    public TasksTableModel(DocumentList tasks){
        this.tasks = tasks;
    }

    @Override
    public String getColumnName(int column) {
        return "Not completed TODO tasks";
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        try{
            if (rowIndex >= tasks.size() || rowIndex<0){
                System.out.println("out of range (row, col, tasks size) "+rowIndex+" "+columnIndex+" "+tasks.size());
                return  "";
            }
            String text = (String) tasks.get(rowIndex).getPropertyValue("text");
            if (text==null){
                return "Wrong document content";
            }
            return text;
        } catch (JsonFormatException e) {
            return "Wrong document content";
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex>=tasks.size()-1){
            this.tasks.add((Document)aValue);
        } else {
            this.tasks.set(rowIndex, (Document) aValue);
        }
        super.setValueAt(aValue, rowIndex, columnIndex);
    }

    public void setData(DocumentList tasks){
        this.tasks = tasks;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    Document getSelectedDocument(int row){
        return this.tasks.get(row);
    }

}
