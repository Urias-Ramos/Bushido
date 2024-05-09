package com.withoutstudios.bushido.map;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.entity.Player;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.UpdateAndRender;
import com.withoutstudios.bushido.tool.animator.AnimationController;
import com.withoutstudios.bushido.tool.animator.AnimationCreator;
import com.withoutstudios.bushido.tool.animator.AnimationManager;
import com.withoutstudios.bushido.tool.enumerators.CardinalPoint;

/**
 * Esta clase es la encargada de crear y administrar los controles o UI del juego en el mapa.
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class PlayerHUD implements UpdateAndRender {
	private Stage stage;
	private Image barBorder, barProgress;
	private Image screenHurt;
	
	private long timeHurt;
	
	private float x, y, elapsedAnim;
	private AnimationController animationController;
	private Label label;
	
	private boolean visible;
	
	private Touchpad touchpad;
	private double angle, degress;

	public PlayerHUD(Bushido bushido) {
		stage = new Stage(new ExtendViewport(GameManifest.SCREEN_WIDTH / GameManifest.PPM, GameManifest.SCREEN_HEIGHT / GameManifest.PPM));
		
		screenHurt = new Image(bushido.getAssets().screenHurt);
		screenHurt.setPosition(0, 0);
		screenHurt.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
		
		stage.addActor(screenHurt);
		stage.getActors().get(0).setVisible(false);
		
		timeHurt = System.nanoTime();
		
		FreeTypeFontGenerator loader = new FreeTypeFontGenerator(Gdx.files.internal("font/thaleahFat.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 32;
		parameter.color = Color.WHITE;
		
		BitmapFont font = loader.generateFont(parameter);
		font.getData().setScale(3 / 64f / 2);
		font.setUseIntegerPositions(false);
		
		label = new Label("x0", new Label.LabelStyle(font, Color.WHITE));
		stage.addActor(label);
		
		AnimationCreator[] dead = new AnimationCreator[1];
		dead[0] = new AnimationCreator(bushido.getAssets().getSkullAnimation()[0], CardinalPoint.NONE, null);
		
		AnimationManager deadAnimation = new AnimationManager(dead);
		
		AnimationManager[] animationManager = new AnimationManager[1];
		animationManager[0] = deadAnimation;
		
		setAnimationController(new AnimationController(animationManager));
		getAnimationController().setCardinalPoint(CardinalPoint.NONE);
		getAnimationController().setIndexAnimation(0);
		
		createBar(bushido);
		createMobileControl();
		
		Touchpad.TouchpadStyle touchpadStyle = new TouchpadStyle();
		Skin touchpadSkin = new Skin();
		touchpadSkin.add("touchBackground", new Texture("ui/touchBackground.png"));
		touchpadSkin.add("touchKnob", new Texture("ui/touchKnob.png"));
		touchpadStyle.background = touchpadSkin.getDrawable("touchBackground");
		touchpadStyle.knob = touchpadSkin.getDrawable("touchKnob");
		touchpadStyle.knob.setMinWidth(42 / GameManifest.PPM);
		touchpadStyle.knob.setMinHeight(42 / GameManifest.PPM);

		touchpad = new Touchpad(32 / GameManifest.PPM, touchpadStyle);
		touchpad.setBounds(42 / GameManifest.PPM, 42 / GameManifest.PPM, 130 / GameManifest.PPM, 130 / GameManifest.PPM);
		touchpad.setResetOnTouchUp(true);
		
		stage.addActor(touchpad);
		
		stage.getActors().get(13).setVisible(false);
	}
	
	private void getMovePlayer(double degress) {
		if(degress > 45 && degress < 135) {
			GameManifest.PLAYER_MOVE_RIGHT = true;
		}
		else if(degress > 255 && degress < 315) {
			GameManifest.PLAYER_MOVE_LEFT = true;
		}
		else if(degress > 135 && degress < 255) {
			GameManifest.PLAYER_MOVE_DOWN = true;
		}
		else if(degress < 45 || degress > 315 || (degress > 135 && degress < 255)) {
			GameManifest.PLAYER_MOVE_UP = true;
		}
	}
	
	private void createBar(Bushido bushido) {
		Image iconHolder = new Image(bushido.getAssets().iconCircleRed);
		iconHolder.setSize(42 / GameManifest.PPM, 42 / GameManifest.PPM);
		iconHolder.setPosition(0.1f, stage.getViewport().getWorldHeight() - 1.4f);
		
		x = iconHolder.getX();
		y = iconHolder.getY() - 1.0f;
		
		label.setPosition(x + 0.8f, y);
		
		Image iconHealth = new Image(bushido.getAssets().iconHealth);
		iconHealth.setSize(24 / GameManifest.PPM, 24 / GameManifest.PPM);
		iconHealth.setPosition((iconHolder.getX() + iconHolder.getWidth() / 4) - 0.03f, (iconHolder.getY() + iconHolder.getHeight() / 4) - 0.04f);
		
		barBorder = new Image(bushido.getAssets().progressBorder);
		barBorder.setSize(180 / GameManifest.PPM, 42 / GameManifest.PPM);
		barBorder.setPosition((iconHolder.getX() + iconHolder.getWidth()) - 1.0f, (iconHolder.getY() - iconHolder.getHeight() / 7.0f) + 0.18f);
		
		barProgress = new Image(bushido.getAssets().progressBar);
		barProgress.setSize(190 / GameManifest.PPM, 26 / GameManifest.PPM);
		barProgress.setPosition((iconHolder.getX() + iconHolder.getWidth()) - 0.68f, (iconHolder.getY() - iconHolder.getHeight() / 7.0f) + 0.46f);
		
		stage.addActor(barBorder);
		stage.addActor(barProgress);
		stage.addActor(iconHolder);
		stage.addActor(iconHealth);
	}
	
	private void createMobileControl() {
		float sizeButton = 1.00f;
		
		Image btnPause = new Image(new Texture(Gdx.files.internal("ui/pause.png")));
		btnPause.setSize(sizeButton, sizeButton);
		btnPause.setPosition(stage.getViewport().getWorldWidth() - sizeButton - 4 / GameManifest.PPM, stage.getViewport().getWorldHeight() - (sizeButton + 0.2f));
		btnPause.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				GameManifest.PAUSE = true;
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				
				super.touchUp(event, x, y, pointer, button);
			}
		});
		
		sizeButton = 60 / GameManifest.PPM;
		
		Image jumpControl = new Image(new Texture(Gdx.files.internal("ui/android-button-action.png")));
		jumpControl.setSize(sizeButton + 0.2f, sizeButton + 0.2f);
		jumpControl.setPosition(stage.getViewport().getWorldWidth() - sizeButton - 24 / GameManifest.PPM, (8 / GameManifest.PPM) + (sizeButton / 2) - 0.5f);
		jumpControl.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				GameManifest.PLAYER_JUMP = true;
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				GameManifest.PLAYER_JUMP = false;
				super.touchUp(event, x, y, pointer, button);
			}
		});
		
		Image shootProjectile = new Image(new Texture(Gdx.files.internal("ui/android-button-action.png")));
		shootProjectile.setSize(sizeButton + 0.2f, sizeButton + 0.2f);
		shootProjectile.setPosition((jumpControl.getX() - 0.8f) - shootProjectile.getWidth() / 2 , jumpControl.getY() + 2.5f);
		shootProjectile.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				GameManifest.PLAYER_ATTACK_2 = true;
				GameManifest.PLAYER_ATTACK_2_PRESSED = true;
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				//GameManifest.PLAYER_ATTACK_2 = false;
				GameManifest.PLAYER_ATTACK_2_PRESSED = false;
				super.touchUp(event, x, y, pointer, button);
			}
		});
		
		Image shootControl = new Image(new Texture(Gdx.files.internal("ui/android-button-action.png")));
		shootControl.setSize(sizeButton + 1.2f, sizeButton + 1.2f);
		shootControl.setPosition(shootProjectile.getX() - (3.8f), shootProjectile.getY() - 1.5f);
		shootControl.addListener(new ClickListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				GameManifest.PLAYER_ATTACK_1 = true;
				return super.touchDown(event, x, y, pointer, button);
			}

			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				GameManifest.PLAYER_ATTACK_1 = false;
				super.touchUp(event, x, y, pointer, button);
			}
		});

        Image iconSword = new Image(new Texture(Gdx.files.internal("ui/icon_sword.png")));
        iconSword.setSize((shootControl.getWidth() / 2) + 0.5f, (shootControl.getHeight() / 2) + 0.5f);
        iconSword.setPosition((shootControl.getX() + shootControl.getWidth() / 2) - iconSword.getWidth() / 2, (shootControl.getY() + shootControl.getHeight() / 2) - iconSword.getHeight() / 2);

        Image iconProjectile = new Image(new Texture(Gdx.files.internal("ui/icon_projectil.png")));
        iconProjectile.setSize((shootProjectile.getWidth() / 2) + 1.0f, (shootProjectile.getHeight() / 2) + 1.0f);
        iconProjectile.setPosition((shootProjectile.getX() + shootProjectile.getWidth() / 2) - iconProjectile.getWidth() / 2, (shootProjectile.getY() + shootProjectile.getHeight() / 2) - iconProjectile.getHeight() / 2);

        Image iconJump = new Image(new Texture(Gdx.files.internal("ui/icon_jump.png")));
        iconJump.setSize(jumpControl.getWidth() / 2, jumpControl.getHeight() / 2);
        iconJump.setPosition((jumpControl.getX() + jumpControl.getWidth() / 2) - iconJump.getWidth() / 2, (jumpControl.getY() + jumpControl.getHeight() / 2) - iconJump.getHeight() / 2);

        stage.addActor(btnPause);
		stage.addActor(iconSword);
		stage.addActor(iconProjectile);
		stage.addActor(iconJump);
		stage.addActor(jumpControl);
		stage.addActor(shootControl);
		stage.addActor(shootProjectile);
		
		if(Gdx.app.getType() == Application.ApplicationType.Android) {
			visible = true;
		}
		else {
			visible = false;
		}
		
		stage.getActors().get(7).setVisible(visible);
		stage.getActors().get(8).setVisible(visible);
		stage.getActors().get(9).setVisible(visible);
		stage.getActors().get(10).setVisible(visible);
		stage.getActors().get(11).setVisible(visible);
		stage.getActors().get(12).setVisible(visible);
	}
	
	public AnimationController getAnimationController() {
		return animationController;
	}

	public void setAnimationController(AnimationController animationController) {
		this.animationController = animationController;
	}

	private int calcularBarraSalud(int saludOriginal, int saludActual, float anchoBarra) {
		return (int) (anchoBarra * saludActual / saludOriginal);
	}
	
	public void updateBar(Player player) {
		barProgress.setWidth(calcularBarraSalud(player.getSaludTotal(), player.getSaludActual(), barBorder.getWidth()));
		
		if(player.isHurt()) {
			stage.getActors().get(0).setVisible(true);
		}
		
		if(GameManifest.isServedTime(timeHurt, 1500)) {
			stage.getActors().get(0).setVisible(false);
			player.setHurt(false);
			timeHurt = System.nanoTime();
		}
	}
	
	public Stage getStage() {
		return stage;
	}
	
	private void visibleUI(boolean visible) {
		stage.getActors().get(1).setVisible(visible);
		stage.getActors().get(2).setVisible(visible);
		stage.getActors().get(3).setVisible(visible);
		stage.getActors().get(4).setVisible(visible);
		
		stage.getActors().get(5).setVisible(visible);
		stage.getActors().get(6).setVisible(visible);
		
		if(Gdx.app.getType() == Application.ApplicationType.Android) {
			stage.getActors().get(2).setVisible(visible);
			stage.getActors().get(7).setVisible(visible);
			stage.getActors().get(8).setVisible(visible);
			stage.getActors().get(9).setVisible(visible);
			stage.getActors().get(10).setVisible(visible);
			stage.getActors().get(11).setVisible(visible);
			stage.getActors().get(12).setVisible(visible);
			stage.getActors().get(13).setVisible(visible);
		}
		
		this.visible = visible;
	}
	
	@Override
	public void update(float delta) {
		elapsedAnim += delta;
		
		GameManifest.PLAYER_MOVE_UP = false;
		GameManifest.PLAYER_MOVE_DOWN = false;
		GameManifest.PLAYER_MOVE_LEFT = false;
		GameManifest.PLAYER_MOVE_RIGHT = false;
		
		if(touchpad.isTouched()) {
			angle = Math.atan2(touchpad.getKnobPercentX(), touchpad.getKnobPercentY());
			degress = Math.toDegrees(angle);

			if(degress < 0) {
				degress += 360;
			}
			getMovePlayer(degress);
		}
		
		if((Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))||(Gdx.input.isKeyJustPressed(Input.Keys.BACK))) {
			GameManifest.PAUSE = !GameManifest.PAUSE;
		}
		
		if(GameManifest.PAUSE) {
			visibleUI(false);
		}
		else {
			label.setText("x"+GameManifest.DEAD_COUNT);
			visibleUI(true);
		}
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		stage.act();
		stage.draw();
		
		getAnimationController().animate(elapsedAnim);
		
		if(visible) {
			batch.draw(getAnimationController().getFrame(elapsedAnim, true), x,  y, 28 / GameManifest.PPM, 28 / GameManifest.PPM);
		}
	}
}