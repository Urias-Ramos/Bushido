package com.withoutstudios.bushido.entity.trap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.tool.GameManifest;

/**
 * 
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class SpikeTrap extends Trap {
	private Bushido bushido;
	private float x, y;
	
	private int spikeCount;
	
	public SpikeTrap(Bushido bushido, Rectangle rectangle) {
		this.bushido = bushido;
		this.x = rectangle.x / GameManifest.PPM;
		this.y = rectangle.y / GameManifest.PPM;
		
		textureHitbox =  new TextureRegion(bushido.getAssets().hitboxTexture);
		
		getHitbox().width = rectangle.width / GameManifest.PPM;
		getHitbox().height = 4 / GameManifest.PPM;
		
		getHitbox().x = x;
		getHitbox().y = y;
		
		spikeCount = (int) rectangle.width / 16;
	}

	@Override
	public void update(float delta) {
		elapsedAnim += delta;
		
		if(getHitbox().overlaps(getPlayer().getHitbox())) {
			getPlayer().recibirGolpe(100);
		}
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		for(int i=0; i<spikeCount; i++) {
			batch.draw(bushido.getAssets().spikeDown, x + (i * 16 / GameManifest.PPM),  y, 16 / GameManifest.PPM, 16 / GameManifest.PPM);
		}
		
		if(GameManifest.DEBUG) {
			batch.draw(textureHitbox, getHitbox().x,  getHitbox().y, getHitbox().width, getHitbox().height);
		}
	}
}