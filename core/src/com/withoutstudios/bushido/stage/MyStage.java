package com.withoutstudios.bushido.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.animator.AnimationController;
import com.withoutstudios.bushido.tool.animator.AnimationCreator;
import com.withoutstudios.bushido.tool.animator.AnimationManager;
import com.withoutstudios.bushido.tool.enumerators.CardinalPoint;

/**
 * Esta clase contiene metodos y variables que las vistas del juego usan
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public abstract class MyStage extends ScreenAdapter {
	private Bushido bushido;
	private Viewport viewport;
	private Stage stage;
	
	private Image layer1, layer2, layer3;
	private Image layer11, layer22, layer33;
	private Image terrain;
	
	private long timeParallax1, timeParallax2;
	private int x1, x2, px11, px12, px21, px22;
	
	private float elapsedAnim;
	
	private AnimationController animationController;

	public MyStage(Bushido bushido) {
		this.bushido = bushido;
		viewport = new FitViewport(GameManifest.SCREEN_WIDTH, GameManifest.SCREEN_HEIGHT);
		this.stage = new Stage(viewport, bushido.getSpriteBatch());
		
        //layer
        Texture textureLayer1 = bushido.getAssets().layer1;
        layer1 = new Image(textureLayer1);
        layer1.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        stage.addActor(layer1);
        
        layer11 = new Image(textureLayer1);
        layer11.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        stage.addActor(layer11);
        //layer
        
        //layer
        Texture textureLayer2 = bushido.getAssets().layer2;
        layer2 = new Image(textureLayer2);
        layer2.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        stage.addActor(layer2);
        
        layer22 = new Image(textureLayer2);
        layer22.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        stage.addActor(layer22);
        //layer
        
        //layer
        Texture textureLayer3 = bushido.getAssets().layer3;
        layer3 = new Image(textureLayer3);
        layer3.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        stage.addActor(layer3);
        
        layer33 = new Image(textureLayer3);
        layer33.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        stage.addActor(layer33);
        //layer
        
        terrain = new Image(bushido.getAssets().terrain);
        terrain.setSize(1024, 32);
        terrain.setPosition(0, 0f);
        stage.addActor(terrain);
        
        AnimationCreator[] walk = new AnimationCreator[1];
		walk[0] = new AnimationCreator(bushido.getAssets().getPlayerAnimation()[2], CardinalPoint.EAST, null);
		
		AnimationManager walkAnimation = new AnimationManager(walk);
		
		AnimationManager[] animationManager = new AnimationManager[1];
		animationManager[0] = walkAnimation;
		
        animationController = new AnimationController(animationManager);
        animationController.setCardinalPoint(CardinalPoint.EAST);
        animationController.setIndexAnimation(0);
        
        timeParallax1 = System.nanoTime();
		timeParallax2 = System.nanoTime();
	}
	
	public abstract void renderScreen(float delta);
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(17f / 255, 17f / 255, 17f / 255, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        if(GameManifest.isServedTime(timeParallax1, 300)) {
			timeParallax1 = System.nanoTime();
			
			if(px11 >= 0 - layer1.getWidth()) {
				px11--;
				px12 = (int) (px11 + layer1.getWidth());
			}
			else {
				px11 = 0;
			}
		}
		
		if(GameManifest.isServedTime(timeParallax2, 150)) {
			timeParallax2 = System.nanoTime();
			
			if(px21 >= 0 - layer1.getWidth()) {
				px21--;
				px22 = (int) (px21 + layer1.getWidth());
			}
			else {
				px21 = 0;
			}
		}
		
		if(x1 >= 0 - layer1.getWidth()) {
			x1--;
			x2 = (int) (x1 + layer1.getWidth());
		}
		else {
			x1 = 0;
		}
		
		layer1.setX(px11);
		layer2.setX(px21);
		layer3.setX(x1);
		
		layer11.setX(px12);
		layer22.setX(px22);
		layer33.setX(x2);
		
		renderScreen(delta);
		
		elapsedAnim += delta;
		animationController.animate(elapsedAnim);
		
        stage.act();
        stage.draw();
        
        bushido.getSpriteBatch().begin();
		bushido.getSpriteBatch().draw(animationController.getFrame(elapsedAnim, true), -90, -14, 144 * 3, 80 * 3);
		bushido.getSpriteBatch().end();
	}

	public Bushido getBushido() {
		return bushido;
	}

	public Viewport getViewport() {
		return viewport;
	}

	public Stage getStage() {
		return stage;
	}
}