package App.dal;

import App.bll.BLUser;

public class DBUser {
    private long _id;
    private String _name;
    private String _login;
    private String _password;
    private boolean _isReadyToOrder;

    public DBUser(long id, String name, String login, String password, boolean isReadyToOrder) {
        _id = id;
        _name = name;
        _login = login;
        _password = password;
        _isReadyToOrder = isReadyToOrder;
    }

    public DBUser(BLUser blUser) {
        _id = blUser.getId();
        _name = blUser.getName();
        _login = blUser.getLogin();
        _password = blUser.getPassword();
        _isReadyToOrder = blUser.isReadyToOrder();
    }

    public DBUser(String name, String login, String password, boolean isReadyToOrder) {
        _id = Math.abs(name.hashCode() + login.hashCode() + password.hashCode() + ((Boolean) isReadyToOrder).hashCode());
        _name = name;
        _login = login;
        _password = password;
        _isReadyToOrder = isReadyToOrder;
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
