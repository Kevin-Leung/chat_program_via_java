/*巨信聊天窗口_202211304116梁嘉宏*/
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatFrame extends JFrame {
    private String serverIp;
    private int serverPort;
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;
    private JButton sendfileButton;
    private JButton drawButton;
    private Socket socket;
    private PrintWriter out;

    public ChatFrame(String username, String serverIp, int serverPort) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;

        setTitle("巨信聊天窗口");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        inputField = new JTextField();
        sendButton = new JButton("发送");
        sendfileButton = new JButton("发送文件");
        drawButton = new JButton("绘图");

        JPanel panel = new JPanel();
        panel.setLayout(null);

        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBounds(20, 20, 360, 200);     //这是输出框
        inputField.setBounds(20, 230, 260, 30);     //这是键入框
        sendButton.setBounds(290, 230, 90, 30);     //这是发送按钮
        sendfileButton.setBounds(20, 270, 130, 30); //这是发送文件的按钮
        drawButton.setBounds(160, 270, 130, 30);    //这是绘画按钮

        panel.add(scrollPane);
        panel.add(inputField);
        panel.add(sendButton);
        panel.add(sendfileButton);
        panel.add(drawButton);

        add(panel);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ipAd;
                try {
                    InetAddress host_address;
                    host_address = InetAddress.getLocalHost();
                    ipAd = host_address.toString();
                } catch (UnknownHostException a) {
                    ipAd = "ERROR";
                }
                String[] parts = ipAd.split("/");
                String ipName = parts[parts.length - 1];

                String message = inputField.getText();
                if (!message.isEmpty()) {
                    out.println(ipName + ": " + message);
                    inputField.setText("");
                }
            }
        });

        inputField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendButton.doClick();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        sendfileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                int option = fileChooser.showOpenDialog(null);
                if (option == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    new Thread(() -> {
                        FileSender.sendFile(file, serverIp, serverPort + 1);
                        chatArea.append(file.getName() + " 已传输完成！\n");
                    }).start();
                }
            }
        });

        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DrawFrame drawFrame = new DrawFrame(ChatFrame.this);
                drawFrame.setVisible(true);
            }
        });

        connectToServer();
        new Thread(new IncomingReader()).start();
        setVisible(true);
    }

    private void connectToServer() {
        try {
            socket = new Socket(serverIp, serverPort);
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class IncomingReader implements Runnable {
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message;
                while ((message = reader.readLine()) != null) {
                    chatArea.append(message + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendDrawing(File drawingFile) {
        new Thread(() -> {
            FileSender.sendFile(drawingFile, serverIp, serverPort + 1);
            chatArea.append(drawingFile.getName() + " 绘图已传输完成！\n");
        }).start();
    }
}