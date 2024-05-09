package com.withoutstudios.bushido.stage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.UpdateAndRender;
import com.withoutstudios.bushido.tool.enumerators.StageScene;

/**
 * Esta clase representa la pantalla de pausa del juego
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class PauseScreen implements UpdateAndRender {
	private Bushido bushido;
	private float buttonSize, x, y, xTitle;
	
	private Viewport viewport;
	private Stage stage;
	
	private ImageButton btnContinue, btnHome, btnReset;
	
	private Image star0, star1, star2, star3;
	private Label lblTitle, lblScoore;

	public PauseScreen(Bushido bushido) {
		this.bushido = bushido;
		viewport = new FitViewport(GameManifest.SCREEN_WIDTH, GameManifest.SCREEN_HEIGHT);
		this.stage = new Stage(viewport, bushido.getSpriteBatch());
        
        //bg
        Texture bgTexture = bushido.getAssets().settingBG;
        Image bg = new Image(bgTexture);
        bg.setSize(320, 400);
        
        x = (viewport.getWorldWidth() / 2) - bg.getWidth() / 2;
        y = (viewport.getWorldHeight() / 2) - bg.getHeight() / 2;
        
        bg.setPosition(x, y);
        stage.addActor(bg);
        //bg
        
        //table
        Texture textureTable = bushido.getAssets().settingTable;
        Image table = new Image(textureTable);
        table.setSize(bg.getWidth() - 20, bg.getHeight() - 100);
        
        x = (viewport.getWorldWidth() / 2) - table.getWidth() / 2;
        y = ((viewport.getWorldHeight() / 2) - table.getHeight() / 2) + 40;
        
        table.setPosition(x, y);
        stage.addActor(table);
        //table
        
        //title
        BitmapFont font = bushido.getAssets().fontMenu;
		font.getData().setScale(1.0f);
        
        lblTitle = new Label("SCOORE", new Label.LabelStyle(font, Color.BLACK));
        lblTitle.setFontScale(0.55f);
        lblTitle.setPosition(table.getX() + (lblTitle.getWidth() / 2) - 45f, table.getY() + table.getHeight() - 90);
        xTitle = lblTitle.getX();
        
        
        stage.addActor(lblTitle);
        //title
        
        buttonSize = 64.0f;
        
        Texture textureReset = new Texture(Gdx.files.internal("ui/restart.png"));
        TextureRegionDrawable textureDrawableReset = new TextureRegionDrawable(textureReset);
        textureDrawableReset.setMinSize(buttonSize, buttonSize);
        
        x = (bg.getX() + bg.getWidth() / 2) - buttonSize - 10;
        y = (bg.getY() + bg.getHeight() / 2) - 138.0f;
        
        btnReset = new ImageButton(textureDrawableReset);
        btnReset.setPosition(x, y, Align.center);
        
        Texture textureContinue = new Texture(Gdx.files.internal("ui/play.png"));
        TextureRegionDrawable textureDrawableContinue = new TextureRegionDrawable(textureContinue);
        textureDrawableContinue.setMinSize(buttonSize, buttonSize);
        
        x = (bg.getX() + bg.getWidth() / 2);
        
        btnContinue = new ImageButton(textureDrawableContinue);
        btnContinue.setPosition(x, y, Align.center);
        
        Texture textureHome = new Texture(Gdx.files.internal("ui/home.png"));
        TextureRegionDrawable textureDrawableHome = new TextureRegionDrawable(textureHome);
        textureDrawableHome.setMinSize(buttonSize, buttonSize);
        
        x = (bg.getX() + bg.getWidth() / 2) + buttonSize + 10;
        
        btnHome = new ImageButton(textureDrawableHome);
        btnHome.setPosition(x, y, Align.center);
        
        star0 = new Image(bushido.getAssets().star0);
        star0.setSize(195, 100);
        star0.setPosition(bg.getX() + (bg.getWidth() / 2) - 97.5f, bg.getY() + bg.getHeight() / 2);
       
        star1 = new Image(bushido.getAssets().star1);
        star1.setSize(195, 100);
        star1.setPosition(bg.getX() + (bg.getWidth() / 2) - 97.5f, bg.getY() + bg.getHeight() / 2);
        
        star2 = new Image(bushido.getAssets().star2);
        star2.setSize(195, 100);
        star2.setPosition(bg.getX() + (bg.getWidth() / 2) - 97.5f, bg.getY() + bg.getHeight() / 2);
        
        star3 = new Image(bushido.getAssets().star3);
        star3.setSize(195, 100);
        star3.setPosition(bg.getX() + (bg.getWidth() / 2) - 97.5f, bg.getY() + bg.getHeight() / 2);
        
        lblScoore = new Label("SCOORE: 00000", new Label.LabelStyle(font, Color.DARK_GRAY));
        lblScoore.setFontScale(0.32f);
        lblScoore.setPosition(star0.getX() + 30, star0.getY() - 80);
        stage.addActor(lblScoore);
        
        stage.addActor(star0);
        stage.addActor(star1);
        stage.addActor(star2);
        stage.addActor(star3);
        
        stage.addActor(btnReset);
        stage.addActor(btnContinue);
        stage.addActor(btnHome);
        
        createListener();
	}
	
	private void createListener() {
		btnContinue.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	if(GameManifest.SCOORE_SCREEN) {
            		GameManifest.PAUSE = false;
            		GameManifest.SCOORE_SCREEN = false;
                	GameManifest.CHANGE_MAP = true;
            	}
            	GameManifest.PAUSE = false;
            }
        });
		
		btnHome.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	GameManifest.SCOORE_SCREEN = false;
            	GameManifest.PAUSE = false;
                bushido.changeStage(StageScene.STARTMENU);
            }
        });
		
		btnReset.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
            	if(!GameManifest.SCOORE_SCREEN) {
            		GameManifest.SCOORE_SCREEN = false;
                	GameManifest.PAUSE = false;
                	bushido.changeStage(StageScene.WAITING);
            	}
            	else {
            		GameManifest.SCOORE_SCREEN = false;
                	GameManifest.PAUSE = false;
            		bushido.getMap().getMapLoader().changeMap();
            		bushido.changeStage(StageScene.MAP);
            	}
            }
        });
	}

	@Override
	public void update(float delta) {
		Gdx.input.setInputProcessor(stage);
		
		if(GameManifest.SCOORE_SCREEN) {
			lblTitle.setText("SCOORE");
			lblTitle.setPosition(xTitle, lblTitle.getY());
		}
		else {
			lblTitle.setText("PAUSE");
			lblTitle.setPosition(xTitle + 12.0f, lblTitle.getY());
		}
		
		GameManifest.PLAYER_MOVE_RIGHT = false;
		GameManifest.PLAYER_MOVE_LEFT = false;
		GameManifest.PLAYER_MOVE_UP = false;
		GameManifest.PLAYER_MOVE_DOWN = false;
		
		GameManifest.PLAYER_JUMP = false;
		GameManifest.PLAYER_ROLLING = false;
		
		GameManifest.PLAYER_ATTACK_1 = false;
		GameManifest.PLAYER_ATTACK_2 = false;
		
		if((GameManifest.SCOORE > -1)&&(GameManifest.SCOORE < 25000)) {
			stage.getActors().get(4).setVisible(true);
			stage.getActors().get(5).setVisible(false);
			stage.getActors().get(6).setVisible(false);
			stage.getActors().get(7).setVisible(false);
		}
		else if((GameManifest.SCOORE > 24999)&&(GameManifest.SCOORE < 50000)) {
			stage.getActors().get(4).setVisible(false);
			stage.getActors().get(5).setVisible(true);
			stage.getActors().get(6).setVisible(false);
			stage.getActors().get(7).setVisible(false);
		}
		else if((GameManifest.SCOORE > 49999)&&(GameManifest.SCOORE < 75000)) {
			stage.getActors().get(4).setVisible(false);
			stage.getActors().get(5).setVisible(false);
			stage.getActors().get(6).setVisible(true);
			stage.getActors().get(7).setVisible(false);
		}
		else if((GameManifest.SCOORE > 74999)&&(GameManifest.SCOORE < 100000)) {
			stage.getActors().get(4).setVisible(false);
			stage.getActors().get(5).setVisible(false);
			stage.getActors().get(6).setVisible(false);
			stage.getActors().get(7).setVisible(true);
		}
		
		lblScoore.setText("SCOORE: "+String.format("%05d", GameManifest.SCOORE));
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		stage.act();
		stage.draw();
	}
}