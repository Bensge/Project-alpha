package com.project.networking;

public class MultiplayerServer {
	public String address;
	public String name;
	public String adminName;
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this) return true;
		if (obj == null) return false;
		if (obj instanceof MultiplayerServer)
		{
			MultiplayerServer server = (MultiplayerServer)obj;
			
			return server.address == address &&
					server.name == name &&
					server.adminName == adminName;
		}
		
		return false;
	}
}
