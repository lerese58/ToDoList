package App.ui;

import App.bll.BLUser;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

public class UIUser {
    private SimpleLongProperty _id;
    private SimpleStringProperty _name;
    private SimpleStringProperty _login;
    private SimpleStringProperty _password;
    private SimpleBooleanProperty _isReadyToOrder;

    public UIUser(Long id, String name, String login, String password, Boolean isReadyToOrder) {
        _id = new SimpleLongProperty(id);
        _name = new SimpleStringProperty(name);
        _login = new SimpleStringProperty(login);
        _password = new SimpleStringProperty(password);
        _isReadyToOrder = new SimpleBooleanProperty(isReadyToOrder);
    }

    public UIUser(BLUser blUser) {
        _id = new SimpleLongProperty(blUser.getId());
        _name = new SimpleStringProperty(blUser.getName());
        _login = new SimpleStringProperty(blUser.getLogin());
        _password = new SimpleStringProperty(blUser.getPassword());
        _isReadyToOrder = new SimpleBooleanProperty(blUser.isReadyToOrder());
    }

    public UIUser(String name, String login, String password, Boolean isReadyToOrder) {
        _id = new SimpleLongProperty(Math.abs(name.hashCode() + login.hashCode() + password.hashCode() + isReadyToOrder.hashCode()));
        _name = new SimpleStringProperty(name);
        _login = new SimpleStringProperty(login);
        _password = new SimpleStringProperty(password);
        _isReadyToOrder = new SimpleBooleanProperty(isReadyToOrder);
    }

    public Long getId() {
        return _id.get();
    }

    public void setId(Long id) {
        _id.set(id);
    }

    public SimpleLongProperty idProperty() {
        return _id;
    }

    public String getName() {
        return _name.get();
    }

    public void setName(String name) {
        _name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return _name;
    }

    public String getLogin() {
        return _login.get();
    }

    public void setLogin(String login) {
        _login.set(login);
    }

    public SimpleStringProperty loginProperty() {
        return _login;
    }

    public String getPassword() {
        return _password.get();
    }

    public void setPassword(String password) {
        _password.set(password);
    }

    public SimpleStringProperty passwordProperty() {
        return _password;
    }

    public Boolean isReadyToOrder() {
        return _isReadyToOrder.get();
    }

    public SimpleBooleanProperty ReadyToOrderProperty() {
        return _isReadyToOrder;
    }

    public void setReadyToOrder(Boolean readyToOrder) {
        _isReadyToOrder.set(readyToOrder);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(_login.get());
        sb.append(",    is open:").append(_isReadyToOrder.get());
        return sb.toString();
    }
}