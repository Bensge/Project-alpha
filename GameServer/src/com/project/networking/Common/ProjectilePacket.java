package com.project.networking.Common;

public class ProjectilePacket extends Packet
{
	public static int packetID = 7;
	
	public byte userID;
	public byte projectileType;
	
	public float originX;
	public float originY;
	
	public float targetX;
	public float targetY;
	
	
	public byte[] generateDataPacket()
	{
		byte[] data = new byte[4*4 + 2];
		this.length = data.length;
		
		NetworkingCommon.writeFloatToBuffer(originX, data, 0*4);
		NetworkingCommon.writeFloatToBuffer(originY, data, 1*4);
		NetworkingCommon.writeFloatToBuffer(targetX, data, 2*4);
		NetworkingCommon.writeFloatToBuffer(targetY, data, 3*4);
		data[4*4] = userID;
		data[4*4 + 1] = projectileType;
		
		return data;
	}
}
