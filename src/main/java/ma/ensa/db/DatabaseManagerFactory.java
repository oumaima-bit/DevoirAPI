package ma.ensa.db;

public class DatabaseManagerFactory {
    public static final String MYSQL = "mysql";
    public static final String POSTGRESQL = "postgresql";
    public static final String SQLSERVER = "sqlserver";
    public static final String H2 = "h2";


    public static DatabaseManager createDatabaseManager(String type, String url, String username, String password) {
        DatabaseManager manager = null;

        switch (type.toLowerCase()) {
            case MYSQL:
                manager = new MySQLManager(url, username, password);
                break;
            case POSTGRESQL:
                manager = new PostgreSQLManager(url, username, password);
                break;
            case SQLSERVER:
                manager = new SQLServerManager(url, username, password);
                break;
            case H2:
                manager = new H2Manager(url, username, password);
                break;
            // Ajouter Oracle si nécessaire
            default:
                throw new IllegalArgumentException("Type de base de données non pris en charge: " + type);
        }
        return manager;
    }

}