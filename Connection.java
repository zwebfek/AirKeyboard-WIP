
import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Connection implements Runnable {

  private volatile Socket socket;
  private volatile List<AirKeyListener> listeners = new ArrayList<AirKeyListener>();
  private DataOutputStream out;

  public void addListener(AirKeyListener listener) {
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

  public void sendKey(int c) throws Exception {
    out.writeInt(c);
  }

  public void run() { 
    try {
      DataInputStream in = new DataInputStream(socket.getInputStream());
      int keyCode;
      while ((keyCode = in.readInt()) != -1) {
        for (AirKeyListener akl : listeners)
          akl.receivedKey(keyCode);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void createServerSocket(int port) throws Exception {
    ServerSocket serverSocket = new ServerSocket(port);
    socket = serverSocket.accept();
    serverSocket.close();
    setDataOutputStream(socket);
  }

  private void createClientSocket(String ip, int port) throws Exception {
    socket = new Socket(ip, port);
    setDataOutputStream(socket);
  }

  private void setDataOutputStream(Socket socket) throws Exception {
    out = new DataOutputStream(socket.getOutputStream());
  }
}