package com.project.networking;

public class MultiplayerServer {
	public String address;
	public String name;
	public String adminName;
	public String key;
	
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
