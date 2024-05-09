package com.withoutstudios.bushido.entity.projectile;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.UpdateAndRender;
import com.withoutstudios.bushido.tool.enumerators.CardinalPoint;
import com.withoutstudios.bushido.tool.enumerators.EntityType;

import box2dLight.PointLight;

/**
 * 
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public abstract class Projectile implements UpdateAndRender {
	private Texture textureHitbox;
	private Body body;
	private Rectangle hitbox;
	protected float x, y;
	private CardinalPoint cardinalPoint;
	
	private float speed;
	
	private int minimumAttack, maximumAttack;
	private boolean randomAttack;
	
	private boolean operative, dead;
	
	public long timeDead;

	public PointLight pointLight;
	
	public Projectile(float speed, int maximumAttack, CardinalPoint cardinalPoint) {
		this.speed = speed;
		this.minimumAttack = maximumAttack;
		this.maximumAttack = maximumAttack;
		this.randomAttack = false;
		this.cardinalPoint = cardinalPoint;
		
		this.operative = true;
		this.dead = false;

		pointLight = null;
	}
	
	public Projectile(float speed, int minimumAttack, int maximumAttack, boolean randomAttack, CardinalPoint cardinalPoint) {
		this.speed = speed;
		this.minimumAttack = minimumAttack;
		this.maximumAttack = maximumAttack;
		this.randomAttack = randomAttack;
		this.cardinalPoint = cardinalPoint;
		
		this.operative = true;
		this.dead = false;

		pointLight = null;
	}
	public CardinalPoint getCardinalPoint() {
		return cardinalPoint;
	}
	public void setCardinalPoint(CardinalPoint cardinalPoint) {
		this.cardinalPoint = cardinalPoint;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public int getMinimumAttack() {
		return minimumAttack;
	}

	public void setMinimumAttack(int minimumAttack) {
		this.minimumAttack = minimumAttack;
	}

	public int getMaximumAttack() {
		return maximumAttack;
	}

	public void setMaximumAttack(int maximumAttack) {
		this.maximumAttack = maximumAttack;
	}

	public boolean isRandomAttack() {
		return randomAttack;
	}

	public void setRandomAttack(boolean randomAttack) {
		this.randomAttack = randomAttack;
	}

	public boolean isOperative() {
		return operative;
	}

	public void setOperative(boolean operative) {
		this.operative = operative;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}
	
	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}
	
	public Rectangle getHitbox() {
		return hitbox;
	}

	public void setHitbox(Rectangle hitbox) {
		this.hitbox = hitbox;
	}
	
	public Texture getTextureHitbox() {
		return textureHitbox;
	}

	public void setTextureHitbox(Texture textureHitbox) {
		this.textureHitbox = textureHitbox;
	}

	public int getAttack() {
		if(isRandomAttack()) {
			return GameManifest.getRandomNumber(getMinimumAttack(), getMaximumAttack());
		}
		
		return getMaximumAttack();
	}
	
	public void launch() {
		timeDead = System.nanoTime();
		if(getCardinalPoint() == CardinalPoint.EAST) {
			getBody().setLinearVelocity(getSpeed(), 0);
		}
		else if(getCardinalPoint() == CardinalPoint.WEST) {
			getBody().setLinearVelocity(-getSpeed(), 0);
		}
	}

	public void createBody(EntityType entityType, World world, float x, float y, float width, float height) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyDef.BodyType.DynamicBody;
		bodyDef.position.set(new Vector2(x, y));
		bodyDef.fixedRotation = true;
		bodyDef.linearDamping = 0.0f;
		
		body = world.createBody(bodyDef);
		body.setFixedRotation(true);
		
		FixtureDef fixtureDef = new FixtureDef();
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(width / 2 / GameManifest.PPM, height / 2 / GameManifest.PPM);
		fixtureDef.shape = shape;
		
		fixtureDef.friction = 1.1f;
		fixtureDef.density = 1.0f;
		fixtureDef.filter.categoryBits = GameManifest.CATEGORY_PROJECTIL;
		
		if(entityType == EntityType.PLAYER) {
			fixtureDef.filter.maskBits = GameManifest.MASK_PLAYER_PROJECTILE;
		}
		else if(entityType == EntityType.ENEMY) {
			fixtureDef.filter.maskBits = GameManifest.MASK_ENEMY_PROJECTILE;
		}
		
		hitbox = new Rectangle(0, 0, width / GameManifest.PPM, height / GameManifest.PPM);
		
		body.createFixture(fixtureDef).setUserData(this);
        
        shape.dispose();
	}
}