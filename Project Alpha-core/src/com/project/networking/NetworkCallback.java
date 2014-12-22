package com.project.networking;

import com.project.networking.Common.Packet;

public interface NetworkCallback {

	public void receivedPacket(Packet p);
}
