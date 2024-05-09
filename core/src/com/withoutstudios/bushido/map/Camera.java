package com.withoutstudios.bushido.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.UpdateAndRender;

/**
 * Esta clase administra la camara, permite crear efectos y hace que la camara no sobrepase los limites del mapa
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class Camera implements UpdateAndRender {
	private Bushido bushido;
	private OrthographicCamera camera;
	
	private float x, y, x1, y1, n;
	private float marginX, marginY;
	
	public boolean activate;
	private boolean effect;
	private long timeffect;

	public Camera(Bushido bushido) {
		this.bushido = bushido;
		camera = new OrthographicCamera();
		activate = false;
	}
	
	private boolean isStaticCamera() {
		if((bushido.getMap().getMapLoader().getMapWidth() <= GameManifest.SCREEN_WIDTH)&&(bushido.getMap().getMapLoader().getMapHeight() <= GameManifest.SCREEN_HEIGHT)) {
			return true;
		}
		return false;
	}
	
	private boolean isCameraStaticWidth() {
		if(bushido.getMap().getMapLoader().getMapWidth() <= bushido.getMap().getViewport().getWorldWidth()) {
			return true;
		}
		return false;
	}
	
	private boolean isCameraStaticHeight() {
		if(bushido.getMap().getMapLoader().getMapHeight() <= bushido.getMap().getViewport().getWorldHeight()) {
			return true;
		}
		return false;
	}
	
	private float getStopCameraLeft(float x) {
		marginX = 0 + bushido.getMap().getViewport().getWorldWidth() / 2;
		if(x < marginX) {
			return marginX;
		}
		return x;
	}
	
	private float getStopCameraRight(float x) {
		marginX = (bushido.getMap().getMapLoader().getMapWidth() / 32) - bushido.getMap().getViewport().getWorldWidth() / 2;
		if(x > marginX) {
			return marginX;
		}
		return x;
	}
	
	private float getStopCameraUp(float y) {
		marginY = (bushido.getMap().getMapLoader().getMapHeight() / 32) - bushido.getMap().getViewport().getWorldHeight() / 2;
		if(y > marginY) {
			return marginY;
		}
		return y;
	}
	
	private float getStopCameraDown(float y) {
		marginY = 0 + bushido.getMap().getViewport().getWorldHeight() / 2;
		if(y < marginY) {
			return marginY;
		}
		return y;
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}
	
	public void updateBushido(Bushido bushido) {
		this.bushido = bushido;
	}

	@Override
	public void update(float delta) {
		if(!isStaticCamera()) {
			if(!isCameraStaticWidth()) {
				x = GameManifest.PLAYER.getBody().getPosition().x;
			}
			
			if(!isCameraStaticHeight()) {
				y = GameManifest.PLAYER.getBody().getPosition().y;
			}
			
			x = getStopCameraLeft(x);
			x = getStopCameraRight(x);
			
			y = getStopCameraUp(y);
			y = getStopCameraDown(y);
		}
		
		camera.position.set(x + x1, y + y1, 0);
		
		if(Gdx.input.isKeyJustPressed(Keys.T)||activate) {
			effect = true;
			activate = false;
			timeffect = System.nanoTime();
		}
		
		if(effect) {
			if(!GameManifest.isServedTime(timeffect, 400)) {
				n = 0.0f;
				n = GameManifest.getRandomNumber(1, 3) * 0.05f;
				
				if(GameManifest.getRandomNumber(1, 11) > 7) {
					n *= -1;
				}
				
				y1 = n;
				
				n = GameManifest.getRandomNumber(1, 3) * 0.05f;
				
				if(GameManifest.getRandomNumber(1, 11) > 7) {
					n *= -1;
				}
				
				x1 = n;
			}
			else{
				x1 = 0;
				y1 = 0;
				effect = false;
			}
		}
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		
	}
}