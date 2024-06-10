package GameLogic;

import UI.GamePage;

// Class representing a command for displaying a message in the game chat
public class MessageCommand implements Command {
    // serialVersionUID for serialization
	private static final long serialVersionUID = 1L;
	
	// Message to be displayed in the chat
	private String msg;

    // Constructor for MessageCommand class
	public MessageCommand(String msg) {
        // Initialize the message
		this.msg = msg;
	}

    // Method to execute the command on the provided GamePage instance
	@Override
	public void execute(GamePage game) {
        // Update the chat on the game page with the provided message
		game.updateChat(msg);
	}
}
