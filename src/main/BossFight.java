package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;

import java.util.List;
import java.util.Random;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class BossFight implements Runnable {

    Thread gameThread;
    int FPS = 60;

    int bossWIndowX = 767;
    int bossWIndowY = 0;

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public static void main(String[] args) {
        System.out.println("Boss Fight!");
        new Main().startGameThread();
    }

    private Clip clip;

	private void playSound(String soundFileName) {
        try {
            if (clip != null && clip.isRunning()) {
                clip.stop(); // 停止正在播放的音乐
            }
            File soundFile = new File(soundFileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            if (clip == null) {
                clip = AudioSystem.getClip();
            }
            clip.open(audioInputStream);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void playSound2(String soundFileName) {
        try {
            File soundFile = new File(soundFileName);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

	private void stopSound() {
        if (clip != null) {
            clip.stop(); // 使用Clip的stop方法停止音乐
        }
    }

    @Override
    public void run() {
        playSound("res/music/boss.wav");
        JFrame window_Boss = new JFrame("Boss");
        window_Boss.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window_Boss.setSize(386, 386);
        window_Boss.setResizable(false);
        window_Boss.setLocationRelativeTo(null);
        window_Boss.setLocation(767, 0);
        window_Boss.setVisible(true);
        ImagePanel panel_Boss = new ImagePanel("/tiles/boss.png");
        window_Boss.add(panel_Boss);
        window_Boss.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {
                // 當窗口移動時，重新設置固定位置
                window_Boss.setLocation(bossWIndowX, bossWIndowY);

            }
        });

        JFrame window_Player = new JFrame("玩家");
        window_Player.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window_Player.setSize(214, 214);
        window_Player.setResizable(false);
        window_Player.setLocationRelativeTo(null);
        window_Player.setLocation(855, 800);
        window_Player.setVisible(true);
        ImagePanel panel_Player = new ImagePanel("/tiles/player.png");
        window_Player.add(panel_Player);
        window_Player.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e) {

            }
        });

        JFrame window_Heart = new JFrame("玩家血量");
        window_Heart.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window_Heart.setSize(170, 86);
        window_Heart.setResizable(false);
        window_Heart.setLocationRelativeTo(null);
        window_Heart.setLocation(0, 100);
        window_Heart.setVisible(true);
        HeartPanel panel_Heart = new HeartPanel(3, true);
        window_Heart.add(panel_Heart);

        JFrame window_Heart2 = new JFrame("Boss血量");
        window_Heart2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window_Heart2.setSize(255, 86);
        window_Heart2.setResizable(false);
        window_Heart2.setLocationRelativeTo(null);
        window_Heart2.setLocation(0, 0);
        window_Heart2.setVisible(true);
        HeartPanel panel_Heart2 = new HeartPanel(5, false);
        window_Heart2.add(panel_Heart2);

        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;

        double speedX = 2;
        double speedY = 1;
        boolean isBossIdle = false;
        int bossMoveIndex = 0;
        int bossAttackIndex = 0;
        boolean isBossHurt = false;
        int bossHurtIndex = 0;

        int playerHealth = 3;
        int bossHealth = 5;
        boolean isPlayerHurt = false;
        int playerHurtIndex = 0;

        boolean isPlayerDead = false;
        boolean isBossDead = false;

        List<JFrame> bossAttackList = new ArrayList<>();
        List<JFrame> bossGuardList = new ArrayList<>();

        DialoguePanel dialoguePanel2 = null;
        DialoguePanel dialoguePanel3 = null;

        JFrame dialogueWindow = new JFrame();
        dialogueWindow.setResizable(false);
        dialogueWindow.setTitle("對話框");
        String[] dialogues = {
                "沒想到你竟然能來到這",
                "受死吧！！！",
                "(快拿起寶劍打敗他, 勇者！)",
                " "
        };
        DialoguePanel dialoguePanel = new DialoguePanel(dialogues);
        dialogueWindow.add(dialoguePanel);
        dialogueWindow.pack();
        dialogueWindow.setLocationRelativeTo(null);
        dialogueWindow.setLocation(767, 600);
        dialogueWindow.setVisible(true);

        while (gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                // update();
                // repaint();
                // boss move up and dow
                if (dialoguePanel != null) {
                    if (dialoguePanel.isDialogueReach(3)) {
                        isBossIdle = true;
                        dialoguePanel.shutdown();
                        dialoguePanel = null;
                    }
                }

                if (isBossIdle) {
                    switch (bossMoveIndex) {
                        case 0:
                            bossWIndowX += speedX;
                            bossWIndowY += speedY;
                            break;
                        case 1:
                            bossWIndowX -= speedX;
                            bossWIndowY -= speedY;
                            break;
                        case 2:
                            bossWIndowX -= speedX;
                            bossWIndowY += speedY;
                            break;
                        case 3:
                            bossWIndowX += speedX;
                            bossWIndowY -= speedY;
                            break;
                        default:
                            break;
                    }
                }
                window_Boss.setLocation(bossWIndowX, bossWIndowY);

                if (window_Player.getX() <= bossWIndowX + 386
                        && window_Player.getX() + 214 >= bossWIndowX
                        && window_Player.getY() <= bossWIndowY + 386
                        && window_Player.getY() + 214 >= bossWIndowY && !isBossHurt) {
                    bossHealth--;
                    System.out.println(bossHealth);
                    panel_Boss.setImage("/tiles/boss_hurt.png");
                    playSound2("res/music/hit1.wav");
                    panel_Heart2.setHeartCount(bossHealth);
                    isBossHurt = true;
                }

                for (JFrame bossAttack : bossAttackList) {
                    int x = bossAttack.getX();
                    int y = bossAttack.getY();
                    bossAttack.setLocation(x, y + 1);
                    if (y > 1200) {
                        bossAttack.dispose();
                    }

                    if (window_Player.getX() <= bossAttack.getX() + 128
                            && window_Player.getX() + 200 >= bossAttack.getX()
                            && window_Player.getY() <= bossAttack.getY() + 128
                            && window_Player.getY() + 200 >= bossAttack.getY() && !isPlayerHurt) {
                        playerHealth--;
                        panel_Heart.setHeartCount(playerHealth);
                        System.out.println(playerHealth);
                        panel_Player.setImage("/tiles/player_hurt.png");
                        playSound2("res/music/hit2.wav");
                        isPlayerHurt = true;
                    }

                }

                for (int i = 0; i < bossGuardList.size(); i++) {
                    JFrame bossGuard = bossGuardList.get(i);
                    if (bossGuard.getX() <= window_Player.getX() + 200
                            && bossGuard.getX() + 128 >= window_Player.getX()
                            && bossGuard.getY() <= window_Player.getY() + 200
                            && bossGuard.getY() + 128 >= window_Player.getY() && !isPlayerHurt) {
                        playerHealth--;
                        panel_Heart.setHeartCount(playerHealth);
                        System.out.println(playerHealth);
                        panel_Player.setImage("/tiles/player_hurt.png");
                        playSound2("res/music/hit2.wav");
                        isPlayerHurt = true;
                    }

                    if (i == 0) {
                        bossGuard.setLocation(bossWIndowX - 100, bossWIndowY);
                    } else if (i == 1) {
                        bossGuard.setLocation(bossWIndowX + 300, bossWIndowY);
                    } else if (i == 2) {
                        bossGuard.setLocation(bossWIndowX - 100, bossWIndowY + 250);
                    } else if (i == 3) {
                        bossGuard.setLocation(bossWIndowX + 300, bossWIndowY + 250);
                    } else if (i == 4) {
                        bossGuard.setLocation(bossWIndowX + 110, bossWIndowY + 300);
                    }
                }

                if (playerHealth <= 0) {
                    if (!isPlayerDead) {
                        isPlayerDead = true;

                        isBossIdle = false;
                        isBossHurt = false;
                        isPlayerHurt = false;
                        panel_Player.setImage("/tiles/player.png");
                        panel_Boss.setImage("/tiles/boss.png");
                        for (JFrame bossGuard : bossGuardList) {
                            bossGuard.dispose();
                        }
                        bossGuardList = new ArrayList<>();
                        for (JFrame bossAttack : bossAttackList) {
                            bossAttack.dispose();
                        }
                        bossAttackList = new ArrayList<>();

                        JFrame dialogueWindow2 = new JFrame();
                        dialogueWindow2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                        dialogueWindow2.setResizable(false);
                        dialogueWindow2.setTitle("對話框");
                        String[] dialogues2 = {
                                "加油！你可以的！",
                                "再試一次！",
                        };
                        dialoguePanel2 = new DialoguePanel(dialogues2);
                        dialogueWindow2.add(dialoguePanel2);
                        dialogueWindow2.pack();
                        dialogueWindow2.setLocationRelativeTo(null);
                        dialogueWindow2.setLocation(767, 600);
                        dialogueWindow2.setVisible(true);
                    }
                    if (isPlayerDead) {
                        if (dialoguePanel2.isDialogueReach(1)) {
                            isPlayerDead = false;
                            isBossIdle = true;
                            playerHealth = 3;
                            bossHealth = 5;
                            panel_Heart.setHeartCount(playerHealth);
                            panel_Heart2.setHeartCount(bossHealth);
                            window_Player.setLocation(855, 800);
                            window_Boss.setLocation(767, 0);
                        }
                    }

                } else if (bossHealth <= 0) {
                    if (!isBossDead) {
                        playSound2("res/music/explosion.wav");
                        isBossDead = true;

                        isBossIdle = false;
                        isBossHurt = false;
                        isPlayerHurt = false;

                        for (JFrame bossGuard : bossGuardList) {
                            bossGuard.dispose();
                        }
                        for (JFrame bossAttack : bossAttackList) {
                            bossAttack.dispose();
                        }
                        bossAttackList = new ArrayList<>();
                        bossGuardList = new ArrayList<>();

                        JFrame dialogueWindow3 = new JFrame();
                        dialogueWindow3.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                        dialogueWindow3.setResizable(false);
                        dialogueWindow3.setTitle("對話框");
                        String[] dialogues3 = {
                                "你成功打敗魔王了！！！",
                                "世界再度恢復和平了",
                                "感謝您的遊玩",
                                " "
                        };
                        dialoguePanel3 = new DialoguePanel(dialogues3);
                        dialogueWindow3.add(dialoguePanel3);
                        dialogueWindow3.pack();
                        dialogueWindow3.setLocationRelativeTo(null);
                        dialogueWindow3.setLocation(767, 600);
                        dialogueWindow3.setVisible(true);
                    }
                    if (isBossDead) {
                        if (dialoguePanel3.isDialogueReach(3)) {
                            System.exit(0);
                        }
                    }
                }

                delta--;
            }

            if (timer >= 1000000000) {
                timer = 0;

                if (isBossIdle) {
                    bossMoveIndex++;
                    if (bossMoveIndex >= 4) {
                        bossMoveIndex = 0;
                    }

                    bossAttackIndex++;
                }
                if (bossHealth <= 2) {
                    bossAttackIndex++;
                }
                if (bossAttackIndex >= 2 && isBossIdle) {
                    bossAttackIndex = 0;

                    JFrame window_BossAttack = new JFrame("Boss Attack");
                    window_BossAttack.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                    window_BossAttack.setSize(128, 128);
                    window_BossAttack.setResizable(false);
                    window_BossAttack.setLocationRelativeTo(null);
                    int random = new Random().nextInt(1800);
                    window_BossAttack.setLocation(random, 0);
                    window_BossAttack.setVisible(true);
                    ImagePanel panel_BossAttack = new ImagePanel("/tiles/attack.png");
                    window_BossAttack.add(panel_BossAttack);
                    bossAttackList.add(window_BossAttack);

                }

                if (isPlayerHurt) {
                    playerHurtIndex++;
                    if (playerHurtIndex >= 3) {
                        isPlayerHurt = false;
                        playerHurtIndex = 0;
                        panel_Player.setImage("/tiles/player.png");
                    }
                }

                if (isBossHurt && isBossIdle) {

                    if (bossHurtIndex == 1) {
                        for (int i = 0; i < 5; i++) {
                            JFrame window_BossAttack = new JFrame("盾牌");
                            window_BossAttack.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                            window_BossAttack.setSize(192, 192);
                            window_BossAttack.setResizable(false);
                            window_BossAttack.setLocationRelativeTo(null);
                            window_BossAttack.setLocation(bossWIndowX, bossWIndowY + 100 * i);
                            window_BossAttack.setVisible(true);
                            ImagePanel panel_BossAttack = new ImagePanel("/tiles/shield.png");
                            window_BossAttack.add(panel_BossAttack);
                            bossGuardList.add(window_BossAttack);
                        }
                    }
                    bossHurtIndex++;
                    if (bossHurtIndex >= 5) {
                        isBossHurt = false;
                        bossHurtIndex = 0;
                        panel_Boss.setImage("/tiles/boss.png");

                        for (JFrame bossGuard : bossGuardList) {
                            bossGuard.dispose();
                        }
                        bossGuardList = new ArrayList<>();
                    }

                    // spawn 5 boss attack
                }
            }
        }
    }
}
