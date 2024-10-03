package Chat;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.*;

public class MulticastChatClientGUI {
    private JPanel panel;
    private JTextArea chatArea;
    private JTextField messageField;
    private JTextField groupIPField;
    private JButton sendButton;
    private JButton joinButton;
    private JButton leaveButton; 
    private MulticastSocket socket;
    private InetAddress group;
    private DatagramSocket unicastSocket;
    private final int port = 4002; 
    private volatile boolean listening; 

    public MulticastChatClientGUI() {
        createUIComponents();

        joinButton.addActionListener(e -> {
            String groupIP = groupIPField.getText();
            try {
                group = InetAddress.getByName(groupIP);
                socket = new MulticastSocket(port);
                socket.joinGroup(group);
                unicastSocket = new DatagramSocket();
                chatArea.append("Joined group: " + groupIP + "\n");

                sendJoinNotification();

                listening = true; 
                new Thread(this::receiveMessages).start();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });

        leaveButton.addActionListener(e -> {
            leaveGroup();
        });

        sendButton.addActionListener(e -> {
            String message = messageField.getText();
            
            String targetIP = groupIPField.getText();
            sendMessage(targetIP, message);
            messageField.setText(""); 
        });
    }

    private void createUIComponents() {
        JFrame frame = new JFrame("Multicast Chat Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));

        chatArea = new JTextArea(15, 30);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        chatArea.setFont(new Font("Arial", Font.PLAIN, 14));
        chatArea.setBackground(Color.WHITE);
        chatArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        panel.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 1));
        inputPanel.setBackground(new Color(240, 240, 240));

        JPanel addressPanel = new JPanel(new FlowLayout());
        addressPanel.setBackground(new Color(240, 240, 240));
        addressPanel.add(new JLabel("Group IP/Target IP:"));
        groupIPField = new JTextField("224.0.0.1", 10);
        groupIPField.setFont(new Font("Arial", Font.PLAIN, 14));
        addressPanel.add(groupIPField);
        inputPanel.add(addressPanel);

        JPanel messagePanel = new JPanel(new FlowLayout());
        messagePanel.setBackground(new Color(240, 240, 240));
        messageField = new JTextField(20);
        messageField.setFont(new Font("Arial", Font.PLAIN, 14));
        messagePanel.add(new JLabel("Message:"));
        messagePanel.add(messageField);
        sendButton = new JButton("Send");
        messagePanel.add(sendButton);
        inputPanel.add(messagePanel);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(new Color(240, 240, 240));
        joinButton = new JButton("Join Group");
        leaveButton = new JButton("Leave Group");
        joinButton.setFocusPainted(false);
        leaveButton.setFocusPainted(false);
        buttonPanel.add(joinButton);
        buttonPanel.add(leaveButton);
        inputPanel.add(buttonPanel);

        panel.add(inputPanel, BorderLayout.SOUTH);

        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null); 
        frame.setVisible(true);
    }

    private void sendJoinNotification() {
        String message = "Client " + getLocalHostAddress() + " has joined.";
        try {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), 4001); // Send to server's port
            unicastSocket.send(packet); // Send the join message to the server
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getLocalHostAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
            return "unknown";
        }
    }

    private void sendMessage(String targetIP, String message) {
        try {
            InetAddress targetAddress = InetAddress.getByName(targetIP);
            String fullMessage = "From " + getLocalHostAddress() + ": " + message;
            byte[] buffer = fullMessage.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, targetAddress, port);
            unicastSocket.send(packet);
            chatArea.append("Sent to " + targetIP + ": " + fullMessage + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveMessages() {
        try {
            byte[] buffer = new byte[1024];
            while (listening) { 
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String message = new String(packet.getData(), 0, packet.getLength());
                chatArea.append("Received: " + message + "\n");
            }
        } catch (IOException e) {
            if (listening) {  
                e.printStackTrace();
            }
        }
    }

    private void leaveGroup() {
        try {
            if (socket != null && group != null) {
                sendLeaveNotification();

                listening = false;
                socket.leaveGroup(group);
                Thread.sleep(100);
                socket.close(); 
                chatArea.append("Leave group: " + group + "/n");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendLeaveNotification() {
        String message = "Client " + getLocalHostAddress() + " has left.";
        try {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("localhost"), 4001); // Send to server's port
            unicastSocket.send(packet); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MulticastChatClientGUI());
    }
}
