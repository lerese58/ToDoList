package App.bll;

import App.dal.Repository;
import App.dal.TaskRepoDB;
import App.model.TaskDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskServiceImpl implements TaskService {

    private final Repository<TaskDTO> _repository;

    public TaskServiceImpl() {
        _repository = new TaskRepoDB();
    }

    public TaskServiceImpl(Repository<TaskDTO> repository) {
        _repository = repository;
    }

    @Override
    public List<TaskDTO> getTasks() {
        List<TaskDTO> tasks = new ArrayList<>(_repository.getAll());
        return tasks;
    }

    @Override
    public List<TaskDTO> getListForThisUser(long userID) {


        List<TaskDTO> tasks = new ArrayList<>();


        for (TaskDTO taskDTO : _repository.getAll()) {
            if (taskDTO.getUserList().contains(userID))
                tasks.add(taskDTO);
        }
        return tasks;
    }

    public TaskDTO getByID(long id) {
        return _repository.getById(id);
    }

    @Override
    public List<TaskDTO> getListBefore(LocalDateTime time) {
        List<TaskDTO> deadlineTasks = new ArrayList<>();
        for (TaskDTO taskDTO : _repository.getAll())
            if (taskDTO.getDeadline().getDateTime().isBefore(time))
                deadlineTasks.add(taskDTO);
        return deadlineTasks;
    }

    @Override
    public List<TaskDTO> getListAfter(LocalDateTime time) {
        List<TaskDTO> tasks = new ArrayList<>();
        for (TaskDTO taskDTO : _repository.getAll())
            if (taskDTO.getDeadline().getDateTime().isAfter(time))
                tasks.add(taskDTO);
        return tasks;
    }

    @Override
    public boolean removeById(long id) {
        _repository.removeById(id);
        return false;
    }

    @Override
    public boolean removeBefore(LocalDateTime time) {
        for (TaskDTO taskDTO : _repository.getAll()) {
            if (taskDTO.getDeadline().getDateTime().isBefore(time)) {
                _repository.removeById(taskDTO.getId());
            }
        }
        return true;
    }

    @Override
    public boolean update(long ID, TaskDTO task) {
        return _repository.update(ID, task);
    }
}
