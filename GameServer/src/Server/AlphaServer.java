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

import com.project.networking.Common.Packet;
import com.project.networking.Common.LoginPacket;
import com.project.networking.Common.MessageReceivePacket;
import com.project.networking.Common.MessageSendPacket;
import com.project.networking.Common.UserActionPacket;
import com.project.networking.Common.NetworkingCommon;
import com.project.networking.Common.UserActionPacket.Action;

public class AlphaServer {

	public static final int MESSAGE_PACKET_ID = 0;

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
	private ArrayList<Client> clients = new ArrayList<Client>();
	private JmDNS dnsServer = null;
	
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

		System.out.println("Set up DNS server: " + info.toString());

	}

	private void tearDown() {
		// Close socket
		try {
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
		SocketAcceptWorker acceptWorker = new SocketAcceptWorker(socket, this);
		acceptWorker.execute();
	}

	public void registerClient(Client client) {
		clients.add(client);
	}

	public void unregisterClient(Client client) {
		clients.remove(client);

		// Notify other clients
		UserActionPacket packet = new UserActionPacket(client.getName(),
				Action.Leave, true);

		sendPacketToClientsBut(client, packet);
	}

	public void processMessage(Client sender, Packet packet)
	{
		System.out.println("Processing message...");

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

			for (Client client : clients) {
				if (client == sender)
					continue;
				// Let newly joined sender know which other clients are
				// connected
				UserActionPacket user = new UserActionPacket(client.getName(),
						Action.Join, false);
				sender.send(user);
			}

			// Notify other clients
			UserActionPacket p = new UserActionPacket(sender.getName(),
					Action.Join, true);
			newPacket = p;
		}

		if (newPacket != null)
			sendPacketToClientsBut(sender, newPacket);
	}

	public void sendPacketToClientsBut(Client sender, Packet newPacket) {
		for (Client client : clients) {
			if (client != sender) {
				client.send(newPacket);
			}
		}
	}

	public ArrayList<Client> getClients() {
		return clients;
	}
}