package App.bll;

import App.model.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();

    UserDTO getById(long id);

    UserDTO getByLoginPassword(String login, String password);

    UserDTO getByLogin(String login);

    boolean removeById(long id);

    boolean update(long id, UserDTO userDTO);
}
