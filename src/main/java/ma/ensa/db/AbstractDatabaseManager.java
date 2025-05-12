package ma.ensa.db;

import lombok.Data;
import java.sql.*;
import java.util.*;

@Data
public abstract class AbstractDatabaseManager implements DatabaseManager {
    protected String url;
    protected String username;
    protected String password;
    protected Connection connection;

    public AbstractDatabaseManager(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Récupère la connexion actuelle à la base de données.
     *
     * @return L'objet Connection actuel
     */
    public Connection getConnection() {
        return this.connection;
    }

    @Override
    public List<Map<String, Object>> executeQuery(String query) throws SQLException {
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return convertResultSetToList(rs);
        }
    }

    @Override
    public int executeUpdate(String query) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            return stmt.executeUpdate(query);
        }
    }

    @Override
    public List<Map<String, Object>> executeQuery(String query, Object... params) throws SQLException {
        try (PreparedStatement pstmt = prepareStatement(query, params);
             ResultSet rs = pstmt.executeQuery()) {
            return convertResultSetToList(rs);
        }
    }

    @Override
    public int executeUpdate(String query, Object... params) throws SQLException {
        try (PreparedStatement pstmt = prepareStatement(query, params)) {
            return pstmt.executeUpdate();
        }
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }

    private PreparedStatement prepareStatement(String query, Object... params) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(query);
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
        return pstmt;
    }

    private List<Map<String, Object>> convertResultSetToList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> results = new ArrayList<>();
        ResultSetMetaData metaData = rs.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (rs.next()) {
            Map<String, Object> row = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = rs.getObject(i);
                row.put(columnName, value);
            }
            results.add(row);
        }
        return results;
    }
}