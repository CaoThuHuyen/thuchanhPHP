package Pop3ClientApplication;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.swing.*;
import java.awt.*;
import java.util.Properties;

public class Pop3ClientApplication {

    private static volatile boolean isFetching = false;  // Biến cờ để kiểm soát quá trình lấy email

    public static void main(String[] args) {
        // Tạo JFrame cho giao diện
        JFrame frame = new JFrame("POP3 Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // Tạo panel nhập liệu
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        // Tạo các trường nhập liệu
        JLabel emailLabel = new JLabel("Email:");
        final JTextField emailField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JLabel serverLabel = new JLabel("POP3 Server:");
        JTextField serverField = new JTextField("pop.gmail.com");  // Máy chủ mặc định Gmail

        // Thêm các trường vào panel
        inputPanel.add(emailLabel);
        inputPanel.add(emailField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(serverLabel);
        inputPanel.add(serverField);

        // Tạo khu vực hiển thị email
        JTextArea emailDisplayArea = new JTextArea(10, 40);
        emailDisplayArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(emailDisplayArea);

        // Nút lấy email
        JButton fetchButton = new JButton("Fetch Emails");
        JButton stopButton = new JButton("Stop");  // Nút dừng

        // Thêm các thành phần vào frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(fetchButton);
        buttonPanel.add(stopButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Hiển thị frame
        frame.setVisible(true);

        // Hành động khi nhấn nút "Fetch Emails"
        fetchButton.addActionListener(e -> {
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            String pop3Server = serverField.getText();

            // Gọi phương thức lấy email
            // Sử dụng SwingWorker để tránh làm đông GUI
            isFetching = true;  // Bắt đầu quá trình lấy email
            SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
                @Override
                protected Void doInBackground() {
                    fetchEmails(email, password, pop3Server, emailDisplayArea);
                    return null;
                }
            };
            worker.execute();
        });

        // Hành động khi nhấn nút "Stop"
        stopButton.addActionListener(e -> {
            isFetching = false;  // Dừng quá trình lấy email
            JOptionPane.showMessageDialog(frame, "Fetching emails has been stopped.");
        });
    }

    // Phương thức lấy email từ POP3 server
    private static void fetchEmails(String email, String password, String pop3Server, JTextArea emailDisplayArea) {
        // Cấu hình POP3 server
        String protocol = "pop3";
        int port = 995;  // Cổng mặc định của POP3S

        // Cấu hình thuộc tính cho POP3
        Properties properties = new Properties();
        properties.put("mail.pop3.host", pop3Server);
        properties.put("mail.pop3.port", String.valueOf(port));
        properties.put("mail.pop3.starttls.enable", "true");
        properties.put("mail.pop3.ssl.enable", "true");  // SSL để kết nối bảo mật

        // Tạo session cho POP3
        Session session = Session.getInstance(properties, null);

        try {
            // Tạo đối tượng Store và kết nối với POP3 server
            Store store = session.getStore(protocol);
            store.connect(pop3Server, email, password);

            // Mở hộp thư đến
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            // Lấy danh sách email trong hộp thư
            Message[] messages = inbox.getMessages();

            // Xóa nội dung cũ trong JTextArea
            SwingUtilities.invokeLater(() -> emailDisplayArea.setText(""));

            // Kiểm tra xem có email không
            if (messages.length == 0) {
                SwingUtilities.invokeLater(() -> emailDisplayArea.append("No emails found.\n"));
            } else {
                // Hiển thị thông tin email
                for (Message message : messages) {
                    // Kiểm tra biến cờ để quyết định có tiếp tục hay không
                    if (!isFetching) {
                        break;  // Dừng quá trình nếu biến cờ là false
                    }

                    StringBuilder emailInfo = new StringBuilder();
                    emailInfo.append("From: ").append(InternetAddress.toString(message.getFrom())).append("\n");
                    emailInfo.append("Subject: ").append(message.getSubject()).append("\n");
                    emailInfo.append("Date: ").append(message.getSentDate()).append("\n");
                    emailInfo.append("-------------------------------\n");

                    // Cập nhật JTextArea
                    SwingUtilities.invokeLater(() -> emailDisplayArea.append(emailInfo.toString()));
                }
            }

            // Đóng kết nối
            inbox.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
            SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Error: " + e.getMessage()));
        }
    }
}
