package App.bll;

import App.dal.DBUser;
import App.dal.Repository;
import App.dal.UserRepoDB;

import java.util.ArrayList;

public class UserServiceImpl implements UserService {

    private final Repository<DBUser> _repository;

    public UserServiceImpl() {
        _repository = new UserRepoDB();
    }

    public UserServiceImpl(Repository<DBUser> userRepository) {
        _repository = userRepository;
    }

    @Override
    public ArrayList<BLUser> getAllUsers() {
        ArrayList<BLUser> allUsers = new ArrayList<>();
        for (DBUser dbUser : _repository.getAll()) {
            allUsers.add(new BLUser(dbUser));
        }
        return allUsers;
    }

    @Override
    public BLUser getById(long id) {
        return new BLUser(_repository.getById(id));
    }

    @Override
    public BLUser getByLoginPassword(String login, String password) {
        for (DBUser dbUser : _repository.getAll()) {
            if (login.equals(dbUser.getLogin()) && password.equals(dbUser.getPassword()))
                return new BLUser(dbUser);
        }
        return null;
    }

    @Override
    public BLUser getByLogin(String login) {
        for (DBUser dbUser : _repository.getAll()) {
            if (login.equals(dbUser.getLogin()))
                return new BLUser(dbUser);
        }
        return null;
    }

    @Override
    public boolean removeById(long id) {
        return _repository.removeById(id);
    }

    @Override
    public boolean update(long id, BLUser blUser) {
        return _repository.update(id, new DBUser(blUser));
    }
}
