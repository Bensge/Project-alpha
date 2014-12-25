package com.project.networking;

import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.project.networking.Common.Packet;


/**
 * Created by bennokrauss on 23.12.14.
 */
public class MultiplayerGameSessionController implements PacketReceivedCallback, Disposable
{
    private static MultiplayerGameSessionController instance = null;

    public static MultiplayerGameSessionController sharedInstance()
    {
        if (instance == null) {
            instance = new MultiplayerGameSessionController();
        }
        return instance;
    }


    private MultiplayerController controller;
    private ObjectMap<Class<? extends Packet>, MultiplayerListener> listeners;

    public MultiplayerGameSessionController()
    {
        listeners = new ObjectMap<>();
    }

    /*
    * +---------------+
    * |  Public APIs  |
    * +---------------+
     */


    /*
    * Registers a packet listener for callbacks when a specific packet type (=class) is received. Only one listener per packet class is
    * Currently possible, because of the usage of a simple ObjectMap.
     */

    public void registerListener(Class<? extends Packet> packetClass, MultiplayerListener listener)
    {
        listeners.put(packetClass,listener);
    }

    public void unregisterListener(Class<? extends Packet> packetClass)
    {
        listeners.remove(packetClass);
    }

    /*
    * Session APIs
     */

    public void startMultiplayerSession(String addr, int port, String userName) throws GdxRuntimeException
    {
        if (controller != null) {
            System.out.println("Starting a new multiplayer session while another session is still active is invalid");
            controller.dispose();
        }

        //This method call may throw a GdxRuntimeException
        controller = new MultiplayerController(addr,port,userName,this);
        controller.login();
    }

    public void endMultiplayerSession()
    {
        controller.dispose();
        controller = null;
    }

    /*
    * +------------------------+
    * |  Packet callback stuff |
    * +------------------------+
     */

    @Override
    public void receivedPacket(Packet p)
    {
        Class packetClass = p.getClass();

        MultiplayerListener listener = listeners.get(packetClass);
        if (listener != null)
            listener.receivedPacket(p);

    }

    @Override
    public void dispose()
    {
        controller.dispose();
        listeners = null;
    }
}
