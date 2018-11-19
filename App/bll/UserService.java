package App.bll;

import java.util.ArrayList;

public interface UserService {

    ArrayList<BLUser> getAllUsers();

    BLUser getById(long id);

    BLUser getByLoginPassword(String login, String password);

    BLUser getByLogin(String login);

    boolean removeById(long id);

    boolean update(long id, BLUser blUser);
}
