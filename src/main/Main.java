package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class Main implements Runnable {

	public static int playerInWindowNum = 1;
	public static String playerDirection;
	public static boolean isMountainSpawned = false;
	public static boolean isBossSpawned = false;
	public static boolean isTalkingToNPC = false;
	public static boolean isTeleported = false;
	public static boolean isOpenChest = false;
	public static boolean isOpenChest2 = false;
	public static boolean isMoveable = false;

	public static int[] playerOrder = { 0, 0, 0, 0 };
	public static int[] correctOrder = { 5, 4, 2, 3 };
	public static boolean orderStart = false;
	public static int orderIndex = 0;

	boolean pass1 = false;
	boolean pass2 = false;
	boolean pass3 = false;

	// up down left right
	public static boolean[] canGoToWhere = new boolean[4];
	// up down left right
	public static int[] nextWindowNum = new int[4];

	public static int[] windowX = new int[10];
	public static int[] windowY = new int[10];
	// window list
	public static JFrame[] windowList = new JFrame[10];
	// gamePanel list
	public static GamePanel[] gamePanelList = new GamePanel[10];
	// dialoguePanel
	DialoguePanel dialoguePanel;

	Thread gameThread;
	int FPS = 60;

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	public void endGameThread() {
		gameThread = null;
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
		playSound("res/music/background.wav");
		// #region window_button
		JFrame window_button = new JFrame();
		window_button.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		window_button.setResizable(false);
		window_button.setTitle("button");
		buttonPanel buttonPanel = new buttonPanel();
		window_button.add(buttonPanel);
		window_button.pack();
		window_button.setLocationRelativeTo(null);
		window_button.setLocation(1000, 900);
		window_button.setVisible(true);
		buttonPanel.startGameThread();
		// #endregion

		// #region window_item
		JFrame itemWindow = new JFrame();
		itemWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		itemWindow.setResizable(false);
		itemWindow.setTitle("物品欄");
		ItemPanel itemPanel = new ItemPanel();
		itemWindow.add(itemPanel);
		itemWindow.pack();
		itemWindow.setLocationRelativeTo(null);
		itemWindow.setLocation(750, 900);
		itemWindow.setVisible(true);
		itemPanel.startGameThread();
		itemWindow.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				// 當窗口移動時，重新設置固定位置
				itemWindow.setLocation(750, 900);
			}
		});
		// #endregion

		// #region window_1
		JFrame window_1 = new JFrame();
		window_1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window_1.setResizable(false);
		window_1.setTitle("home");
		windowList[1] = window_1;

		GamePanel gamePanel_1 = new GamePanel(1, 6, 6);
		gamePanelList[1] = gamePanel_1;
		window_1.add(gamePanel_1);

		window_1.pack();

		window_1.setLocationRelativeTo(null);
		window_1.setLocation(0, 0);
		window_1.setVisible(true);

		gamePanel_1.startGameThread();
		gamePanel_1.isPlayerInWindow = true;

		window_1.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				// 當窗口移動時，重新設置固定位置
				window_1.setLocation(0, 0);
			}
		});

		// #endregion

		// #region window_2
		JFrame window_2 = new JFrame();
		window_2.setResizable(false);
		window_2.setTitle("森林小徑1");
		window_2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		windowList[2] = window_2;
		GamePanel gamePanel_2 = new GamePanel(2, 5, 5);
		gamePanelList[2] = gamePanel_2;
		window_2.add(gamePanel_2);
		window_2.pack();
		window_2.setLocationRelativeTo(null);
		window_2.setLocation(288, 368);
		windowX[2] = window_2.getLocation().x;
		windowY[2] = window_2.getLocation().y;
		window_2.setVisible(true);
		gamePanel_2.startGameThread();
		window_2.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				// 當窗口移動時，重新設置固定位置

				if (!isMoveable)
					window_2.setLocation(windowX[2], windowY[2]);
				else if (!itemPanel.isChestOpened && playerInWindowNum == 2)
					window_2.setLocation(windowX[2], windowY[2]);
			}

		});
		// #endregion

		// #region window_3
		JFrame window_3 = new JFrame();
		window_3.setResizable(false);
		window_3.setTitle("森林小徑2");
		window_3.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		windowList[3] = window_3;
		GamePanel gamePanel_3 = new GamePanel(3, 5, 5);
		gamePanelList[3] = gamePanel_3;
		window_3.add(gamePanel_3);
		window_3.pack();
		window_3.setLocationRelativeTo(null);
		window_3.setLocation(800, 467);
		windowX[3] = window_3.getLocation().x;
		windowY[3] = window_3.getLocation().y;
		window_3.setVisible(true);
		gamePanel_3.startGameThread();
		window_3.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				// 當窗口移動時，重新設置固定位置
				if (!isMoveable)
					window_3.setLocation(windowX[3], windowY[3]);
				else if (!itemPanel.isChestOpened && playerInWindowNum == 3)
					window_3.setLocation(windowX[3], windowY[3]);
			}

		});
		// #endregion

		// #region window_4
		JFrame window_4 = new JFrame();
		window_4.setResizable(false);
		window_4.setTitle("森林小徑3");
		window_4.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		windowList[4] = window_4;
		GamePanel gamePanel_4 = new GamePanel(4, 5, 5);
		gamePanelList[4] = gamePanel_4;
		window_4.add(gamePanel_4);
		window_4.pack();
		window_4.setLocationRelativeTo(null);
		window_4.setLocation(288, 100);
		windowX[4] = window_4.getLocation().x;
		windowY[4] = window_4.getLocation().y;
		window_4.setVisible(true);
		gamePanel_4.startGameThread();
		window_4.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				// 當窗口移動時，重新設置固定位置

				if (!isMoveable)
					window_4.setLocation(windowX[4], windowY[4]);
				else if (!itemPanel.isChestOpened && playerInWindowNum == 4)
					window_4.setLocation(windowX[4], windowY[4]);
			}

		});
		// #endregion

		// #region window_5
		JFrame window_5 = new JFrame();
		window_5.setResizable(false);
		window_5.setTitle("森林小徑4");
		window_5.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		windowList[5] = window_5;
		GamePanel gamePanel_5 = new GamePanel(5, 5, 5);
		gamePanelList[5] = gamePanel_5;
		window_5.add(gamePanel_5);
		window_5.pack();
		window_5.setLocationRelativeTo(null);
		window_5.setLocation(1200, 243);
		windowX[5] = window_5.getLocation().x;
		windowY[5] = window_5.getLocation().y;
		window_5.setVisible(true);
		gamePanel_5.startGameThread();
		window_5.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				// 當窗口移動時，重新設置固定位置
				if (!isMoveable)
					window_5.setLocation(windowX[5], windowY[5]);
				else if (!itemPanel.isChestOpened && playerInWindowNum == 5)
					window_5.setLocation(windowX[5], windowY[5]);
			}

		});
		// #endregion

		// #region window_6
		JFrame window_6 = new JFrame();
		window_6.setResizable(false);
		window_6.setTitle("村莊");
		windowList[6] = window_6;
		window_6.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		GamePanel gamePanel_6 = new GamePanel(6, 5, 7);
		gamePanelList[6] = gamePanel_6;
		window_6.add(gamePanel_6);
		window_6.pack();
		window_6.setLocationRelativeTo(null);
		window_6.setVisible(true);
		gamePanel_6.startGameThread();
		window_6.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentMoved(ComponentEvent e) {
				// 當窗口移動時，重新設置固定位置
				window_6.setLocation(48, 318);
			}
		});
		// #endregion

		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;

		while (gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += currentTime - lastTime;
			lastTime = currentTime;

			if (delta >= 1) {
				Update();
				delta--;

				if(buttonPanel.isButtonClicked){
					playSound2("res/music/button.wav");
				}

				// #region Teleport/Event
				if (playerDirection == "up") {
					if (nextWindowNum[0] == 3) {
						buttonPanel.canButtonBeClicked = true;
						if (buttonPanel.isButtonClicked) {
							orderIndex = 3;
							TeleportPlayer(3, 1, 3);
							buttonPanel.isButtonClicked = false;
						}
					} else if (nextWindowNum[0] == 4) {
						buttonPanel.canButtonBeClicked = true;
						if (buttonPanel.isButtonClicked) {
							orderIndex = 4;
							TeleportPlayer(4, 3, 3);
							buttonPanel.isButtonClicked = false;
						}
					} else if (nextWindowNum[0] == 7) {
						buttonPanel.canButtonBeClicked = true;
						if (buttonPanel.isButtonClicked) {
							TeleportPlayer(7, 2, 3);
							buttonPanel.isButtonClicked = false;
						}
					} else {
						buttonPanel.canButtonBeClicked = false;
					}
				} else if (playerDirection == "down") {
					if (nextWindowNum[1] == 2) {
						buttonPanel.canButtonBeClicked = true;
						if (buttonPanel.isButtonClicked) {
							orderIndex = 2;
							TeleportPlayer(2, 3, 1);
							buttonPanel.isButtonClicked = false;
						}
					} else if (nextWindowNum[1] == 3) {
						buttonPanel.canButtonBeClicked = true;
						if (buttonPanel.isButtonClicked) {
							orderIndex = 3;
							TeleportPlayer(4, 3, 1);
							buttonPanel.isButtonClicked = false;
						}
					} else if (nextWindowNum[1] == 5) {
						buttonPanel.canButtonBeClicked = true;
						if (buttonPanel.isButtonClicked) {
							orderIndex = 5;
							TeleportPlayer(5, 1, 1);
							buttonPanel.isButtonClicked = false;
						}
					} else if (nextWindowNum[1] == 8) {
						buttonPanel.canButtonBeClicked = true;
						if (buttonPanel.isButtonClicked) {
							orderIndex = 5;
							TeleportPlayer(8, 2, 1);
							buttonPanel.isButtonClicked = false;
						}
					} else {
						buttonPanel.canButtonBeClicked = false;
					}
				} else if (playerDirection == "left") {
					if (nextWindowNum[2] == 1) {
						buttonPanel.canButtonBeClicked = true;
						if (buttonPanel.isButtonClicked) {
							TeleportPlayer(1, 4, 3);
							buttonPanel.isButtonClicked = false;
						}
					} else if (nextWindowNum[2] == 3) {
						buttonPanel.canButtonBeClicked = true;
						if (buttonPanel.isButtonClicked) {
							orderIndex = 3;
							TeleportPlayer(3, 3, 1);
							buttonPanel.isButtonClicked = false;
						}
					} else if (nextWindowNum[2] == 5) {
						buttonPanel.canButtonBeClicked = true;
						if (buttonPanel.isButtonClicked) {
							orderIndex = 5;
							TeleportPlayer(5, 3, 3);
							buttonPanel.isButtonClicked = false;
						}
					} else if (nextWindowNum[2] == 6) {
						buttonPanel.canButtonBeClicked = true;
						if (buttonPanel.isButtonClicked) {
							TeleportPlayer(6, 3, 4);
							buttonPanel.isButtonClicked = false;
						}
					} else {
						buttonPanel.canButtonBeClicked = false;
					}
				} else if (playerDirection == "right") {
					if (nextWindowNum[3] == 2) {
						buttonPanel.canButtonBeClicked = true;

						if (buttonPanel.isButtonClicked) {
							orderIndex = 2;
							TeleportPlayer(2, 1, 3);
							buttonPanel.isButtonClicked = false;
						}
					} else if (nextWindowNum[3] == 4) {
						buttonPanel.canButtonBeClicked = true;
						if (buttonPanel.isButtonClicked) {
							orderIndex = 4;
							TeleportPlayer(4, 1, 1);
							buttonPanel.isButtonClicked = false;
						}
					} else if (nextWindowNum[3] == 9) {
						buttonPanel.canButtonBeClicked = true;
						if (buttonPanel.isButtonClicked) {

							BossFight bossFight = new BossFight();
							bossFight.startGameThread();

							for (int i = 0; i < 10; i++) {
								// shutdown all
								if (windowList[i] != null) {
									gamePanelList[i].stopGameThread();
									windowList[i].dispose();
									windowList[i] = null;
									gamePanelList[i] = null;
								}
							}

							stopSound();
							itemWindow.dispose();
							itemPanel.stopGameThread();
							window_button.dispose();
							buttonPanel.stopGameThread();
							isTeleported = true;

							endGameThread();

							buttonPanel.isButtonClicked = false;
						}
					} else {
						buttonPanel.canButtonBeClicked = false;
					}
				} else if (playerDirection == "oldMan") {
					buttonPanel.canButtonBeClicked = true;
					if (buttonPanel.isButtonClicked) {
						isTalkingToNPC = true;
						buttonPanel.isButtonClicked = false;
					} else {
						isTalkingToNPC = false;
					}
				} else if (playerDirection == "chest" && playerInWindowNum == 7) {
					buttonPanel.canButtonBeClicked = true;
					if (buttonPanel.isButtonClicked) {
						isOpenChest = true;
						buttonPanel.isButtonClicked = false;
					} else {
						isOpenChest = false;
					}
				} else if (playerDirection == "chest" && playerInWindowNum == 8) {
					buttonPanel.canButtonBeClicked = true;
					if (buttonPanel.isButtonClicked) {
						isOpenChest2 = true;
						buttonPanel.isButtonClicked = false;
					} else {
						isOpenChest2 = false;
					}
				} else if (playerDirection == "sign") {
					buttonPanel.canButtonBeClicked = true;
					if (buttonPanel.isButtonClicked) {

						if (playerInWindowNum == 2) {
							JFrame dialogueWindow = new JFrame();
							dialogueWindow.setResizable(false);
							dialogueWindow.setTitle("告示牌");
							String[] dialogues = {
									"「+=」"
							};
							DialoguePanel dialoguePanel = new DialoguePanel(dialogues);
							dialogueWindow.add(dialoguePanel);
							dialogueWindow.pack();
							dialogueWindow.setLocationRelativeTo(null);
							dialogueWindow.setLocation(window_2.getLocation().x, window_2.getLocation().y + 220);
							dialogueWindow.setVisible(true);
						}

						if (playerInWindowNum == 3) {
							JFrame dialogueWindow = new JFrame();
							dialogueWindow.setResizable(false);
							dialogueWindow.setTitle("告示牌");
							String[] dialogues = {
									"「Key」"
							};
							DialoguePanel dialoguePanel = new DialoguePanel(dialogues);
							dialogueWindow.add(dialoguePanel);
							dialogueWindow.pack();
							dialogueWindow.setLocationRelativeTo(null);
							dialogueWindow.setLocation(window_3.getLocation().x, window_3.getLocation().y + 220);
							dialogueWindow.setVisible(true);
						}

						if (playerInWindowNum == 4) {
							JFrame dialogueWindow = new JFrame();
							dialogueWindow.setResizable(false);
							dialogueWindow.setTitle("告示牌");
							String[] dialogues = {
									"「.Item」"
							};
							DialoguePanel dialoguePanel = new DialoguePanel(dialogues);
							dialogueWindow.add(dialoguePanel);
							dialogueWindow.pack();
							dialogueWindow.setLocationRelativeTo(null);
							dialogueWindow.setLocation(window_4.getLocation().x, window_4.getLocation().y - 25);
							dialogueWindow.setVisible(true);
						}

						if (playerInWindowNum == 5) {
							JFrame dialogueWindow = new JFrame();
							dialogueWindow.setResizable(false);
							dialogueWindow.setTitle("告示牌");
							String[] dialogues = {
									"「Player」"
							};
							DialoguePanel dialoguePanel = new DialoguePanel(dialogues);
							dialogueWindow.add(dialoguePanel);
							dialogueWindow.pack();
							dialogueWindow.setLocationRelativeTo(null);
							dialogueWindow.setLocation(window_4.getLocation().x, window_4.getLocation().y - 25);
							dialogueWindow.setVisible(true);
						}

						buttonPanel.isButtonClicked = false;
					} else {
						isOpenChest = false;
						isOpenChest2 = false;
					}
				} else {
					buttonPanel.canButtonBeClicked = false;
					// isTalkingToNPC = false;
				}
				// #endregion

				if (dialoguePanel != null) {
					if (dialoguePanel.isDialogueReach(8)) {
						itemPanel.isTalkedToNPC = true;
						dialoguePanel = null;
					}
				}

				if (itemPanel.isItem1Clicked) {
					isMoveable = true;
				}

				// #region SpawnMountain
				if (itemPanel.isItem2Clicked && !isMountainSpawned) {

					// #region window_Mountain
					JFrame mountainWindow = new JFrame();
					mountainWindow.setResizable(false);
					mountainWindow.setTitle("山群");
					mountainWindow.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					GamePanel mountainPanel = new GamePanel(7, 5, 5);
					mountainWindow.add(mountainPanel);
					mountainWindow.pack();
					mountainWindow.setLocation(1680, 90);
					mountainWindow.setVisible(true);
					windowList[7] = mountainWindow;
					gamePanelList[7] = mountainPanel;
					mountainPanel.startGameThread();
					mountainWindow.addComponentListener(new ComponentAdapter() {
						@Override
						public void componentMoved(ComponentEvent e) {
							// 當窗口移動時，重新設置固定位置
							mountainWindow.setLocation(1680, 90);
						}

					});

					int[] x = { 1800, 1750, 1700, 1770, 1730, 1670 };
					int[] y = { 100, 130, 160, 230, 210, 250 };

					for (int i = 0; i < 6; i++) {
						JFrame frame = new JFrame("雲");
						frame.setSize(32, 96); // 設置每個窗口的尺寸
						frame.setLocation(x[i], y[i]);
						ImagePanel panel = new ImagePanel("/tiles/cloud.png"); // 指定圖片的路徑

						frame.add(panel);
						frame.setVisible(true);
					}

					isMountainSpawned = true;
					// #endregion
				}

				if (isOpenChest) {
					itemPanel.isChestOpened = true;
					JFrame dialogueWindow = new JFrame();
					dialogueWindow.setResizable(false);
					dialogueWindow.setTitle("對話框");
					String[] dialogues = {
							"已獲得「傳說寶劍」",
							"你的權杖的能力變得更強大了！",
							"玩家所在地可以移動了"
					};
					DialoguePanel dialoguePanel = new DialoguePanel(dialogues);
					dialogueWindow.add(dialoguePanel);
					dialogueWindow.pack();
					dialogueWindow.setLocationRelativeTo(null);
					dialogueWindow.setLocation(680, 800);
					dialogueWindow.setVisible(true);
					isOpenChest = false;
				}

				if (windowList[7] != null) {
					if (playerInWindowNum != 7 && itemPanel.isChestOpened) {
						windowList[7].dispose();
						gamePanelList[7].stopGameThread();
						windowList[7] = null;
						gamePanelList[7] = null;
					}
				}
				// #endregion

				int[] space = { 0, 0, 0, 0 };
				if (orderIndex == 5 && !orderStart && playerOrder[0] == 0) {
					System.out.println("orderStart");
					orderStart = true;
					playerOrder[0] = 5;
				} else if (orderIndex != 5 && orderStart && !pass1) {
					if (orderIndex != 4) {
						orderStart = false;
						playerOrder = space;
						System.out.println("WrongOrder");
					} else {
						playerOrder[1] = 4;
						pass1 = true;
					}
				} else if (pass1 && orderIndex != 4) {
					if (orderIndex != 2) {
						orderStart = false;
						playerOrder = space;
						pass1 = false;
						System.out.println("WrongOrder");
					} else {
						playerOrder[2] = 2;
						pass2 = true;
					}
				} else if (pass2 && orderIndex != 2) {
					if (orderIndex != 3) {
						orderStart = false;
						playerOrder = space;
						pass1 = false;
						pass2 = false;
						System.out.println("WrongOrder");
					} else {
						playerOrder[3] = 3;
						orderStart = false;
						pass1 = false;
						pass2 = false;
						pass3 = true;
						JFrame dialogueWindow = new JFrame();
						dialogueWindow.setResizable(false);
						dialogueWindow.setTitle("對話框");
						String[] dialogues = {
								"迷宮森林的「秘密鑰匙」出現了！",
						};
						DialoguePanel dialoguePanel = new DialoguePanel(dialogues);
						dialogueWindow.add(dialoguePanel);
						dialogueWindow.pack();
						dialogueWindow.setLocationRelativeTo(null);
						dialogueWindow.setLocation(680, 800);
						dialogueWindow.setVisible(true);
					}
				}

				if (pass3) {
					pass3 = false;

					JFrame window_8 = new JFrame();
					window_8.setResizable(false);
					window_8.setTitle("迷宮森林的鑰匙");
					window_8.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					windowList[8] = window_8;
					GamePanel gamePanel_8 = new GamePanel(8, 5, 5);
					gamePanelList[8] = gamePanel_8;
					window_8.add(gamePanel_8);
					window_8.pack();
					window_8.setLocationRelativeTo(null);
					window_8.setLocation(1000, 604);
					window_8.setVisible(true);
					gamePanel_8.startGameThread();

					window_8.addComponentListener(new ComponentAdapter() {
						@Override
						public void componentMoved(ComponentEvent e) {
							// 當窗口移動時，重新設置固定位置
							window_8.setLocation(1000, 604);
						}

					});
				}

				if (isOpenChest2) {
					itemPanel.isChest2Opened = true;
					JFrame dialogueWindow = new JFrame();
					dialogueWindow.setResizable(false);
					dialogueWindow.setTitle("對話框");
					String[] dialogues = {
							"已獲得「秘密鑰匙」"
					};
					DialoguePanel dialoguePanel = new DialoguePanel(dialogues);
					dialogueWindow.add(dialoguePanel);
					dialogueWindow.pack();
					dialogueWindow.setLocationRelativeTo(null);
					dialogueWindow.setLocation(680, 800);
					dialogueWindow.setVisible(true);
					isOpenChest = false;
				}

				if (windowList[8] != null) {
					if (playerInWindowNum != 8 && itemPanel.isChest2Opened) {
						windowList[8].dispose();
						gamePanelList[8].stopGameThread();
						windowList[8] = null;
						gamePanelList[8] = null;
					}
				}

				if (!isBossSpawned && itemPanel.isChest2Opened) {
					// windowq_9
					JFrame window_9 = new JFrame();
					window_9.setResizable(false);
					window_9.setTitle("魔王城");
					window_9.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
					windowList[9] = window_9;
					GamePanel gamePanel_9 = new GamePanel(9, 5, 5);
					gamePanelList[9] = gamePanel_9;
					window_9.add(gamePanel_9);
					window_9.pack();
					window_9.setLocationRelativeTo(null);
					window_9.setLocation(1673, 740);
					window_9.setVisible(true);
					gamePanel_9.startGameThread();
					isBossSpawned = true;
					window_9.addComponentListener(new ComponentAdapter() {
						@Override
						public void componentMoved(ComponentEvent e) {
							// 當窗口移動時，重新設置固定位置
							window_9.setLocation(1673, 740);
						}
					});
				}

			}

			if (timer >= 1000000000) {
				timer = 0;

				System.out.println(playerDirection + " " + playerInWindowNum);
				System.out.println(nextWindowNum[0] + " " + nextWindowNum[1] + " " +
						nextWindowNum[2] + " " + nextWindowNum[3]);
				// System.out.println(playerOrder[0] + " " + playerOrder[1] + " " +
				// playerOrder[2] + " " + playerOrder[3]);
			}
		}
	}

	public void Update() {

		if (!isTeleported) {
			CheckCanGOWhere();
			CheckStaticWindow();
			CheckWIndowLoacation();
			CheckTalkingToNPC();

			if (playerInWindowNum == 1) {
				if (CheckPosition(5, 3, 3, 2)) {
					playerDirection = "right";
				} else {
					playerDirection = "none";
				}
			}
			if (playerInWindowNum == 2) {
				if (CheckPosition(1, 0, 4, 2)) {
					playerDirection = "left";
				} else if (CheckPosition(5, 2, 1, 0)) {
					playerDirection = "up";
				} else if (CheckPosition(5, 2, 5, 1) || CheckPosition(4, 1, 4, 2)) {
					playerDirection = "sign";
				} else {
					playerDirection = "none";
				}
			}
			if (playerInWindowNum == 3) {
				if (CheckPosition(5, 2, 1, 0)) {
					playerDirection = "right";
				} else if (CheckPosition(1, 0, 4, 2)) {
					playerDirection = "down";
				} else if (CheckPosition(5, 2, 5, 1) || CheckPosition(4, 1, 4, 2)) {
					playerDirection = "sign";
				} else {
					playerDirection = "none";
				}
			}
			if (playerInWindowNum == 4) {
				if (CheckPosition(1, 0, 2, 0)) {
					playerDirection = "left";
				} else if (CheckPosition(5, 2, 5, 2)) {
					playerDirection = "down";
				} else if (CheckPosition(5, 1, 1, 0) || CheckPosition(4, 2, 3, 1)) {
					playerDirection = "sign";
				} else {
					playerDirection = "none";
				}
			}
			if (playerInWindowNum == 5) {
				if (CheckPosition(5, 2, 4, 2)) {
					playerDirection = "right";
				} else if (CheckPosition(2, 0, 1, 0)) {
					playerDirection = "up";
				} else if (CheckPosition(5, 2, 5, 1) || CheckPosition(4, 1, 2, 0)) {
					playerDirection = "sign";
				} else {
					playerDirection = "none";
				}
			}
			if (playerInWindowNum == 6) {
				if (CheckPosition(5, 2, 5, 3)) {
					playerDirection = "right";
				} else if (CheckPosition(3, 1, 3, 1)) {
					playerDirection = "oldMan";
				} else {
					playerDirection = "none";
				}
			}
			if (playerInWindowNum == 7) {
				if (CheckPosition(3, 1, 4, 2)) {
					playerDirection = "down";
				} else if (CheckPosition(3, 1, 2, 0)) {
					playerDirection = "chest";
				} else {
					playerDirection = "none";
				}
			}
			if (playerInWindowNum == 8) {
				if (CheckPosition(3, 1, 1, 0)) {
					playerDirection = "up";
				} else if (CheckPosition(3, 1, 5, 1)) {
					playerDirection = "chest";
				} else {
					playerDirection = "none";
				}
			}
		}

	}

	public static void main(String[] args) {
		new Main().startGameThread();
	}

	public void TeleportPlayer(int windowNum, int x, int y) {

		System.out.println("button clicked");
		gamePanelList[playerInWindowNum].isPlayerInWindow = false;
		gamePanelList[windowNum].isPlayerInWindow = true;
		playerInWindowNum = windowNum;
		playerDirection = "none";

		gamePanelList[windowNum].player.x = x * gamePanelList[windowNum].tileSize;
		gamePanelList[windowNum].player.y = y * gamePanelList[windowNum].tileSize;

		for (int i = 0; i < 4; i++) {
			nextWindowNum[i] = 0;
		}
	}

	public boolean CheckPosition(int xMax, int xMin, int yMax, int yMin) {

		if (gamePanelList[playerInWindowNum].player.x <= xMax * gamePanelList[playerInWindowNum].tileSize
				&& gamePanelList[playerInWindowNum].player.x >= xMin * gamePanelList[playerInWindowNum].tileSize + 24
				&& gamePanelList[playerInWindowNum].player.y <= yMax * gamePanelList[playerInWindowNum].tileSize
				&& gamePanelList[playerInWindowNum].player.y >= yMin * gamePanelList[playerInWindowNum].tileSize + 24) {
			return true;
		} else {
			return false;

		}

	}

	public void CheckCanGOWhere() {
		if (playerInWindowNum == 1) {
			canGoToWhere[0] = false;
			canGoToWhere[1] = true;
			canGoToWhere[2] = false;
			canGoToWhere[3] = true;
		} else if (playerInWindowNum == 2) {
			canGoToWhere[0] = true;
			canGoToWhere[1] = false;
			canGoToWhere[2] = true;
			canGoToWhere[3] = false;
		} else if (playerInWindowNum == 3) {
			canGoToWhere[0] = false;
			canGoToWhere[1] = true;
			canGoToWhere[2] = false;
			canGoToWhere[3] = true;
		} else if (playerInWindowNum == 4) {
			canGoToWhere[0] = false;
			canGoToWhere[1] = true;
			canGoToWhere[2] = true;
			canGoToWhere[3] = false;
		} else if (playerInWindowNum == 5) {
			canGoToWhere[0] = true;
			canGoToWhere[1] = false;
			canGoToWhere[2] = false;
			canGoToWhere[3] = true;
		} else if (playerInWindowNum == 6) {
			canGoToWhere[0] = true;
			canGoToWhere[1] = false;
			canGoToWhere[2] = false;
			canGoToWhere[3] = true;
		} else if (playerInWindowNum == 7) {
			canGoToWhere[0] = false;
			canGoToWhere[1] = true;
			canGoToWhere[2] = false;
			canGoToWhere[3] = false;
		}
	}

	public void CheckWIndowLoacation() {
		for (int i = 0; i < 10; i++) {
			if (windowList[i] == null || !isMoveable || i == playerInWindowNum) {
				continue;
			}
			windowX[i] = windowList[i].getLocation().x;
			windowY[i] = windowList[i].getLocation().y;
		}
	}

	public void CheckTalkingToNPC() {
		if (isTalkingToNPC) {

			// #region window_dialogue
			JFrame dialogueWindow = new JFrame();
			dialogueWindow.setResizable(false);
			dialogueWindow.setTitle("對話框");
			String[] dialogues = {
					"你好冒險者.",
					"如果要打敗魔王, 你必須",
					"找到 藏在深山的「傳說寶劍」",
					"解開 迷宮森林的「秘密鑰匙」.",
					"唯有找到這兩樣物品, 才能夠前往城堡 .",
					"我將會賜予你「移動大地之魔杖」",
					"請善加利用，並根據地圖與提示找到寶劍與鑰匙.",
					"祝你好運！",
					"獲得：「移動大地之魔杖」, 「地圖」,「提示」",
			};
			dialoguePanel = new DialoguePanel(dialogues);
			dialogueWindow.add(dialoguePanel);
			dialogueWindow.pack();
			dialogueWindow.setLocationRelativeTo(null);
			dialogueWindow.setLocation(184, 346);
			dialogueWindow.setVisible(true);
			// #endregion

			isTalkingToNPC = false;
		}
	}

	public void CheckStaticWindow() {

		// int x = windowList[playerInWindowNum].getLocation().x;
		// int y = windowList[playerInWindowNum].getLocation().y;
		boolean[] canGoToWhere = new boolean[4];
		int[] gapSize = new int[4];

		for (int i = 0; i < 10; i++) {

			if (windowList[i] == null || i == playerInWindowNum) {
				continue;
			}

			int curX = windowList[playerInWindowNum].getLocation().x;
			int curY = windowList[playerInWindowNum].getLocation().y;
			int nextX = windowList[i].getLocation().x;
			int nextY = windowList[i].getLocation().y;

			if (playerInWindowNum == 1) {
				if (i == 2) {
					gapSize = new int[] { -24, 24, 0, 0 };
				} else if (i == 3) {
					gapSize = new int[] { -24, 72, -24, 72 };
				} else if (i == 4) {
					gapSize = new int[] { -72, 120, -24, 72 };
				}
			} else if (playerInWindowNum == 2) {
				if (i == 1) {
					gapSize = new int[] { -24, 24, 0, 0 };
				} else if (i == 3) {
					gapSize = new int[] { 72, 120, -120, -72 };
				} else if (i == 4) {
					gapSize = new int[] { 0, 0, -24, 24 };
				} else if (i == 5) {
					gapSize = new int[] { -24, 24, 0, 0 };
				} else if (i == 6) {
					gapSize = new int[] { -72, -24, 0, 0 };
				} else if (i == 7) {
					gapSize = new int[] { 0, 0, -72, -24 };
				}
			} else if (playerInWindowNum == 3) {
				if (i == 2) {
					gapSize = new int[] { -120, -72, -120, -72 };
				} else if (i == 4) {
					gapSize = new int[] { -24, 24, 0, 0 };
				} else if (i == 5) {
					gapSize = new int[] { 0, 0, -24, 24 };
				} else if (i == 8) {
					gapSize = new int[] { 0, 0, -72, -24 };
				} else if (i == 9) {
					gapSize = new int[] { -72, -24, 0, 0 };
				}

			} else if (playerInWindowNum == 4) {
				if (i == 1) {
					gapSize = new int[] { -120, -72, 0, 0 };
				} else if (i == 2) {
					gapSize = new int[] { 0, 0, -24, 24 };
				} else if (i == 3) {
					gapSize = new int[] { -24, 24, 0, 0 };
				} else if (i == 5) {
					gapSize = new int[] { -120, -72, 72, 120 };
				} else if (i == 6) {
					gapSize = new int[] { -168, -120, 0, 0 };
				} else if (i == 8) {
					gapSize = new int[] { 0, 0, 24, 72 };
				}
			} else if (playerInWindowNum == 5) {
				if (i == 2) {
					gapSize = new int[] { -24, 24, 0, 0 };
				} else if (i == 3) {
					gapSize = new int[] { 0, 0, -24, 24 };
				} else if (i == 4) {
					gapSize = new int[] { 72, 120, 72, 120 };
				} else if (i == 7) {
					gapSize = new int[] { 0, 0, 24, 72 };
				} else if (i == 9) {
					gapSize = new int[] { 24, 72, 0, 0 };
				}

			} else if (playerInWindowNum == 6) {
				if (i == 2) {
					gapSize = new int[] { 24, 72, 0, 0 };
				} else if (i == 4) {
					gapSize = new int[] { 120, 168, 0, 0 };
				}
			} else if (playerInWindowNum == 7) {
				if (i == 2) {
					gapSize = new int[] { 0, 0, -72, -24 };
				} else if (i == 5) {
					gapSize = new int[] { 0, 0, 24, 72 };
				}
			} else if (playerInWindowNum == 8) {
				if (i == 3) {
					gapSize = new int[] { 0, 0, -72, -24 };
				} else if (i == 4) {
					gapSize = new int[] { 0, 0, 24, 72 };
				}
			}

			// Check if the next window is near the up of the current window
			if (nextWindowNum[0] == 0 || nextWindowNum[0] == i) {
				if (curX >= nextX + gapSize[2]
						&& curX <= nextX + gapSize[3]
						&& curY <= nextY + windowList[i].getHeight() + 12
						&& curY >= nextY + windowList[i].getHeight() * 0.9) {
					nextWindowNum[0] = i;
				} else {
					nextWindowNum[0] = 0;
				}
			}

			// Check if the next window is near the down of the current window
			if (nextWindowNum[1] == 0 || nextWindowNum[1] == i) {
				if (nextX >= curX + gapSize[2]
						&& nextX <= curX + gapSize[3]
						&& curY + windowList[playerInWindowNum].getHeight() >= nextY
						&& curY + windowList[playerInWindowNum].getHeight() <= nextY + 12) {
					nextWindowNum[1] = i;
				} else {
					nextWindowNum[1] = 0;
				}
			}

			// Check if the next window is near the left of the current window
			if (nextWindowNum[2] == 0 || nextWindowNum[2] == i) {
				if (curX <= nextX + windowList[i].getWidth() + 12
						&& curX >= nextX + windowList[i].getWidth() * 0.9
						&& nextY >= curY + gapSize[0]
						&& nextY <= curY + gapSize[1]) {
					nextWindowNum[2] = i;
				} else {
					nextWindowNum[2] = 0;
				}
			}
			// check if the next window is near the right of the current window
			if (nextWindowNum[3] == 0 || nextWindowNum[3] == i) {
				if (nextX >= curX + windowList[playerInWindowNum].getWidth() - 24
						&& nextX <= curX + windowList[playerInWindowNum].getWidth()
						&& nextY >= curY + gapSize[0]
						&& nextY <= curY + gapSize[1]) {
					nextWindowNum[3] = i;
				} else {
					nextWindowNum[3] = 0;
				}
			}
		}

	}

}
