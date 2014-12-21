package com.project.networking.UI;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.project.networking.MultiplayerServer;

public class MultiplayerServerButton extends TextButton
{
	private MultiplayerServer server;
	
	public MultiplayerServerButton(MultiplayerServer server, TextButtonStyle style, ChangeListener listener)
	{
		super(server.name + " [" + server.adminName + "]",style);
		this.addListener(listener);
		this.server = server;
	}
	
	public MultiplayerServer getServer()
	{
		return server;
	}
}
