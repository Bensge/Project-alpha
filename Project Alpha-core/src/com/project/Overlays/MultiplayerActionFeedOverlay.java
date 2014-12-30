package com.project.Overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ObjectMap;
import com.project.Preferences.AppPreferences;
import com.project.UI.Table;
import com.project.networking.Common.DamagePacket;
import com.project.networking.Common.MessageReceivePacket;
import com.project.networking.Common.Packet;
import com.project.networking.Common.UserActionPacket;
import com.project.networking.MultiplayerGameSessionController;
import com.project.networking.MultiplayerListener;
import com.project.networking.MultiplayerServer;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bennokrauss on 25.12.14.
 */
public class MultiplayerActionFeedOverlay extends Overlay implements MultiplayerListener
{
    private Table table;
    private Timer dismissTimer;
    private int cellsToBeRemoved = 0;
    private ObjectMap<Byte,String> playersNames;

    private final int MAX_TABLE_ENTRIES = 3;

    public MultiplayerActionFeedOverlay()
    {
        super(ScreenCorner.TopRight, Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 6);

        playersNames = new ObjectMap<>();

        table = new Table();
        table.setBounds(0, 0, getWidth(), getHeight());
        //Set table content alignment
        table.top().right();
        addActor(table);

        dismissTimer = new Timer();

        //Register for user action and chat message packets
        MultiplayerGameSessionController.sharedInstance().registerListener(UserActionPacket.class, this);
        MultiplayerGameSessionController.sharedInstance().registerListener(MessageReceivePacket.class, this);
        MultiplayerGameSessionController.sharedInstance().registerListener(DamagePacket.class,this);
    }

    private void addEntry(String event)
    {
        Label l = new Label(event, labelStyle);
        table.row().pad(10);
        final Cell dismissCell = table.add(l);

        while (table.getCells().size > MAX_TABLE_ENTRIES) {
            table.removeRow(0);
            cellsToBeRemoved--;
        }

        //Dismiss entry after 5 seconds
        cellsToBeRemoved++;
        dismissTimer.schedule(new TimerTask()
        {
            public void run()
            {
                if (cellsToBeRemoved > 0) {
                    table.removeRow(0);
                    cellsToBeRemoved--;
                }
            }
        }, 5 * 1000);
    }

    @Override
    public void multiplayerSessionStarted(MultiplayerServer server)
    {

    }

    @Override
    public void multiplayerSessionEnded()
    {
        table.clear();
    }

    @Override
    public void receivedPacket(Packet p)
    {
        if (p instanceof UserActionPacket) {
            //Notify user
            UserActionPacket packet = (UserActionPacket)p;
            if (packet.isCurrent)
                addEntry(packet.niceTextString());
            //Keep track of players
            if (packet.action == UserActionPacket.Action.Join) {
                playersNames.put(packet.userID,packet.userName);
            }
            else if (packet.action == UserActionPacket.Action.Leave) {
                playersNames.remove(packet.userID);
            }
        }
        else if (p instanceof MessageReceivePacket) {
            MessageReceivePacket packet = (MessageReceivePacket)p;
            addEntry(packet.niceTextString());
        }
        else if (p instanceof DamagePacket) {
            DamagePacket packet = (DamagePacket)p;
            if (packet.restLife <= 0) {
                String hunterName = playersNames.get(packet.hunterID);
                if (hunterName == null) {
                    hunterName = AppPreferences.sharedInstance().getUserName();
                }
                String targetName = playersNames.get(packet.targetID);

                addEntry(hunterName + " killed " + targetName);
            }

        }
    }
}
