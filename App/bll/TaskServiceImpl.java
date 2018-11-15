package App.bll;

import App.dal.DBTask;
import App.dal.Repository;
import App.dal.TaskRepoDB;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class TaskServiceImpl implements TaskService {

    private final Repository<DBTask> _repository;

    public TaskServiceImpl() {
        _repository = new TaskRepoDB();
    }

    public TaskServiceImpl(Repository<DBTask> repository) {
        _repository = repository;
    }

    @Override
    public ArrayList<BLTask> getTasks() {
        ArrayList<BLTask> blTasks = new ArrayList<>();
        for (DBTask dbtask : _repository.getAll())
            blTasks.add(new BLTask(dbtask));
        return blTasks;
    }

    @Override
    public ArrayList<BLTask> getListForThisUser(long userID) {
        ArrayList<BLTask> tasks = new ArrayList<>();
        for (DBTask dbtask : _repository.getAll()) {
            if (dbtask.getUserList().contains(userID))
                tasks.add(new BLTask(dbtask));
        }
        return tasks;
    }

    public BLTask getByID(long id) {
        return new BLTask(_repository.getById(id));
    }

    @Override
    public ArrayList<BLTask> getListBefore(LocalDateTime time) {
        ArrayList<BLTask> deadlineTasks = new ArrayList<>();
        for (DBTask dbtask : _repository.getAll())
            if (dbtask.getDeadline().getDateTime().isBefore(time))
                deadlineTasks.add(new BLTask(dbtask));
        return deadlineTasks;
    }

    @Override
    public ArrayList<BLTask> getListAfter(LocalDateTime time) {
        ArrayList<BLTask> tasks = new ArrayList<>();
        for (DBTask dbtask : _repository.getAll())
            if (dbtask.getDeadline().getDateTime().isAfter(time))
                tasks.add(new BLTask(dbtask));
        return tasks;
    }

    @Override
    public boolean removeById(long id) {
        _repository.removeById(id);
        return false;
    }

    @Override
    public boolean remove(BLTask blTask) {
        _repository.removeById(blTask.getId());
        return false;
    }

    @Override
    public boolean removeBefore(LocalDateTime time) {
        for (DBTask dbTask : _repository.getAll()) {
            if (dbTask.getDeadline().getDateTime().isBefore(time)) {
                _repository.removeById(dbTask.getId());
            }
        }
        return true;
    }

    @Override
    public boolean update(long ID, BLTask task) {
        return _repository.update(ID, new DBTask(task));
    }
}
