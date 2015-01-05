package com.project.networking.Common;

public class DamagePacket extends Packet
{
	public byte targetID, hunterID, restLife;
	public static int packetID = 9;
	
	@Override
	public byte[] generateDataPacket() {
		byte[] data = new byte[3];
		
		data[0] = targetID;
		data[1] = hunterID;
		data[2] = restLife;
		
		this.length = data.length;
		
		return data;
	}
	
	public void parseData(int packetSize, byte[] bulkPacket)
	{
		targetID = bulkPacket[0];
		hunterID = bulkPacket[1];
		restLife = bulkPacket[2];
	}
}
