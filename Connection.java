
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Connection implements Runnable {

  private volatile Socket socket;
  private volatile List<CharListener> listeners = new ArrayList<CharListener>();

  public void addListener(CharListener listener) {
    listeners.add(listener);
  }

  public Connection(String ip, int port) throws Exception {
    try {
      createClientSocket(ip, port);
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println("SERVER");
      createServerSocket(port);
    }
  }

  public void sendChar(char c) throws Exception {
    new DataOutputStream(socket.getOutputStream()).write((byte)c);
  }

  public void run() {
    try {
      InputStreamReader in = new InputStreamReader(socket.getInputStream());
      int oneByte;
      while ((oneByte = in.read()) != -1) {
        System.out.println(oneByte);
        for (CharListener cl : listeners)
          cl.receivedChar((char)oneByte);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void createServerSocket(int port) throws Exception {
    ServerSocket serverSocket = new ServerSocket(port);
    socket = serverSocket.accept();
    serverSocket.close();
  }

  private void createClientSocket(String ip, int port) throws Exception {
    socket = new Socket(ip, port);
  }
}