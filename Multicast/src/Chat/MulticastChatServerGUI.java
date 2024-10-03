package Chat;

import javax.swing.*;
import java.awt.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class MulticastChatServerGUI {
    private JPanel panel;
    private JTextArea serverLog;
    private DatagramSocket socket;
    private final int port = 4001; 

    public MulticastChatServerGUI() {
        createUIComponents();

        try {
            socket = new DatagramSocket(port);  
            new Thread(this::listenForClientJoins).start(); 
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void createUIComponents() {
        JFrame frame = new JFrame("Multicast Chat Server");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panel = new JPanel(new BorderLayout());

        serverLog = new JTextArea(10, 30);
        serverLog.setEditable(false);
        panel.add(new JScrollPane(serverLog), BorderLayout.CENTER);

        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }

    private void listenForClientJoins() {
        try {
            byte[] buffer = new byte[1024];
            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                if (message.contains("has joined")) {
                    clientJoined(message);
                } else if (message.contains("has left")) {
                    clientLeft(message);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clientJoined(String message) {
        serverLog.append(message + "\n");
        serverLog.setCaretPosition(serverLog.getDocument().getLength()); 
    }

    public void clientLeft(String message) {
        serverLog.append(message + "\n");
        serverLog.setCaretPosition(serverLog.getDocument().getLength()); 
    }

    public static void main(String[] args) {
        new MulticastChatServerGUI();
    }
}
