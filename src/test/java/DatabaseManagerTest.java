import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import ma.ensa.db.AbstractDatabaseManager;
import ma.ensa.db.DatabaseManager;
import ma.ensa.db.H2Manager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class DatabaseManagerTest {

    private DatabaseManager dbManager;

    @Before
    public void setUp() throws SQLException {
        // Utiliser H2 en mémoire pour les tests
        dbManager = new H2Manager("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1", "sa", "");
        dbManager.connect();

        // Créer la table de test
        try (Statement stmt = ((AbstractDatabaseManager) dbManager).getConnection().createStatement()) {

            stmt.execute("DROP TABLE IF EXISTS utilisateurs");

            stmt.execute("CREATE TABLE utilisateurs (id INT PRIMARY KEY, nom VARCHAR(100), email VARCHAR(100))");

            // Charger les données de test à partir du fichier CSV
            try (BufferedReader br = new BufferedReader(new FileReader("src/test/resources/test_data.csv"))) {
                String line;
                boolean isFirstLine = true;

                while ((line = br.readLine()) != null) {
                    if (isFirstLine) {
                        isFirstLine = false;
                        continue; // Sauter l'en-tête
                    }

                    String[] data = line.split(",");
                    if (data.length == 3) {
                        String sql = "INSERT INTO utilisateurs VALUES (" + data[0] + ", '" + data[1] + "', '" + data[2] + "')";
                        stmt.execute(sql);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                fail("Erreur lors du chargement des données de test: " + e.getMessage());
            }
        }
    }

    @After
    public void tearDown() throws SQLException {
        if (dbManager != null) {
            dbManager.close();
        }
    }

    @Test
    public void testConnection() {
        Connection connection = ((AbstractDatabaseManager) dbManager).getConnection();
        assertNotNull("La connexion ne devrait pas être null", connection);
        try {
            assertFalse("La connexion ne devrait pas être fermée", connection.isClosed());
        } catch (SQLException e) {
            fail("Exception SQL lors de la vérification de la connexion: " + e.getMessage());
        }
    }

    @Test
    public void testExecuteQuery() throws SQLException {
        List<Map<String, Object>> results = dbManager.executeQuery("SELECT * FROM utilisateurs");

        assertNotNull("Le résultat ne devrait pas être null", results);
        assertEquals("Le nombre d'utilisateurs devrait être 3", 3, results.size());

        // Vérifier le premier utilisateur
        Map<String, Object> firstUser = results.get(0);
        assertEquals("L'ID du premier utilisateur doit être 1", 1, firstUser.get("ID"));
        assertEquals("Le nom du premier utilisateur doit être zerbouhi", "zerbouhi", firstUser.get("NOM"));
        assertEquals("L'email du premier utilisateur doit être oumaima.zerbouhi1@example.com",
                "oumaima.zerbouhi1@example.com", firstUser.get("EMAIL"));
    }

    @Test
    public void testExecuteQueryWithParams() throws SQLException {
        List<Map<String, Object>> results = dbManager.executeQuery(
                "SELECT * FROM utilisateurs WHERE id = ?", 2);

        assertNotNull("Le résultat ne devrait pas être null", results);
        assertEquals("Une seule ligne devrait être retournée", 1, results.size());

        Map<String, Object> user = results.get(0);
        assertEquals("L'ID de l'utilisateur doit être 2", 2, user.get("ID"));
        assertEquals("Le nom de l'utilisateur doit être mahdaoui", "mahdaoui", user.get("NOM"));
    }

    @Test
    public void testExecuteUpdate() throws SQLException {
        int rowsAffected = dbManager.executeUpdate(
                "UPDATE utilisateurs SET nom = 'zerbouhi_updated' WHERE id = 1");

        assertEquals("Une ligne devrait être affectée", 1, rowsAffected);

        // Vérifier la mise à jour
        List<Map<String, Object>> results = dbManager.executeQuery("SELECT * FROM utilisateurs WHERE id = 1");
        assertEquals("Le nom devrait être mis à jour", "zerbouhi_updated", results.get(0).get("NOM"));
    }

    @Test
    public void testExecuteUpdateWithParams() throws SQLException {
        int rowsAffected = dbManager.executeUpdate(
                "UPDATE utilisateurs SET email = ? WHERE id = ?",
                "nouveau_email@example.com", 3);

        assertEquals("Une ligne devrait être affectée", 1, rowsAffected);

        // Vérifier la mise à jour
        List<Map<String, Object>> results = dbManager.executeQuery("SELECT * FROM utilisateurs WHERE id = 3");
        assertEquals("L'email devrait être mis à jour", "nouveau_email@example.com", results.get(0).get("EMAIL"));
    }
}