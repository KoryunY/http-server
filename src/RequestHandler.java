import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.Callable;

public class RequestHandler implements Callable<Void> {
    private final Socket connection;

    public RequestHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public Void call() throws Exception {
        try {
            OutputStream raw = new BufferedOutputStream(connection.getOutputStream());
            Writer out = new OutputStreamWriter(raw);
            Reader in = new InputStreamReader(new BufferedInputStream(connection.getInputStream()), StandardCharsets.US_ASCII);
            StringBuilder requestLine = new StringBuilder();
            int c;
            while ((c = in.read()) != -1) {
                requestLine.append((char) c);
                if (requestLine.length() > 3) break;
            }
            String[] tokens = requestLine.toString().split("\\s+");

            if (tokens[0].equals("GET")) {
                out.write("HTTP/1.1 200 OK" + "\r\n");
                Date now = new Date();
                out.write("Date: " + now + "\r\n");
                out.write("Server: MyServer\r\n");
                out.write("Content-type: text/plain" + "\r\n\r\n");
                out.flush();
                raw.write("Hello World".getBytes(StandardCharsets.UTF_8));
                raw.flush();
            } else{
                out.write("HTTP/1.1 400 Bad Request" + "\r\n");
                Date now = new Date();
                out.write("Date: " + now + "\r\n");
                out.write("Server: MyServer\r\n");
                out.write("Content-type: text/plain" + "\r\n\r\n");
                out.flush();
                raw.write("Error:Cant Handle this Request".getBytes(StandardCharsets.UTF_8));
                raw.flush();
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
