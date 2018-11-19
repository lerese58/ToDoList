package App.bll;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface TaskService {

    ArrayList<BLTask> getTasks();

    ArrayList<BLTask> getListForThisUser(long userID);

    BLTask getByID(long id);

    ArrayList<BLTask> getListBefore(LocalDateTime time);

    ArrayList<BLTask> getListAfter(LocalDateTime time);

    boolean removeById(long id);

    boolean remove(BLTask blTask);

    boolean removeBefore(LocalDateTime time);

    boolean update(long ID, BLTask task);

}
