
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JTextArea;

public class AirKeyboard implements AirKeyListener, KeyListener {

  private Robot robot;
  private Connection connection;
  private JFrame frame;
  private Thread connectionThread;

  public AirKeyboard(String ip, int port) throws Exception {
    robot = new Robot();
    frame = new JFrame("AirKeyboard");
    frame.setVisible(true);
    frame.setPreferredSize(new Dimension(500, 500));
    frame.setMinimumSize(new Dimension(500, 500));
    frame.setFocusable(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLocation(500, 500);
    //frame.addKeyListener(this);
    JTextArea t = new JTextArea();
    t.addKeyListener(this);
    frame.getContentPane().add(t);
    frame.pack();
    connection = new Connection(ip, port);
    connection.addListener(this);
    connectionThread = new Thread(connection);
    connectionThread.start();
  }
  
  private void writeKey(int c) {
    robot.keyPress(c);
    robot.delay(1);
    robot.keyRelease(c);
    robot.delay(1);
  }

  @Override
  public void receivedKey(int c) {
    writeKey(c);
  }
  
  @Override
  public void keyTyped(KeyEvent e) {

  }
  
  @Override
  public void keyPressed(KeyEvent e) {
    try {
      connection.sendKey(e.getKeyCode());
      System.out.println(e.getKeyCode());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  @Override
  public void keyReleased(KeyEvent e) {
    
  }

  public static void main(String[] args) throws Exception {
    new AirKeyboard(args[0], Integer.parseInt(args[1]));
  }
}