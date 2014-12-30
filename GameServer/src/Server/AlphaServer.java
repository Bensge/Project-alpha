package Server;

import java.io.IOException;
import java.net.*;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.awt.EventQueue;

import ClientConnection.Client;

import com.project.networking.Common.*;
import com.project.networking.Common.UserActionPacket.Action;

public class AlphaServer {

	public static AlphaServer instance = null;


	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					instance = new AlphaServer();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// Register Shutdown handler
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				instance.tearDown();
			}
		});

		while (true) {
			// This is stupid. Stupid, stupid, stupid!
			// Keeps the main thread running, otherwise the whole program might terminate in weird circumstances
			Thread.sleep(1000);
		}
	}

	/* NOT SO CONSTANT CONSTANTS */
	private int port = NetworkingCommon.SCHOOL_PORT;

	/* IVARS */
	private ServerSocket socket;
	//A map containing clients as keys, and integers (client IDs) as values. Crazy world!
	private Map<Client,Byte> clients = new HashMap<>();
	//Why -128 and not 0? Because bytes are always signed in java. It hurts...
	private byte userIDCounter = -128;
	private JmDNS dnsServer = null;
	private SocketAcceptWorker acceptWorker;
	
	private String serverName = null;
	private String adminName = null;

	/* HELPER */

	/* METHODS */
	public AlphaServer() throws Exception {
		// Hello
		port = NetworkingCommon.DEFAULT_PORT;
		System.out.println("AlphaServer by Justus & Benno");
		System.out.println("+----------------------------+");
		System.out.println("|           PREMIUM          |");
		System.out.println("+----------------------------+\n");
		
		Scanner scanner = new Scanner(System.in);
		
		System.out.println("Please enter the server name:");
		serverName = scanner.nextLine();
		
		System.out.println("Admin name (for privileged permissions):");
		adminName = scanner.nextLine();
		
		scanner.close();

		startUp();
		listen();

	}

	private void startUp() {
		// Open socket
		try {
			socket = new ServerSocket(port);
		} catch (Exception e) {
			System.out.println("Error opening socket: " + e.toString());
		}
		try {
			// Get inet address
			System.out.println("Server address: " + socket.getInetAddress()
					+ " Server IP: "
					+ InetAddress.getLocalHost().getHostAddress());
		} catch (Exception e) {
			System.out.println("Error getting local IP address: "
					+ e.toString());
		}

		try {
			dnsServer = JmDNS.create();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ServiceInfo info = ServiceInfo.create("projectalpha", serverName, port,
				"");
		Map<String, String> infoMap = new HashMap<String, String>();
		infoMap.put("adminName", adminName);
		info.setText(infoMap);
		try {
			dnsServer.registerService(info);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("Set up multicast DNS server.");

	}

	private void tearDown() {
		// Close socket
		try {
			acceptWorker.terminate();
			socket.close();
			dnsServer.unregisterAllServices();
			System.out.println("Unregistered services, waiting...");
			Thread.sleep(10 * 1000);
			dnsServer.close();
			Thread.sleep(2 * 1000);
		} catch (Exception e) {
		}
	}

	private void listen() throws Exception {
		acceptWorker = new SocketAcceptWorker(socket, this);
		acceptWorker.execute();
	}

	public void registerClient(Client client)
	{
		clients.put(client,new Byte(userIDCounter++));
	}

	public void unregisterClient(Client client)
	{
		//The client map has the client as keys and not objects for this very reason. We can remove clients
		// with one single call, instead of having to iterate over the whole map to find the key of the client value
		Byte clientID = clients.remove(client);
		// Notify other clients
		UserActionPacket packet = new UserActionPacket(clientID.byteValue(),client.getName(), Action.Leave, true);
		sendPacketToClientsBut(client, packet);
	}

	public void processMessage(Client sender, Packet packet)
	{
		//System.out.println("Processing message of class: " + packet.getClass().getName());

		Packet newPacket = null;
		if (packet instanceof MessageSendPacket) {
			MessageReceivePacket p = new MessageReceivePacket();
			p.text = ((MessageSendPacket) packet).text;
			p.timestamp = NetworkingCommon.currentUnixTime();
			p.sender = sender.getName();
			newPacket = p;
		} else if (packet instanceof LoginPacket) {
			// get name of packet
			sender.setName(((LoginPacket) packet).name);
			
			// Send client connect confirmation
			MessageReceivePacket confirmationPacket = MessageReceivePacket.serverMessagePacket("You joined!");
			sender.send(confirmationPacket);

			for (Client client : clients.keySet()) {
				if (client == sender)
					continue;
				// Let newly joined sender know which other clients are
				// connected
				UserActionPacket user = new UserActionPacket(clients.get(client).byteValue(), client.getName(),
						Action.Join, false);
				sender.send(user);
			}

			// Notify other clients
			UserActionPacket p = new UserActionPacket(clients.get(sender).byteValue(), sender.getName(),
					Action.Join, true);
			newPacket = p;
		}
		//Forward all these packages
		else if (packet instanceof PlayerUpdatePacket || packet instanceof ProjectilePacket)
		{
			//Set userID for these packages
			//Loooooooooooooooool JVM looooooool
			if (packet instanceof  PlayerUpdatePacket)
				((PlayerUpdatePacket) packet).userID = clients.get(sender);
			else if(packet instanceof ProjectilePacket)
				((ProjectilePacket) packet).userID = clients.get(sender);
			
			newPacket = packet;
			//Forward update packets right away
		}
		else if(packet instanceof DamagePacket){
		
			DamagePacket p = (DamagePacket) packet;
			p.targetID = clients.get(sender);
			newPacket = p;
		}
		

		if (newPacket != null)
			sendPacketToClientsBut(sender, newPacket);
	}

	public void sendPacketToClientsBut(Client sender, Packet newPacket) {
		for (Client client : clients.keySet()) {
			if (client != sender) {
				client.send(newPacket);
			}
		}
	}
}