package com.project.UI;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
//import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.project.networking.MultiplayerServer;
import com.project.networking.UI.MultiplayerServerButton;

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

	private boolean isRecursive = false;
	public void removeRow(int index)
	{
		if (!isRecursive)
			System.out.println("removeIndex::: " + index);
		boolean shouldReset = !isRecursive;
		isRecursive = true;
		Array<Cell> cells = getCells();
		Cell cell = cells.get(index);
		removeActor(cell.getActor());
		if (index + 1 < cells.size) {
			Cell cellBelow = cells.get(index+1);
			Actor a = cellBelow.getActor();
			float padding = cellBelow.getPadTop();
			removeRow(index+1);
			row().pad(padding);
			add(a);
		}
		cells.removeIndex(index);
		isRecursive = false;
	}
	
	public void insertServer(int index, MultiplayerServer server, TextButtonStyle style, ChangeListener listener)
	{
		MultiplayerServerButton button = new MultiplayerServerButton(server, style, listener);
		insertRow(index, button);
	}
	
	public void removeServer(MultiplayerServer server)
	{
		for (Cell cell : getCells())
		{
			if (cell.getActor() instanceof MultiplayerServerButton)
			{
				MultiplayerServerButton label = (MultiplayerServerButton)cell.getActor();
				
				if (label.getServer().equals(server))
				{
					//Remove actor, kill cell
					removeActor(label);
					cell.pad(100);
					getCells().removeValue(cell, true);
				}
			}
		}
	}
}
