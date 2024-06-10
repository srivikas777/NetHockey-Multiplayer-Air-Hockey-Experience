package GameLogic;

import UI.GamePage;

// Class representing a command for moving the puck
public class PuckMovement implements Command {
    // serialVersionUID for serialization
    private static final long serialVersionUID = 1L;
    
    // Coordinates and speed of the puck
    protected double x;
    protected double y;
    private int speed;

    // Constructor for PuckMovement class
    public PuckMovement(double x, double y, int speed) {
        // Initialize puck coordinates and speed
        this.x = x;
        this.y = y;
        this.speed = speed;
        // Flip coordinates to ensure proper reflection over the center of the game board
        flip();
    }

    // Method to flip the puck coordinates for proper reflection
    private void flip() {
        // Calculate the center of the game board
        double Whalf = GamePage.GAMEWIDTH / 2;
        double Lhalf = GamePage.FRAMEHEIGHT / 2;

        // Perform reflection over x and y axis based on the quadrant
        double diffx = Math.abs(Whalf - x);
        double diffy = Math.abs(Lhalf - y);
        if (x >= Whalf && y >= Lhalf) {
            x = Whalf - diffx;
            y = Lhalf - diffy;
        } else if (x <= Whalf && y <= Lhalf) {
            x = Whalf + diffx;
            y = Lhalf + diffy;
        } else if (y >= Lhalf && x <= Whalf) {
            x = Whalf + diffx;
            y = Lhalf - diffy;
        } else if (y <= Lhalf && x >= Whalf) {
            x = Whalf - diffx;
            y = Lhalf + diffy;
        }
    }

    // Method to execute the command on the provided GamePage instance
    @Override
    public void execute(GamePage world) {
        // Update the puck coordinates and speed on the game page
        world.updatePuckCoordinates(x, y, speed);
    }
}
