package com.withoutstudios.bushido.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.animator.AnimationController;
import com.withoutstudios.bushido.tool.animator.AnimationCreator;
import com.withoutstudios.bushido.tool.animator.AnimationManager;
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
public class Portal extends Entity {
    private Player player;
    private float x, y;
    public PointLight pointLight;

    private AnimationController animationController;

    public Portal(Bushido bushido, float x, float y) {
        super(EntityType.NONE);
        this.x = x;
        this.y = y;

        getHitbox().width = 18 / GameManifest.PPM;
        getHitbox().height = 64 / GameManifest.PPM;

        textureHitbox =  new TextureRegion(bushido.getAssets().hitboxTexture);
        setCardinalPoint(CardinalPoint.NONE);
        createAnimation(bushido);
        
		getHitbox().x = x + (36 / GameManifest.PPM);
        getHitbox().y = y;

        pointLight = new PointLight(bushido.getMap().getMapLoader().getRayHandler(), 10, Color.PURPLE, 75 / GameManifest.PPM, 0, 0);
        pointLight.setXray(true);
    }

    private void createAnimation(Bushido bushido) {
        AnimationCreator[] idle = new AnimationCreator[2];
        idle[0] = new AnimationCreator(bushido.getAssets().getPortalAnimation()[0], CardinalPoint.NONE, null);
        
        AnimationCreator[] on = new AnimationCreator[2];
        on[0] = new AnimationCreator(bushido.getAssets().getPortalAnimation()[1], CardinalPoint.NONE, new int[] {7});

        AnimationManager idleAnimation = new AnimationManager(idle);
        AnimationManager onAnimation = new AnimationManager(on);

        AnimationManager[] animationManager = new AnimationManager[2];
        animationManager[0] = idleAnimation;
        animationManager[1] = onAnimation;

        setAnimationController(new AnimationController(animationManager));
        getAnimationController().setCardinalPoint(getCardinalPoint());
        getAnimationController().setIndexAnimation(0);
    }

    public AnimationController getAnimationController() {
        return animationController;
    }

    public void setAnimationController(AnimationController animationController) {
        this.animationController = animationController;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public void update(float delta) {
        elapsedAnim += delta;
        
        getHitbox().x = x + (36 / GameManifest.PPM);
        getHitbox().y = y;

        getAnimationController().setCardinalPoint(getCardinalPoint());
        getAnimationController().animate(elapsedAnim);
        
        if(getHitbox().overlaps(getPlayer().getHitbox())) {
        	getAnimationController().setIndexAnimation(1);
        	GameManifest.PLAYER.setVisible(false);
        }
        
        if(getAnimationController().getIndexAnimation() == 1) {
        	if(getAnimationController().getIndexAction() == 7) {
        		getAnimationController().resetIndexAction();
        		GameManifest.PAUSE = true;
            	GameManifest.SCOORE_SCREEN = true;
        	}
        }

        pointLight.setPosition(getHitbox().x + 0.25f, getHitbox().y + 1.35f);
    }

    @Override
    public void render(SpriteBatch batch, float delta) {
        batch.draw(getAnimationController().getFrame(elapsedAnim, true), x,  y, 90 / GameManifest.PPM, 90 / GameManifest.PPM);

        if(GameManifest.DEBUG) {
            batch.draw(textureHitbox, getHitbox().x,  getHitbox().y, getHitbox().width, getHitbox().height);
        }
    }
}