package com.project.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.Protocol;
import com.badlogic.gdx.net.Socket;
import com.badlogic.gdx.net.SocketHints;

public class MultiplayerController implements Runnable, NetworkCallback{

	private Socket socket;
	private PrintWriter writer;
	private BufferedReader in;
	private Scanner s;
	private InetSocketAddress address;
	private SocketHints hints;
	private boolean connected = false;
	private char[] input = new char[1];

	public MultiplayerController(String addr, int port)
	{
		System.out.println("lol");
	    s = new Scanner(System.in);
	    address = new InetSocketAddress(addr, port);
	    
	    hints = new SocketHints();
	    socket = Gdx.net.newClientSocket(Protocol.TCP, address.getAddress().toString(), address.getPort(), hints);
	    start();
	    
	    System.out.println("Connected to:" + socket.getRemoteAddress());
	    
	      
	    
	    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	    
	    run();
	  }
	
	@Override
	public void receivedPacket(Packet p) {
		
	}

	
	  public void start(){
	    try {
	      //socket.connect(address);
	  
	      connected = socket.isConnected();
	      socket.getOutputStream().write( "hua".getBytes());
	    } catch (IOException e) {
	      
	      e.printStackTrace();
	    }
	  }
	  
	  public void sendText(String msg){
	    //for text
	    
	    try{
	      socket.getOutputStream().write(msg.getBytes());
	      }
	      catch(Exception e){}
	    System.out.println("sent: " + msg);
	  }
	  
	  public String receive(){
	    String ret = "";
	    
	    
	    return null;
	  }
	  
	  @Override
	  public void run() {
	    while(connected){
	      //String f = receive();
	      
	      sendText(s.next());
	      connected = socket.isConnected();
	    }
	    
	    try{
	      in.close();
	      }catch(Exception e ){}
	  }
}
