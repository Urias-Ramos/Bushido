package com.withoutstudios.bushido.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.tool.enumerators.StageScene;

/**
 * Esta clase representa la pantalla principal o menu inicio del juego
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class StartScreen extends MyStage {
	private float x, y;
	private TextButton btnPlay, btnSetting, btnExit, btnCredit;

	public StartScreen(Bushido bushido) {
		super(bushido);
        
        Texture textureTitle = bushido.getAssets().titleGame;
        Image title = new Image(textureTitle);
        title.setSize(400f, 135f);
       
        x = getViewport().getWorldWidth() / 2 - (title.getWidth() / 2) + 160 ;
        y = getViewport().getWorldHeight() - 150.0f;
        
        title.setPosition(x, y);
        
        getStage().addActor(title);
        
        FreeTypeFontGenerator loader = new FreeTypeFontGenerator(Gdx.files.internal("font/thaleahFat.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 32;
		parameter.color = Color.WHITE;
		
		BitmapFont font = loader.generateFont(parameter);
		font.getData().setScale(264 / 64f / 2);
		font.setUseIntegerPositions(false);
		
		int margin = 55;
        
        TextButtonStyle style = new TextButtonStyle();
        style.font = font;
        style.fontColor = new Color(33 / 255f, 38 / 255f, 43 / 255f, 1f);
        style.overFontColor = new Color(70 / 255f, 130 / 255f, 50 / 255f, 1f);
        
        LabelStyle lblStyle = new LabelStyle();
        lblStyle.font = font;
        lblStyle.fontColor = new Color(217 / 255f, 217 / 255f, 217 / 255f, 1f);
        
        Label lblPlay = new Label("PLAY", lblStyle);
        lblPlay.setPosition((title.getX() + title.getWidth() / 2) - (lblPlay.getWidth() / 2) + 1.5f, 2.0f + (getViewport().getWorldHeight() / 2) + 30);
        getStage().addActor(lblPlay);
        
        btnPlay = new TextButton("PLAY", style);
        btnPlay.setPosition((title.getX() + title.getWidth() / 2) - (btnPlay.getWidth() / 2), (getViewport().getWorldHeight() / 2) + 30);
        getStage().addActor(btnPlay);
        
        Label lblSetting = new Label("SETTING", lblStyle);
        lblSetting.setPosition(1.5f + btnPlay.getX() + 30, 2.0f + btnPlay.getY() - margin);
        getStage().addActor(lblSetting);
        
        btnSetting = new TextButton("SETTING", style);
        btnSetting.setPosition(btnPlay.getX() + 30, btnPlay.getY() - margin);
        getStage().addActor(btnSetting);
        
        Label lblCredits = new Label("CREDITS", lblStyle);
        lblCredits.setPosition(1.5f + btnSetting.getX() + 30, 2.0f + btnSetting.getY() - margin);
        getStage().addActor(lblCredits);
        
        btnCredit = new TextButton("CREDITS", style);
        btnCredit.setPosition(btnSetting.getX() + 30, btnSetting.getY() - margin);
        getStage().addActor(btnCredit);
        
        Label lblExit = new Label("EXIT", lblStyle);
        lblExit.setPosition(1.5f + btnCredit.getX() + 30, 2.0f + btnCredit.getY() - margin);
        getStage().addActor(lblExit);
        
        btnExit = new TextButton("EXIT", style);
        btnExit.setPosition(btnCredit.getX() + 30, btnCredit.getY() - margin);
        getStage().addActor(btnExit);
        
        createListener();
	}
	
	private void createListener() {
		btnPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	dispose();
            	getBushido().changeStage(StageScene.LEVELMENU);
            }
        });
		
		btnCredit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	dispose();
            	getBushido().changeStage(StageScene.CREDITS);
            }
        });
		
		btnSetting.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	dispose();
            	getBushido().changeStage(StageScene.SETTINGSMENU);
            }
        });
		
		btnExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	Gdx.app.exit();
            }
        });
	}

	@Override
	public void resize(int width, int height) {
		getViewport().update(width, height, true);
	}
	
	@Override
	public void hide() {
		super.hide();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(getStage());
	}

	@Override
	public void dispose() {
		
	}

	@Override
	public void renderScreen(float delta) {
		if((Gdx.input.isKeyJustPressed(Input.Keys.BACK))) {
            Gdx.app.exit();
        }
	}
}