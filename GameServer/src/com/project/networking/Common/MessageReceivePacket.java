package com.project.networking.Common;

public class MessageReceivePacket extends MessageSendPacket {
	public static int packetID = 3;
	
	public String sender;
	public int timestamp;
	
	public MessageReceivePacket()
	{
		
	}
	
	public static MessageReceivePacket serverMessagePacket(String message)
	{
		MessageReceivePacket packet = new MessageReceivePacket();
		packet.sender = "Server";
		packet.timestamp = NetworkingCommon.currentUnixTime();
		packet.text = message;
		return packet;
	}
	
	public byte[] generateDataPacket()
	{
		byte[] senderBytes = sender.getBytes();
		int senderLength = senderBytes.length;
		
		byte[] textBytes = text.getBytes();
		int textLength = textBytes.length;
		
		this.length = /*Timestamp*/ 4 + /*Sender-Length*/ 4 + /*sender*/ senderLength + /*text*/ textLength;
		
		byte[] data = new byte[this.length];
		
		NetworkingCommon.writeIntToBuffer(timestamp, data, 0);
		NetworkingCommon.writeIntToBuffer(senderLength, data, 4);
		NetworkingCommon.writeBytesToBuffer(senderBytes, data, 8);
		NetworkingCommon.writeBytesToBuffer(textBytes, data, 8 + senderLength);
		
		return data;
	}

	public String niceTextString()
	{
		return sender + ": " + text;
	}
}
