package com.project.networking.Common;

/**
 * Created by bennokrauss on 27.12.14.
 */
public class PlayerUpdatePacket extends Packet
{
    public static int packetID = 8;

    public byte userID;

    public int locationX;
    public int locationY;


    public byte[] generateDataPacket()
    {
        byte[] data = new byte[1 + 2*4];

        data[0] = userID;
        NetworkingCommon.writeIntToBuffer(locationX,data,1);
        NetworkingCommon.writeIntToBuffer(locationY,data,5);

        this.length = data.length;
        return data;
    }
}
