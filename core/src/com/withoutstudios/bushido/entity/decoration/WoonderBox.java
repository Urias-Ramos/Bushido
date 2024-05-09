package com.withoutstudios.bushido.entity.decoration;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.MassData;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.tool.GameManifest;

/**
 * 
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class WoonderBox extends Decoration {
	private Bushido bushido;
	private Sprite sprite;

	public WoonderBox(Bushido bushido, Rectangle rectangle) {
		this.bushido = bushido;
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(rectangle.x / GameManifest.PPM, rectangle.y / GameManifest.PPM));
		bodyDef.fixedRotation = false;
		
		Body body = null;
		
		body = bushido.getMap().getWorld().createBody(bodyDef);
		MassData mass = new MassData();
		mass.mass = 999999;
		
		body.setMassData(mass);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(12 / GameManifest.PPM, 12 / GameManifest.PPM);
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		
		fixtureDef.friction = 1.0f;
		fixtureDef.density = 999f;
		
		fixtureDef.filter.categoryBits = GameManifest.CATEGORY_ITEM;
		fixtureDef.filter.maskBits = GameManifest.MASK_ITEM;
		
		body.createFixture(fixtureDef).setUserData(this);
		
		setBody(body);
		
		shape.dispose();
		
		//body.setAngularVelocity(1.0f);
		//body.setLinearVelocity(0, -10);
		//body.setLinearDamping(0.01f);
		sprite = new  Sprite(bushido.getAssets().woonderBoxTexture);
		sprite.setSize(24 / GameManifest.PPM, 24 / GameManifest.PPM);
		sprite.setOrigin(24 / 2 / GameManifest.PPM, 24 / 2 / GameManifest.PPM);
	}

	@Override
	public void update(float delta) {
		x = getBody().getPosition().x - 24 / 2 / GameManifest.PPM;
		y = getBody().getPosition().y - 24 / 2 / GameManifest.PPM;
		
		sprite.setRotation((getBody().getAngle() * MathUtils.radiansToDegrees));
		sprite.setPosition(x, y);
	}

	@Override
	public void render(SpriteBatch batch, float delta) {
		sprite.draw(batch);
		//batch.draw(sprite, x, y, 16 / GameManifest.PPM, 16 / GameManifest.PPM);
	}
}