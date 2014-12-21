package com.project.UI;

import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;
import com.project.networking.MultiplayerServer;
import com.project.networking.UI.MultiplayerServerLabel;

public class Table extends com.badlogic.gdx.scenes.scene2d.ui.Table
{
	public void insertRow(int index, Actor actor)
	{
		Array<Cell> cells = getCells();
		Array<Array<Actor>> actors = new Array<Array<Actor>>();
		
		int lastRow = -2; //Just an impossible index
		for (Cell c : cells)
		{
			if (lastRow == c.getRow())
			{
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
		
		clearChildren();
		
		for (int i = 0; i < index; i ++)
		{
			row().pad(20);
			
			Array<Actor> items = actors.get(i);
			for (Actor a : items)
				add(a);
		}
		
		
		//Insert the new row with a small padding
		row().pad(20);
		add(actor);
		
		for (int i = index; i < actors.size; i ++)
		{
			//Add back rows below the inserted row, with standard padding
			row().pad(20);
			
			Array<Actor> items = actors.get(i);
			for (Actor a : items)
				add(a);
		}
	}
	
	public void insertServer(int index, MultiplayerServer server, LabelStyle style)
	{
		 insertRow(index, new MultiplayerServerLabel(server, style));
	}
	
	public void removeServer(MultiplayerServer server)
	{
		Array<Cell> cells = getCells();
		for (Cell cell : cells)
		{
			if (cell.getActor() instanceof MultiplayerServerLabel)
			{
				MultiplayerServerLabel label = (MultiplayerServerLabel)cell.getActor();
				
				if (label.getServer().equals(server))
				{
					//Remove actor, kill cell
					removeActor(label);
					cell.pad(0);
				}
			}
		}
	}
}
