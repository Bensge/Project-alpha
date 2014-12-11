package com.project.alpha.desktop;



import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.project.main.Main;


public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		Main m = new Main();
		
		config.vSyncEnabled = true;
		config.backgroundFPS = 0;
		config.foregroundFPS = 0;
		
		LwjglApplication lwjglApplication = new LwjglApplication(m,config);
	}
}
