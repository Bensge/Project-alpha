package com.project.networking;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ObjectMap;
import com.project.Preferences.AppPreferences;
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
    private ObjectMap<Class<? extends Packet>, Array<MultiplayerListener>> listeners;

    public MultiplayerGameSessionController()
    {
        listeners = new ObjectMap<>();
    }

    /*
    * +----------------+
    * |  Private APIs  |
    * +----------------+
     */

    private Array<MultiplayerListener> listenersForPacketType(Class<? extends Packet> type)
    {
        //Packet type null means this method will return all listeners
        if (type == null) {
            Array<MultiplayerListener> allListeners = new Array<>();
            for (Array<MultiplayerListener> list : listeners.values())
            {
                for (MultiplayerListener listener : list)
                {
                    //Avoid duplicates entries
                    if (!allListeners.contains(listener,true))
                        allListeners.add(listener);
                }
            }
            return allListeners;
        }
        else
        {
            return listeners.get(type);
        }
    }

    /*
    * +---------------+
    * |  Public APIs  |
    * +---------------+
     */


    /*
    * Registers a packet listener for callbacks when a specific packet type (=class) is received.
    * Multiple listeners per packet class are supported
     */

    public void registerListener(Class<? extends Packet> packetClass, MultiplayerListener listener)
    {
        //Get list of listeners for this class
        Array<MultiplayerListener> list = listeners.get(packetClass);
        if (list == null)
            list = new Array<MultiplayerListener>(2);
        //Insert listener
        list.add(listener);
        //Put array back into map
        listeners.put(packetClass,list);
    }

    public void unregisterListener(Class<? extends Packet> packetClass, MultiplayerListener listener)
    {
        //Get list of listeners for this class
        Array<MultiplayerListener> list = listeners.get(packetClass);
        if (list != null) {
            list.removeValue(listener,true);
        }
    }

    public void sendPacket(Packet p)
    {
        controller.sendPacket(p);
    }

    /*
    * Session APIs
     */

    public void startMultiplayerSession(MultiplayerServer server) throws GdxRuntimeException
    {
        String userName = AppPreferences.sharedInstance().getUserName();
        String addr = server.address;
        int port = server.port;

        if (controller != null) {
            System.out.println("Starting a new multiplayer session while another session is still active is invalid");
            controller.dispose();
        }

        //This method call may throw a GdxRuntimeException
        controller = new MultiplayerController(addr,port,userName,this);
        controller.login();

        //Let listeners know about new session
        for (MultiplayerListener listener : listenersForPacketType(null)) {
            listener.multiplayerSessionStarted(server);
        }
    }

    public void endMultiplayerSession()
    {
        //Let listeners know about new session
        for (MultiplayerListener listener : listenersForPacketType(null)) {
            listener.multiplayerSessionEnded();
        }

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
        Class<? extends Packet> packetClass = p.getClass();

        Array<MultiplayerListener> list = listeners.get(packetClass);
        if (list != null) {
            for (MultiplayerListener listener : list)
                listener.receivedPacket(p);
        }

    }

    @Override
    public void dispose()
    {
        controller.dispose();
        listeners = null;
    }
}
