package App.dal;

import App.model.EntityDTO;

import java.util.List;

public interface Repository<T extends EntityDTO> {

    List<T> getAll();

    List<T> getList(long id);

    Long getCount();

    T getById(long id);

    boolean removeById(long id);

    boolean update(long ID, T dbObject);

    boolean create(T dbObject);
}
