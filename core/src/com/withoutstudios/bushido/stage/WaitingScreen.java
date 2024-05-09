package com.withoutstudios.bushido.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.enumerators.StageScene;

/**
 * Esta clase representa la pantalla de espera del juego
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class WaitingScreen extends ScreenAdapter {
	private Bushido bushido;
	private Viewport viewport;
	
	private float x, y;
	
	private Stage stage;

	public WaitingScreen(Bushido bushido) {
		this.bushido = bushido;
		viewport = new FitViewport(GameManifest.SCREEN_WIDTH, GameManifest.SCREEN_HEIGHT);
		
		stage = new Stage(viewport, bushido.getSpriteBatch());
		
		Label label = new Label("...", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
		label.setFontScale(1.0f);
		
		stage.addActor(label);
	}
	
	@Override
	public void show() {
		if(!GameManifest.CHANGE_MAP) {
			bushido.getMap().loadMap();
		}
		else {
			GameManifest.PAUSE = false;
			GameManifest.SCOORE_SCREEN = false;
			GameManifest.CURRENT_LEVEL++;
			bushido.getMap().getMapLoader().changeMap();
		}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(17f / 255, 17f / 255, 17f / 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(!bushido.getMap().isLoadedMap()) {
			bushido.getSpriteBatch().setProjectionMatrix(viewport.getCamera().combined);

			x = viewport.getWorldWidth() / 2 - stage.getActors().get(0).getWidth() * 1.5f;
			y = viewport.getWorldHeight() / 2 - stage.getActors().get(0).getHeight() / 2;

			stage.getActors().get(0).setPosition(x, y);

			stage.act();
			stage.draw();
		}
		else {
			bushido.changeStage(StageScene.MAP);
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void dispose() {
		
	}
}