package com.withoutstudios.bushido.map.trigger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.UpdateAndRender;

/**
 * Esta clase representa un disparador de texto, muestra un texto en pantalla que se movera de forma aleatoria y luego se desvanece
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class DisplayText implements UpdateAndRender {
	private String text;
	private float x, y;
	
	private boolean animated;
	private float speedX, speedY;
	private long time;
	
	private int random;
	private BitmapFont font;
	
	private long timeDead;
	private boolean add, dead;
	
	public DisplayText(String text, float x, float y) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.animated = false;
		
		FreeTypeFontGenerator loader = new FreeTypeFontGenerator(Gdx.files.internal("font/thaleahFat.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.color = Color.WHITE;
		
		font = loader.generateFont(parameter);
		dead = false;
		add = false;
		timeDead = System.nanoTime();
	}
	
	public DisplayText(String text, float x, float y, float speedX, float speedY) {
		this.text = text;
		this.x = x;
		this.y = y;
		this.animated = true;
		this.speedX = speedX;
		this.speedY = speedY;
		
		FreeTypeFontGenerator loader = new FreeTypeFontGenerator(Gdx.files.internal("font/thaleahFat.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 32;
		parameter.color = Color.RED;
		
		font = loader.generateFont(parameter);
		font.getData().setScale(2 / 64f / 2);
		font.setUseIntegerPositions(false);
		add = false;
		dead = false;
		timeDead = System.nanoTime();
	}
	
	public void start() {
		setDead(false);
		setAdd(true);
		timeDead = System.nanoTime();
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public boolean isAnimated() {
		return animated;
	}

	public void setAnimated(boolean animated) {
		this.animated = animated;
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}
	
	public boolean isAdd() {
		return add;
	}

	public void setAdd(boolean add) {
		this.add = add;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	@Override
	public void update(float delta) {
		if(!GameManifest.isServedTime(timeDead, 4500)) {
			if(isAnimated()) {
				if(GameManifest.isServedTime(time, 8)) {
					random = GameManifest.getRandomNumber(1, 13);
					y += 0.01f;
					
					if(random > 6) {
						x += -0.01;
					}
					else {
						x += 0.01;
					}
					
					time = System.nanoTime();
				}
			}
		}
		else {
			setDead(true);
		}
	}
	
	@Override
	public void render(SpriteBatch batch, float delta) {
		if(!isDead())
			font.draw(batch, getText(), x, y);
	}
}