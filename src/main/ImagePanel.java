package main;

import javax.swing.*;
import java.awt.*;

public class ImagePanel extends JPanel {
    private Image image;

    public ImagePanel(String imagePath) {
        // 嘗試從指定路徑加載圖片
        image = Toolkit.getDefaultToolkit().getImage(getClass().getResource(imagePath));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 在JPanel上繪製圖片
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }

    public static void main(String[] args) {
        
    }

    public void setImage(String imagePath) {
        image = Toolkit.getDefaultToolkit().getImage(getClass().getResource(imagePath));
        repaint();
    }
}
