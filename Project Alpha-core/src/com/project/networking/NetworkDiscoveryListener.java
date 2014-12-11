package com.project.networking;

public interface NetworkDiscoveryListener {
	public void foundServer(MultiplayerServer server);
	public void lostServer(MultiplayerServer server);
}
