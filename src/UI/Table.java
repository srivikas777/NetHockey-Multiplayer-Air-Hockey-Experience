package UI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import GameLogic.Command;
import GameLogic.Puck;

// Class representing the game table
public class Table extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final double HITDIS = Puck.PUCKRADIUS + Mallet.MALLETRADIUS;
    private static final int GOALPICSIZE = GamePage.GAMEWIDTH / 8;

    private int width = GamePage.GAMEWIDTH;
    private int height = GamePage.FRAMEHEIGHT;

    public int PaddedWidth;
    public int PaddedHeight;

    private Puck puck;
    private Mallet mallet1;
    private Mallet mallet2;
    private Image tableImg;

    private Image animated;
    private ScheduledExecutorService executor;

    private Runnable decreaseSpeed = () -> {
        if (puck.isMoving()) {
            puck.decreaseSpeed();
        } else {
            executor.shutdown();
        }
    };

    // Constructor for the Table
    public Table() throws IOException {
        setSize(width, height);
        setPreferredSize(new Dimension(width, height));
        puck = new Puck();
        mallet1 = new Mallet(height / 4 * 3 - 10);
        mallet2 = new Mallet(height / 4 + 10);
        executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(decreaseSpeed, 0, 1000, TimeUnit.MILLISECONDS);
        tableImg = ImageIO.read(getClass().getResource("pics/table1.jpg"));
        animated = new ImageIcon(getClass().getResource("pics/giphy.gif")).getImage();
    }

    // Method to paint the components of the Table
    @Override
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(tableImg, 0, 0, width, height, null);
		puck.draw(g);
		mallet1.draw(g);
		mallet2.draw(g);
		Graphics2D g2 = (Graphics2D) g;
		if (puck.getGoal()) {
			g2.drawImage(animated, width / 2 - GOALPICSIZE / 2, height / 2 - GOALPICSIZE / 2, GOALPICSIZE + 1,
					GOALPICSIZE, this);
		}
	}
    
    public void paintComponentAdapt(Graphics g) {
        super.paintComponent(g);
        int padding = 20;
        int paddedWidth, paddedHeight;

        double aspectRatio = (double) tableImg.getWidth(null) / tableImg.getHeight(null);
        double panelAspectRatio = (double) width / height;

        if (aspectRatio > panelAspectRatio) {
            paddedWidth = width - 2 * padding;
            paddedHeight = (int) (paddedWidth / aspectRatio);
        } else {
            paddedHeight = height - 2 * padding;
            paddedWidth = (int) (paddedHeight * aspectRatio);
        }

        PaddedWidth = paddedWidth;
        PaddedHeight = paddedHeight;

        int x = (width - paddedWidth) / 2;
        int y = (height - paddedHeight) / 2;

        g.drawImage(tableImg, x, y, paddedWidth, paddedHeight, null);
        puck.draw(g);
        mallet1.draw(g);
        mallet2.draw(g);
        Graphics2D g2 = (Graphics2D) g;
        if (puck.getGoal()) {
            g2.drawImage(animated, width / 2 - GOALPICSIZE / 2, height / 2 - GOALPICSIZE / 2, GOALPICSIZE + 1,
                    GOALPICSIZE, this);
        }
    }

    // Method to move the puck
    protected int movePuck() throws LineUnavailableException, IOException {
        int point = puck.move();
        if (checkHit()) {
            puck.decreaseSpeed();
            puck.changeColor();
        }
        return point;
    }

    // Method to check if there's a hit between the puck and a mallet
    private boolean checkHit() {
        return calcMallet(mallet1) || calcMallet(mallet2);
    }

    // Method to calculate the hit between the puck and a mallet
    private boolean calcMallet(Mallet mallet) {
        double malletX = mallet.posX;
        double malletY = mallet.posY;
        double diff = Math.sqrt(Math.pow(malletX - puck.posX, 2) + Math.pow(malletY - puck.posY, 2));
        if (diff <= HITDIS) {
            puck.setSlope(malletX, malletY);
            return true;
        }
        return false;
    }

    // Method to update the coordinates of a mallet
    protected void updateMalletCoordinates(double x, double y, int malletNum) {
        if (malletNum == 1) {
            mallet1.setMalletXY(x, y);
        }
        if (checkHit()) {
            puck.hit();
            if (executor.isShutdown()) {
                executor = Executors.newScheduledThreadPool(1);
                executor.scheduleAtFixedRate(decreaseSpeed, 0, 1000, TimeUnit.MILLISECONDS);
            }
        }
        if (malletNum == 2) {
            mallet2.updateCoordinates(x, y);
        }
        repaint();
    }

    // Method to update the puck coordinates
    protected void updatePuck(double x, double y, int speed) {
        puck.update(x, y, speed);
        repaint();
    }

    // Method to get the puck speed
    protected int getPuckSpeed() {
        return puck.getSpeed();
    }

    // Method to set the initial position of the puck
    protected void setPuck(int number) {
        puck.setResetY(number);
    }

    // Method to get the command of a positionable object
    public Command getCommand(char positionable) {
        if (positionable == 'p') {
            return puck.getCommand();
        }
        return mallet1.getCommand();
    }
}
