package com.project.networking.Common;


import java.util.HashMap;
import java.util.Map;

public abstract class Packet
{
	public static int packetID = 0;
	public static Map<Integer,Class<? extends Packet>> packetClassMap;
	
	public int length;
	public int type;
	
	private static void loadClasses()
	{
		//Initialize class map if necessary
		if (packetClassMap == null) {
			packetClassMap = new HashMap<>();
		}
		else return;
		
		//Register all classes here.
		//This implementation kind of /sucks/, but loading all subclasses of Packet at runtime seems to be incredibly complex and wasteful in Java.
		//Therefore, we need to register them manually :/
		new LoginPacket().registerClass();
		new MessageSendPacket().registerClass();
		new MessageReceivePacket().registerClass();
		new PlayerUpdatePacket().registerClass();
		new UserActionPacket().registerClass();
		new ProjectilePacket().registerClass();
		new DamagePacket().registerClass();
	}
	
	protected void registerClass()
	{
		//Register class
		if (!packetClassMap.containsKey(type)) {
			packetClassMap.put(type,this.getClass());
		}
	}

	public Packet()
	{
		//Get class ID
		try {
			type = this.getClass().getField("packetID").getInt(this);
			if (type == 0)
				throw new Exception("You forgot to set the static packetID variable (shall not be 0) in Class :" + this.getClass().getName());
		} catch (Exception e) {
			System.out.println("Error getting packetID:" + e.toString());
			e.printStackTrace();
		}
		//Load all subclasses.
		if (packetClassMap == null)
			loadClasses();
	}
	/*
	 * SERIALIZATION
	 * -------------
	 * 
	 *  All subclasses of ChatPacket MUST override this method
	 *  and correctly serialize all contents following the 
	 *  specifications from the Protocol documentation!
	 */
	
	public abstract byte[] generateDataPacket();
	
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
	
	public abstract void parseData(int packetSize, byte[] bulkPacket);
	
	public static Packet parseDataPacket(byte[] prePacket, byte[] bulkPacket)
	{
		//Load all subclasses into map if not loaded yet.
		if (packetClassMap == null)
			loadClasses();
		
		//Parse pre-packet
		int packetType = NetworkingCommon.intFromBuffer(prePacket, 0);
		int packetSize = NetworkingCommon.intFromBuffer(prePacket, 4);
		Class<? extends Packet> packetClass = packetClassMap.get(packetType);
		if (packetClass == null)
		{
			//Unchecked exceptions, wee.
			throw new RuntimeException("Attempted parsing an unknown packet type. stop.");
		}
		
		Packet packet = null;
		try
		{
			packet = packetClass.newInstance();
		}
		catch (Exception e)
		{
			System.out.println("Constructing packet failed:");
			e.printStackTrace();
			return null;
		}
		
		//Call specialized packet parser
		packet.parseData(packetSize, bulkPacket);
		
		return packet;
	}
}
