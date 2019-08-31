
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class AirKeyboard implements CharListener, KeyListener {

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
  
  private void writeChar(char c) {
    if (Character.isUpperCase(c)) {
      robot.keyPress(KeyEvent.VK_SHIFT);
    }
    robot.keyPress(Character.toUpperCase(c));
    robot.keyRelease(Character.toUpperCase(c));
      
    if (Character.isUpperCase(c)) {
      robot.keyRelease(KeyEvent.VK_SHIFT);
    }
    robot.delay(1);
  }

  @Override
  public void receivedChar(char c) {
    writeChar(c);
  }
  
  @Override
  public void keyTyped(KeyEvent e) {
    try {
      System.out.println(e.getKeyChar());
      connection.sendChar(e.getKeyChar());
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
  
  @Override
  public void keyPressed(KeyEvent e) {
    
  }
  
  @Override
  public void keyReleased(KeyEvent e) {
    
  }

  public static void main(String[] args) throws Exception {
    new AirKeyboard("localhost", 9876);
  }
}