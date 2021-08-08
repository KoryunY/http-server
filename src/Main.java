import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService pool= Executors.newCachedThreadPool();
        ServerSocket server = null;
        try {
            server = new ServerSocket(8080);
            while (true) {
                Socket request = server.accept();
                pool.submit(new RequestHandler(request));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
