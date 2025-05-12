package ma.ensa.util;

import lombok.Getter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBConfigLoader {
    private String type;
    private String url;
    private String username;
    private String password;

    public DBConfigLoader(String configPath) {
        loadConfig(configPath);
    }

    private void loadConfig(String configPath) {
        Properties properties = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream(configPath)) {
            if (input == null) {
                throw new IOException("Fichier de configuration introuvable : " + configPath);
            }

            properties.load(input);

            this.type = properties.getProperty("db.type");
            this.url = properties.getProperty("db.url");
            this.username = properties.getProperty("db.username");
            this.password = properties.getProperty("db.password");

        } catch (IOException ex) {
            System.err.println("Error loading database configuration: " + ex.getMessage());
            ex.printStackTrace();
        }
    }


    // Ajout des getters manuels
    public String getType() {
        return type;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}