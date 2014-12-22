package com.project.networking;

import javax.jmdns.ServiceEvent;

public class MultiplayerServer
{
	public String address;
	public String name;
	public String adminName;
	public String key;
	public int port;
	
	public MultiplayerServer(ServiceEvent event)
	{
		super();
		address = event.getInfo().getHostAddresses()[0];
		port = event.getInfo().getPort();
		name = event.getInfo().getNiceTextString();
		adminName = "Unknown";
		key = event.getInfo().getKey();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this) return true;
		if (obj == null) return false;
		if (obj instanceof MultiplayerServer)
		{
			MultiplayerServer server = (MultiplayerServer)obj;
			
			return server.key.equals(key);
		}
		
		return false;
	}

	public String toString()
	{
		return "(" + super.toString() + " name=" + name + " address=" + address + " adminName=" + adminName + " key=" + key + ")";
	}
}
