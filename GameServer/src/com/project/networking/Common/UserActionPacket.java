package com.project.networking.Common;

public class UserActionPacket extends Packet {
	
	public static int packetID = 6;
	
	public enum Action {
		Join,
		Leave
	};

	public byte userID;
	public String userName;
	public Action action;
	public boolean isCurrent;
	
	public UserActionPacket()
	{
		
	}
	
	public UserActionPacket(byte userID, String userName, Action action, boolean isCurrent)
	{
		this.userID = userID;
		this.userName = userName;
		this.action = action;
		this.isCurrent = isCurrent;
	}
	
	public byte[] generateDataPacket()
	{
		int act = this.action.ordinal();
		if (act > Byte.MAX_VALUE)
		{
			System.out.println("This is a fatal error! Enum Action value is bigger than the available byte!!!");
		}
		byte ac = (byte)act;
		byte isC = (byte)(this.isCurrent ? 1 : 0);
		
		byte[] usr = userName.getBytes();
		int userLength = usr.length;

		//Length of 3 bytes (userID, action, isCurrent) + length of user name string
		int totalLength = 3 + userLength;
		byte[] packet = new byte[totalLength];
		this.length = totalLength;

		packet[0] = userID;
		packet[1] = ac;
		packet[2] = isC;
		NetworkingCommon.writeBytesToBuffer(usr, packet, 3);
		
		return packet;
	}
	
	public String toString()
	{
		return this.getClass().toString() + " (" + this.userName + " [" + userID + "] " + " did action:" + this.action + " isCurrent: " + this.isCurrent + " )";
	}

	public String niceTextString()
	{
		return userName + " " + (action == Action.Leave ? "left" : "joined");
	}
}