package main;

import javax.swing.*;
import java.awt.*;

public class HeartPanel extends JPanel {
    private Image image;
    public int heartCount = 3;

    public HeartPanel(int heartCount, boolean isPlayer) {
        this.setBackground(Color.black);
        // 嘗試從指定路徑加載圖片
        if (isPlayer) {
            image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/tiles/heart.png"));
        } else {
            image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/tiles/heart_boss.png"));
        }
        this.heartCount = heartCount;

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 在JPanel上繪製圖片
        if (heartCount > 0) {
            for (int i = 0; i < heartCount; i++) {
                g.drawImage(image, i * 48, 0, 48, 48, this);
            }
        }
    }

    public static void main(String[] args) {

    }

    public void setImage(String imagePath) {
        image = Toolkit.getDefaultToolkit().getImage(getClass().getResource(imagePath));
        repaint();
    }

    public void setHeartCount(int count) {
        heartCount = count;
        repaint();
    }
}
