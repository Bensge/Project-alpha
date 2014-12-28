package ClientConnection;

import com.project.networking.Common.Packet;



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

	public Client(ClientReadingWorker reader){
		this.reader = reader;
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
		if (writer == null) {
			writer = new ClientWritingWorker(getReader().out);
		}

		writer.dispatchPacketSend(packet);
	}
	
}
