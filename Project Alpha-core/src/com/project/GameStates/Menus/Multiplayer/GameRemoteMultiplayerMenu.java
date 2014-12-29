package com.project.GameStates.Menus.Multiplayer;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.project.GameStates.GameStateManager;
import com.project.GameStates.Menus.GameMenu;
import com.project.Preferences.AppPreferences;
import com.project.UI.Alert;
import com.project.constants.Constants;
import com.project.networking.MultiplayerGameSessionController;
import com.project.networking.MultiplayerServer;

public class GameRemoteMultiplayerMenu extends GameMenu {

	public GameRemoteMultiplayerMenu(GameStateManager manager) {
		super(manager);


		TextField field = new TextField("", Constants.uiSkin);
		field.setMessageText("address:port");
		
		String lastIP = AppPreferences.sharedInstance().getLastIP();
		if(lastIP != "")
			field.setText(lastIP);

		field.setTextFieldListener(new TextField.TextFieldListener()
		{
			@Override
			public void keyTyped(TextField textField, char c)
			{
			if (c == '\r' || c == '\n') {
				String text = textField.getText();
				if (text.length() > 0)
					connect(text);
				else {
					//Dismiss focus manually
					stage.setKeyboardFocus(null);
				}
			}
			}
		});

		TextButton userNameLabel = new TextButton("Server address", style);
		table.row().pad(20);
		table.add(userNameLabel);

		table.row().pad(20);
		table.add(field);
		
		TextButton button = new TextButton("Back", style);
	    table.row().pad(20);
	    table.add(button);
	    button.addListener(backButtonListener());
	}
	
	protected String header() {
		return "Remote Server";
	}

	private void connect(String address)
	{
		MultiplayerGameSessionController clr = MultiplayerGameSessionController.sharedInstance();
		try {
			int index = address.indexOf(":");
			if (index == -1)
			{
				throw new GdxRuntimeException("Wrong server address format\n");
			}
			MultiplayerServer server = new MultiplayerServer();
			server.address = address.substring(0, index);
			//Skip ':', use port to end of string
			server.port = Integer.parseInt(address.substring(index + 1, address.length()));

			clr.startMultiplayerSession(server);
			
			AppPreferences.sharedInstance().setLastIP(address);
			
			manager.setRenderBackgroundStateExclusively(true);
		}
		catch (GdxRuntimeException e)
		{
			System.out.println("Connecting failed with error" + e.getMessage() + "\n" + e);
			new Alert("Error connecting to server", e.getMessage()).show(stage);
		}
	}
}
