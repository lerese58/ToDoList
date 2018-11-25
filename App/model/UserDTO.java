package App.model;

import App.ui.UIUser;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserDTO extends EntityDTO {

    private String _name;
    private String _login;
    private String _password;
    private boolean _isReadyToOrder;

    public UserDTO(long id, String name, String login, String password, boolean isReadyToOrder) {
        __id = id;
        _name = name;
        _login = login;
        _password = password;
        _isReadyToOrder = isReadyToOrder;
        __modified = LocalDateTime.now();
    }

    public UserDTO(String name, String login, String password, boolean isReadyToOrder) {
        __id = Math.abs(name.hashCode() + login.hashCode() + password.hashCode() + ((Boolean) isReadyToOrder).hashCode());
        _name = name;
        _login = login;
        _password = password;
        _isReadyToOrder = isReadyToOrder;
        __modified = LocalDateTime.now();
    }

    public UserDTO(UIUser uiUser) {
        __id = uiUser.getId();
        _name = uiUser.getName();
        _login = uiUser.getLogin();
        _password = uiUser.getPassword();
        _isReadyToOrder = uiUser.isReadyToOrder();
        __modified = LocalDateTime.now();
    }

    public long getId() {
        return __id;
    }

    public String getName() {
        return _name;
    }

    public String getLogin() {
        return _login;
    }

    public String getPassword() {
        return _password;
    }

    public boolean isReadyToOrder() {
        return _isReadyToOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return _isReadyToOrder == userDTO._isReadyToOrder &&
                Objects.equals(_name, userDTO._name) &&
                Objects.equals(_login, userDTO._login) &&
                Objects.equals(_password, userDTO._password) &&
                __id == userDTO.__id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, _login, _password, _isReadyToOrder);
    }

    @Override
    public String toString() {
        String sb = String.valueOf(__id) + "/" +
                _name + "/" +
                _login + "/" +
                _password + "/" +
                _isReadyToOrder;
        return sb;
    }
}
