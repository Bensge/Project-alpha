package com.project.UI;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.project.constants.Constants;

/**
 * Created by bennokrauss on 24.12.14.
 */
public class Alert extends Dialog
{
    public Alert(String title, String text)
    {
        super(title, Constants.uiSkin);

        if (text != null) {
            text(text);
        }

        button("OK");
    }
}
