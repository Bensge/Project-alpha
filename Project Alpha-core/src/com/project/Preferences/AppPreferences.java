package com.project.Preferences;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

/**
 * Created by bennokrauss on 24.12.14.
 */
public class AppPreferences
{
    /*
    * Singleton boilerplate code
     */

    static AppPreferences instance = null;

    public static AppPreferences sharedInstance()
    {
        if (instance == null)
            instance = new AppPreferences();
        return instance;
    }

    private Preferences prefs;

    private AppPreferences()
    {
        prefs = Gdx.app.getPreferences("ProjectAlpha");
    }

    /*
    * +------------------------------+
    * |  Settings Getters & Setters  |
    * +------------------------------+
     */

    private final String userNameKey = "userName";

    public String getUserName()
    {
        return prefs.getString(userNameKey, "Player1");
    }

    public void setUserName(String name)
    {
        prefs.putString(userNameKey, name);
        prefs.flush();
    }
}
