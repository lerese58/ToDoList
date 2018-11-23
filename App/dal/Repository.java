package App.dal;

import App.model.EntityDTO;

import java.util.List;

public interface Repository<T extends EntityDTO> {

    List<T> getAll();

    T getById(long id);

    boolean removeById(long id);

    boolean update(long ID, T dbObject);

    boolean create(T dbObject);
}
