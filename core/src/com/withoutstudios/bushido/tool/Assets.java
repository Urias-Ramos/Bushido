package com.withoutstudios.bushido.tool;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;

/**
 * Esta clase se encarga de administrar los recursos del juego, texturas, sonidos, fuente etc...
 * 
 * @author Urias Ramos
 * @since 2023-06-10
 * @version 1.0
 */
public class Assets {
	private AssetManager assetManager;
	
	public Music ambient;
	public Texture parallaxFrontal;
	
	public Texture titleGame;
	public Texture btnClose, settingBG, settingTable;
	public Texture checkOn, checkOff;
	public Texture terrain;
	
	public Texture levelBg, levelTable, levelSlot, levelLock;
	
	public Texture layer1, layer2, layer3;
	
	public Texture progressBar, progressBorder, iconHealth, iconCircleRed;
	
	public Texture projectile1, projectileEffect1, projectile2;
	
	public Texture screenHurt;
	
	public TiledMap tiledMap;
	
	public Texture ligthTrap2Texture;
	public Texture cjDeadTexture;
	
	public Texture skullTexture;
	
	//sound
	public Sound soundPlayerWalk, soundPlayerJump, soundSword;
	public Sound soundPlayerProjectile;
	public Sound skeletonDead, skeletonHit, bringerDead;
	public Sound mushroomZombieDead;
	public Sound wormTrapSound;
	public Sound lightTrap2Sound;
	public Sound mutatedBatDead;
	public Sound vaseSound;
	public Sound ladderSound, levelLockedSound;
	//sound
	
	public Texture hitboxTexture;
	//slime
	public Texture bringerDeathTexture;
	public Texture skeletonTexture, slimeTexture, mutatedBatTexture;
	public Sound slimeDead;
	//slime

	public Texture playerTexture, portalTexture;
	
	public Texture mushroomZombieTexture;
	public Texture fireWormTexture;
	
	public Texture wormTrap;
	public Texture SpikeUp, spikeDown, spikeLeft, spikeRight;
	
	public Texture ladderTexture;
	public Texture gravityBlock;
	public Texture checkPointTexture;
	
	public Texture star0, star1, star2, star3;
	
	public Texture trainingDummyTexture, vaseTexture, woonderBoxTexture;
	
	public BitmapFont fontMenu, fontLevelSlot;
	
	//monster
	private Texture flyingEyeTexture;
	//monster
	
	private Animation<TextureRegion>[] playerAnimation, skullAnimation;
	
	private Animation<TextureRegion>[] skeletonAnimation, fireWormAnimation, mushroomZombieAnimation;
	private Animation<TextureRegion>[] mutatedBatAnimation, slimeAnimation, projectile1Animation, projectile2Animation;
	private Animation<TextureRegion>[] projectile1EffectAnimation, projectile2EffectAnimation;
	
	private Animation<TextureRegion>[] lightTrap2Animation, wormTrapAnimation, checkPointAnimation;
	private Animation<TextureRegion>[] portalAnimation, SimpleDecorationAnimation, bringerDeathAnimation;
	private Animation<TextureRegion>[] bringerInvocationAnimation, flyingEyeAnimation;
	
	private Animation<TextureRegion>[] trainingDummyAnimation, vaseAnimation;
	
	public Texture particle1Texture;
	public ParticleEffect particle;
	
	public Assets() {
		assetManager = new AssetManager();
	}
	
	public void loadAsset() {
		assetManager.load("map/parallax1.png", Texture.class);
		
		assetManager.load("texture/player_1.png", Texture.class);
		
		assetManager.load("map/particle1.png", Texture.class);
		assetManager.load("map/particle1.pe", ParticleEffect.class);
		
		assetManager.load("ui/game-title.png", Texture.class);
		assetManager.load("ui/terreno.png", Texture.class);
		
		assetManager.load("map/background_layer_1.png", Texture.class);
		assetManager.load("map/background_layer_2.png", Texture.class);
		assetManager.load("map/background_layer_3.png", Texture.class);
		
		assetManager.load("ui/close.png", Texture.class);
		assetManager.load("ui/setting_bg.png", Texture.class);
		assetManager.load("ui/setting_table.png", Texture.class);
		
		assetManager.load("ui/level_bg.png", Texture.class);
		assetManager.load("ui/level_table.png", Texture.class);
		assetManager.load("ui/level_slot.png", Texture.class);
		assetManager.load("ui/level_lock.png", Texture.class);
		
		assetManager.load("ui/level_star_0.png", Texture.class);
		assetManager.load("ui/level_star_1.png", Texture.class);
		assetManager.load("ui/level_star_2.png", Texture.class);
		assetManager.load("ui/level_star_3.png", Texture.class);
		
		assetManager.load("ui/setting_check_on.png", Texture.class);
		assetManager.load("ui/setting_check_off.png", Texture.class);
		
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		
		assetManager.load("ui/heart-border-bar.png", Texture.class);
		assetManager.load("ui/heart-progress-bar.png", Texture.class);
		assetManager.load("ui/icon_holder_red.png", Texture.class);
		assetManager.load("ui/health.png", Texture.class);
		
		assetManager.load("texture/skeleton.png", Texture.class);
		
		assetManager.load("texture/fire-worm-projectil.png", Texture.class);
		assetManager.load("texture/fire-worm-projectil-effect.png", Texture.class);
		
		assetManager.load("texture/projectil-effect-2.png", Texture.class);
		
		assetManager.load("ui/blurry-Blood-Overlay.png", Texture.class);
		
		assetManager.load("ui/skull.png", Texture.class);
		
		//sound
		assetManager.load("sound/effect/sword.ogg", Sound.class);
		assetManager.load("sound/effect/projectile1.ogg", Sound.class);
		assetManager.load("sound/effect/skeleton-dead.ogg", Sound.class);
		assetManager.load("sound/effect/bringer-dead.ogg", Sound.class);
		assetManager.load("sound/effect/skeleton-hit.ogg", Sound.class);
		assetManager.load("sound/effect/mushroom-dead.ogg", Sound.class);
		assetManager.load("sound/effect/slime_dead.ogg", Sound.class);
		assetManager.load("sound/effect/sound-721t.ogg", Sound.class);
		assetManager.load("sound/effect/player-jump.ogg", Sound.class);
		assetManager.load("sound/effect/flag.ogg", Sound.class);
		assetManager.load("sound/effect/light-Trap2.ogg", Sound.class);
		assetManager.load("sound/effect/mutated_bat_dead.ogg", Sound.class);
		assetManager.load("sound/effect/vase_destroy.ogg", Sound.class);
		assetManager.load("sound/effect/ladder.ogg", Sound.class);
		assetManager.load("sound/effect/level_locked.ogg", Sound.class);
		//sound

		assetManager.load("texture/Purple-Portal.png", Texture.class);

		assetManager.load("ui/hitbox.png", Texture.class);
		assetManager.load("texture/Mutated_Bat.png", Texture.class);
		assetManager.load("texture/slime.png", Texture.class);
		assetManager.load("texture/mushroomZombie.png", Texture.class);
		assetManager.load("texture/fire-worm.png", Texture.class);
		assetManager.load("texture/Bringer-of-Death.png", Texture.class);
		assetManager.load("texture/flying-eye.png", Texture.class);
		
		assetManager.load("texture/worm-trap.png", Texture.class);
		assetManager.load("texture/spikeUp.png", Texture.class);
		assetManager.load("texture/spikeDown.png", Texture.class);
		assetManager.load("texture/spikeLeft.png", Texture.class);
		assetManager.load("texture/spikeRight.png", Texture.class);
		assetManager.load("texture/LightningTrap_Level2.png", Texture.class);
		
		assetManager.load("texture/gravityBlock.png", Texture.class);
		assetManager.load("texture/ladder.png", Texture.class);
		assetManager.load("texture/checkpoint.png", Texture.class);
		assetManager.load("texture/Training-Dummy.png", Texture.class);
		assetManager.load("texture/vase.png", Texture.class);
		assetManager.load("texture/woonderBox.png", Texture.class);
		
		FileHandleResolver resolver = new InternalFileHandleResolver();
        assetManager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
        assetManager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

        FreetypeFontLoader.FreeTypeFontLoaderParameter font = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        font.fontFileName = "font/thaleahFat.ttf";
        font.fontParameters.size = 86;
        font.fontParameters.color = Color.WHITE;
        assetManager.load("font/thaleahFat.ttf", BitmapFont.class, font);
	}
	
	@SuppressWarnings("unchecked")
	public void createResources() {
		playerTexture = assetManager.get("texture/player_1.png", Texture.class);
		
		parallaxFrontal = assetManager.get("map/parallax1.png", Texture.class);
		
		particle1Texture = assetManager.get("map/particle1.png", Texture.class);
		particle = assetManager.get("map/particle1.pe", ParticleEffect.class);
		
		titleGame = assetManager.get("ui/game-title.png", Texture.class);
		terrain = assetManager.get("ui/terreno.png", Texture.class);
		
		layer1 = assetManager.get("map/background_layer_1.png", Texture.class);
		layer2 = assetManager.get("map/background_layer_2.png", Texture.class);
		layer3 = assetManager.get("map/background_layer_3.png", Texture.class);
		
		star0 = assetManager.get("ui/level_star_0.png", Texture.class);
		star1 = assetManager.get("ui/level_star_1.png", Texture.class);
		star2 = assetManager.get("ui/level_star_2.png", Texture.class);
		star3 = assetManager.get("ui/level_star_3.png", Texture.class);
		
		btnClose = assetManager.get("ui/close.png", Texture.class);
		settingBG = assetManager.get("ui/setting_bg.png", Texture.class);
		settingTable = assetManager.get("ui/setting_table.png", Texture.class);
		
		checkOn= assetManager.get("ui/setting_check_on.png", Texture.class);
		checkOff = assetManager.get("ui/setting_check_off.png", Texture.class);
		
		progressBorder = assetManager.get("ui/heart-border-bar.png", Texture.class);
		progressBar = assetManager.get("ui/heart-progress-bar.png", Texture.class);
		iconCircleRed = assetManager.get("ui/icon_holder_red.png", Texture.class);
		iconHealth = assetManager.get("ui/health.png", Texture.class);
		
		projectile1 = assetManager.get("texture/fire-worm-projectil.png", Texture.class);
		projectileEffect1 = assetManager.get("texture/fire-worm-projectil-effect.png", Texture.class);
		
		projectile2 = assetManager.get("texture/projectil-effect-2.png", Texture.class);
		
		screenHurt = assetManager.get("ui/blurry-Blood-Overlay.png", Texture.class);
		
		//sound
		soundSword = assetManager.get("sound/effect/sword.ogg", Sound.class);
		bringerDead = assetManager.get("sound/effect/bringer-dead.ogg", Sound.class);
		soundPlayerProjectile = assetManager.get("sound/effect/projectile1.ogg", Sound.class);
		skeletonDead = assetManager.get("sound/effect/skeleton-dead.ogg", Sound.class);
		skeletonHit = assetManager.get("sound/effect/skeleton-hit.ogg", Sound.class);
		mushroomZombieDead = assetManager.get("sound/effect/mushroom-dead.ogg", Sound.class);
		slimeDead = assetManager.get("sound/effect/slime_dead.ogg", Sound.class);
		soundPlayerWalk = assetManager.get("sound/effect/sound-721t.ogg", Sound.class);
		soundPlayerJump = assetManager.get("sound/effect/player-jump.ogg", Sound.class);
		mutatedBatDead = assetManager.get("sound/effect/mutated_bat_dead.ogg", Sound.class);
		vaseSound = assetManager.get("sound/effect/vase_destroy.ogg", Sound.class);
		
		wormTrapSound = assetManager.get("sound/effect/flag.ogg", Sound.class);
		lightTrap2Sound = assetManager.get("sound/effect/light-Trap2.ogg", Sound.class);
		//sound
		
		trainingDummyTexture = assetManager.get("texture/Training-Dummy.png", Texture.class);
		vaseTexture = assetManager.get("texture/vase.png", Texture.class);
		ladderSound = assetManager.get("sound/effect/ladder.ogg", Sound.class);
		levelLockedSound = assetManager.get("sound/effect/level_locked.ogg", Sound.class);

		portalTexture = assetManager.get("texture/Purple-Portal.png", Texture.class);

		hitboxTexture = assetManager.get("ui/hitbox.png", Texture.class);
		slimeTexture = assetManager.get("texture/slime.png", Texture.class);
		mutatedBatTexture = assetManager.get("texture/Mutated_Bat.png", Texture.class);
		mushroomZombieTexture = assetManager.get("texture/mushroomZombie.png", Texture.class);
		fireWormTexture = assetManager.get("texture/fire-worm.png", Texture.class);
		skeletonTexture = assetManager.get("texture/skeleton.png", Texture.class);
		bringerDeathTexture = assetManager.get("texture/Bringer-of-Death.png", Texture.class);
		flyingEyeTexture = assetManager.get("texture/flying-eye.png", Texture.class);
		
		wormTrap = assetManager.get("texture/worm-trap.png", Texture.class);
		SpikeUp = assetManager.get("texture/spikeUp.png", Texture.class);
		spikeDown = assetManager.get("texture/spikeDown.png", Texture.class);
		spikeLeft = assetManager.get("texture/spikeLeft.png", Texture.class);
		spikeRight = assetManager.get("texture/spikeRight.png", Texture.class);
		ligthTrap2Texture = assetManager.get("texture/LightningTrap_Level2.png", Texture.class);
		
		skullTexture = assetManager.get("ui/skull.png", Texture.class);
		
		levelBg = assetManager.get("ui/level_bg.png", Texture.class);
		levelTable = assetManager.get("ui/level_table.png", Texture.class);
		levelSlot = assetManager.get("ui/level_slot.png", Texture.class);
		levelLock = assetManager.get("ui/level_lock.png", Texture.class);
		
		gravityBlock = assetManager.get("texture/gravityBlock.png", Texture.class);
		ladderTexture = assetManager.get("texture/ladder.png", Texture.class);
		checkPointTexture = assetManager.get("texture/checkpoint.png", Texture.class);
		woonderBoxTexture = assetManager.get("texture/woonderBox.png", Texture.class);
		
		fontMenu = assetManager.get("font/thaleahFat.ttf", BitmapFont.class);
		fontLevelSlot = assetManager.get("font/thaleahFat.ttf", BitmapFont.class);
		
		skeletonAnimation = new Animation[8];
		skeletonAnimation[0] = createAnimation(3, skeletonTexture, 4, 64, 64, 0.1f);
		skeletonAnimation[1] = createAnimation(3, skeletonTexture, 4, 64, 64, 0.1f);
		flipAnimation(skeletonAnimation[1], true, false);
		
		skeletonAnimation[2] = createAnimation(2, skeletonTexture, 12, 64, 64, 0.1f);
		skeletonAnimation[3] = createAnimation(2, skeletonTexture, 12, 64, 64, 0.1f);
		flipAnimation(skeletonAnimation[3], true, false);
		
		skeletonAnimation[4] = createAnimation(0, skeletonTexture, 13, 64, 64, 0.13f);
		skeletonAnimation[5] = createAnimation(0, skeletonTexture, 13, 64, 64, 0.13f);
		flipAnimation(skeletonAnimation[5], true, false);
		
		skeletonAnimation[6] = createAnimation(1, skeletonTexture, 13, 64, 64, 0.15f);
		skeletonAnimation[7] = createAnimation(1, skeletonTexture, 13, 64, 64, 0.15f);
		flipAnimation(skeletonAnimation[7], true, false);
		
		skullAnimation = new Animation[1];
		skullAnimation[0] = createAnimation(0, skullTexture, 2, 450, 450, 0.12f);
		
		int width = 90, height = 90;
		
		fireWormAnimation = new Animation[10];
		fireWormAnimation[0] = createAnimation(0, fireWormTexture, 9, width, height, 0.1f);
		fireWormAnimation[1] = createAnimation(0, fireWormTexture, 9, width, height, 0.1f);
		flipAnimation(fireWormAnimation[1], true, false);
		
		fireWormAnimation[2] = createAnimation(1, fireWormTexture, 9, width, height, 0.1f);
		fireWormAnimation[3] = createAnimation(1, fireWormTexture, 9, width, height, 0.1f);
		flipAnimation(fireWormAnimation[3], true, false);
		
		fireWormAnimation[4] = createAnimation(2, fireWormTexture, 16, width, height, 0.13f);
		fireWormAnimation[5] = createAnimation(2, fireWormTexture, 16, width, height, 0.13f);
		flipAnimation(fireWormAnimation[5], true, false);
		
		fireWormAnimation[6] = createAnimation(3, fireWormTexture, 3, width, height, 0.13f);
		fireWormAnimation[7] = createAnimation(3, fireWormTexture, 3, width, height, 0.13f);
		flipAnimation(fireWormAnimation[7], true, false);
		
		fireWormAnimation[8] = createAnimation(4, fireWormTexture, 9, width, height, 0.15f);
		fireWormAnimation[9] = createAnimation(4, fireWormTexture, 9, width, height, 0.15f);
		flipAnimation(fireWormAnimation[9], true, false);
		
		width = 50;
		height = 56;
		
		mushroomZombieAnimation = new Animation[14];
		//mushroom
		mushroomZombieAnimation[0] = createAnimation(0, mushroomZombieTexture, 1, width, height, 0.1f);
		mushroomZombieAnimation[1] = createAnimation(0, mushroomZombieTexture, 1, width, height, 0.1f);
		flipAnimation(mushroomZombieAnimation[1], true, false);
		
		//awaken
		mushroomZombieAnimation[2] = createAnimation(0, mushroomZombieTexture, 8, width, height, 0.1f);
		mushroomZombieAnimation[3] = createAnimation(0, mushroomZombieTexture, 8, width, height, 0.1f);
		flipAnimation(mushroomZombieAnimation[3], true, false);
		
		//idle
		mushroomZombieAnimation[4] = createAnimation(1, mushroomZombieTexture, 4, width, height, 0.1f);
		mushroomZombieAnimation[5] = createAnimation(1, mushroomZombieTexture, 4, width, height, 0.1f);
		flipAnimation(mushroomZombieAnimation[5], true, false);
		
		//walk
		mushroomZombieAnimation[6] = createAnimation(2, mushroomZombieTexture, 8, width, height, 0.1f);
		mushroomZombieAnimation[7] = createAnimation(2, mushroomZombieTexture, 8, width, height, 0.1f);
		flipAnimation(mushroomZombieAnimation[7], true, false);
		
		//attack
		mushroomZombieAnimation[8] = createAnimation(3, mushroomZombieTexture, 5, width, height, 0.13f);
		mushroomZombieAnimation[9] = createAnimation(3, mushroomZombieTexture, 5, width, height, 0.13f);
		flipAnimation(mushroomZombieAnimation[9], true, false);
		
		//hit
		mushroomZombieAnimation[10] = createAnimation(4, mushroomZombieTexture, 9, width, height, 0.13f);
		mushroomZombieAnimation[11] = createAnimation(4, mushroomZombieTexture, 9, width, height, 0.13f);
		flipAnimation(mushroomZombieAnimation[11], true, false);
		
		//dead
		mushroomZombieAnimation[12] = createAnimation(5, mushroomZombieTexture, 7, width, height, 0.15f);
		mushroomZombieAnimation[13] = createAnimation(5, mushroomZombieTexture, 7, width, height, 0.15f);
		flipAnimation(mushroomZombieAnimation[13], true, false);
		
		width = 96;
		height = 80;
		mutatedBatAnimation = new Animation[4];
		mutatedBatAnimation[0] = createAnimation(0, mutatedBatTexture, 6, width, height, 0.05f);
		mutatedBatAnimation[1] = createAnimation(0, mutatedBatTexture, 6, width, height, 0.05f);
		flipAnimation(mutatedBatAnimation[0], true, false);
		
		mutatedBatAnimation[2] = createAnimation(1, mutatedBatTexture, 8, width, height, 0.11f);
		mutatedBatAnimation[3] = createAnimation(1, mutatedBatTexture, 8, width, height, 0.11f);
		flipAnimation(mutatedBatAnimation[2], true, false);
		
		width = 64;
		height = 41;
		slimeAnimation = new Animation[8];
		
		slimeAnimation[0] = createAnimation(0, slimeTexture, 4, width, height, 0.1f);
		slimeAnimation[1] = createAnimation(0, slimeTexture, 4, width, height, 0.1f);
		flipAnimation(slimeAnimation[0], true, false);
		
		slimeAnimation[2] = createAnimation(1, slimeTexture, 18, width, height, 0.05f);
		slimeAnimation[3] = createAnimation(1, slimeTexture, 18, width, height, 0.05f);
		flipAnimation(slimeAnimation[2], true, false);
		
		slimeAnimation[4] = createAnimation(2, slimeTexture, 14, width, height, 0.10f);
		slimeAnimation[5] = createAnimation(2, slimeTexture, 14, width, height, 0.10f);
		flipAnimation(slimeAnimation[4], true, false);
		
		slimeAnimation[6] = createAnimation(3, slimeTexture, 12, width, height, 0.11f);
		slimeAnimation[7] = createAnimation(3, slimeTexture, 12, width, height, 0.11f);
		flipAnimation(slimeAnimation[6], true, false);
		
		width = 46;
		height = 46;
		
		projectile1Animation = new Animation[2];
		projectile1Animation[0] = createAnimation(1, projectile1, 6, width, height, 0.1f);
		projectile1Animation[1] = createAnimation(0, projectile1, 6, width, height, 0.1f);
		
		projectile1EffectAnimation = new Animation[2];
		projectile1EffectAnimation[0] = createAnimation(0, projectileEffect1, 7, width, height, 0.1f);
		projectile1EffectAnimation[1] = createAnimation(0, projectileEffect1, 7, width, height, 0.1f);
		flipAnimation(projectile1EffectAnimation[1], true, false);
		
		width = 48;
		height = 48;
		
		projectile2Animation = new Animation[2];
		projectile2Animation[0] = createAnimation(0, projectile2, 1, width, height, 0.1f);
		projectile2Animation[1] = createAnimation(0, projectile2, 1, width, height, 0.1f);
		
		projectile2EffectAnimation = new Animation[2];
		projectile2EffectAnimation[0] = createAnimation(0, projectile2, 8, width, height, 0.1f);
		projectile2EffectAnimation[1] = createAnimation(0, projectile2, 8, width, height, 0.1f);
		flipAnimation(projectile1EffectAnimation[1], true, false);
		
		width = 96;
		height = 96;
		
		lightTrap2Animation = new Animation[1];
		lightTrap2Animation[0] = createAnimation(0, ligthTrap2Texture, 12, width, height, 0.12f);
		
		width = 128;
		height = 64;
		
		wormTrapAnimation = new Animation[1];
		wormTrapAnimation[0] = createAnimation(0, wormTrap, 81, width, height, 0.1f);
		
		width = 110;
		height = 110;
		
		checkPointAnimation = new Animation[3];
		checkPointAnimation[0] = createAnimation(0, checkPointTexture, 1, width, height, 0.1f);
		checkPointAnimation[1] = createAnimation(1, checkPointTexture, 21, width, height, 0.1f);
		checkPointAnimation[2] = createAnimation(2, checkPointTexture, 1, width, height, 0.1f);
		
		width = 64;
		height = 64;
		
		portalAnimation = new Animation[2];
		portalAnimation[0] = createAnimation(0, portalTexture, 8, width, height, 0.1f);
		portalAnimation[1] = createAnimation(2, portalTexture, 8, width, height, 0.1f);
		
		width = 144;
		height = 80;
		playerAnimation = new Animation[20];
		
		playerAnimation[0] = createAnimation(0, playerTexture, 8, width, height, 0.1f);
		playerAnimation[1] = createAnimation(0, playerTexture, 8, width, height, 0.1f);
		flipAnimation(playerAnimation[1], true, false);
		
		playerAnimation[2] = createAnimation(1, playerTexture, 8, width, height, 0.08f);
		playerAnimation[3] = createAnimation(1, playerTexture, 8, width, height, 0.08f);
		flipAnimation(playerAnimation[3], true, false);
		
		playerAnimation[4] = createAnimation(10, playerTexture, 4, width, height, 0.11f);
		playerAnimation[5] = createAnimation(10, playerTexture, 4, width, height, 0.11f);
		flipAnimation(playerAnimation[5], true, false);
		
		playerAnimation[6] = createAnimation(18, playerTexture, 9, width, height, 0.11f);
		playerAnimation[7] = createAnimation(18, playerTexture, 9, width, height, 0.11f);
		flipAnimation(playerAnimation[7], true, false);
		
		playerAnimation[8] = createAnimation(24, playerTexture, 13, width, height, 0.15f);
		playerAnimation[9] = createAnimation(24, playerTexture, 13, width, height, 0.15f);
		flipAnimation(playerAnimation[9], true, false);
		
		playerAnimation[10] = createAnimation(5, playerTexture, 7, width, height, 0.1f);
		playerAnimation[11] = createAnimation(5, playerTexture, 7, width, height, 0.1f);
		flipAnimation(playerAnimation[11], true, false);
		
		playerAnimation[12] = createAnimation(8, playerTexture, 3, width, height, 0.1f);
		playerAnimation[13] = createAnimation(8, playerTexture, 3, width, height, 0.1f);
		flipAnimation(playerAnimation[13], true, false);
		
		playerAnimation[14] = createAnimation(17, playerTexture, 8, width, height, 0.1f);
		playerAnimation[15] = createAnimation(17, playerTexture, 8, width, height, 0.1f);
		
		playerAnimation[16] = createAnimation(11, playerTexture, 4, width, height, 0.14f);
		playerAnimation[17] = createAnimation(11, playerTexture, 4, width, height, 0.14f);
		flipAnimation(playerAnimation[17], true, false);
		
		playerAnimation[18] = createAnimation(12, playerTexture, 6, width, height, 0.16f);
		playerAnimation[19] = createAnimation(12, playerTexture, 6, width, height, 0.16f);
		flipAnimation(playerAnimation[19], true, false);
		
		width = 140;
		height = 93;
		
		bringerDeathAnimation = new Animation[10];
		bringerDeathAnimation[0] = createAnimation(0, bringerDeathTexture, 8, width, height, 0.1f);
		bringerDeathAnimation[1] = createAnimation(0, bringerDeathTexture, 8, width, height, 0.1f);
		flipAnimation(bringerDeathAnimation[1], true, false);
		
		bringerDeathAnimation[2] = createAnimation(1, bringerDeathTexture, 8, width, height, 0.1f);
		bringerDeathAnimation[3] = createAnimation(1, bringerDeathTexture, 8, width, height, 0.1f);
		flipAnimation(bringerDeathAnimation[3], true, false);
		
		bringerDeathAnimation[4] = createAnimation(2, bringerDeathTexture, 8, width, height, 0.1f);
		bringerDeathAnimation[5] = createAnimation(2, bringerDeathTexture, 8, width, height, 0.1f);
		flipAnimation(bringerDeathAnimation[5], true, false);
	
		bringerDeathAnimation[6] = createAnimation(4, bringerDeathTexture, 7, width, height, 0.1f);
		bringerDeathAnimation[7] = createAnimation(4, bringerDeathTexture, 7, width, height, 0.1f);
		flipAnimation(bringerDeathAnimation[7], true, false);
		
		bringerDeathAnimation[8] = createAnimation(5, bringerDeathTexture, 8, width, height, 0.1f);
		bringerDeathAnimation[9] = createAnimation(5, bringerDeathTexture, 8, width, height, 0.1f);
		flipAnimation(bringerDeathAnimation[9], true, false);
		
		bringerInvocationAnimation = new Animation[4];
		bringerInvocationAnimation[0] = createAnimation(6, bringerDeathTexture, 8, width, height, 0.1f);
		bringerInvocationAnimation[1] = createAnimation(6, bringerDeathTexture, 8, width, height, 0.1f);
		flipAnimation(bringerInvocationAnimation[1], true, false);
		
		bringerInvocationAnimation[2] = createAnimation(7, bringerDeathTexture, 8, width, height, 0.1f);
		bringerInvocationAnimation[3] = createAnimation(7, bringerDeathTexture, 8, width, height, 0.1f);
		flipAnimation(bringerInvocationAnimation[3], true, false);
		
		width = 32;
		height = 32;
		
		trainingDummyAnimation  = new Animation[3];
		trainingDummyAnimation[0] = createAnimation(0, trainingDummyTexture, 4, width, height, 0.15f);
		trainingDummyAnimation[1] = createAnimation(1, trainingDummyTexture, 5, width, height, 0.1f);
		trainingDummyAnimation[2] = createAnimation(2, trainingDummyTexture, 8, width, height, 0.07f);
		
		vaseAnimation  = new Animation[2];
		vaseAnimation[0] = createAnimation(0, vaseTexture, 1, width, height, 100f);
		vaseAnimation[1] = createAnimation(0, vaseTexture, 5, width, height, 0.1f);
		
		width = 64;
		height = 64;
		
		flyingEyeAnimation = new Animation[12];
		flyingEyeAnimation[0] = createAnimation(0, flyingEyeTexture, 8, width, height, 0.08f);
		flyingEyeAnimation[1] = createAnimation(0, flyingEyeTexture, 8, width, height, 0.08f);
		flipAnimation(flyingEyeAnimation[1], true, false);
		
		flyingEyeAnimation[2] = createAnimation(1, flyingEyeTexture, 8, width, height, 0.1f);
		flyingEyeAnimation[3] = createAnimation(1, flyingEyeTexture, 8, width, height, 0.1f);
		flipAnimation(flyingEyeAnimation[3], true, false);
		
		flyingEyeAnimation[4] = createAnimation(2, flyingEyeTexture, 8, width, height, 0.08f);
		flyingEyeAnimation[5] = createAnimation(2, flyingEyeTexture, 8, width, height, 0.08f);
		flipAnimation(flyingEyeAnimation[5], true, false);
		
		flyingEyeAnimation[6] = createAnimation(3, flyingEyeTexture, 6, width, height, 0.13f);
		flyingEyeAnimation[7] = createAnimation(3, flyingEyeTexture, 6, width, height, 0.13f);
		flipAnimation(flyingEyeAnimation[7], true, false);
		
		flyingEyeAnimation[8] = createAnimation(4, flyingEyeTexture, 4, width, height, 0.1f);
		flyingEyeAnimation[9] = createAnimation(4, flyingEyeTexture, 4, width, height, 0.1f);
		flipAnimation(flyingEyeAnimation[9], true, false);
		
		flyingEyeAnimation[10] = createAnimation(5, flyingEyeTexture, 5, width, height, 0.1f);
		flyingEyeAnimation[11] = createAnimation(5, flyingEyeTexture, 5, width, height, 0.1f);
		flipAnimation(flyingEyeAnimation[11], true, false);
	}

	public void loadMap(int level) {
		GameManifest.NIGHTMODE = false;
		switch(level) {
			case 1:
					assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
					assetManager.load("map/level1.tmx", TiledMap.class);
					assetManager.finishLoading();
					tiledMap = assetManager.get("map/level1.tmx");
				break;
				case 2:
					assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
					assetManager.load("map/level2.tmx", TiledMap.class);
					assetManager.finishLoading();
					tiledMap = assetManager.get("map/level2.tmx");
					break;
				case 3:
					assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
					assetManager.load("map/level3.tmx", TiledMap.class);
					assetManager.finishLoading();
					tiledMap = assetManager.get("map/level3.tmx");
					break;
				case 4:
					assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
					assetManager.load("map/level4.tmx", TiledMap.class);
					assetManager.finishLoading();
					tiledMap = assetManager.get("map/level4.tmx");
					break;
				case 5:
					GameManifest.NIGHTMODE = true;
					assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
					assetManager.load("map/level5.tmx", TiledMap.class);
					assetManager.finishLoading();
					tiledMap = assetManager.get("map/level5.tmx");
					break;
				case 6:
					assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
					assetManager.load("map/level6.tmx", TiledMap.class);
					assetManager.finishLoading();
					tiledMap = assetManager.get("map/level6.tmx");
					break;
				case 7:
					assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
					assetManager.load("map/level7.tmx", TiledMap.class);
					assetManager.finishLoading();
					tiledMap = assetManager.get("map/level7.tmx");
					break;
				case 8:
					assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
					assetManager.load("map/level8.tmx", TiledMap.class);
					assetManager.finishLoading();
					tiledMap = assetManager.get("map/level8.tmx");
					break;
				case 9:
					GameManifest.NIGHTMODE = true;
					assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
					assetManager.load("map/level9.tmx", TiledMap.class);
					assetManager.finishLoading();
					tiledMap = assetManager.get("map/level9.tmx");
					break;
				default:
					GameManifest.GAME_FINISH = true;
			}
	}
	
	private Animation<TextureRegion> createAnimation(int row, Texture texture,  int totalFrame, int width, int height, float frameDuraton) {
		TextureRegion[] frame = new TextureRegion[totalFrame];
		for(int i=0; i<frame.length; i++) {
			frame[i] = new TextureRegion(texture, i * width, height * row, width, height);
		}
		
		return new Animation<TextureRegion>(frameDuraton, frame);
	}
	
	private void flipAnimation(Animation<TextureRegion> animation, boolean x, boolean y) {
		for(TextureRegion frame: animation.getKeyFrames()) {
			frame.flip(x, y);
		}
	}
	
	public void loadMusic(int id) {
		switch(id) {
		case 1:
			ambient = Gdx.audio.newMusic(Gdx.files.internal("sound/music/Action1Loop.ogg"));
			break;
		case 2:
			ambient = Gdx.audio.newMusic(Gdx.files.internal("sound/music/Action2Loop.ogg"));
			break;
		case 3:
			ambient = Gdx.audio.newMusic(Gdx.files.internal("sound/music/Action3Loop.ogg"));
			break;
		case 4:
			ambient = Gdx.audio.newMusic(Gdx.files.internal("sound/music/Action4Loop.ogg"));
			break;
		case 5:
			ambient = Gdx.audio.newMusic(Gdx.files.internal("sound/music/Action5Loop.ogg"));
			break;
		}
		
		ambient.setLooping(true);
	}

	public Animation<TextureRegion>[] getSkullAnimation() {
		return skullAnimation;
	}

	public Animation<TextureRegion>[] getSkeletonAnimation() {
		return skeletonAnimation;
	}

	public Animation<TextureRegion>[] getFireWormAnimation() {
		return fireWormAnimation;
	}

	public Animation<TextureRegion>[] getMushroomZombieAnimation() {
		return mushroomZombieAnimation;
	}

	public Animation<TextureRegion>[] getMutatedBatAnimation() {
		return mutatedBatAnimation;
	}

	public Animation<TextureRegion>[] getSlimeAnimation() {
		return slimeAnimation;
	}

	public Animation<TextureRegion>[] getBringerDeathAnimation() {
		return bringerDeathAnimation;
	}

	public Animation<TextureRegion>[] getBringerInvocationAnimation() {
		return bringerInvocationAnimation;
	}

	public Animation<TextureRegion>[] getFlyingEyeAnimation() {
		return flyingEyeAnimation;
	}

	public Animation<TextureRegion>[] getProjectile1Animation() {
		return projectile1Animation;
	}

	public Animation<TextureRegion>[] getProjectile1EffectAnimation() {
		return projectile1EffectAnimation;
	}
	
	public Animation<TextureRegion>[] getProjectile2Animation() {
		return projectile2Animation;
	}

	public Animation<TextureRegion>[] getProjectile2EffectAnimation() {
		return projectile2EffectAnimation;
	}

	public Animation<TextureRegion>[] getLightTrap2Animation() {
		return lightTrap2Animation;
	}

	public Animation<TextureRegion>[] getWormTrapAnimation() {
		return wormTrapAnimation;
	}

	public Animation<TextureRegion>[] getCheckPointAnimation() {
		return checkPointAnimation;
	}

	public Animation<TextureRegion>[] getPortalAnimation() {
		return portalAnimation;
	}

	public Animation<TextureRegion>[] getSimpleDecorationAnimation() {
		return SimpleDecorationAnimation;
	}

	public Animation<TextureRegion>[] getTrainingDummyAnimation() {
		return trainingDummyAnimation;
	}

	public Animation<TextureRegion>[] getVaseAnimation() {
		return vaseAnimation;
	}

	public Animation<TextureRegion>[] getPlayerAnimation() {
		return playerAnimation;
	}

	public AssetManager getAssetManager() {
		return assetManager;
	}

	public void setAssetManager(AssetManager assetManager) {
		this.assetManager = assetManager;
	}
}