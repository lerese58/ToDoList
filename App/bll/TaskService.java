package App.bll;

import App.model.TaskDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {

    List<TaskDTO> getListForUser(long userID);

    List<TaskDTO> getNotificationForUser(long userID);

    IBackgroundOperation<List<TaskDTO>> getAllBO();

    Long getCount();

    TaskDTO getByID(long id);

    List<TaskDTO> getListBefore(LocalDateTime time);

    List<TaskDTO> getListAfter(LocalDateTime time);

    boolean removeById(long id);

    boolean removeBefore(LocalDateTime time);

    boolean update(long ID, TaskDTO task);

    boolean create(TaskDTO task);
}
