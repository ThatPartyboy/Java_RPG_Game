package main;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ItemPanel extends JPanel implements Runnable {

    Image[] image = new Image[6];

    boolean isItem1Clicked = false;
    boolean isItem2Clicked = false;
    boolean isItem3Clicked = false;
    boolean isTalkedToNPC = false;
    boolean isMap1Used = false;
    boolean isChestOpened = false;
    boolean isChest2Opened = false;

    public ItemPanel() {

        this.setPreferredSize(new Dimension(216, 68));

        try {
            image[0] = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/item/item01.png"));
            image[1] = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/item/item02.png"));
            image[2] = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/item/item03.png"));
            image[3] = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/item/wand.png"));
            image[4] = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/item/sword.png"));
            image[5] = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/item/key.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                if (isTalkedToNPC) {
                    if (evt.getX() > 24 && evt.getX() < 24 + 48 && evt.getY() > 12 && evt.getY() < 12 + 48) {
                        isItem1Clicked = true;
                        JFrame dialogueWindow = new JFrame();
                        dialogueWindow.setResizable(false);
                        dialogueWindow.setTitle("對話框");
                        String[] dialogues = {
                                "「移動大地之魔杖」",
                                "魔杖能讓你輕易移動大地",
                                "創造你自己的道路！",
                                "注意：玩家所在地不可移動！",
                        };
                        DialoguePanel dialoguePanel = new DialoguePanel(dialogues);
                        dialogueWindow.add(dialoguePanel);
                        dialogueWindow.pack();
                        dialogueWindow.setLocationRelativeTo(null);
                        dialogueWindow.setLocation(680, 800);
                        dialogueWindow.setVisible(true);
                    }
                    if (evt.getX() > 84 && evt.getX() < 84 + 48 && evt.getY() > 12 && evt.getY() < 12 + 48) {
                        isItem2Clicked = true;
                        if (!isMap1Used) {
                            JFrame dialogueWindow = new JFrame();
                            dialogueWindow.setResizable(false);
                            dialogueWindow.setTitle("對話框");
                            String[] dialogues = {
                                    "已使用「地圖：傳說寶劍」",
                            };
                            DialoguePanel dialoguePanel = new DialoguePanel(dialogues);
                            dialogueWindow.add(dialoguePanel);
                            dialogueWindow.pack();
                            dialogueWindow.setLocationRelativeTo(null);
                            dialogueWindow.setLocation(680, 800);
                            dialogueWindow.setVisible(true);
                            isMap1Used = true;
                        } else if (isMap1Used && isChestOpened) {
                            JFrame dialogueWindow = new JFrame();
                            dialogueWindow.setResizable(false);
                            dialogueWindow.setTitle("對話框");
                            String[] dialogues = {
                                    "「藏在深山的「傳說寶劍」」",
                                    "有了傳說寶劍，才能打敗魔王",
                            };
                            DialoguePanel dialoguePanel = new DialoguePanel(dialogues);
                            dialogueWindow.add(dialoguePanel);
                            dialogueWindow.pack();
                            dialogueWindow.setLocationRelativeTo(null);
                            dialogueWindow.setLocation(680, 800);
                            dialogueWindow.setVisible(true);
                            isMap1Used = true;
                        }
                    }
                    if (evt.getX() > 144 && evt.getX() < 144 + 48 && evt.getY() > 12 && evt.getY() < 12 + 48) {
                        isItem3Clicked = true;

                        if (!isChestOpened) {
                            JFrame dialogueWindow = new JFrame();
                            dialogueWindow.setResizable(false);
                            dialogueWindow.setTitle("對話框");
                            String[] dialogues = {
                                    "請先完成「地圖：傳說寶劍」任務",
                            };
                            DialoguePanel dialoguePanel = new DialoguePanel(dialogues);
                            dialogueWindow.add(dialoguePanel);
                            dialogueWindow.pack();
                            dialogueWindow.setLocationRelativeTo(null);
                            dialogueWindow.setLocation(680, 800);
                            dialogueWindow.setVisible(true);
                        } else if (!isChest2Opened) {

                            JFrame dialogueWindow = new JFrame();
                            dialogueWindow.setResizable(false);
                            dialogueWindow.setTitle("對話框");
                            String[] dialogues = {
                                    "已使用「迷宮森林的提示」",
                                    "上面寫著看不懂的古文",
                                    "「Pseudocode」, 「玩家獲得鑰匙」"
                            };
                            DialoguePanel dialoguePanel = new DialoguePanel(dialogues);
                            dialogueWindow.add(dialoguePanel);
                            dialogueWindow.pack();
                            dialogueWindow.setLocationRelativeTo(null);
                            dialogueWindow.setLocation(680, 800);
                            dialogueWindow.setVisible(true);
                        } else {
                            JFrame dialogueWindow = new JFrame();
                            dialogueWindow.setResizable(false);
                            dialogueWindow.setTitle("對話框");
                            String[] dialogues = {
                                    "迷宮森林的「秘密鑰匙」",
                                    "你現在能通往魔王所在地了！"
                            };
                            DialoguePanel dialoguePanel = new DialoguePanel(dialogues);
                            dialogueWindow.add(dialoguePanel);
                            dialogueWindow.pack();
                            dialogueWindow.setLocationRelativeTo(null);
                            dialogueWindow.setLocation(680, 800);
                            dialogueWindow.setVisible(true);
                        }
                    }
                } else {
                    // isItem3Clicked = false;
                    // isItem2Clicked = false;
                    // isItem3Clicked = false;

                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        g.drawImage(image[0], 12, 0, 48 * 4, 17 * 4, this);

        if (isTalkedToNPC) {
            g.drawImage(image[3], 24, 12, 48, 48, this);
        }
        if (isTalkedToNPC && !isItem2Clicked) {
            g.drawImage(image[1], 84, 12, 48, 48, this);
        }
        if (isTalkedToNPC && !isChest2Opened) {
            g.drawImage(image[2], 144, 12, 48, 48, this);
        }

        if (isChestOpened) {
            g.drawImage(image[4], 84, 12, 48, 48, this);
        }
        if (isChest2Opened) {
            g.drawImage(image[5], 144, 12, 48, 48, this);
        }

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
