import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer extends Thread {
    private int serverPort;

    public TCPServer(String name, int serverPort) {
        this.serverPort = serverPort;
    }

    public void run() {
        ServerSocket serverSocket = null; // potrzebny do komunikacji strumieniowej p2p klient-server
        Socket connectedSocket = null;
        InputStream inputStream = null;
        OutputStream outputStream = null;

        String responseMessage =
                "HTTP/1.1 200 OK\r\n\r\n"
                        + "<!DOCTYPE html>"
                        + "<html><head><title>Serwer TCP</title></head><body>"
                        + "<br/><center><b>Witaj na serwerze TCP!</b></center>"
                        + "</body></html>";

        byte[] response = responseMessage.getBytes();
        byte[] buffer = new byte[2048];

        try {
            serverSocket = new ServerSocket(serverPort);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (!this.isInterrupted()) {
            try {
                connectedSocket = serverSocket.accept();
                ConnectedTCP myThread = new ConnectedTCP("server");
                myThread.start();
                inputStream = connectedSocket.getInputStream();
                outputStream = connectedSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }


            try {
                outputStream.write(response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                int counter = inputStream.read(buffer);
                if (counter > 0) {
                    String testMessage = new String(buffer, 0, buffer.length);
                    System.out.println(testMessage);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                outputStream.flush(); // opóźnia strumień wyjścioiwy i wymusza zapisanie danych z bufora
                outputStream.close();
                inputStream.close();
                connectedSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
