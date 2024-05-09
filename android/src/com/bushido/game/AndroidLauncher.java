package com.bushido.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.withoutstudios.bushido.Bushido;

/**
 * Esta clase es el lanzador para la version de movil (Android)
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		initialize(new Bushido(), config);
	}
}