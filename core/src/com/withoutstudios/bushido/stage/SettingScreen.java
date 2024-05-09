package com.withoutstudios.bushido.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
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
 * Esta clase representa la pantalla de ajustes del juego, aqui se podra activar o desactivar los sonidos y otros ajustes mas
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class SettingScreen extends MyStage {
	private float buttonSize, x, y;
	
	private ImageButton btnClose;
    private CheckBox btnMusic, btnSoundEffect;

	public SettingScreen(Bushido bushido) {
		super(bushido);
		
        //bg
        Texture bgTexture = bushido.getAssets().settingBG;
        Image bg = new Image(bgTexture);
        bg.setSize(450, 325);
        
        x = (getViewport().getWorldWidth() / 2) - bg.getWidth() / 2;
        y = (getViewport().getWorldHeight() / 2) - bg.getHeight() / 2;
        
        bg.setPosition(x, y);
        getStage().addActor(bg);
        //bg
        
        //table
        Texture textureTable = bushido.getAssets().settingTable;
        Image table = new Image(textureTable);
        table.setSize(bg.getWidth() - 10, bg.getHeight() - 10);
        
        x = (getViewport().getWorldWidth() / 2) - table.getWidth() / 2;
        y = (getViewport().getWorldHeight() / 2) - table.getHeight() / 2;
        
        table.setPosition(x, y);
        getStage().addActor(table);
        //table
        
        //title
        Label lblTile = new Label("SETTINGS", new Label.LabelStyle(bushido.getAssets().fontMenu, Color.BLACK));
        lblTile.setFontScale(0.55f);
        lblTile.setPosition((bg.getX() + lblTile.getWidth() / 2) - 10.5f, table.getY() + table.getHeight() - 95);
        
        getStage().addActor(lblTile);
        //title
        
        buttonSize = 48.0f;
        
        TextureRegionDrawable textureCloseDrawable = new TextureRegionDrawable(bushido.getAssets().btnClose);
        textureCloseDrawable.setMinSize(buttonSize, buttonSize);
        
        x = (bg.getX() + bg.getWidth() - buttonSize / 2);
        y = (bg.getY() + bg.getHeight() - buttonSize / 2);
        
        btnClose = new ImageButton(textureCloseDrawable);
        btnClose.setPosition(x, y, Align.center);
        
        buttonSize = 48.0f;

        getStage().addActor(btnClose);
        
        //Toggle music
        TextureRegionDrawable checkOff = new TextureRegionDrawable(bushido.getAssets().checkOff);
        checkOff.setMinSize(92, 32);
        
        TextureRegionDrawable checkOn = new TextureRegionDrawable(bushido.getAssets().checkOn);
        checkOn.setMinSize(92, 32);
        
        CheckBoxStyle style = new CheckBoxStyle();
        style.font = new BitmapFont();
        
        style.checkboxOff =  checkOff;
        style.checkboxOn =  checkOn;
        
        x = table.getX() - 60 + table.getWidth() - checkOff.getMinWidth();
        y = table.getY() + table.getHeight() / 2;
        
        btnMusic = new CheckBox("", style);
        btnMusic.setChecked(GameManifest.MUSIC);
        btnMusic.setPosition(x, y);
        
        Label lblMusic = new Label("Music:", new Label.LabelStyle(bushido.getAssets().fontMenu, Color.BLACK));
        lblMusic.setFontScale(0.40f);
        lblMusic.setPosition(table.getX() + 50, y - 24);
        
        getStage().addActor(lblMusic);
        getStage().addActor(btnMusic);
        //Toggle music
        
        //Toggle sound effect
        y -= 40;
        btnSoundEffect = new CheckBox("", style);
        btnSoundEffect.setChecked(GameManifest.SOUND);
        btnSoundEffect.setPosition(x, y);
        
        Label lblSoundEffect = new Label("Sound Effect:", new Label.LabelStyle(bushido.getAssets().fontMenu, Color.BLACK));
        lblSoundEffect.setFontScale(0.40f);
        lblSoundEffect.setPosition(table.getX() + 50, y - 30);
        
        getStage().addActor(lblSoundEffect);
        getStage().addActor(btnSoundEffect);
        //Toggle sound effect
        
        createListener();
	}
	
	private void createListener() {
		btnClose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	dispose();
            	
            	getBushido().getPreferences().putBoolean("music", GameManifest.MUSIC);
            	getBushido().getPreferences().putBoolean("soundEffect", GameManifest.SOUND);
            	getBushido().getPreferences().putInteger("levelUnlocked", GameManifest.LEVEL);
            	getBushido().getPreferences().flush();
            	
                getBushido().changeStage(StageScene.STARTMENU);
            }
        });
	}

	@Override
	public void renderScreen(float delta) {
        GameManifest.MUSIC = btnMusic.isChecked();
        GameManifest.SOUND = btnSoundEffect.isChecked();
        
        if((Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))||(Gdx.input.isKeyJustPressed(Input.Keys.BACK))) {
        	dispose();
        	
        	getBushido().getPreferences().putBoolean("music", GameManifest.MUSIC);
        	getBushido().getPreferences().putBoolean("soundEffect", GameManifest.SOUND);
        	getBushido().getPreferences().putInteger("levelUnlocked", GameManifest.LEVEL);
        	getBushido().getPreferences().flush();
        	
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