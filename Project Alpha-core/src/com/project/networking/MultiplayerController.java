package com.project.networking;

import java.net.InetSocketAddress;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.project.networking.Common.LoginPacket;
import com.project.networking.Common.Packet;
import com.project.networking.Workers.ReadingWorker;
import com.project.networking.Workers.WritingWorker;

class MultiplayerController implements Disposable, PacketReceivedCallback
{
	
	private Socket socket;
	private InetSocketAddress address;
	
	private WritingWorker writingWorker;
	private ReadingWorker readingWorker;
	
	private String userName;
	
	private PacketReceivedCallback listener;
	
	
	
	public MultiplayerController(String addr, int port, String userName, PacketReceivedCallback listener) throws GdxRuntimeException
	{
		this.userName = userName;
		this.listener = listener;

		SocketHints hints = new SocketHints();
		hints.tcpNoDelay = true;

		socket = Gdx.net.newClientSocket(Protocol.TCP, addr, port, hints);

		writingWorker = new WritingWorker(socket);
		readingWorker = new ReadingWorker(socket, this);

		System.out.println("Connected to:" + socket.getRemoteAddress());
	}

	public void receivedPacket(Packet p)
	{
		if (this.listener != null)
		{
			this.listener.receivedPacket(p);
		}
	}

	public void sendPacket(Packet p)
	{
		writingWorker.dispatchPacketSend(p);
	}
	
	public void login()
	{
		readingWorker.listen();
		
		//Handshake
		LoginPacket loginPacket = new LoginPacket();
		loginPacket.name = userName;
		
		writingWorker.dispatchPacketSend(loginPacket);
	}

	@Override
	public void dispose()
	{
		readingWorker.terminate();
		socket.dispose();
	}
}
