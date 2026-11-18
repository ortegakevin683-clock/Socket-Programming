/** Credenziali dimostrative condivise; la registrazione atomica, senza persistenza su disco. */
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class AutenticazioneManager {
    private final ConcurrentMap<String, String> credenziali;
    
    public AutenticazioneManager() {
        credenziali = new ConcurrentHashMap<>();
        // Credenziali predefinite (in produzione andrebbero in un database)
        credenziali.put("mario", "mario123");
        credenziali.put("luigi", "luigi123");
        credenziali.put("anna", "anna123");
        credenziali.put("admin", "admin");
    }
    
    public boolean autentica(String username, String password) {
        if (username == null || password == null) {
            return false;
        }
        String passwordCorretta = credenziali.get(username);
        return password.equals(passwordCorretta);
    }
    
    public boolean registra(String username, String password) {
        if (username == null || username.isBlank() || password == null || password.isBlank()) return false;
        // Anche due registrazioni contemporanee dello stesso nome hanno un solo vincitore.
        return credenziali.putIfAbsent(username, password) == null;
    }
    
    public int getNumeroUtenti() {
        return credenziali.size();
    }
}
