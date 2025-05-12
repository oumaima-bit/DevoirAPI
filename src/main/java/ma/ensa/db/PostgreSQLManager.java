package ma.ensa.db;

import lombok.NoArgsConstructor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@NoArgsConstructor
public class PostgreSQLManager extends AbstractDatabaseManager {

    public PostgreSQLManager() {
        super(null, null, null);
    }

    public PostgreSQLManager(String url, String username, String password) {
        super(url, username, password);
    }

    @Override
    public Connection connect() throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
            this.connection = DriverManager.getConnection(url, username, password);
            return this.connection;
        } catch (ClassNotFoundException e) {
            throw new SQLException("PostgreSQL JDBC Driver not found", e);
        }
    }
}