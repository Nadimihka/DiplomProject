import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static final int PORT = 8989;

    public static void main(String[] args) throws Exception {
        BooleanSearchEngine engine = new BooleanSearchEngine(new File("pdfs"));

        try (ServerSocket serverSocket = new ServerSocket(PORT);) {
            System.out.println("Сервер ожидает запрос от клиента");

            while (true) {
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                ) {
                    // обработка одного подключения
                    final String word = in.readLine();

                   /* Gson gson = new GsonBuilder()
                            .setPrettyPrinting()
                            .create();
                    String json = gson.toJson(engine.search(word));
                    System.out.println(json);*/

                    Gson gson = new Gson();
                    String json = gson.toJson(engine.search(word));
                    out.println(json);
                }
            }
        } catch (
                IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }
}