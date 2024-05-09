package com.bushido.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.withoutstudios.bushido.Bushido;


/**
 * Esta clase es el lanzador para la version de PC escritorio
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("Bushido");
		config.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());
		
		new Lwjgl3Application(new Bushido(), config);
	}
}