package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DialoguePanel extends JPanel {
    private JLabel dialogueLabel;
    private int currentDialogueIndex = 0;
    private String[] dialogues;

    public boolean isDialogueReach(int index) {
        return currentDialogueIndex == index;
    }

    public DialoguePanel(String[] dialogues) {
        this.dialogues = dialogues;
        this.setPreferredSize(new Dimension(400, 68));
        this.setBackground(Color.black);
        this.dialogueLabel = new JLabel(dialogues[currentDialogueIndex], SwingConstants.CENTER);
        this.dialogueLabel.setForeground(Color.white);
        this.dialogueLabel.setFont(new Font("Serif", Font.BOLD, 16));
        this.setLayout(new BorderLayout());
        this.add(dialogueLabel, BorderLayout.CENTER);
        setupMouseListener();
    }

    private void setupMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                advanceDialogue();
            }
        });
    }

    public void shutdown() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        frame.dispose();
    }

    private void advanceDialogue() {
        currentDialogueIndex++;
        if (currentDialogueIndex >= dialogues.length) {
            currentDialogueIndex = 0;  // Loop back to the first dialogue or handle the end differently.
            shutdown();
        }
        dialogueLabel.setText(dialogues[currentDialogueIndex]);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Dialogue Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
