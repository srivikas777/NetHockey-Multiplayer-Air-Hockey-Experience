package GameLogic;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

// Class for reading objects from a socket in a separate thread
public class ReaderThread extends Thread {
    // Socket for reading
	private Socket socket;
    // Listener for handling read objects
	private ReaderListener listener;

    // Constructor for ReaderThread class
	public ReaderThread(Socket socket, ReaderListener listener) {
        // Initialize the socket and listener
		this.socket = socket;
		this.listener = listener;
	}

    // Method to be executed when the thread starts
	@Override
	public void run() {
		try {
            // Get the input stream from the socket
			InputStream in = socket.getInputStream();
            // Create an ObjectInputStream to read objects from the input stream
			ObjectInputStream objIn = new ObjectInputStream(in);

            // Continuously read objects from the input stream
			while (true) {
                // Read an object from the input stream
				Command command = (Command) objIn.readObject();
                // Notify the listener about the read object
				listener.onObjectRead(command);
			}
		}
        // Catch ClassNotFoundException and IOException
		catch (ClassNotFoundException | IOException e) {
            // Print stack trace if an exception occurs
			e.printStackTrace();
		}
        // Notify the listener when the socket is closed
		listener.onCloseSocket(socket);
	}
}
