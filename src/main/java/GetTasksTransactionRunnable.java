import net.jards.core.*;
import net.jards.errors.LocalStorageException;

import javax.swing.*;

/**
 * Created by jDzama on 21.3.2017.
 */
public class GetTasksTransactionRunnable implements TransactionRunnable {

    @Override
    public void run(ExecutionContext executionContext, Transaction transaction, Object... objects) {
        JTable table  = (JTable) objects[0];
        ResultSet tasksResultSet = null;
        try {
            tasksResultSet = executionContext.getCollection("tasks").find(null);
        } catch (LocalStorageException e) {
            e.printStackTrace();
        }
        if (tasksResultSet == null){
            return;
        }
        TasksTableModel tableModel = new TasksTableModel(tasksResultSet.getDocuments());
        table.setModel(tableModel);
        tasksResultSet.addActualDocumentsListener(documentList -> {
            SwingUtilities.invokeLater(() -> {
                tableModel.setData(documentList);
                tableModel.fireTableDataChanged();
            });

        });

    }
}
