package com.withoutstudios.bushido.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.enumerators.StageScene;

/**
 * Esta clase representa la pantalla de niveles del juego, aqui se podra seleccionar el nivel a jugar
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class LevelScreen extends MyStage {
	private float buttonSize, x, y;
	
	private ImageButton btnClose;
	private ImageButton[] btnSlot;

	public LevelScreen(Bushido bushido) {
		super(bushido);
		
        //bg
        Texture bgTexture = bushido.getAssets().levelBg;
        Image bg = new Image(bgTexture);
        bg.setSize(375, 375);
        
        x = (getViewport().getWorldWidth() / 2) - bg.getWidth() / 2;
        y = (getViewport().getWorldHeight() / 2) - bg.getHeight() / 2;
        
        bg.setPosition(x, y);
        getStage().addActor(bg);
        //bg
        
        //table
        Texture textureTable = bushido.getAssets().levelTable;
        Image table = new Image(textureTable);
        table.setSize(bg.getWidth() - 60, bg.getHeight() - 60);
        
        x = (getViewport().getWorldWidth() / 2) - table.getWidth() / 2;
        y = (getViewport().getWorldHeight() / 2) - table.getHeight() / 2;
        
        table.setPosition(x, y);
        getStage().addActor(table);
        //table
        
        //title
		BitmapFont font = bushido.getAssets().fontMenu;
		font.getData().setScale(1.0f);
        
        Label lblTile = new Label("SELECT LEVEL", new Label.LabelStyle(font, Color.BLACK));
        lblTile.setFontScale(0.55f);
        lblTile.setPosition((table.getX() + table.getWidth() / 2) - (lblTile.getHeight() * 3.35f) / 2, table.getY() + table.getHeight() - 82);
        
        getStage().addActor(lblTile);
        //title
        
        buttonSize = 48.0f;
        
        TextureRegionDrawable textureCloseDrawable = new TextureRegionDrawable(bushido.getAssets().btnClose);
        textureCloseDrawable.setMinSize(buttonSize, buttonSize);
        
        x = (bg.getX() + bg.getWidth() - buttonSize / 2);
        y = (bg.getY() + bg.getHeight() - buttonSize / 2);
        
        btnClose = new ImageButton(textureCloseDrawable);
        btnClose.setPosition(x, y, Align.center);
        
        buttonSize = 56.0f;

        getStage().addActor(btnClose);
        
        Texture slotTexture = bushido.getAssets().levelSlot;
        btnSlot = new ImageButton[9];
        
        float margin = 20.0f;
        x = (table.getWidth() / 2) + ((margin * 2) + (buttonSize * 2));
        y = ((table.getY() + table.getHeight() / 2) + 55.0f);
        
        int row = 0;
        
        Label lblTxt = null;
        FreeTypeFontGenerator loader = new FreeTypeFontGenerator(Gdx.files.internal("font/thaleahFat.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.color = Color.WHITE;
		parameter.size = 22;
		
		Texture textureLock = bushido.getAssets().levelLock;
		
		BitmapFont fontSlot = new BitmapFont();
		fontSlot = loader.generateFont(parameter);
		fontSlot.getData().setScale(2.0f);
        
        for(int i=0; i<9; i++) {
            TextureRegionDrawable textureSettingDrawable = new TextureRegionDrawable(new TextureRegion(slotTexture));
            textureSettingDrawable.setMinSize(buttonSize, buttonSize);
            
            if(i == 3 || i == 6) {
            	y -= margin + buttonSize;
            }
            
            if(row > 2) {
            	row = 0;
            }
            
            btnSlot[i] = new ImageButton(textureSettingDrawable);
            btnSlot[i].setPosition(x + (margin + buttonSize) * row, y, Align.center);
            
            font.getData().scale(0.011f);
            lblTxt = new Label(""+(i+1), new Label.LabelStyle(fontSlot, Color.BLACK));
            
            Image lock = new Image(textureLock);
            lock.setSize(buttonSize / 2, buttonSize / 2);
            
            btnSlot[i].addActor(lblTxt);
            lblTxt.setPosition((lblTxt.getX() + buttonSize / 4) + 3.0f, lblTxt.getY() + 5.0f);
            lock.setPosition((lblTxt.getX() - 1.5f), lblTxt.getY() + 7.0f);
            
            btnSlot[i].addActor(lock);            
            getStage().addActor(btnSlot[i]);
            
            row++;
        }
        
        createListener();
	}
	
	private void selectLevel(int level) {
		if(!btnSlot[level - 1].getChild(2).isVisible()) {
    		GameManifest.CURRENT_LEVEL = level;
    		getBushido().getMap().getMapLoader().changeMap();
    		getBushido().changeStage(StageScene.MAP);
    	}
		else {
			getBushido().playSoundEffect(getBushido().getAssets().levelLockedSound);
		}
	}
	
	private void createListener() {
		btnClose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	dispose();
                getBushido().changeStage(StageScene.STARTMENU);
            }
        });
		
		btnSlot[0].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	selectLevel(1);
            }
        });
		
		btnSlot[1].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	selectLevel(2);
            }
        });
		
		btnSlot[2].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	selectLevel(3);
            }
        });
		
		btnSlot[3].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	selectLevel(4);
            }
        });
		
		btnSlot[4].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	selectLevel(5);
            }
        });
		
		btnSlot[5].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	selectLevel(6);
            }
        });
		
		btnSlot[6].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	selectLevel(7);
            }
        });
		
		btnSlot[7].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	selectLevel(8);
            }
        });
		
		btnSlot[8].addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	selectLevel(9);
            }
        });
	}

	@Override
	public void renderScreen(float delta) {
		for(int i=0; i<btnSlot.length; i++) {
			btnSlot[i].getChild(0).setVisible(true);
			btnSlot[i].getChild(1).setVisible(false);
			btnSlot[i].getChild(2).setVisible(true);
			if(i < GameManifest.LEVEL) {
				btnSlot[i].getChild(0).setVisible(true);
				btnSlot[i].getChild(1).setVisible(true);
				btnSlot[i].getChild(2).setVisible(false);
			}
		}
		
		if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)||(Gdx.input.isKeyJustPressed(Input.Keys.BACK))) {
        	dispose();
			getBushido().changeStage(StageScene.STARTMENU);
        }
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
}