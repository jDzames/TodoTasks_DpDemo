import net.jards.core.*;
import net.jards.errors.LocalStorageException;
import net.jards.local.sqlite.SQLiteLocalStorage;
import net.jards.remote.ddp.DDPConnectionSettings;
import net.jards.remote.ddp.DDPRemoteStorage;

import javax.swing.*;

import java.awt.event.*;

import static net.jards.remote.ddp.DDPConnectionSettings.LoginType.Username;

/**
 * Created by jDzama on 20.3.2017.
 */
public class MainForm {
    private JTextField textFieldTaskText;
    private JButton buttonAdd;
    private JButton buttonRemoveTask;
    private JPanel mainPanel;
    private JTable tableTasks;
    private JButton buttonUpdate;

    private static Storage storage;

    public MainForm() {
        buttonAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (textFieldTaskText.getText().length()<1){
                    return;
                }
                String taskText = textFieldTaskText.getText();
                storage.callAsync("tasks.insertWithSeed", taskText);
                textFieldTaskText.setText("");
            }
        });
        buttonRemoveTask.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Document d = ((TasksTableModel)tableTasks.getModel()).getSelectedDocument(tableTasks.getSelectedRow());
                if (d!=null){
                    storage.callAsync("tasks.remove", d.getId());
                    textFieldTaskText.setText("");
                }
            }
        });
        buttonUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Document d = ((TasksTableModel)tableTasks.getModel()).getSelectedDocument(tableTasks.getSelectedRow());
                if (d!=null){
                    //String taskJson = "{\"text\":\""++"\"}";
                    storage.callAsync("tasks.update", d.getId(), textFieldTaskText.getText());
                    textFieldTaskText.setText("");
                }
            }
        });
    }

    public static void main(String[] args) {
        StorageSetup storageSetup = new StorageSetup();
        storageSetup.setPrefix("demo_");
        storageSetup.addCollectionSetup("tasks", false);
        DDPConnectionSettings connectionSettings = new DDPConnectionSettings("localhost", 3000, Username, "testik", "testik");
        RemoteStorage remoteStorage = new DDPRemoteStorage(storageSetup, connectionSettings);
        LocalStorage localStorage;
        storage = null;
        try {
            localStorage = new SQLiteLocalStorage(storageSetup, "jdbc:sqlite:test.db");
            storage = new Storage(storageSetup, remoteStorage, localStorage);
            storage.registerSpeculativeMethod("tasks.insertWithSeed", new AddTaskTransactionRunnable());

            storage.start("");

            storage.subscribe("tasks");

        } catch (LocalStorageException e) {
            e.printStackTrace();
        }

        //storage.callAsync("tasks.insert", "Added through DDP 7");

        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addWindowListener( new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                storage.stop();
            }
        });
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        //custom component creation code here
        tableTasks = new JTable();
        storage.execute(new GetTasksTransactionRunnable(), tableTasks);

        tableTasks.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = tableTasks.getSelectedRow();
                    int column = tableTasks.getSelectedColumn();
                    if (row>=0 && column >=0){
                        String cellText = (String) tableTasks.getValueAt(row, column);
                        textFieldTaskText.setText(cellText);
                    }
                }
            }
        });
    }


}
