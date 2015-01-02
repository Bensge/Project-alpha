package Server;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import javax.swing.SwingWorker;

import ClientConnection.Client;
import ClientConnection.ClientReadingWorker;



public class SocketAcceptWorker extends SwingWorker<Void, Client> {

	private ServerSocket socket;
	private AlphaServer server;

	private boolean shouldTerminate;
	
	public SocketAcceptWorker(ServerSocket socket, AlphaServer server) {
		this.socket = socket;
		this.server = server;
	}

	public void terminate()
	{
		shouldTerminate = true;
	}

	@Override
	protected Void doInBackground() throws Exception
	{
		System.out.println("Accepting connections..");
		while (!shouldTerminate)
		{
			try
			{
				Socket clientSocket = socket.accept();
				//Dat netcode doe
				clientSocket.setTcpNoDelay(true);

				if (clientSocket != null)
				{
					Client client = new Client(server,clientSocket);
					publish(client);
				}
			}
			catch (Exception e)
			{
				System.out.println("Error accepting connection: " + e.toString());
			}
			
		}
		System.out.println("SocketAcceptWriter: bailing out");
		return null;
	}
	
	@Override
	protected void process(List<Client> chunks) {
		for (Client client : chunks)
		{
			server.registerClient(client);
		}
	}
	
	@Override
	protected void done() {
		System.out.println("SocketAcceptWorker: exiting!");
	}

}
