package com.project.alpha.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.project.main.Main;


public class DesktopLauncher {
	public static void main (String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.useHDPI = true;
		config.resizable = false;
		config.width = 800;
		config.height = 600;
		//config.fullscreen = true;
		
		Main m = new Main();
		
		LwjglApplication lwjglApplication = new LwjglApplication(m,config);
	}
}
