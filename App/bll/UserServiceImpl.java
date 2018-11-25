package App.bll;

import App.dal.Repository;
import App.dal.UserRepoDB;
import App.model.UserDTO;

import java.util.ArrayList;
import java.util.List;

public class UserServiceImpl implements UserService {

    private final Repository<UserDTO> _userRepo;

    public UserServiceImpl() {
        _userRepo = new UserRepoDB();
    }

    public UserServiceImpl(Repository<UserDTO> userRepository) {
        _userRepo = userRepository;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return new ArrayList<>(_userRepo.getAll());
    }

    @Override
    public UserDTO getById(long id) {
        return _userRepo.getById(id);
    }

    @Override
    public UserDTO getByLoginPassword(String login, String password) {
        for (UserDTO userDTO : _userRepo.getAll()) {
            if (login.equals(userDTO.getLogin()) && password.equals(userDTO.getPassword()))
                return userDTO;
        }
        return null;
    }

    @Override
    public UserDTO getByLogin(String login) {
        for (UserDTO userDTO : _userRepo.getAll()) {
            if (login.equals(userDTO.getLogin()))
                return userDTO;
        }
        return null;
    }

    @Override
    public boolean removeById(long id) {
        return _userRepo.removeById(id);
    }

    @Override
    public boolean update(long id, UserDTO userDTO) {
        return _userRepo.update(id, userDTO);
    }

    @Override
    public boolean create(UserDTO userDTO) {
        return _userRepo.create(userDTO);
    }
}
