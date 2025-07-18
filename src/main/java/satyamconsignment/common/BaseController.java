package satyamconsignment.common;

import java.sql.Connection;

public abstract class BaseController {
    protected Connection connection;

    public void setDatabase(Connection connection) {
        this.connection = connection;
    }
}