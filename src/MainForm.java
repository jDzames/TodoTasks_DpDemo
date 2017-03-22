import net.jards.core.*;
import net.jards.errors.LocalStorageException;
import net.jards.local.sqlite.SQLiteLocalStorage;
import net.jards.remote.ddp.DDPConnectionSettings;
import net.jards.remote.ddp.DDPRemoteStorage;

import javax.swing.*;

import static net.jards.remote.ddp.DDPConnectionSettings.LoginType.Username;

/**
 * Created by jDzama on 20.3.2017.
 */
public class MainForm {
    private JTextField textFieldSubscription;
    private JButton buttonSubscribe;
    private JButton buttonRemoveTask;
    private JButton buttonUpdate;
    private JPanel mainPanel;
    private JTable tableTasks;
    private static Storage storage;


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

            storage.start("");
            storage.subscribe("tasks");
        } catch (LocalStorageException e) {
            e.printStackTrace();
        }

        //storage.callAsync("tasks.insert", "Added through DDP 7");

        JFrame frame = new JFrame("MainForm");
        frame.setContentPane(new MainForm().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private void createUIComponents() {
        //custom component creation code here
        storage.execute(new GetTasksTransactionRunnable(), tableTasks);
    }
}
