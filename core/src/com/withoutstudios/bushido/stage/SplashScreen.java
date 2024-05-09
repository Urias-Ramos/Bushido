package com.withoutstudios.bushido.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.enumerators.StageScene;

/**
 * Esta clase representa la pantalla de carga del juego, es la primera vista que se muestra
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class SplashScreen extends ScreenAdapter {
	private Bushido bushido;
	private Viewport viewport;
	
	private float x, y;
	
	private Stage stage;

	public SplashScreen(Bushido bushido) {
		this.bushido = bushido;
		viewport = new FitViewport(GameManifest.SCREEN_WIDTH, GameManifest.SCREEN_HEIGHT);
		
		stage = new Stage(viewport, bushido.getSpriteBatch());
		
		FreeTypeFontGenerator loader = new FreeTypeFontGenerator(Gdx.files.internal("font/thaleahFat.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.color = Color.WHITE;
		parameter.size = 42;
		
		BitmapFont font = new BitmapFont();
		font = loader.generateFont(parameter);
		font.getData().setScale(1.0f);
		
		Label label = new Label("Without Studios", new Label.LabelStyle(font, Color.WHITE));
		
		stage.addActor(label);
	}
	
	@Override
	public void show() {
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f / 255, 0f / 255, 0f / 255, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(!bushido.getAssets().getAssetManager().update()) {
			bushido.getSpriteBatch().setProjectionMatrix(viewport.getCamera().combined);
			
			x = viewport.getWorldWidth() / 2 - stage.getActors().get(0).getWidth() / 2;
			y = viewport.getWorldHeight() / 2 - stage.getActors().get(0).getHeight() / 2;
			
			stage.getActors().get(0).setPosition(x, y);
			
			stage.act();
			stage.draw();
		}
		else {
			bushido.getAssets().createResources();
			bushido.createMenu();
			dispose();
			bushido.changeStage(StageScene.STARTMENU);
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
	}

	@Override
	public void dispose() {
		viewport = null;
		stage.dispose();
	}
}