package ClientConnection;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import javax.swing.SwingWorker;

import com.project.networking.Common.Packet;
import com.project.networking.Common.NetworkingCommon;

import Server.AlphaServer;


public class ClientReadingWorker extends SwingWorker<Void, Packet> {

	private InputStream in;
	public OutputStream out;
	private AlphaServer server;
	public Client client;
	
	public ClientReadingWorker(InputStream in, OutputStream out, AlphaServer server) {
		this.in = in;
		this.out = out;
		this.server = server;
	}
	
	@Override
	protected Void doInBackground() throws Exception
	{
		byte[] prePacket = new byte[NetworkingCommon.INT_FIELD_SIZE * 2];
		
	    System.out.println("Listening to client messages");
	    while (true)
	    {
	    	//Clean out prePacket for the case that it's filled with the old package.
	    	Arrays.fill( prePacket, (byte) 0 );

	        int result = in.read(prePacket,0,8);
	        
	        if (result == -1)
	        {
	        	System.out.println("Client disconnected!");
	        	//It's been great talking to you, Client!
	        	break;
	        }
	        
	        int packetType = NetworkingCommon.intFromBuffer(prePacket, 0);
	        
	        if (packetType == 0)
	        {
	        	//Got invalid packet
	        	continue;
	        }


//	        System.out.println("Received Pre-Packet!" + prePacket);
//
//	        System.out.println("Packet type: " + packetType);

	        int packetSize = NetworkingCommon.intFromBuffer(prePacket, 4);
//	        System.out.println("Packet size: " + packetSize);
	        
	        byte[] packetBuffer = new byte[packetSize];
	        
	        result = 0;
	        int receivedCount = 0;
	        while (receivedCount < packetSize)
	        {
		        result = in.read(packetBuffer,receivedCount,packetSize - receivedCount);
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
	        Packet packet = Packet.parseDataPacket(prePacket, packetBuffer);
	        
	        publish(packet);
	    }
	    
	    return null;
	}
	
	@Override
	protected void process(List<Packet> chunks) {
		// TODO Auto-generated method stub
		for (Packet p : chunks)
		{
			server.processMessage(this.client, p);
		}
	}
	
	@Override
	protected void done() {
		server.unregisterClient(this.client);
	}

}
