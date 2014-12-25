package com.project.networking;

import com.project.networking.Common.Packet;

public interface PacketReceivedCallback
{

	public void receivedPacket(Packet p);
}
