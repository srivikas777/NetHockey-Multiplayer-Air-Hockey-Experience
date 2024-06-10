package GameLogic;

import java.io.Serializable;

import UI.GamePage;

// Interface for game commands
public interface Command extends Serializable {
    // Method to execute the command on a GamePage instance
	public void execute(GamePage game);
}
