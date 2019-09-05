
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

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
    frame.addKeyListener(this);
    connection = new Connection(ip, port);
    connection.addListener(this);
    connectionThread = new Thread(connection);
    connectionThread.start();
  }

  @Override
  public void receivedKey(int c) {

    if (c > 0) {
      System.out.println("RECEIVE PRESS " + c);
      robot.keyPress(c);
    } else {
      System.out.println("RECEIVE RELEASE " + c);
      robot.keyRelease(-1 * c);
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    System.out.println("PRESS " + e.getKeyCode());
    try {
      connection.sendKey(e.getKeyCode());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
    System.out.println("RELEASE " + e.getKeyCode());
    try {
      connection.sendKey(-1 * e.getKeyCode());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    new AirKeyboard(args[0], Integer.parseInt(args[1]));
  }
}