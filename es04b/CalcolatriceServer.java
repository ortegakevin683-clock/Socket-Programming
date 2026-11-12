import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class CalcolatriceServer {
    private static final int PORTA = 8844;

    public static void main(String[] args) {
        // 1. Apre il server sulla porta 8844
        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Server avviato sulla porta " + PORTA);

            // 2. Ciclo infinito per accettare un client alla volta
            while (true) {
                try (Socket client = serverSocket.accept();
                     BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream(), StandardCharsets.UTF_8));
                     PrintWriter out = new PrintWriter(client.getOutputStream(), true, StandardCharsets.UTF_8)) {

                    System.out.println("Nuovo client connesso!");
                    int contatoreOperazioni = 0;
                    String richiesta;

                    // 3. Legge le righe inviate dal client finch non si disconnette
                    while ((richiesta = in.readLine()) != null) {
                        // Controlla se il client vuole chiudere
                        if (richiesta.trim().equalsIgnoreCase("ESCI")) {
                            out.println("Connessione chiusa. Operazioni eseguite: " + contatoreOperazioni);
                            System.out.println("Client disconnesso.");
                            break;
                        }

                        // Elabora il calcolo e invia la risposta
                        String risposta = elaboraRichiesta(richiesta);
                        if (!risposta.startsWith("ERRORE")) {
                            contatoreOperazioni++;
                        }
                        out.println(risposta + " (Totale operazioni: " + contatoreOperazioni + ")");
                    }
                } catch (IOException e) {
                    System.out.println("Errore nella comunicazione con il client: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Impossibile avviare il server: " + e.getMessage());
        }
    }

    // Metodo che controlla la stringa (es. "10 + 5") e gestisce gli errori
    private static String elaboraRichiesta(String richiesta) {
        String[] parti = richiesta.trim().split("\\s+");
        if (parti.length != 3) {
            return "ERRORE: Formato non valido (usa: NUMERO OPERAZIONE NUMERO)";
        }

        try {
            double num1 = Double.parseDouble(parti[0]);
            String operazione = parti[1];
            double num2 = Double.parseDouble(parti[2]);

            if (!Double.isFinite(num1) || !Double.isFinite(num2)) {
                return "ERRORE: Sono ammessi soltanto numeri finiti";
            }

            double risultato = calcola(num1, operazione, num2);
            return "Risultato: " + risultato;

        } catch (NumberFormatException e) {
            return "ERRORE: Inserisci numeri validi";
        } catch (ArithmeticException | IllegalArgumentException e) {
            return "ERRORE: " + e.getMessage();
        }
    }

    // Metodo che esegue l'operazione matematica vera e propria
    private static double calcola(double num1, String operazione, double num2) {
        switch (operazione) {
            case "+": return num1 + num2;
            case "-": return num1 - num2;
            case "*": return num1 * num2;
            case "/":
                if (num2 == 0) throw new ArithmeticException("Divisione per zero");
                return num1 / num2;
            default: throw new IllegalArgumentException("Operazione non supportata");
        }
    }
}
