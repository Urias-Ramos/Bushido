package com.withoutstudios.bushido.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.withoutstudios.bushido.Bushido;
import com.withoutstudios.bushido.entity.CheckPoint;
import com.withoutstudios.bushido.entity.Player;
import com.withoutstudios.bushido.entity.Portal;
import com.withoutstudios.bushido.entity.trap.LightTrap2;
import com.withoutstudios.bushido.entity.trap.WormTrap;
import com.withoutstudios.bushido.tool.GameManifest;
import com.withoutstudios.bushido.tool.enumerators.StageScene;

/**
 * Esta clase es la encargada de administrar el juego, camara, mapa, UI etc.
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class Map extends ScreenAdapter {
	private Bushido bushido;
	
	private TiledMap tiledMap;
	private World world;
	private Box2DDebugRenderer debugRenderer;
	
	private Viewport viewport;
	public Camera camera;
	private OrthogonalTiledMapRenderer tiledMapRenderer;
	
	private MapLoader mapLoader;
	
	private SpriteBatch batch;
	
	private PlayerHUD playerHUD;
	public static MapRenderer mapRenderer;
	
	private boolean loadedMap;
	
	private float positionX, positionY;
	
	public int randomMusic = 0;

	public Map(Bushido bushido) {
		this.bushido = bushido;
		this.batch = bushido.getSpriteBatch();
		
		tiledMap = bushido.getAssets().tiledMap;
		world = new World(new Vector2(0, -9.81f), true);
		world.setContactListener(new CollisionListener());
		
		camera = new Camera(bushido);
		
		mapLoader = new MapLoader(tiledMap, world, bushido);
		
		viewport = new ExtendViewport(GameManifest.SCREEN_WIDTH / 48, GameManifest.SCREEN_HEIGHT / 48, camera.getCamera());
		tiledMapRenderer = new OrthogonalTiledMapRenderer(null, 1 / GameManifest.PPM);
		debugRenderer = new Box2DDebugRenderer();
		
		GameManifest.PLAYER = new Player(world, bushido, GameManifest.PLAYER_RESPAWN, getMapLoader().getRayHandler());
		
		playerHUD = new PlayerHUD(bushido);
		mapRenderer = new MapRenderer();
		
		loadedMap = true;
		
		bushido.getAssets().particle.scaleEffect(0.08f);
		bushido.getAssets().particle.setPosition(0, 0);
		bushido.getAssets().particle.start();
	}
	
	public MapLoader getMapLoader() {
		return mapLoader;
	}
	
	@Override
	public void show() {
		if(GameManifest.MUSIC) {
			bushido.getAssets().ambient.play();
		}
		
		Gdx.input.setInputProcessor(playerHUD.getStage());
	}
	
	private void updateMap(float delta) {
		camera.updateBushido(bushido);
		camera.update(delta);
		
		GameManifest.PLAYER.update(delta);
		if(getMapLoader().getEnemyList().size() > 0) {
        	for(int i=getMapLoader().getEnemyList().size() - 1; i>=0; i--) {
        		if(getMapLoader().getEnemyList().get(i).isDead()) {
        			world.destroyBody(getMapLoader().getEnemyList().get(i).getBody());
        			getMapLoader().getEnemyList().remove(i);
        			GameManifest.SCOORE = (int) ((GameManifest.ENEMY_COUNT - getMapLoader().getEnemyList().size()) * GameManifest.SCOORE_ENEMY);
        		}
        	}
        }
		
		if(getMapLoader().getEnemyList().size() > 0) {
			positionX = 0;
			positionY = 0;
			
        	for(int i=0; i<getMapLoader().getEnemyList().size(); i++) {
        		
        		if(getMapLoader().getEnemyList().get(i).isHibernate()) {
        			positionX = getMapLoader().getEnemyList().get(i).getBody().getPosition().x;
        			positionY = getMapLoader().getEnemyList().get(i).getBody().getPosition().y;
        			
        			if(!isInsideScreen(positionX, positionY)) {        		
        				continue;
        			}
        		}
        		
        		getMapLoader().getEnemyList().get(i).setPlayer(GameManifest.PLAYER);
        		
        		if(!getMapLoader().getEnemyList().get(i).isDead()) {
        			getMapLoader().getEnemyList().get(i).update(delta);
        		}
        	}
        }
		
		if(getMapLoader().getProjectilList().size() > 0) {
        	for(int i=getMapLoader().getProjectilList().size() - 1; i>=0; i--) {
        		if(getMapLoader().getProjectilList().get(i).isDead()) {
        			world.destroyBody(getMapLoader().getProjectilList().get(i).getBody());
        			getMapLoader().getProjectilList().remove(i);
        		}
        	}
        }
		
		if(getMapLoader().getProjectilList().size() > 0) {
        	for(int i=0; i<getMapLoader().getProjectilList().size(); i++) {
        		if(!getMapLoader().getProjectilList().get(i).isDead()) {
        			getMapLoader().getProjectilList().get(i).update(delta);
        		}
        	}
        }
		
		if(getMapLoader().getEntityList().size() > 0) {
        	for(int i=getMapLoader().getEntityList().size() - 1; i>=0; i--) {
        		if(getMapLoader().getEntityList().get(i).isDead() && getMapLoader().getEntityList().get(i).getBody() != null) {
        			world.destroyBody(getMapLoader().getEntityList().get(i).getBody());
        			getMapLoader().getEntityList().remove(i);
        		}
        	}
        }

		if(getMapLoader().getEntityList().size() > 0) {
			for(int i=0; i<getMapLoader().getEntityList().size(); i++) {
				if(!getMapLoader().getEntityList().get(i).isDead()) {
					if(getMapLoader().getEntityList().get(i) instanceof Portal)
						((Portal)getMapLoader().getEntityList().get(i)).setPlayer(GameManifest.PLAYER);
					else if(getMapLoader().getEntityList().get(i) instanceof CheckPoint)
						((CheckPoint)getMapLoader().getEntityList().get(i)).setPlayer(GameManifest.PLAYER);
					
					getMapLoader().getEntityList().get(i).update(delta);
				}
			}
		}
		
		mapRenderer.update(delta);
		
		if(getMapLoader().getDecorationList().size() > 0) {
			
			for(int i=0; i<getMapLoader().getDecorationList().size(); i++) {
				if(!getMapLoader().getDecorationList().get(i).isDead()) {
					getMapLoader().getDecorationList().get(i).update(delta);
				}
			}
		}
		
		playerHUD.updateBar(GameManifest.PLAYER);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(!GameManifest.PAUSE) {
			world.step(1 / 60f, 8, 3);
			
			if((GameManifest.MUSIC)&&(!bushido.getAssets().ambient.isPlaying())) {
				bushido.getAssets().ambient.play();
			}
			
			Gdx.input.setInputProcessor(playerHUD.getStage());
			updateMap(delta);
		}
		else {
			bushido.getAssets().ambient.pause();
			bushido.getPauseScreen().update(delta);
		}
		
		playerHUD.update(delta);
        
        batch.setProjectionMatrix(camera.getCamera().combined);
        
        tiledMapRenderer.setView(camera.getCamera());
        tiledMapRenderer.render();
        
        batch.begin();
        if(getMapLoader().getEntityList().size() > 0) {
			for(int i=0; i<getMapLoader().getEntityList().size(); i++) {
				if(!getMapLoader().getEntityList().get(i).isDead()) {
					getMapLoader().getEntityList().get(i).render(batch, delta);
				}
			}
		}
        batch.end();
        
        batch.begin();
        GameManifest.PLAYER.render(batch, delta);
        batch.end();
        
        batch.begin();
        if(getMapLoader().getEnemyList().size() > 0) {
        	for(int i=0; i<getMapLoader().getEnemyList().size(); i++) {
        		if(!getMapLoader().getEnemyList().get(i).isDead()) {
        			
        			if(getMapLoader().getEnemyList().get(i).isHibernate()) {
            			positionX = getMapLoader().getEnemyList().get(i).getBody().getPosition().x;
            			positionY = getMapLoader().getEnemyList().get(i).getBody().getPosition().y;
            			
            			if(!isInsideScreen(positionX, positionY)) {        		
            				continue;
            			}
            		}
        			
        			getMapLoader().getEnemyList().get(i).render(batch, delta);
        		}
        	}
        }
        
        if(getMapLoader().getProjectilList().size() > 0) {
        	for(int i=0; i<getMapLoader().getProjectilList().size(); i++) {
        		if(!getMapLoader().getProjectilList().get(i).isDead()) {
        			getMapLoader().getProjectilList().get(i).setTextureHitbox(bushido.getAssets().hitboxTexture);
        			getMapLoader().getProjectilList().get(i).render(batch, delta);
        		}
        	}
        }
        
        if(getMapLoader().getTrapList().size() > 0) {
        	for(int i=0; i<getMapLoader().getTrapList().size(); i++) {
        		if(getMapLoader().getTrapList().get(i).isHibernate()) {
        			
        			if(getMapLoader().getTrapList().get(i) instanceof WormTrap) {
        				positionX = ((WormTrap) getMapLoader().getTrapList().get(i)).getX();
            			positionY = ((WormTrap) getMapLoader().getTrapList().get(i)).getY();
            			
            			if(!isInsideScreen(positionX, positionY)) {        		
            				continue;
            			}
        			}
        			else if(getMapLoader().getTrapList().get(i) instanceof LightTrap2) {
        				positionX = ((LightTrap2) getMapLoader().getTrapList().get(i)).getX();
            			positionY = ((LightTrap2) getMapLoader().getTrapList().get(i)).getY();
            			
            			if(!isInsideScreen(positionX, positionY)) {        		
            				continue;
            			}
        			}
        		}
        		
        		getMapLoader().getTrapList().get(i).setPlayer(GameManifest.PLAYER);
        		getMapLoader().getTrapList().get(i).update(delta);
        		getMapLoader().getTrapList().get(i).render(batch, delta);
        	}
        }
        
        if(getMapLoader().getDecorationList().size() > 0) {
			for(int i=0; i<getMapLoader().getDecorationList().size(); i++) {
				if(!getMapLoader().getDecorationList().get(i).isDead()) {
					getMapLoader().getDecorationList().get(i).render(batch, delta);
				}
			}
		}
        
        batch.draw(bushido.getAssets().parallaxFrontal, 0 / GameManifest.PPM, 0 / GameManifest.PPM, 4800 / GameManifest.PPM, 320 / GameManifest.PPM);
        
        batch.end();
        
        batch.begin();
        mapRenderer.render(batch, delta);
        batch.end();
        
        if(GameManifest.DEBUG) {
        	debugRenderer.render(world, camera.getCamera().combined);
        }
        
        if((GameManifest.CHANGE_MAP)||(GameManifest.PLAYER.getBody().getPosition().y < getMapLoader().getAbyssMap())||(GameManifest.PLAYER.isDead())) {
        	bushido.changeStage(StageScene.WAITING);
		}
        
        if(GameManifest.NIGHTMODE) {
			getMapLoader().getRayHandler().setCombinedMatrix(camera.getCamera());
			getMapLoader().getRayHandler().updateAndRender();
		}

		if(GameManifest.PAUSE) {
			bushido.getPauseScreen().render(batch, delta);
		}

        batch.begin();
        
        playerHUD.render(batch, delta);
        
        if(!GameManifest.PAUSE)
        	bushido.getAssets().particle.draw(batch, delta);
        
        batch.end();
        
        camera.getCamera().update();
	}
	
	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		playerHUD.getStage().getViewport().update(width, height, true);
		camera.getCamera().update();
	}
	
	public void loadMap() {
		GameManifest.DEAD_COUNT++;
		GameManifest.RESET_MAP = true;
		setLoadedMap(false);
		
		mapRenderer.getHitList().clear();
		getMapLoader().getProjectilList().clear();
		
		GameManifest.PLAYER.setSaludActual(GameManifest.PLAYER.getSaludTotal());
		GameManifest.PLAYER.getBody().setTransform(GameManifest.PLAYER_RESPAWN, 0);
		GameManifest.PLAYER.setDead(false);
		GameManifest.PLAYER.getAnimationController().setIndexAnimation(0);
		
		GameManifest.PLAYER_MOVE_RIGHT = false;
		GameManifest.PLAYER_MOVE_LEFT = false;
		GameManifest.PLAYER_MOVE_UP = false;
		GameManifest.PLAYER_MOVE_DOWN = false;
		
		GameManifest.PLAYER_JUMP = false;
		GameManifest.PLAYER_ROLLING = false;
		
		GameManifest.PLAYER_ATTACK_1 = false;
		GameManifest.PLAYER_ATTACK_2 = false;
		GameManifest.PLAYER.setLadder(false);
		GameManifest.PLAYER.setJump(true);
		
		setLoadedMap(true);
		GameManifest.RESET_MAP = false;
	}
	
	private boolean isInsideScreen(float x, float y) {
		if((x > camera.getCamera().position.x + (viewport.getWorldWidth() / 2))||
				(x < camera.getCamera().position.x - (viewport.getWorldWidth() / 2))||
				(y > camera.getCamera().position.y + (viewport.getWorldHeight() / 2))||
				(y < camera.getCamera().position.y - (viewport.getWorldHeight() / 2))) {        		
			return false;
		}
		return true;
	}
	
	public Viewport getViewport() {
		return viewport;
	}

	public boolean isLoadedMap() {
		return loadedMap;
	}

	public void setLoadedMap(boolean loadedMap) {
		this.loadedMap = loadedMap;
	}

	public MapRenderer getMapRenderer() {
		return mapRenderer;
	}

	public World getWorld() {
		return world;
	}

	public TiledMap getTiledMap() {
		return tiledMap;
	}

	public void setTiledMap(TiledMap tiledMap) {
		this.tiledMap = tiledMap;
	}

	public OrthogonalTiledMapRenderer getTiledMapRenderer() {
		return tiledMapRenderer;
	}

	public void setTiledMapRenderer(OrthogonalTiledMapRenderer tiledMapRenderer) {
		this.tiledMapRenderer = tiledMapRenderer;
	}

	public void setWorld(World world) {
		this.world = world;
	}
}