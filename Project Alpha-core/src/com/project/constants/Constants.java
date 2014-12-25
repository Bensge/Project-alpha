package com.project.constants;

import java.awt.Point;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class Constants
{
	//Constants
	public final static BitmapFont menlo32Font;
	public final static BitmapFont menlo50Font;

	public final static Skin uiSkin;
	
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

		uiSkin = new Skin(Gdx.files.internal("skin/uiskin.json"));
		uiSkin.add("default-font", menlo32Font);
	}
	
	
	public static boolean circleIntersectsRectangle(Point circle, float radius, Point rectangle, float width, float height){
		//1. kreis in Rechteck
		if(circle.x >= rectangle.x && circle.x <= rectangle.x + width &&
			circle.y >= rectangle.y && circle.y >= rectangle.y + height)
			return true;
		
		//2. eckke beruehrt kreis
		if(pythagoras(circle, rectangle) <= radius ||
			pythagoras(circle, new Point(rectangle.x, (int) (rectangle.y + height))) <= radius ||
			pythagoras(circle, new Point((int) (rectangle.x + width), rectangle.y)) <= radius ||
			pythagoras(circle, new Point((int) (rectangle.x + width), (int) (rectangle.y + height))) <= radius)
			return true;
		
		//3.seite beruehrt Kreis
		if(Math.abs(circle.y - (rectangle.y + height)) < radius && circle.x <= rectangle.x + width && circle.x >= rectangle.x ||
			Math.abs(rectangle.y - circle.y) < radius && circle.x <= rectangle.x + width && circle.x >= rectangle.x ||	
			Math.abs(rectangle.x - circle.x) < radius && circle.y <= rectangle.y + height && circle.y >= rectangle.y ||	
			Math.abs((rectangle.x + width) - circle.x) < radius && circle.y <= rectangle.y + height && circle.y >= rectangle.y)
		return true;	
			
		return false;
	}
	
	
	private static float pythagoras(Point a, Point b){
		//satz des pythagoras
		return (float) Math.sqrt((b.y - a.y)*(b.y - a.y) + (b.x - a.x)*(b.x - a.x));
	}
	
}