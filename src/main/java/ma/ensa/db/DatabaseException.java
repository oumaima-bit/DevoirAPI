package ma.ensa.db;

/**
 * Exception personnalisée pour les erreurs liées aux opérations de base de données.
 * Cette classe permet de fournir des messages d'erreur plus significatifs
 * et adaptés au contexte de l'application.
 */
public class DatabaseException extends RuntimeException {

    /**
     * Constructeur avec message d'erreur.
     *
     * @param message Le message expliquant l'erreur
     */
    public DatabaseException(String message) {
        super(message);
    }

    /**
     * Constructeur avec message d'erreur et cause.
     *
     * @param message Le message expliquant l'erreur
     * @param cause   La cause originale de l'erreur
     */
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Crée une nouvelle exception pour les erreurs de connexion.
     *
     * @param dbType Le type de base de données
     * @param cause  La cause originale de l'erreur
     * @return Une nouvelle instance de DatabaseException
     */
    public static DatabaseException connectionError(String dbType, Throwable cause) {
        return new DatabaseException(
                "Erreur lors de la connexion à la base de données " + dbType + ": " + cause.getMessage(),
                cause);
    }

    /**
     * Crée une nouvelle exception pour les erreurs d'exécution de requête.
     *
     * @param query La requête SQL qui a échoué
     * @param cause La cause originale de l'erreur
     * @return Une nouvelle instance de DatabaseException
     */
    public static DatabaseException queryError(String query, Throwable cause) {
        return new DatabaseException(
                "Erreur lors de l'exécution de la requête: " + query + "\nCause: " + cause.getMessage(),
                cause);
    }
}