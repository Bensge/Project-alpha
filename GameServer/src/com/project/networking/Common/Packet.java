package com.project.networking.Common;


public class Packet {
	public static int packetID = 0;
	
	public int length;
	public int type;
	
	
	public Packet()
	{
		try {
			type = this.getClass().getField("packetID").getInt(this);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * SERIALIZATION
	 * -------------
	 * 
	 *  All subclasses of ChatPacket MUST override this method
	 *  and correctly serialize all contents following the 
	 *  specifications from the Protocol documentation!
	 */
	
	public byte[] generateDataPacket()
	{
		return null;
	}
	
	/*
	 * You MUST call generateDataPacket() before calling this method! 
	 * Subclasses generally only determine the packet size when
	 * generating the data packet. Does not need to be overwritten 
	 * by subclasses
	 */
	
	public byte[] generatePrePacket()
	{
		if (length == 0)
		{
			System.out.println("!!!WARNING!!!");
			System.out.println("You forgot to call generateDataPacket() before calling generatePrePacket() on " + this.toString());
		}
		byte[] b = new byte[8];
		NetworkingCommon.writeIntToBuffer(type, b, 0);
		NetworkingCommon.writeIntToBuffer(length, b, 4);
		return b;
	}
	
	/*
	 * PARSING
	 * -------
	 * 
	 * Parsing follows the the Specifications defined in the Protocol documentation
	 */
	
	
	public static Packet parseDataPacket(byte[] prePacket, byte[] bulkPacket)
	{
		int packetType = NetworkingCommon.intFromBuffer(prePacket, 0);
		int packetSize = NetworkingCommon.intFromBuffer(prePacket, 4);
		
		Packet packet = null;
		
		System.out.println("Parsing packet of type (" + packetType + ") and length (" + packetSize + ")");
		
		if (packetType == MessageSendPacket.packetID)
		{
			//A message packet from a client
			MessageSendPacket p = new MessageSendPacket();
			p.text = new String(bulkPacket);
			packet = p;
		}
		else if (packetType == MessageReceivePacket.packetID)
		{
			//A message packet from the server
			MessageReceivePacket p = new MessageReceivePacket();
			
			//Timestamp
			p.timestamp = NetworkingCommon.intFromBuffer(bulkPacket, 0);
			//Sender
			int senderLength = NetworkingCommon.intFromBuffer(bulkPacket, 4);
			// String constructor (byte[], offset, length)
			p.sender = new String(bulkPacket, 8, senderLength);
			//Text
			int textOffset = 8 + senderLength;
			int textLength = packetSize - textOffset;
			p.text = new String(bulkPacket, textOffset, textLength);
			
			packet = p;
		}
		else if (packetType == LoginPacket.packetID)
		{
			LoginPacket p = new LoginPacket();
			p.name = new String(bulkPacket);
			packet = p;
		}
		
		else if (packetType == UserActionPacket.packetID)
		{
			UserActionPacket p = new UserActionPacket();
			
			byte action = bulkPacket[0];
			p.action = UserActionPacket.Action.values()[(int)action];
			
			boolean isC = bulkPacket[1] != 0;
			p.isCurrent = isC;
			
			String user = new String(bulkPacket, 6, bulkPacket.length - 6);
			p.user = user;
			
			packet = p;
		}
		
		return packet;
	}
}