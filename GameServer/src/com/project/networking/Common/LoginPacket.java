package com.project.networking.Common;

public class LoginPacket extends Packet
{
	public static int packetID = 1;
	
	public String name;
	
	public byte[] generateDataPacket()
	{
		byte[] data = name.getBytes();
		this.length = data.length;
		return data;
	}
}
