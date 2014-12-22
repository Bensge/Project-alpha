package com.project.networking;

import java.net.InetSocketAddress;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;
import com.badlogic.gdx.utils.Disposable;
import com.project.networking.Common.LoginPacket;
import com.project.networking.Common.Packet;
import com.project.networking.Workers.ReadingWorker;
import com.project.networking.Workers.WritingWorker;

public class MultiplayerController implements Disposable, NetworkCallback
{
	
	private Socket socket;
	private InetSocketAddress address;
	
	private WritingWorker writingWorker;
	private ReadingWorker readingWorker;
	
	private String userName;
	
	private NetworkCallback listener;
	
	
	
	public MultiplayerController(String addr, int port, String userName, NetworkCallback listener)
	{
		address = new InetSocketAddress(addr, port);
		this.userName = userName;
		this.listener = listener;

		SocketHints hints = new SocketHints();
		hints.tcpNoDelay = true;
		socket = Gdx.net.newClientSocket(Protocol.TCP, address.getAddress().toString(), address.getPort(), hints);
		
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
	
	public void login()
	{
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
