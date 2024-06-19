/*巨信登陆界面_202211304116梁嘉宏*/
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class LoginFrame extends JFrame {
    private JTextArea ipAddress;
    private JTextField ipField;
    private JButton connectButton;
    private JButton backButton;

    public LoginFrame() {
        setTitle("请输入服务器ip地址");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        String ipAd;
        try {
            InetAddress host_address;
            host_address = InetAddress.getLocalHost();
            ipAd = host_address.toString();
        } catch (UnknownHostException a) {
            ipAd = "ERROR";
        }
        String[] parts = ipAd.split("/");
        String ipAdd = "您的ip地址为：" + parts[parts.length - 1];

        ipAddress = new JTextArea(ipAdd);
        ipAddress.setEditable(false);
        ipField = new JTextField();
        connectButton = new JButton("连接");
        backButton = new JButton("返回");

        JPanel panel = new JPanel();
        panel.setLayout(null);
        ipAddress.setBounds(50, 20, 300, 30);
        ipField.setBounds(50, 60, 300, 30);
        connectButton.setBounds(220, 110, 100, 30);
        backButton.setBounds(70, 110, 100, 30);

        panel.add(ipAddress);
        panel.add(ipField);
        panel.add(connectButton);
        panel.add(backButton);
        add(panel);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MainFrame().setVisible(true);
                dispose();
            }
        });

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });

        ipField.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    connectToServer();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    private void connectToServer() {
        String ip = ipField.getText();
        int port = 10579;

        try {
            Socket socket = new Socket(ip, port);
            new Thread(() -> FileReceiver.main(new String[0])).start();
            // 连接成功，打开ChatFrame
            new ChatFrame(ip, ip, port);
            dispose();
        } catch (IOException e) {
            // 捕获异常并显示错误信息
            JOptionPane.showMessageDialog(this, "ERROR:404\n找不到服务器!", "网络错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame().setVisible(true);
            }
        });
    }
}
