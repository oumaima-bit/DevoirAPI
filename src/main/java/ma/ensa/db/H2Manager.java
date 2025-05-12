package ma.ensa.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class H2Manager extends AbstractDatabaseManager {

        public H2Manager(String url, String username, String password) {
            super(url, username, password);
        }

        @Override
        public Connection connect() throws SQLException {
            try {
                Class.forName("org.h2.Driver");
                this.connection = DriverManager.getConnection(url, username, password);
                return this.connection;
            } catch (ClassNotFoundException e) {
                throw new SQLException("H2 JDBC Driver not found", e);
            }
        }
    }

