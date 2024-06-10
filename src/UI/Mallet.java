package UI;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.IOException;

import javax.imageio.ImageIO;

import GameLogic.MalletMovement;

// Class representing the mallet
public class Mallet {
    public double posX; // X coordinate of the mallet
    public double posY; // Y coordinate of the mallet
    protected Image image; // Image representing the mallet

    // Radius of the mallet
    public static final int MALLETRADIUS = 20;

    // Constructor for the mallet
    public Mallet(int sideCenter) throws IOException {
        // Initialize the mallet's position to the center of the game table
        posX = GamePage.GAMEWIDTH / 2;
        posY = sideCenter;
        // Load the image of the mallet
        image = ImageIO.read(getClass().getResource("pics/mallet.jpg"));
    }

    // Method to set the position of the mallet based on mouse input
    public void setMalletXY(double x, double y) {
		Point point = MouseInfo.getPointerInfo().getLocation();
		if (point.getY() - y - (MALLETRADIUS * 2) >= GamePage.FRAMEHEIGHT / 2) {
			posX = point.getX() - x;
			posY = point.getY() - y - MALLETRADIUS * 2;
		}
	}


    // Method to update the coordinates of the mallet
    public void updateCoordinates(double x, double y) {
        posX = x;
        posY = y;
    }

    // Method to draw the mallet
    public void draw(Graphics g) {
        // Draw the mallet image at the specified position
        g.drawImage(image, (int) posX - MALLETRADIUS, (int) posY - MALLETRADIUS, MALLETRADIUS * 2, MALLETRADIUS * 2,
                null);
    }

    // Method to draw the mallet with scaling
    protected void drawScaled(Graphics g, double scaleX, double scaleY) {
        int scaledWidth = (int) (MALLETRADIUS * 2 * Math.min(scaleX, scaleY));
        int scaledHeight = scaledWidth;
        int scaledX = (int) (posX * scaleX) - scaledWidth / 2;
        int scaledY = (int) (posY * scaleY) - scaledHeight / 2;
        g.drawImage(image, scaledX, scaledY, scaledWidth, scaledHeight, null);
    }

    // Method to get the command representing the mallet movement
    public MalletMovement getCommand() {
        return new MalletMovement(posX, posY);
    }
}
