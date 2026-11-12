import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class CalcolatriceClient {
    private static final String HOST = "localhost";
    private static final int PORTA = 8844;

    public static void main(String[] args) {
        // 1. Tenta di connettersi al server e apre i canali di testo e la tastiera
        try (Socket socket = new Socket(HOST, PORTA);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true, StandardCharsets.UTF_8);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             Scanner scanner = new Scanner(System.in)) {

            // 2. Stampa il menu
            System.out.println("=== CALCOLATRICE REMOTA ===");
            System.out.println("Formato: NUMERO OPERAZIONE NUMERO (es. 10 + 5)");
            System.out.println("Operazioni: + - * /");
            System.out.println("Scrivi 'esci' per uscire\n");
	    System.out.println("Ricordati di lasciare uno spazio tra i numeri e le operazioni altrimenti non funzionera'! ");

            // 3. Ciclo di lettura da tastiera e invio al server
            while (true) {
                System.out.print("Calcolo > ");
                if (!scanner.hasNextLine()) {
                    break;
                }
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("esci")) {
                    out.println("ESCI");
                    String chiusura = in.readLine();
                    System.out.println(chiusura == null ? "Server disconnesso, a presto" : chiusura);
                    break;
                }

                // Invia l'operazione al server e aspetta la risposta
                out.println(input);
                String risposta = in.readLine();
                if (risposta == null) {
                    System.out.println("Server disconnesso, a presto");
                    break;
                }
                System.out.println(risposta);
            }

        } catch (ConnectException e) {
            System.err.println("Errore: Impossibile connettersi al server. Assicurati che sia avviato!");
        } catch (IOException e) {
            System.err.println("Errore di comunicazione: " + e.getMessage());
        }
    }
}
