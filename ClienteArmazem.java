import java.io.*;
import java.net.*;
import java.util.Scanner;

public class ClienteArmazem {
    public static void main(String[] args) {
        String host = "localhost"; // ou IP do servidor
        int porta = 12345;

        try (Socket socket = new Socket(host, porta)) {
            System.out.println("Conectado ao Armazém A!");

            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter saida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            Scanner scanner = new Scanner(System.in);

            // Thread para receber mensagens
            Thread receber = new Thread(() -> {
                try {
                    String mensagem;
                    while ((mensagem = entrada.readLine()) != null) {
                        System.out.println("[Armazém A]: " + mensagem);
                    }
                } catch (IOException e) {
                    System.out.println("Conexão encerrada.");
                }
            });

            receber.start();

            // Enviar mensagens
            String msg;
            while (true) {
                System.out.print("[Armazém B]: ");
                msg = scanner.nextLine();
                saida.write(msg);
                saida.newLine();
                saida.flush();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
