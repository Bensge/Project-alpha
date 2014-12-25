package com.project.networking.Workers;

import java.io.InputStream;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.net.Socket;
import com.project.networking.PacketReceivedCallback;
import com.project.networking.Common.NetworkingCommon;
import com.project.networking.Common.Packet;

public class ReadingWorker
{
	private InputStream stream;
	private PacketReceivedCallback listener;
	private boolean isListening;
	private boolean shouldTerminate;
	
	public ReadingWorker(Socket socket, PacketReceivedCallback listener)
	{
		this.stream = socket.getInputStream();
		this.listener = listener;
	}
	
	public void terminate()
	{
		shouldTerminate = true;
	}
	
	public boolean listen()
	{
		if (isListening)
			return false;
		
		isListening = true;
		
		new Thread()
		{
			public void run()
			{
				System.out.println("Now receiving from server!");
				//Buffer of fixed length that is always needed when receiving a packet, thus cached and only created once.
				byte[] prePacket = new byte[NetworkingCommon.INT_FIELD_SIZE * 2];
				
				//The usual BSD Socket 'Workflow'
				while (!shouldTerminate)
				{
					//Clean out prePacket for the case that it's filled with the old package.
			    	Arrays.fill( prePacket, (byte) 0);
			    	
			    	int result = -1;
			    	try
			    	{
			    		result = stream.read(prePacket,0,8);
			    	}
			    	catch (Exception e)
			    	{
			    		System.out.println("Exception while reading stream:" + e);
			    		e.printStackTrace();
			    		continue;
			    	}
			        
			        if (result == -1)
			        {
			        	System.out.println("Server disconnected!!!!!!");
			        	//It's been great talking to you, Client!
			        	break;
			        }
			        
			        int packetType = NetworkingCommon.intFromBuffer(prePacket, 0);
			        
			        if (packetType == 0)
			        {
			        	//Got invalid packet
			        	System.out.println("Got invalid packet type 0");
			        	continue;
			        }
			        
			        System.out.println("Packet type: " + packetType);
			        int packetSize = NetworkingCommon.intFromBuffer(prePacket, 4);
			        System.out.println("Packet size: " + packetSize);
			        
			        byte[] packetBuffer = new byte[packetSize];
			        
			        result = 0;
			        int receivedCount = 0;
			        while (receivedCount < packetSize)
			        {
			        	try
			        	{
			        		result = stream.read(packetBuffer,receivedCount,packetSize - receivedCount);
			        	}
			        	catch (Exception e)
			        	{
			        		System.out.println("Exception while reading stream:" + e);
			        		e.printStackTrace();
			        	}
			        	
				        if (result < 0)
				        {
				        	System.out.println("Error reading big packet!!!" + result);
				        	break;
				        }
				        else
				        {
				        	receivedCount += result;
				        	if (receivedCount < packetSize)
					        {
					        	System.out.println("Reading progress: " + ((float)receivedCount / (float)packetSize * 100.f) + "%");
					        }
				        }
			        }
			        
			        //Create model object from packet data
			        final Packet packet = Packet.parseDataPacket(prePacket, packetBuffer);
			        
			        //Send packet to listener on main thread
			        Gdx.app.postRunnable(new Runnable() {
						public void run() {
							listener.receivedPacket(packet);
						}
					});
			        
			        //End of reading loop 
				}
			}
		}.start();
		
		return isListening;
	}
}