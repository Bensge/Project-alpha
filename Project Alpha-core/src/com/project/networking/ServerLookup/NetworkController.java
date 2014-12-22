package com.project.networking.ServerLookup;

import java.io.IOException;
import java.net.InetAddress;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceInfo;
import javax.jmdns.ServiceListener;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.project.networking.MultiplayerServer;

public class NetworkController {
	
	private boolean isHostingSetUp;
	private boolean isDiscoverySetUp;
	
	private Array<MultiplayerServer> serverCache;
	private NetworkDiscoveryListener listener = null;
	
	private JmDNS dns = null;
	
	private static NetworkController instance = null;
	public static NetworkController sharedInstance()
	{
		if (instance == null)
			instance = new NetworkController();
		
		return instance;
	}

	public NetworkController()
	{
		super();
		
		//Create a small server cache array
		serverCache = new Array<>(2);
	}
	
	private boolean setUpDNS()
	{
		try {
			InetAddress address = null;
			if (Gdx.app.getType() == ApplicationType.iOS)
			{
				 address = InetAddress.getByName("127.0.0.1");
			}
			dns = JmDNS.create(address);
			return true;
		} catch (IOException e1) {
			e1.printStackTrace();
			return false;
		}
	}
	
	public boolean setUpHosting()
	{
		if (!isHostingSetUp)
		{
			if (!setUpDNS()) return false;
			
		    ServiceInfo info = ServiceInfo.create("projectalpha","Messenger",80,"Messenger chat thingy");
		    try {
				dns.registerService(info);
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		    
		    System.out.println("Set up DNS server: " + info.toString());
		    
		    
		    
		    //TODO: Set up socket server
		    
		    isHostingSetUp = true;
		    
		    return true;
		}
		else
			return false;
	}
	
	public NetworkDiscoveryListener getListener()
	{
		return listener;
	}
	
	
	public boolean setUpListeningWithListener(NetworkDiscoveryListener listener)
	{
		this.listener = listener;
		
		if (!isDiscoverySetUp)
		{
			if (!setUpDNS()) return false;
			
			dns.addServiceListener("_projectalpha._tcp.local.", new ServiceListener(){
		    	@Override
		    	public void serviceAdded(ServiceEvent event)
		    	{
		    		System.out.println("Service added: " + event.toString());
		    	}

				@Override
				public void serviceRemoved(ServiceEvent event)
				{
					System.out.println("Service removed: " + event.toString());
					
					MultiplayerServer server = new MultiplayerServer(event);
					serverCache.removeValue(server, false);
					getListener().lostServer(server);
				}

				@Override
				public void serviceResolved(ServiceEvent event)
				{
					System.out.println("Service resolved: " + event.toString());
					
					MultiplayerServer server = new MultiplayerServer(event);
					serverCache.add(server);
					getListener().foundServer(server);
				}
		    });
			
			isDiscoverySetUp = true;
		}
		else
		{
			//Tell the new listener about all cached servers
			if (listener != null)
			{
				for (MultiplayerServer server : serverCache)
				{
					getListener().foundServer(server);
				}
			}
		}
		
		return isDiscoverySetUp;
	}
}
