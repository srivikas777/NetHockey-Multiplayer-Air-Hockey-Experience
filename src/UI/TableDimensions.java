package UI;

import java.awt.Image;
import javax.imageio.ImageIO;

// Class to calculate dimensions of the game table
public class TableDimensions {
    private static final int PADDING = 20; // Define your desired padding value here
    private static int gameTableWidth;
    private static int gameTableHeight;

    static {
        try {
            // Read the table image to calculate dimensions
            Image tableImg = ImageIO.read(TableDimensions.class.getResource("pics/table3.jpg"));
            double aspectRatio = (double) tableImg.getWidth(null) / tableImg.getHeight(null);
            double panelAspectRatio = (double) GamePage.GAMEWIDTH / GamePage.FRAMEHEIGHT;

            // Calculate width and height based on aspect ratios
            if (aspectRatio > panelAspectRatio) {
                gameTableWidth = GamePage.GAMEWIDTH - 2 * PADDING;
                gameTableHeight = (int) (gameTableWidth / aspectRatio);
            } else {
                gameTableHeight = GamePage.FRAMEHEIGHT - 2 * PADDING;
                gameTableWidth = (int) (gameTableHeight * aspectRatio);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Set default dimensions in case of error
            gameTableWidth = GamePage.GAMEWIDTH;
            gameTableHeight = GamePage.FRAMEHEIGHT;
        }
    }

    // Getter for the game table width
    public static int getGameTableWidth() {
        return gameTableWidth;
    }

    // Getter for the game table height
    public static int getGameTableHeight() {
        return gameTableHeight;
    }
}
