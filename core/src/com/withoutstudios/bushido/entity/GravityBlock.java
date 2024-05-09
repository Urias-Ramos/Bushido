package com.withoutstudios.bushido.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
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
public class GravityBlock extends Entity {
	private float x, y;
	private Bushido bushido;
	private int damage;

	public GravityBlock(Bushido bushido, World world, Rectangle rectangle) {
		super(EntityType.NONE);
		this.bushido = bushido;
		this.x = rectangle.x / GameManifest.PPM;
		this.y = rectangle.y / GameManifest.PPM;
		
		setDamage(100);
		
		textureHitbox =  new TextureRegion(bushido.getAssets().hitboxTexture);
		
		setBody(createBody(world, x, y, rectangle.width / GameManifest.PPM, rectangle.height / GameManifest.PPM));
	}
	
	private Body createBody(World world, float postX, float postY, float width, float height) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(postX + width / 2, postY + height / 2));
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 0.0f;
		Body body = null;
		
		body = world.createBody(bodyDef);
		body.setFixedRotation(true);
		body.setGravityScale(0);
		
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2, height / 2);
		fixtureDef.shape = shape;
		fixtureDef.isSensor = true;
		
		getHitbox().width = (width / 2 / GameManifest.PPM) * 2;
		getHitbox().height = (height / 2 / GameManifest.PPM) * 2;
		
		fixtureDef.friction = 1500.0f;
		fixtureDef.density = 1500f;
		fixtureDef.restitution = 0.05f;
		fixtureDef.filter.categoryBits = GameManifest.CATEGORY_PROJECTIL;
		fixtureDef.filter.maskBits = GameManifest.MASK_ENEMY_PROJECTILE;
		body.createFixture(fixtureDef).setUserData(this);
		
		//sensor
      	shape.setAsBox(width / 2, (height * 2), new Vector2(0, 0 - (height * 2)), 0);
        fixtureDef.density = 0;
        fixtureDef.filter.categoryBits = GameManifest.CATEGORY_SENSOR_VISION;
        fixtureDef.filter.maskBits = GameManifest.MASK_ENEMY_PROJECTILE;
        fixtureDef.isSensor = true;
        body.createFixture(fixtureDef).setUserData(this);
        //sensor
        
        shape.dispose();
        
        return body;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	@Override
	public void update(float delta) {
		elapsedAnim += delta;
		
		getHitbox().x = x;
		getHitbox().y = y;
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		
		batch.draw(bushido.getAssets().gravityBlock, getBody().getPosition().x - 32 / GameManifest.PPM, getBody().getPosition().y - 16 / GameManifest.PPM, 64 / GameManifest.PPM, 32 / GameManifest.PPM);
		if(GameManifest.DEBUG) {
			batch.draw(textureHitbox, getHitbox().x, getHitbox().y, getHitbox().width, getHitbox().height);
		}
	}
}