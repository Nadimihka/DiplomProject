import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private static final int PORT = 8989;
    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {


        try (Socket clientSocket = new Socket(HOST, PORT)) {
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));

            System.out.println("Введите слово для анализа");
            Scanner scanner = new Scanner(System.in);
            String word = scanner.nextLine();
            out.println(word);

            String jsonIn = in.readLine();
            System.out.println(jsonIn);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
