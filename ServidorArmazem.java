import java.io.*;
import java.net.*;
import java.util.concurrent.*;

public class ServidorArmazem {
    private static final int PORTA = 12345;
    private static final int MAX_CONEXOES = 10; // Limite de conexões simultâneas

    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(MAX_CONEXOES); // ThreadPool

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            System.out.println("Servidor aguardando conexões na porta " + PORTA);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Novo cliente conectado: " + socket.getInetAddress());

                pool.execute(new ClienteHandler(socket)); // Usa uma thread do pool
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pool.shutdown(); // Fecha o pool de threads ao encerrar o servidor
        }
    }
}

// Classe que gerencia cada cliente em uma thread separada
class ClienteHandler implements Runnable {
    private Socket socket;

    public ClienteHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedWriter saida = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            String mensagem;
            while ((mensagem = entrada.readLine()) != null) {
                System.out.println("[Cliente]: " + mensagem);
                saida.write("Recebido: " + mensagem);
                saida.newLine();
                saida.flush();
            }
        } catch (IOException e) {
            System.out.println("Conexão encerrada.");
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
