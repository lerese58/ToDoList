package App.dal;

import java.util.ArrayList;

public interface Repository<T> {

    ArrayList<T> getAll();

    T getById(long id);

    boolean removeById(long id);

    boolean update(long ID, T dbObject);
}
