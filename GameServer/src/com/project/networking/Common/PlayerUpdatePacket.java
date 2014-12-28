package com.project.networking.Common;

/**
 * Created by bennokrauss on 27.12.14.
 */
public class PlayerUpdatePacket extends Packet
{
    public static int packetID = 8;

    public int playerID;

    public int locationX;
    public int locationY;


    public byte[] generateDataPacket()
    {
        byte[] data = new byte[4 * 3];

        NetworkingCommon.writeIntToBuffer(playerID,data,0);
        NetworkingCommon.writeIntToBuffer(locationX,data,4);
        NetworkingCommon.writeIntToBuffer(locationY,data,8);

        this.length = data.length;
        return data;
    }
}
