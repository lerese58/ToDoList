package App.bll;

import App.dal.DBUser;
import App.ui.UIUser;

public class BLUser {
    private long _id;
    private String _name;
    private String _login;
    private String _password;
    private boolean _isReadyToOrder;

    public BLUser(long id, String name, String login, String password, boolean isReadyToOrder) {
        _id = id;
        _name = name;
        _login = login;
        _password = password;
        _isReadyToOrder = isReadyToOrder;
    }

    public BLUser(DBUser dbUser) {
        _id = dbUser.getId();
        _name = dbUser.getName();
        _login = dbUser.getLogin();
        _password = dbUser.getPassword();
        _isReadyToOrder = dbUser.isReadyToOrder();
    }

    public BLUser(UIUser uiUser) {
        _id = uiUser.getId();
        _name = uiUser.getName();
        _login = uiUser.getLogin();
        _password = uiUser.getPassword();
        _isReadyToOrder = uiUser.isReadyToOrder();
    }

    public long getId() {
        return _id;
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
    public String toString() {
        String sb = String.valueOf(_id) + "/" +
                _name + "/" +
                _login + "/" +
                _password + "/" +
                _isReadyToOrder;
        return sb;
    }
}
