package com.project.networking.UI;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.project.networking.MultiplayerServer;

public class MultiplayerServerLabel extends Label
{
	private MultiplayerServer server;
	
	public MultiplayerServerLabel(MultiplayerServer server, LabelStyle style)
	{
		super(server.name + " " + server.address, style);
		
		this.server = server;		
	}
	
	public MultiplayerServer getServer()
	{
		return server;
	}
}
