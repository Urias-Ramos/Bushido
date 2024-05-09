package com.withoutstudios.bushido.entity;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.withoutstudios.bushido.tool.UpdateAndRender;
import com.withoutstudios.bushido.tool.enumerators.CardinalPoint;
import com.withoutstudios.bushido.tool.enumerators.EntityType;

/**
 * 
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public abstract class Entity implements UpdateAndRender {
	private EntityType entityType;
	private Body body;
	private CardinalPoint cardinalPoint;
	
	private Rectangle hitbox;
	protected TextureRegion textureHitbox;
	protected float elapsedAnim;
	
	private boolean hibernate;
	private boolean dead;

	public Entity(EntityType entityType) {
		setEntityType(entityType);
		setCardinalPoint(CardinalPoint.NONE);
		
		hitbox = new Rectangle(0, 0, 16, 16);
		
		setHibernate(false);
		setDead(false);
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public void setEntityType(EntityType entityType) {
		this.entityType = entityType;
	}

	public Body getBody() {
		return body;
	}

	public void setBody(Body body) {
		this.body = body;
	}

	public CardinalPoint getCardinalPoint() {
		return cardinalPoint;
	}

	public void setCardinalPoint(CardinalPoint cardinalPoint) {
		this.cardinalPoint = cardinalPoint;
	}

	public Rectangle getHitbox() {
		return hitbox;
	}

	public void setHitbox(Rectangle hitbox) {
		this.hitbox = hitbox;
	}

	public boolean isHibernate() {
		return hibernate;
	}

	public void setHibernate(boolean hibernate) {
		this.hibernate = hibernate;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}
}