package GameLogic;

import java.net.Socket;

// Interface for listeners handling object reads from sockets
public interface ReaderListener {
    
    // Method called when an object is read from the socket
    void onObjectRead(Command command);

    // Method called when the socket is closed
    void onCloseSocket(Socket socket);
}
