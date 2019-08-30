
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
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
      createServerSocket(port);
    }
  }

  public void run() {
    try {
      BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      String messageIn;
      while ((messageIn = in.readLine()) != null) {
        for (CharListener cl : listeners)
          cl.receivedChar('c');
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