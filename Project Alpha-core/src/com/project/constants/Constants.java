package com.project.constants;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class Constants
{
	//Constants
	public final static BitmapFont menlo32Font;
	public final static BitmapFont menlo50Font;
	
	public static float SCREEN_SCALING_FACTOR = 1;
	
	//Static constructor
	static
	{
		if (Gdx.app.getType() == ApplicationType.iOS || Gdx.app.getType() == ApplicationType.Android)
			SCREEN_SCALING_FACTOR = 2;
		
		
		//Double-sized font, scaled down to size 32 for retina sharpness and high-resolution displays.
		menlo32Font = new BitmapFont(Gdx.files.internal("fonts/Menlo-64.fnt"),Gdx.files.internal("fonts/Menlo.png"), false);
		menlo32Font.setScale(0.5f);
		
		menlo50Font = new BitmapFont(Gdx.files.internal("fonts/Menlo-100.fnt"),Gdx.files.internal("fonts/Menlo.png"), false);
		menlo50Font.setScale(0.5f);
	}
}