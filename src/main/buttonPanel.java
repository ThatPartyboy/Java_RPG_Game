package main;

import java.awt.*;

import javax.swing.JPanel;

public class buttonPanel extends JPanel implements Runnable {

    boolean isButtonClicked = false;
    boolean isButtonHovered = false;
    public boolean canButtonBeClicked = false;

    Image[] image = new Image[5];

    public buttonPanel() {

        this.setPreferredSize(new Dimension(128, 68));

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                isButtonHovered = true;
                repaint(); // 重繪按鈕
                // System.out.println("button hovered");
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                isButtonHovered = false;
                repaint(); // 重繪按鈕
                // System.out.println("button not hovered");
            }

            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (canButtonBeClicked) {
                    isButtonClicked = true;
                    repaint(); // 重繪按鈕
                    // System.out.println("button clicked");
                }
            }
        });

        try {
            image[0] = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/tiles/button01.png"));
            image[1] = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/tiles/button02.png"));
            image[2] = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/tiles/button03.png"));
            image[3] = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/tiles/button04.png"));
            image[4] = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/tiles/button05.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // 覆蓋 paintComponent 方法來繪製按鈕顏色
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (isButtonClicked) {
            g.drawImage(image[2], 0, 0, 32 * 4, 17 * 4, this);
        } else if (canButtonBeClicked && isButtonHovered) {
            g.drawImage(image[4], 0, 0, 32 * 4, 17 * 4, this);
        } else if (canButtonBeClicked) {
            g.drawImage(image[3], 0, 0, 32 * 4, 17 * 4, this);
        } else if (isButtonHovered) {
            // setBackground(Color.GRAY); // 默認顏色，可以自行設置
            g.drawImage(image[1], 0, 0, 32 * 4, 17 * 4, this);
        } else {
            // setBackground(Color.WHITE); // 默認顏色，可以自行設置
            g.drawImage(image[0], 0, 0, 32 * 4, 17 * 4, this);
        }

    }

    // 設置 canButtonBeClicked 的方法，並更新顏色
    public void setCanButtonBeClicked(boolean canBeClicked) {
        this.canButtonBeClicked = canBeClicked;
    }

    Thread gameThread;
    int FPS = 60;

    public void startGameThread() {

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void stopGameThread() {
        gameThread = null;
    }

    @Override
    public void run() {

        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }

            if (timer >= 1000000000) {
                timer = 0;
            }
        }

    }

    public void update() {
    }

}
