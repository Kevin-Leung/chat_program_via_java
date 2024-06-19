/*巨信绘画窗口_202211304116梁嘉宏*/
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DrawFrame extends JFrame {
    private BufferedImage canvas;
    private Graphics2D g2d;
    private int lastX, lastY;
    private Color currentColor = Color.BLACK;

    public DrawFrame(ChatFrame chatClient) {
        setTitle("绘图板");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        canvas = new BufferedImage(400, 300, BufferedImage.TYPE_INT_ARGB);
        g2d = canvas.createGraphics();
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        g2d.setColor(currentColor);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        DrawPanel drawPanel = new DrawPanel();
        panel.add(drawPanel, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        JButton saveButton = new JButton("保存并发送");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File file = new File("drawing.png");
                    ImageIO.write(canvas, "png", file);
                    chatClient.sendDrawing(file);
                    dispose();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        JButton colorButton = new JButton("选择笔触颜色");
        colorButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Color selectedColor = JColorChooser.showDialog(null, "选择笔触颜色", currentColor);
                if (selectedColor != null) {
                    currentColor = selectedColor;
                    g2d.setColor(currentColor);
                }
            }
        });

        controls.add(colorButton);
        controls.add(saveButton);
        panel.add(controls, BorderLayout.SOUTH);

        add(panel);

        drawPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                lastX = e.getX();
                lastY = e.getY();
            }
        });

        drawPanel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();
                g2d.drawLine(lastX, lastY, x, y);
                lastX = x;
                lastY = y;
                drawPanel.repaint();
            }
        });
    }

    private class DrawPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(canvas, 0, 0, null);
        }
    }
}
