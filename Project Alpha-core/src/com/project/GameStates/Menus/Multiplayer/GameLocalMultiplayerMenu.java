package com.project.GameStates.Menus.Multiplayer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.project.GameStates.GameStateManager;
import com.project.GameStates.Menus.GameMenu;
import com.project.UI.LoadingIndicator;
import com.project.constants.Constants;
import com.project.networking.MultiplayerServer;
import com.project.networking.ServerLookup.NetworkController;
import com.project.networking.ServerLookup.NetworkDiscoveryListener;
import com.sun.corba.se.impl.activation.ServerMain;
import com.sun.istack.internal.FinalArrayList;

public class GameLocalMultiplayerMenu extends GameMenu implements NetworkDiscoveryListener {
	
	//Index of table row to insert newly found servers. 1 By default, because below the header.
	private int tableInsertionIndex = 1;
	private LabelStyle labelStyle;

	public GameLocalMultiplayerMenu(GameStateManager manager) {
		super(manager);
		
		labelStyle = new LabelStyle(Constants.menlo32Font, Color.WHITE);
		
		//Set up loading cell
		Table loadingTable = new Table();
		
	    Label loadingLabel = new Label("Searching", labelStyle);
	    LoadingIndicator loadingIndicator = new LoadingIndicator();
	    
	    loadingTable.row().pad(0);
	    loadingTable.add(loadingLabel, loadingIndicator);
	    
	    table.row().pad(20);
	    table.add(loadingTable);
	    
	    tableInsertionIndex ++;
		
		//Set up back button
	    table.row().pad(20);
	    TextButton button = new TextButton("Back", style);
	    table.add(button);
	    button.addListener(backButtonListener());
	    
	    
	    //Set up server lookup
	    NetworkController.sharedInstance().setUpListeningWithListener(this);
	}
	
	protected String header() {
		return "Local Server";
	}

	@Override
	public void foundServer(final MultiplayerServer server)
	{
		table.insertServer(tableInsertionIndex, server, style, new ChangeListener() {
			public void changed(ChangeEvent event, Actor actor)
			{
				selectedServer(server);
			}
		});
	}

	@Override
	public void lostServer(MultiplayerServer server)
	{
		table.removeServer(server);
	}
	
	private void selectedServer(MultiplayerServer server)
	{
		System.out.println("Need to connect to server!!!" + server.toString());
	}
	
	@Override
	public void dispose() {
		super.dispose();
		
		NetworkController.sharedInstance().setUpListeningWithListener(null);
	}
}