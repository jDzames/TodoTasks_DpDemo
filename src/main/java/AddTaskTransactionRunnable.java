import net.jards.core.*;
import net.jards.errors.LocalStorageException;

/**
 * Created by jDzama on 27.3.2017.
 */
public class AddTaskTransactionRunnable implements TransactionRunnable {

    @Override
    public void run(ExecutionContext executionContext, Transaction transaction, Object... objects) {
        Collection tasks = executionContext.getCollection("tasks");
        String taskJson = "{\"text\":\""+(String) objects[0]+"\"}";
        Document document = new Document(taskJson);
        try {
            tasks.create(document, transaction);
        } catch (LocalStorageException e) {
            e.printStackTrace();
        }
    }
}
