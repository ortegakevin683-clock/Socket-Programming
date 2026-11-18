# ES04c - Prenotazione Posti Aereo

Applicazione client-server multithread in Java su socket TCP per la prenotazione concorrente dei posti di un aereo (6 file x 4 posti).

## File principali
* `PrenotazioneServer.java`: avvia il server sulla porta `8888` e accetta le connessioni.
* `ClientHandler.java`: gestisce ogni singolo client tramite thread dedicato.
* `Posto.java`: gestisce lo stato dei posti e la sincronizzazione (`synchronized`).
* `PrenotazioneClient.java`: client testuale con menu interattivo.

## Come compilare ed eseguire

1. **Compilazione:**
   ```bash
   javac *.java
