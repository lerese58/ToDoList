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

    private final Repository<TaskDTO> _taskRepo;
    private final Repository<UserDTO> _userRepo;

    public TaskServiceImpl() {
        _taskRepo = new TaskRepoDB();
        _userRepo = new UserRepoDB();
    }

    public TaskServiceImpl(Repository<TaskDTO> taskRepo, Repository<UserDTO> userRepo) {
        _taskRepo = taskRepo;
        _userRepo = userRepo;
    }

    @Override
    public List<TaskDTO> getTasks() {
        return new ArrayList<>(_taskRepo.getAll());
    }

    @Override
    public List<TaskDTO> getListForThisUser(long userID) {
        List<TaskDTO> tasks = new ArrayList<>();
        _taskRepo.getAll().forEach(task -> {
            if (task.getUserList().containsKey(userID))
                tasks.add(task);
        });
        return tasks;
    }

    @Override
    public List<TaskDTO> getNotificationForUser(long userID) {
        List<TaskDTO> tasks = new ArrayList<>();
        getListForThisUser(userID).forEach(task -> {
            if (task.getUserList().containsValue(NotifyStatus.NON_SEEN))
                tasks.add(task);
        });
        return tasks;
    }

    public TaskDTO getByID(long id) {
        return _taskRepo.getById(id);
    }

    @Override
    public List<TaskDTO> getListBefore(LocalDateTime time) {
        List<TaskDTO> deadlineTasks = new ArrayList<>();
        _taskRepo.getAll().forEach(task -> {
            if (task.getDeadline().getDateTime().isBefore(time))
                deadlineTasks.add(task);
        });
        return deadlineTasks;
    }

    @Override
    public List<TaskDTO> getListAfter(LocalDateTime time) {
        List<TaskDTO> tasks = new ArrayList<>();
        _taskRepo.getAll().forEach(task -> {
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
        _taskRepo.getAll().forEach(task -> {
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
