import net.jards.core.Document;
import net.jards.core.DocumentList;

import javax.swing.table.AbstractTableModel;

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
        return tasks.get(rowIndex).getJsonData();
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
}
