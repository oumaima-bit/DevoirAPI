package ma.ensa;

import ma.ensa.db.DatabaseManager;
import ma.ensa.db.DatabaseManagerFactory;
import ma.ensa.util.DBConfigLoader;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting Database Manager Application");

        // Load database configuration
        String configPath = "db.properties";
        DBConfigLoader configLoader = new DBConfigLoader(configPath);

        // Create database manager
        DatabaseManager dbManager = null;
        try {
            // Create manager based on configuration
            dbManager = DatabaseManagerFactory.createDatabaseManager(
                    configLoader.getType(),
                    configLoader.getUrl(),
                    configLoader.getUsername(),
                    configLoader.getPassword()
            );

            // Connect to database
            dbManager.connect();
            System.out.println("Successfully connected to " + configLoader.getType() + " database");

            // Example query - you can customize this part
            System.out.println("Running sample query...");
            List<Map<String, Object>> results = dbManager.executeQuery("SELECT 1 as test");

            // Display results
            if (!results.isEmpty()) {
                System.out.println("Query executed successfully!");
                System.out.println("Result: " + results.get(0));
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Close connection
            if (dbManager != null) {
                try {
                    dbManager.close();
                    System.out.println("Database connection closed");
                } catch (SQLException e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}