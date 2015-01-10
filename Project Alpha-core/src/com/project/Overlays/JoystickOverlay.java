package com.project.Overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.project.common.Constants;

/**
 * Created by bennokrauss on 06.01.15.
 */
public class JoystickOverlay extends Overlay
{
    private Touchpad pad;

    public Vector2 position;
    public boolean isTouched;

    public JoystickOverlay(Overlay.ScreenCorner corner)
    {
        //Stupid java compiler forces me to call this method twice :(
        //Dem C programmers cry
        super(corner, Gdx.graphics.getWidth() / 4,Gdx.graphics.getWidth() / 4);

        Touchpad.TouchpadStyle style = Constants.uiSkin.get(Touchpad.TouchpadStyle.class);
        style.background = null;

        position = new Vector2(0,0);
        isTouched = false;

        pad = new Touchpad(getWidth() / 4, style);
        pad.addListener(new ChangeListener()
        {
            public void changed(ChangeEvent event, Actor actor)
            {
                position.x = pad.getKnobPercentX();
                position.y = pad.getKnobPercentY();

                isTouched = pad.isTouched();
            }
        });
        pad.setSize(getWidth(),getHeight());
        addActor(pad);
    }
}