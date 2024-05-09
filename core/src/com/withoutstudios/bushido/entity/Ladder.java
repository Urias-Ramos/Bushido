package com.withoutstudios.bushido.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.enumerators.EntityType;

/**
 * 
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class Ladder extends Entity {
	private Bushido bushido;
	private float x, y;
	
	private int ladderCount;

	public Ladder(Bushido bushido, World world, float x, float y, float height) {
		super(EntityType.NONE);
		this.bushido = bushido;
		this.x = x;
		this.y = y;
		
		this.ladderCount = (int) height / 32;
		
		textureHitbox =  new TextureRegion(bushido.getAssets().hitboxTexture);
		
		setBody(createBody(world, x, y, 1, height));
	}
	
	private Body createBody(World world, float postX, float postY, float width, float height) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.KinematicBody;
		bodyDef.position.set(new Vector2(postX, postY + (height / 2) / GameManifest.PPM));
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 0.0f;
		Body body = null;
		
		body = world.createBody(bodyDef);
		body.setFixedRotation(true);
		
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2 / GameManifest.PPM, height / 2 / GameManifest.PPM);
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;
		
		getHitbox().width = (width / 2 / GameManifest.PPM) * 2;
		getHitbox().height = (height / 2 / GameManifest.PPM) * 2;
		
		fixtureDef.friction = 1.1f;
		fixtureDef.density = 1.0f;
		fixtureDef.filter.categoryBits = GameManifest.CATEGORY_LADDER;
		fixtureDef.filter.maskBits = GameManifest.MASK_LADDER;
		body.createFixture(fixtureDef).setUserData(this);
        
        shape.dispose();
        
        return body;
	}

	@Override
	public void update(float delta) {
		elapsedAnim += delta;
		
		getHitbox().x = x;
		getHitbox().y = y;
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		for(int i=0; i<ladderCount; i++) {
			batch.draw(bushido.getAssets().ladderTexture, x - (16 / GameManifest.PPM),  y + (i * 32 / GameManifest.PPM), 34 / GameManifest.PPM, 32 / GameManifest.PPM);
		}
	}
}