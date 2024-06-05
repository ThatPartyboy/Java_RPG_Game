package entity;

import main.GamePanel;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class NPC extends Entity {

    GamePanel gp;

    public NPC(GamePanel gp) {
        this.gp = gp;
        setDefauleValues();
        getNPCImage();
    }

    public void setDefauleValues() {
        x = 2 * gp.tileSize;
        y = 2 * gp.tileSize;
    }

    public void getNPCImage() {
        try {
            down1 = ImageIO.read(getClass().getResourceAsStream("/tiles/oldman_down_1.png"));
            down2 = ImageIO.read(getClass().getResourceAsStream("/tiles/oldman_down_2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {

        spriteCounter++;
        if (spriteCounter > 30) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

    }

    public void draw(Graphics2D g) {

        BufferedImage image = null;

        if(spriteNum == 1) {
            image = down1;
        }
        if(spriteNum == 2) {
            image = down2;
        }

        g.drawImage(image, x, y, gp.tileSize, gp.tileSize, null);
    }
}
