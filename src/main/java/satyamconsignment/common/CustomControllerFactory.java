package satyamconsignment.common;

import javafx.util.Callback;

import java.sql.Connection;

public class CustomControllerFactory implements Callback<Class<?>, Object> {

    private final Connection connection;

    public CustomControllerFactory(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Object call(Class<?> controllerClass) {
        try {
            Object controller = controllerClass.getDeclaredConstructor().newInstance();
            if (controller instanceof BaseController) {
                ((BaseController) controller).setDatabase(connection);
            }
            return controller;
        } catch (Exception e) {
            throw new RuntimeException("Failed to instantiate controller: " + controllerClass.getName(), e);
        }
    }
}
