package com.project.networking.Common;

public class ProjectilePacket extends Packet
{
	public static int packetID = 7;
	
	public float originX;
	public float originY;
	
	public float directionX;
	public float directionY;
	
	public byte[] generateDataPacket()
	{
		byte[] data = new byte[4*4];
		this.length = data.length;
		
		NetworkingCommon.writeFloatToBuffer(originX, data, 0*4);
		NetworkingCommon.writeFloatToBuffer(originY, data, 1*4);
		NetworkingCommon.writeFloatToBuffer(directionX, data, 2*4);
		NetworkingCommon.writeFloatToBuffer(directionY, data, 3*4);
		
		return data;
	}
}
