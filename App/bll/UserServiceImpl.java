package App.bll;

import App.dal.Repository;
import App.dal.UserRepoDB;
import App.model.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final Repository<UserDTO> _repository;

    public UserServiceImpl() {
        _repository = new UserRepoDB();
    }

    public UserServiceImpl(Repository<UserDTO> userRepository) {
        _repository = userRepository;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<UserDTO> allUsers = new ArrayList<>(_repository.getAll());
        return allUsers;
    }

    @Override
    public UserDTO getById(long id) {
        return _repository.getById(id);
    }

    @Override
    public UserDTO getByLoginPassword(String login, String password) {
        for (UserDTO userDTO : _repository.getAll()) {
            if (login.equals(userDTO.getLogin()) && password.equals(userDTO.getPassword()))
                return userDTO;
        }
        return null;
    }

    @Override
    public UserDTO getByLogin(String login) {
        for (UserDTO userDTO : _repository.getAll()) {
            if (login.equals(userDTO.getLogin()))
                return userDTO;
        }
        return null;
    }

    @Override
    public boolean removeById(long id) {
        return _repository.removeById(id);
    }

    @Override
    public boolean update(long id, UserDTO userDTO) {
        return _repository.update(id, userDTO);
    }
}
