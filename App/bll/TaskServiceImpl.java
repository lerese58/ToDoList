package App.bll;

import App.dal.Repository;
import App.dal.TaskRepoDB;
import App.dal.UserRepoDB;
import App.model.TaskDTO;
import App.model.UserDTO;
import App.utils.NotifyStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskServiceImpl implements TaskService {

    private final Repository<TaskDTO> _taskRepo = new TaskRepoDB();
    private final Repository<UserDTO> _userRepo = new UserRepoDB();
    private Long _currentUserID;

    public TaskServiceImpl(Long userID) {
        _currentUserID = userID;
    }

    public Long getCurrentUserID() {
        return _currentUserID;
    }

    public void setCurrentUserID(Long userID) {
        _currentUserID = userID;
    }

    @Override
    public List<TaskDTO> getListForUser(long userID) {
        List<TaskDTO> tasks = new ArrayList<>();
        _taskRepo.getList(userID).forEach(taskDTO -> {
            if (taskDTO.getUserList().get(userID).equals(NotifyStatus.CONFIRMED))
                tasks.add(taskDTO);
        });
        return tasks;
    }

    @Override
    public List<TaskDTO> getNotificationForUser(long userID) {
        List<TaskDTO> newTasks = new ArrayList<>();
        _taskRepo.getList(userID).forEach(taskDTO -> {
            if (taskDTO.getUserList().get(userID).equals(NotifyStatus.NON_SEEN))
                newTasks.add(taskDTO);
        });
        return newTasks;
    }

    public TaskDTO getByID(long id) {
        return _taskRepo.getById(id);
    }

    @Override
    public List<TaskDTO> getListBefore(LocalDateTime time) {
        List<TaskDTO> deadlineTasks = new ArrayList<>();
        _taskRepo.getList(_currentUserID).forEach(task -> {
            if (task.getDeadline().getDateTime().isBefore(time))
                deadlineTasks.add(task);
        });
        return deadlineTasks;
    }

    @Override
    public List<TaskDTO> getListAfter(LocalDateTime time) {
        List<TaskDTO> tasks = new ArrayList<>();
        _taskRepo.getList(_currentUserID).forEach(task -> {
            if (task.getDeadline().getDateTime().isAfter(time))
                tasks.add(task);
        });
        return tasks;
    }

    @Override
    public boolean removeById(long id) {
        return _taskRepo.removeById(id);
    }

    @Override
    public boolean removeBefore(LocalDateTime time) {
        _taskRepo.getList(_currentUserID).forEach(task -> {
            if (task.getDeadline().getDateTime().isBefore(time))
                _taskRepo.removeById(task.getId());
        });
        return true;
    }

    @Override
    public boolean update(long ID, TaskDTO task) {
        return _taskRepo.update(ID, task);
    }

    @Override
    public boolean create(TaskDTO task) {
        return _taskRepo.create(task);
    }
}
