import net.jards.core.*;

import javax.swing.*;

/**
 * Created by jDzama on 21.3.2017.
 */
public class GetTasksTransactionRunnable implements TransactionRunnable {
    @Override
    public void run(ExecutionContext executionContext, Transaction transaction, Object... objects) {
        JTable table  = (JTable) objects[0];
        ResultSet tasksResultSet = executionContext.getCollection("tasks").find(null);
        TasksTableModel tableModel = new TasksTableModel(tasksResultSet.getDocuments());
        tasksResultSet.addActualDocumentsListener(documentList -> {
            tableModel.setData(documentList);
            tableModel.fireTableDataChanged();
        });
    }
}
