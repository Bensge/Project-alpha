package com.project.networking.ServerLookup;

import com.project.networking.MultiplayerServer;

public interface NetworkDiscoveryListener {
	public void foundServer(MultiplayerServer server);
	public void lostServer(MultiplayerServer server);
}
