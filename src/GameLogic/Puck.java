package GameLogic;

import java.awt.Color;
import java.awt.Graphics;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.sound.sampled.LineUnavailableException;

import UI.TableDimensions;
import UI.GamePage;

// Class representing the game puck
public class Puck {
    // Position of the puck
    public double posX;
    public double posY;

    // Radius of the puck
//    public static final int PUCK_RADIUS = TableDimensions.getGameTableHeight() / 50;
//
//    // Dimensions of the game table
//    private final int tableWidth = TableDimensions.getGameTableWidth();
//    private final int tableHeight = TableDimensions.getGameTableHeight();
//
//    // Gaps between the game table and the frame
//    private final int widthGap = GamePage.GAMEWIDTH / 2 - tableWidth / 2;
//    private final int heightGap = GamePage.GAMEHEIGHT / 2 - tableHeight / 2;
    
    public static final int PUCKRADIUS = 12;
	private int width = GamePage.GAMEWIDTH;
	private int height = GamePage.FRAMEHEIGHT;
	
    // Movement parameters
    private double deltaX;
    private double deltaY;
    private int speed;
    private float colorNum;
    private int resetY;

    // Goal state and timer
    private boolean goal;
    private ScheduledExecutorService executor;
    private int time;

    // Timer task to count down goal time
    private final Runnable timer = () -> {
        time--;
        if (time == 0) {
            goal = false;
        }
    };

    // Constructor for Puck class
	public Puck() throws IOException {
		// image = ImageIO.read(getClass().getResource("pics/puck.jpg"));
		posX = width / 2;
		posY = height / 2;
		resetY = height / 4;
		speed = 0;
		colorNum = 0;
		executor = Executors.newScheduledThreadPool(1);
	}

	public int move() throws LineUnavailableException, IOException{
		posX += deltaX;
		posY += deltaY;

		// if hit side wall
		if (posX - PUCKRADIUS <= 4 || posX + PUCKRADIUS >= width - 4) {
			colorNum += .02;
			deltaX = -deltaX;
			decreaseSpeed();
			changeColor();
		}

		// hit top/bottom walls
		// if the puck hit a side within the goal range, returns the player who
		// scored a point
		// otherwise returns 0
		else {
			if (posY - PUCKRADIUS <= 4) {
				return checkGoal(1);
			}
			else if (posY + PUCKRADIUS >= height - 4) {
				return checkGoal(2);
			}
		}
		return 0;
	}

	// if there is a goal, this method will return the player that called checkGoal, otherwise, will return 0
		private int checkGoal(int player) throws LineUnavailableException, IOException {
			// if puck within goal range, return player who scores
			if (posX > 70 && posX < width - 70) {
				time = 2;
				// use executor to ensure that goal shows for right amount of time
				executor.scheduleAtFixedRate(timer, 0, 4, TimeUnit.SECONDS);
				reset();
				// TODO sound.changeTrack("sound/goal.wav");
				goal = true;
				return player;
			}

			// otherwise bounce off wall
			deltaY = -deltaY;
			decreaseSpeed();
			changeColor();
			return 0;
		}

    // Method to reset the puck position
		private void reset() {
			posX = width / 2;
			if (resetY == (int) height / 4) {
				resetY *= 3;
			}
			else {
				resetY = height / 4;
			}
			posY = resetY;
			speed = 0;
		}

    // Method to set the puck's movement slope based on the mallet position
    public void setSlope(double malletX, double malletY) {
        deltaY = posY - malletY;
        deltaX = posX - malletX;

        int xNeg = 1;
        int yNeg = 1;
        if (deltaX < 0) {
            xNeg = -1;
        }
        if (deltaY < 0) {
            yNeg = -1;
        }
        deltaX = Math.abs(deltaX);
        deltaY = Math.abs(deltaY);

        if (deltaX != 0 && deltaY != 0) {
            double angle1 = Math.atan(deltaX / deltaY);
            double angle2 = 90 - angle1;
            deltaY = Math.sin(angle2) * yNeg;
            deltaX = Math.sin(angle1) * xNeg;
        } else if (deltaX == 0) {
            deltaY = yNeg;
        } else {
            deltaX = xNeg;
        }
    }

    // Method to decrease the puck's speed
    public void decreaseSpeed() {
        speed--;
    }

    // Method to get the puck's speed
    public int getSpeed() {
        return speed;
    }

    // Method to change the puck's color
    public void changeColor() {
        colorNum += .02;
    }

    // Method to get the goal state
    public boolean getGoal() {
        return goal;
    }

    // Method to set the reset position for the puck
    public void setResetY(int num) {
        resetY = height / 4 * num;
    }

    // Method to simulate a hit on the puck
    public void hit() {
        changeColor();
        speed = 20;
    }

    // Method to check if the puck is moving
    public boolean isMoving() {
        return speed > 0;
    }

    // Method to update the puck's position and speed
    public void update(double x, double y, int speed) {
        updateCoordinates(x, y);
        this.speed = speed;
    }

    // Method to update the puck's position
    protected void updateCoordinates(double x, double y) {
        posX = x;
        posY = y;
    }

    // Method to draw the puck
    public void draw(Graphics g) {
        g.setColor(Color.getHSBColor(colorNum, 1, 1));
        g.fillOval((int) (posX - PUCKRADIUS), (int) (posY - PUCKRADIUS), PUCKRADIUS * 2, PUCKRADIUS * 2);
    }

    // Method to get the command for the puck movement
    public PuckMovement getCommand() {
        return new PuckMovement(posX, posY, speed);
    }
}
