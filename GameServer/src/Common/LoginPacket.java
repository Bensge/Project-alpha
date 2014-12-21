package Common;

public class LoginPacket extends ChatPacket{
	public static int packetID = 1;
	
	public String name;
	
	public byte[] generateDataPacket()
	{
		byte[] data = name.getBytes();
		this.length = data.length;
		return data;
	}
}
