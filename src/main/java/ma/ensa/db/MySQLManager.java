package ma.ensa.db;

import lombok.NoArgsConstructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NoArgsConstructor
public class MySQLManager extends AbstractDatabaseManager {

    public MySQLManager() {
        super(null, null, null);
    }

    public MySQLManager(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public Connection connect() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
            return this.connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }
}
