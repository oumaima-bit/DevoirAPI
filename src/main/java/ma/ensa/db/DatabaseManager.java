package ma.ensa.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

    public interface DatabaseManager {
        // Méthode pour établir une connexion
        Connection connect() throws SQLException;

        // Méthode pour exécuter une requête SELECT
        List<Map<String, Object>> executeQuery(String query) throws SQLException;

        // Méthode pour exécuter un INSERT, UPDATE ou DELETE
        int executeUpdate(String query) throws SQLException;

        // Méthode pour exécuter une requête avec des paramètres
        List<Map<String, Object>> executeQuery(String query, Object... params) throws SQLException;

        // Méthode pour exécuter un INSERT, UPDATE ou DELETE avec des paramètres
        int executeUpdate(String query, Object... params) throws SQLException;

        // Méthode pour fermer la connexion
        void close() throws SQLException;
    }
