package ClientConnection;

import Server.AlphaServer;
import com.project.networking.Common.Packet;

import java.net.Socket;


public class Client {
	
	private ClientReadingWorker reader;
	private ClientWritingWorker writer;
	public String name;
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Client(AlphaServer server,Socket socket)
	{
		reader = new ClientReadingWorker(socket,server,this);
		writer = new ClientWritingWorker(socket,server,this);

		reader.execute();
	}
	
	public ClientReadingWorker getReader() {
		return reader;
	}
	
	public ClientWritingWorker getWriter() {
		return writer;
	}

	public void setWriter(ClientWritingWorker clientWritingWorker) {
		writer = clientWritingWorker;
	}
	
	public void setReader(ClientReadingWorker clientReadingWorker) {
		reader = clientReadingWorker;
	}
	
	public void send(Packet packet)
	{
		writer.dispatchPacketSend(packet);
	}
	
}
