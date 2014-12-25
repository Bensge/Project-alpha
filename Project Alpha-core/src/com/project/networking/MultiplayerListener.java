package com.project.networking;

/**
 * Created by bennokrauss on 24.12.14.
 */
public interface MultiplayerListener extends PacketReceivedCallback
{
    public void multiplayerSessionStarted(MultiplayerServer server);

    public void multiplayerSessionEnded();
}
