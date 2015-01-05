package com.project.networking.Common;

public class MessageSendPacket extends Packet {
	public static int packetID = 2;
	
	public String text;
	
	
	
	public byte[] generateDataPacket()
	{
		byte[] data = text.getBytes();
		this.length = data.length;
		return data;
	}
	
	public void parseData(int packetSize, byte[] bulkPacket)
	{
		//A message packet from a client
		text = new String(bulkPacket);
	}
}
