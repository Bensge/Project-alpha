package com.project.Overlays;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.project.constants.Constants;
/**
 * Created by bennokrauss on 25.12.14.
 */
public class Overlay extends Group
{
    public enum ScreenCorner {
        TopLeft,
        TopRight,
        BottomLeft,
        BottomRight
    }

    protected LabelStyle labelStyle = new LabelStyle(Constants.menlo10Font, new Color(200, 10, 50, 255));
    protected ScreenCorner corner;
    protected int overlayWidth, overlayHeight;

    public Overlay(ScreenCorner corner, int overlayWidth, int overlayHeight)
    {
        super();
        this.corner = corner;
        this.overlayWidth = overlayWidth;
        this.overlayHeight = overlayHeight;

        setWidth(overlayWidth);
        setHeight(overlayHeight);

        int width = Gdx.graphics.getWidth();
        int height = Gdx.graphics.getHeight();

        int transformX;
        int transformY;

        switch (corner) {
            case TopRight:
                transformX = width - overlayWidth;
                transformY = height - overlayHeight;
                break;
            case TopLeft:
                transformX = 0;
                transformY = height - overlayHeight;
                break;
            case BottomRight:
                transformX = width - overlayWidth;
                transformY = 0;
                break;
            default:
            case BottomLeft:
                transformX = 0;
                transformY = 0;
                break;
        }

        setPosition(transformX,transformY);
    }
}
