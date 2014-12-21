package com.project.GameStates.Menus.Multiplayer;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.project.GameStates.GameStateManager;
import com.project.GameStates.Menus.GameMenu;
import com.project.UI.LoadingIndicator;
import com.project.constants.Constants;
import com.project.networking.MultiplayerServer;
import com.project.networking.NetworkController;
import com.project.networking.NetworkDiscoveryListener;

public class GameLocalMultiplayerMenu extends GameMenu implements NetworkDiscoveryListener {
	
	//Index of table row to insert newly found servers. 1 By default, because below the header.
	private int tableInsertionIndex = 1;
	private LabelStyle labelStyle;

	public GameLocalMultiplayerMenu(GameStateManager manager) {
		super(manager);
		
		labelStyle = new LabelStyle(Constants.menlo32Font, Color.WHITE);
		
		//Set up loading cell
		Table loadingTable = new Table().debugAll();
		
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
	    //NetworkController.sharedInstance().setUpListeningWithListener(this);
	    
	    //Debug server table
	    MultiplayerServer server = new MultiplayerServer();
	    server.name = "Benno's Server";
	    server.address = "127.0.0.1";
	    foundServer(server);
	}
	
	protected String header() {
		return "Local Server";
	}

	@Override
	public void foundServer(MultiplayerServer server)
	{
		
		Array<Cell> cells = table.getCells();
		Array<Array<Actor>> actors = new Array<Array<Actor>>();
		
		int lastRow = -9000;
		for (Cell c : cells)
		{
			if (lastRow == c.getRow())
			{
				System.out.println("Last row=" + lastRow + " current row=" + c.getRow());
				Array<Actor> items = actors.get(actors.size-1);
				items.add(c.getActor());
			}
			else {
				Array<Actor> items = new Array<Actor>();
				items.add(c.getActor());
				actors.add(items);
			}
			
			lastRow = c.getRow();
		}
		
		table.clearChildren();
		
		
		for (int i = 0; i < tableInsertionIndex; i ++)
		{
			table.row().pad(20);
			
			Array<Actor> items = actors.get(i);
			for (Actor a : items)
				table.add(a);
		}
		
		Label label = new Label(server.name + " " + server.address, labelStyle);
		
		//Insert the new row with a small padding
		table.row().pad(10);
		table.add(label);
		
		for (int i = tableInsertionIndex; i < actors.size; i ++)
		{
			//Add back rows below the inserted row, with standard padding
			table.row().pad(20);
			
			Array<Actor> items = actors.get(i);
			for (Actor a : items)
				table.add(a);
		}
	}

	@Override
	public void lostServer(MultiplayerServer server)
	{
		
	}
}
