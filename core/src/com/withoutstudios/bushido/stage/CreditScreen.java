package com.withoutstudios.bushido.stage;

import java.io.BufferedReader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.tool.enumerators.StageScene;

/**
 * Esta clase representa la pantalla de creditos del juego
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class CreditScreen extends MyStage {
	private TextArea textArea;
	private int y;

	public CreditScreen(Bushido bushido ) {
		super(bushido);
		
		FileHandle file = Gdx.files.internal("file/credits.txt");
		BufferedReader reader = new BufferedReader(file.reader());
		String line = "", content = "";
		try {
			while((line = reader.readLine()) != null) {
				content += line+"\n";
			}
			reader.close();
		} catch(Exception e) {
			
		}
		
		FreeTypeFontGenerator loader = new FreeTypeFontGenerator(Gdx.files.internal("font/thaleahFat.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.color = Color.WHITE;
		parameter.size = 22;
		
		BitmapFont fontSlot = new BitmapFont();
		fontSlot = loader.generateFont(parameter);
		fontSlot.getData().setScale(0.95f);
		
		TextFieldStyle textFieldStyle = new TextFieldStyle();
		textFieldStyle.font = fontSlot;
		textFieldStyle.fontColor = Color.WHITE;
		
		textArea = new TextArea(content, textFieldStyle);
		textArea.setSize(450, 925);
		textArea.setPosition(10, -textArea.getHeight());
		textArea.setDisabled(true);
		
		addListener();
		
		getStage().addActor(textArea);
		addListener();
	}
	
	private void addListener() {
		getStage().addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				dispose();
				getBushido().changeStage(StageScene.STARTMENU);
			}
		});
	}

	@Override
	public void renderScreen(float delta) {
		y += 1;
		
		textArea.setPosition(getViewport().getWorldWidth() - textArea.getWidth(), y);
		
        if(y > (Gdx.graphics.getHeight() / 2)) {
        	y = (int) -textArea.getHeight();
        }
        
        if((Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))||(Gdx.input.isKeyJustPressed(Input.Keys.BACK))) {
        	dispose();
			getBushido().changeStage(StageScene.STARTMENU);
        }
	}
	
	@Override
	public void show() {
		if(getBushido().getAssets().ambient != null) {
			if(getBushido().getAssets().ambient.isPlaying()) {
				getBushido().getAssets().ambient.stop();
			}
		}
		
		y = (int) -textArea.getHeight();
		Gdx.input.setInputProcessor(getStage());
	}

	@Override
	public void resize(int width, int height) {
		getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		
	}
}