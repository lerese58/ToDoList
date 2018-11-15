package App.bll;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;

public interface TaskService {

    ArrayList<BLTask> getTasks() throws IOException;

    ArrayList<BLTask> getListForThisUser(long userID) throws FileNotFoundException;

    BLTask getByID(long id) throws FileNotFoundException;

    ArrayList<BLTask> getListBefore(LocalDateTime time) throws FileNotFoundException;

    ArrayList<BLTask> getListAfter(LocalDateTime time) throws FileNotFoundException;

    boolean removeById(long id) throws IOException;

    boolean remove(BLTask blTask) throws IOException;

    boolean removeBefore(LocalDateTime time) throws IOException;

    boolean update(long ID, BLTask task);

}
