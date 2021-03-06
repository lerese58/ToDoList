package App.ui;

import App.model.UserDTO;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.Objects;

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

    public UIUser(UserDTO userDTO) {
        _id = new SimpleLongProperty(userDTO.getId());
        _name = new SimpleStringProperty(userDTO.getName());
        _login = new SimpleStringProperty(userDTO.getLogin());
        _password = new SimpleStringProperty(userDTO.getPassword());
        _isReadyToOrder = new SimpleBooleanProperty(userDTO.isReadyToOrder());
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UIUser uiUser = (UIUser) o;
        return this.getId().equals(uiUser.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(_name, _login, _password, _isReadyToOrder);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(_login.get());
        sb.append(",    is open:").append(_isReadyToOrder.get());
        return sb.toString();
    }
}