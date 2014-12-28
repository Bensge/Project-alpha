package ClientConnection;

import java.io.OutputStream;

import javax.swing.SwingWorker;

import com.project.networking.Common.Packet;


public class ClientWritingWorker
{
	private OutputStream out;

	public ClientWritingWorker(OutputStream out) {
		this.out = out;
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
					e.printStackTrace();
				}
			}
		}.run();
	}
}
