package com.project.Overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.project.UI.Table;
import com.project.networking.Common.MessageReceivePacket;
import com.project.networking.Common.Packet;
import com.project.networking.Common.UserActionPacket;
import com.project.networking.MultiplayerGameSessionController;
import com.project.networking.MultiplayerListener;
import com.project.networking.MultiplayerServer;

/**
 * Created by bennokrauss on 25.12.14.
 */
public class MultiplayerActionFeedOverlay extends Overlay implements MultiplayerListener
{
    private Table table;
    private Timer dismissTimer;

    private final int MAX_TABLE_ENTRIES = 3;

    public MultiplayerActionFeedOverlay()
    {
        super(ScreenCorner.TopRight, Gdx.graphics.getWidth() / 3, Gdx.graphics.getHeight() / 6);

        table = new Table();
        table.setBounds(0, 0, getWidth(), getHeight());
        //Set table content alignment
        table.top().right();
        addActor(table);

        dismissTimer = new Timer();

        //Register for user action and chat message packets
        MultiplayerGameSessionController.sharedInstance().registerListener(UserActionPacket.class, this);
        MultiplayerGameSessionController.sharedInstance().registerListener(MessageReceivePacket.class, this);
    }

    private void addEntry(String event)
    {
        Label l = new Label(event, labelStyle);
        table.row().pad(10);
        final Cell dismissCell = table.add(l);

        while (table.getCells().size > MAX_TABLE_ENTRIES)
            table.removeRow(0);

        //Dismiss entry after 5 seconds
        dismissTimer.scheduleTask(new Task()
        {
            public void run()
            {
                int index = table.getCells().indexOf(dismissCell,true);
                if (index != -1)
                    table.removeRow(index);
            }
        }, 5);
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
        System.out.println("receivedPacket" + p.toString());

        if (p instanceof UserActionPacket) {
            UserActionPacket packet = (UserActionPacket)p;
            if (packet.isCurrent)
                addEntry(packet.niceTextString());
        }
        else if (p instanceof MessageReceivePacket) {
            MessageReceivePacket packet = (MessageReceivePacket)p;
            addEntry(packet.niceTextString());
        }
    }
}
