/*巨信主程序1.0_202211304116梁嘉宏*/
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class MainFrame extends JFrame {
    private JButton ServerButton;
    private JButton LoginButton;
    private JLabel imageLabel;
    public MainFrame() {
        setTitle("JuChat 1.0");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        ImageIcon icon = new ImageIcon("src/Resources/logo.png");       //自定义logo
        imageLabel = new JLabel(icon);
        ServerButton = new JButton("创建聊天服务器");
        LoginButton = new JButton("加入巨信聊天室");
        JPanel panel = new JPanel();
        panel.setLayout(null);
        imageLabel.setBounds(50, 20, 300, 116);
        ServerButton.setBounds(100, 160, 200, 30);
        LoginButton.setBounds(100, 210, 200, 30);
        panel.add(imageLabel);
        panel.add(ServerButton);
        panel.add(LoginButton);
        add(panel);
        ServerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new Thread(() -> ChatServer.main(new String[0])).start();
                String ipAd;
                try
                {
                    InetAddress host_address;
                    host_address=InetAddress.getLocalHost();
                    ipAd=host_address.toString();
                }
                catch(UnknownHostException a)
                {
                    ipAd="/ERROR:NOT FOUND Internet Card.";
                }
                String[] parts = ipAd.split("/");
                String ipAddress = parts[parts.length - 1];
                JOptionPane.showMessageDialog(null, "服务器已启动，服务器ip为："+ipAddress);
            }
        });
        LoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
}