package com.project.GameStates.Menus.Settings;

import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.project.GameStates.GameStateManager;
import com.project.GameStates.Menus.GameMenu;
import com.project.Preferences.AppPreferences;
import com.project.common.Constants;

/**
 * Created by bennokrauss (and justus(mimimi)) on 24.12.14.
 */
public class SettingsMenu extends GameMenu
{
    public SettingsMenu(GameStateManager manager)
    {
        super(manager);

        TextField field = new TextField(AppPreferences.sharedInstance().getUserName(),Constants.uiSkin);

        field.setTextFieldListener(new TextField.TextFieldListener()
        {
            @Override
            public void keyTyped(TextField textField, char c)
            {
            if (c == '\r' || c == '\n') {
                AppPreferences.sharedInstance().setUserName(textField.getText());
                //Dismiss focus
                stage.setKeyboardFocus(null);
            }
            }
        });

        TextButton userNameLabel = new TextButton("Player name", style);
        table.row().pad(20);
        table.add(userNameLabel);

        table.row().pad(20);
        table.add(field);

        TextButton button = new TextButton("Back", style);
        table.row().pad(20);
        table.add(button);
        button.addListener(backButtonListener());
    }

    @Override
    protected String header()
    {
        return "Settings";
    }
}
