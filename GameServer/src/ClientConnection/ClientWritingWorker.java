package ClientConnection;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.Socket;

import javax.swing.*;

import Server.AlphaServer;
import com.project.networking.Common.Packet;


public class ClientWritingWorker
{
	private OutputStream out;
	private AlphaServer server;
	public Client client;

	public ClientWritingWorker(Socket socket, AlphaServer server, Client client) {
		try {
			this.out = socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.server = server;
		this.client = client;
	}

	public void dispatchPacketSend(Packet packet)
	{
		//This method will return almost instantly, the actual sending process will be done asynchronously
		final ClientWritingWorker worker = this;
		final byte[] data = packet.generateDataPacket();
		final byte[] preData = packet.generatePrePacket();

		new Thread()
		{
			public void run()
			{
				try
				{
					//We synchronize the sending process with the writing worker,
					//which has the effect that writing to the socket will always only happen from one thread at a time.
					synchronized (worker) {
						out.write(preData);
						out.write(data);
					}
				}
				catch (Exception e)
				{
					System.out.println("Error writing to outputStream: " + e.toString());
					//Disconnect from client
					try {
						//Unregister client from server on the main thread
						SwingUtilities.invokeAndWait(new Runnable()
                        {
                            public void run()
                            {
								reportBrokenPipe();
                            }
                        });
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		}.start();
	}

	private void reportBrokenPipe()
	{
		server.unregisterClient(client);
		System.out.println("Client disconnected");
	}
}
