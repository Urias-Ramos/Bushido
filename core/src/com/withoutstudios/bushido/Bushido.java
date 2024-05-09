package com.withoutstudios.bushido;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.withoutstudios.bushido.map.Map;
import com.withoutstudios.bushido.stage.CreditScreen;
import com.withoutstudios.bushido.stage.LevelScreen;
import com.withoutstudios.bushido.stage.PauseScreen;
import com.withoutstudios.bushido.stage.SettingScreen;
import com.withoutstudios.bushido.stage.SplashScreen;
import com.withoutstudios.bushido.stage.StartScreen;
import com.withoutstudios.bushido.stage.WaitingScreen;
import com.withoutstudios.bushido.tool.Assets;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.enumerators.StageScene;

/**
 * Esta es la clase principal del juego, aqui se crean todas las vistas y se cargan los recursos del juego
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class Bushido extends Game {
	private Assets assets;
	private SpriteBatch spriteBatch;
	
	//stage
	private SplashScreen splashScreen;
	private StartScreen startScreen;
	private LevelScreen levelScreen;
	private CreditScreen creditScreen;
	private SettingScreen settingScreen;
	private Map map;
	private PauseScreen pauseScreen;
	private WaitingScreen waitingScreen;
	
	private Preferences preferences;

	@Override
	public void create() {
		//se cargan las preferencias del juego, o datos de la aplicacion
		preferences = Gdx.app.getPreferences("bushidoData");
		
		//se verifica si existe alguna de las opciones guardadas
		if(!preferences.contains("music")) {//no existe se guardan los valores por defecto
			preferences.putBoolean("music", GameManifest.MUSIC);
			preferences.putBoolean("soundEffect", GameManifest.SOUND);
			preferences.putInteger("levelUnlocked", GameManifest.LEVEL);
			preferences.flush();
		}
		else {//si existen se cargan
			GameManifest.MUSIC = preferences.getBoolean("music");
			GameManifest.SOUND = preferences.getBoolean("soundEffect");
			GameManifest.LEVEL = preferences.getInteger("levelUnlocked");
		}
		
		assets = new Assets();
		spriteBatch = new SpriteBatch();
		
		assets.loadAsset();
		splashScreen = new SplashScreen(this);
		
		//evita que al presionar la tecla retroceso de los telefonos moviles se salga de la aplicacion
		Gdx.input.setCatchKey(Input.Keys.BACK, true);
		
		//La primera vista que muestra es la pantalla de carga
		changeStage(StageScene.SPLASHSCREEN);
	}
	
	/**
	 * Este metodo crea las vistas o menu del juego
	 */
	public void createMenu() {
		startScreen = new StartScreen(this);
		levelScreen = new LevelScreen(this);
		creditScreen = new CreditScreen(this);
		settingScreen = new SettingScreen(this);
		map = new Map(this);
		pauseScreen = new PauseScreen(this);
		waitingScreen = new WaitingScreen(this);
	}
	
	/**
	 * Este metodo permite cambiar las vistas del juego
	 * 
	 * @param stageScene tipo de escena a cambiar
	 */
	public void changeStage(StageScene stageScene) {
		switch(stageScene) {
		case SPLASHSCREEN:
			setScreen(splashScreen);
			break;
		case STARTMENU:
			setScreen(startScreen);
			break;
		case LEVELMENU:
			setScreen(levelScreen);
			break;
		case CREDITS:
			setScreen(creditScreen);
			break;
		case SETTINGSMENU:
			setScreen(settingScreen);
			break;
		case MAP:
			setScreen(map);
			break;
		case WAITING:
			setScreen(waitingScreen);
			break;
			default:
		}
	}
	
	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}
	
	@Override
	public void dispose() {
		super.dispose();
		spriteBatch.dispose();
		getAssets().getAssetManager().dispose();
	}

	public Map getMap() {
		return map;
	}

	public PauseScreen getPauseScreen() {
		return pauseScreen;
	}
	
	public Assets getAssets() {
		return assets;
	}

	public Preferences getPreferences() {
		return preferences;
	}
	
	public void playSoundEffect(Sound sound) {
		if(GameManifest.SOUND) {
			sound.play();
		}
	}
}