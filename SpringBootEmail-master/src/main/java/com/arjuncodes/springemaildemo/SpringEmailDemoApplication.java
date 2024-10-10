package com.arjuncodes.springemaildemo;

import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Properties;

public class SpringEmailDemoApplication {

    public static void main(String[] args) {
        // Tạo JFrame
        JFrame frame = new JFrame("SMTP Client");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        // Tạo panel cho phần nhập liệu
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2)); // 4 hàng, 2 cột

        // Tạo các trường nhập liệu
        JLabel fromLabel = new JLabel("From:");
        JTextField fromField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField();

        JLabel toLabel = new JLabel("To:");
        JTextField toField = new JTextField();

        JLabel subjectLabel = new JLabel("Subject:");
        JTextField subjectField = new JTextField();

        // Thêm các trường vào panel
        inputPanel.add(fromLabel);
        inputPanel.add(fromField);
        inputPanel.add(passwordLabel);
        inputPanel.add(passwordField);
        inputPanel.add(toLabel);
        inputPanel.add(toField);
        inputPanel.add(subjectLabel);
        inputPanel.add(subjectField);

        // Tạo phần body
        JLabel bodyLabel = new JLabel("Body:");
        JTextArea bodyArea = new JTextArea(5, 20);
        JScrollPane scrollPane = new JScrollPane(bodyArea);

        // Tạo nút chọn file
        JButton attachButton = new JButton("Attach File");
        JLabel filePathLabel = new JLabel("No file selected");

        // Tạo nút gửi
        JButton sendButton = new JButton("Send");

        // Thêm các thành phần vào frame
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2, 1));
        bottomPanel.add(attachButton);
        bottomPanel.add(filePathLabel);
        frame.add(bottomPanel, BorderLayout.EAST);
        frame.add(sendButton, BorderLayout.SOUTH);

        // Thiết lập kích thước và hiển thị frame
        frame.setVisible(true);

        // Biến để lưu file đã chọn
        final File[] selectedFile = {null};

        // Action khi nhấn nút "Attach File"
        attachButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(frame);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile[0] = fileChooser.getSelectedFile();
                filePathLabel.setText(selectedFile[0].getAbsolutePath());
            }
        });

        // Action khi nhấn nút "Send"
        sendButton.addActionListener(e -> {
            String fromEmail = fromField.getText();
            String password = new String(passwordField.getPassword());
            String toEmail = toField.getText();
            String subject = subjectField.getText();
            String body = bodyArea.getText();

            // Gọi phương thức sendEmail để gửi email kèm tệp đính kèm
            sendEmail(fromEmail, password, toEmail, subject, body, selectedFile[0]);

            // Sau khi gửi xong, reset giao diện về trạng thái ban đầu
            fromField.setText("");
            passwordField.setText("");
            toField.setText("");
            subjectField.setText("");
            bodyArea.setText("");
            filePathLabel.setText("No file selected");
            selectedFile[0] = null; // Reset file
        });
    }

    // Phương thức gửi email kèm tệp đính kèm
    private static void sendEmail(String fromEmail, String password, String toEmail, String subject, String body, File attachment) {
        // Cấu hình SMTP server
        String host = "smtp.gmail.com";
        int port = 587;

        // Thiết lập thuộc tính SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));

        // Tạo session
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Tạo đối tượng MimeMessage
            Message message = new MimeMessage(session);

            // Set thông tin email
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            // Tạo đối tượng MimeBodyPart cho phần nội dung email
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);

            // Tạo đối tượng MimeBodyPart cho tệp đính kèm
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            if (attachment != null) {
                MimeBodyPart attachmentPart = new MimeBodyPart();
                attachmentPart.attachFile(attachment);
                multipart.addBodyPart(attachmentPart);
            }

            // Set nội dung email
            message.setContent(multipart);

            // Gửi email
            Transport.send(message);

            // Thông báo khi gửi thành công
            JOptionPane.showMessageDialog(null, "Email sent successfully with attachment!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}
