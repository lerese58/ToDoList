package App.bll;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public interface UserService {

    ArrayList<BLUser> getAllUsers() throws FileNotFoundException;

    BLUser getById(long id);

    BLUser getByLoginPassword(String login, String password) throws FileNotFoundException;

    BLUser getByLogin(String login) throws FileNotFoundException;

    boolean removeById(long id);

    boolean update(long id, BLUser blUser);
}
