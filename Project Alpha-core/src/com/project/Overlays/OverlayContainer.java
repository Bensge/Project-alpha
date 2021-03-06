package com.project.Overlays;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

/**
 * Created by bennokrauss on 26.12.14.
 */
public class OverlayContainer extends Stage
{
    static OverlayContainer instance = null;

    public static OverlayContainer sharedInstance()
    {
        if (instance == null)
            instance = new OverlayContainer();
        return instance;
    }

    Array<Overlay> overlays;

    public OverlayContainer()
    {
        super();

        overlays = new Array<>();
    }

    public void addOverlay(Overlay overlay)
    {
        overlays.add(overlay);
        addActor(overlay);
    }

    @Override
    public void draw()
    {
        super.draw();
    }
}
