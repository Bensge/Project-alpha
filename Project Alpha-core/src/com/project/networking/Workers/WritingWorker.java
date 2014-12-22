package com.project.networking.Workers;

import java.io.OutputStream;
import com.badlogic.gdx.net.Socket;
import com.project.networking.Common.Packet;

public class WritingWorker
{
	private OutputStream stream;
	
	public WritingWorker(Socket socket)
	{
		this.stream = socket.getOutputStream();
	}
	
	public void dispatchPacketSend(Packet packet)
	{
		//This method will return almost instantly, the actual sending process will be done asynchronously
		final WritingWorker worker = this;
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
						stream.write(preData);
						stream.write(data);
					}
				}
				catch (Exception e)
				{
					System.out.println("Error writing to outputStream: " + e.toString());
					e.printStackTrace();
				}
			};
		}.run();
	}
}
