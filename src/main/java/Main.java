public class Main {
    public static void main(String[] args) {
        TCPServer server = new TCPServer("server", 8081);
        server.start();
    }
}