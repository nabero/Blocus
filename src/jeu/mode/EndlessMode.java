package jeu.mode;

import menu.ui.GameFinishedUi;
import menu.ui.Overlay;
import jeu.Physic;
import jeu.Profil;
import jeu.Rubico;
import jeu.Stats;
import jeu.Strings;
import jeu.levels.Builder;
import jeu.mode.extensions.Buttons;
import jeu.mode.extensions.DesktopTests;
import jeu.mode.extensions.Score;
import jeu.mode.extensions.ScreenShake;
import jeu.mode.extensions.TemporaryText;
import jeu.mode.extensions.Transition;
import shaders.Bloom;
import assets.AssetMan;
import assets.SoundMan;
import box2dLight.RayHandler;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;

import elements.generic.Ball;
import elements.generic.blocs.Bloc;
import elements.generic.blocs.Column;
import elements.generic.bonus.Bonus;
import elements.generic.bonus.Diamond;
import elements.generic.paddles.AbstractPaddle;
import elements.particles.Particles;
import elements.particles.individual.StaticSmoke;
import elements.particles.individual.explosions.ExplodingBloc;
import elements.world.BlocWorld;

/**
 * Classe principale gerant le mode infini du jeu
 * @author Julien
 */
public class EndlessMode implements Screen {
	
	private static Game game;
	private GL20 gl;
	private static SpriteBatch batch = Rubico.batch;
	public static Bloom bloom = Rubico.bloom;
	public static boolean alternate = true, pause = false, scoreSent = false, triggerStop = false, lost = false;
	public static AbstractPaddle ship;
	public static float now = 0;
	
	public static final float FONT_HEIGHT = Rubico.heightDiv10/3;
	// *************************  D  P  A  D  ****************************

	public static int difficulty, nbBonusStop = 0, nbBombes = 0;
	public static float bloomOriginalIntensity = 1, delta = 0, timeStopBonus = 0, delta15 = 0, deltaDiv3, deltaDiv2, delta2, deltaU, deltaMicroU, UnPlusDelta, unPlusDelta2, unPlusDelta3, deltaPlusExplosion, delta25, delta4;
	public static boolean effetBloom = false, xpAjout = false;
	public static float originalDelta = 0, timeSinceLost = 0;
	public static int fps, perf = 3, explosions = 0, fpsMinus;
	private static boolean started = false;
	public static final float STOP = 3;
	public static boolean invicibility = false, freeze = false, frameByFrame = false, invoque = true;
	public static Transition transition = new Transition();
	public static int oneToFour = 1;
	public static boolean endless = true, finished, surprise;
	
	private GameFinishedUi ui;

	public EndlessMode(Game game, SpriteBatch batch, int level, boolean endless, boolean surprise) {
		super();
		Gdx.input.setCatchBackKey(true);
		EndlessMode.batch = batch;
		EndlessMode.game = game;
		ship = Rubico.profile.getSelectedPaddle();
		difficulty = level;
		EndlessMode.endless = endless;
		EndlessMode.surprise = surprise;
		init();
		gl = Gdx.graphics.getGL20();
		gl.glViewport(0, 0, (int)Rubico.screenWidth, (int)Rubico.screenHeight);
		bloom = Rubico.bloom;
		
//		BlocWorld.initWalls();
	}

	public static void init() {
		Rubico.talkToTheWorld.loadInterstitial();
		Overlay.reset();
		Rubico.reset();
		ship.clear();
		bloom.setBloomIntesity(Profil.intensiteBloom);
		ship.initialiser(Rubico.profile.getSelectedPaddleLvl()); // Pour remettre les positions mais garder shield et adds
		// ** DEPLACEMENT ZONE DE JEU
		
        scoreSent = false;
        xpAjout = false;
        pause = false;
        lost = false;
        Score.init();
        now = 0;
        timeSinceLost = 0;
        Gdx.graphics.setVSync(false);
        SoundMan.playMusic();
		timeStopBonus = 0;
		nbBonusStop = 0;
		triggerStop = false;
		nbBombes = 0;
		Buttons.init();
		Rubico.profilManager.persist();
		transition.reset();
		started = false;
		Ball.clear();
		Ball.addBall(Rubico.tierWidth, Rubico.screenHeight * 0.4f, 1, -1.2f);
		Builder.init();
		Bonus.clear();
		finished = false;
		Array<Body> bodies = new Array<Body>();
		
		BlocWorld.world.getBodies(bodies);
		for (Body b : bodies)
			b.setActive(false);
		BlocWorld.rayHandler.setAmbientLight(0.1f, 0.1f, 0.11f, 0.4f);
		RayHandler.setGammaCorrection(true);
//		BlocWorld.rayHandler.setBlur(true);
//		BlocWorld.rayHandler.setBlurNum(10);
//		DirectionalLight dirLight = new DirectionalLight(BlocWorld.rayHandler, 24, new Color(0, 1, 1, 0.2f), -75);
	}

	@Override
	public void render(float delta) {
//		delta = Math.min(delta, 0.066f);
		
		if (!started) {
			delta = 0;
			Rubico.talkToTheWorld.showAds(false);
			if (!Rubico.profile.isAdsFree())
				Rubico.talkToTheWorld.displayInterstitial();
			if (Gdx.input.justTouched()) {
				SoundMan.playMusic();
				started = true;
			}
		}
		alternate();
		originalDelta = delta;
		if (Gdx.app.getVersion() == 0)
			DesktopTests.debug();
		Rubico.cam();
		
		bloomActive();
		explosions = 0;
		batch.begin();
		
		Particles.background(batch);
		
		if (Gdx.input.isKeyPressed(Keys.BACK)) { 
			Buttons.backButton(batch, game);
			pause = true;
		}
		if (!pause && !freeze) {
			if (delta < 1) { 
				if (!Gdx.input.isTouched())
					delta /= 5;
				EndlessMode.delta = delta;
				majDeltas();
			}
			// S I   O N   A   P A S   E N C O R E   P E R D U
			if (!lost && !triggerStop) {
				transition.act();
				affichageNonPerdu();
			} else {
				if (triggerStop) {
					affichageEtUpdateStop();
				} else {
   					if (lost && !scoreSent) {
						bloom.setBloomIntesity(1.1f);
						if (endless) {
							if (Score.score > 9000)
								Rubico.talkToTheWorld.unlockAchievementGPGS(Strings.ACH_9000);
							Rubico.talkToTheWorld.submitScore(Strings.LEADERBOARD_KEY, Score.score );
						} else
							Rubico.talkToTheWorld.submitScore(Strings.LEADERBOARD_LEVEL_KEY, Rubico.profile.lvl );
						scoreSent = true;
					}
				}
				affichagePerdu();
				if (!triggerStop && lost) {
					Column.explode(1);
				}
			}
			if (started && !lost)
				update();
		} else { // D O N C   E N   P A U S E
			EndlessMode.delta = 0;
			majDeltas();
			affichagePerdu();
		}
		StaticSmoke.draw(batch);
		if (triggerStop)
			stopActivated();
		Column.draw(batch);
		if (!lost)
			Bonus.draw(batch);
		batch.end();
		// LATE
		if (!Rubico.profile.lights())
			bloom.render();
		BlocWorld.act();
		batch.begin();
		ExplodingBloc.draw(batch, Bloc.EXPLOSIONS);
		TemporaryText.draw(batch);
		ui();
		Ball.draw(batch);
		Overlay.act(batch);
		if (surprise) {
			if (Gdx.input.isTouched() && Physic.getXClic() < 4)	batch.setColor(0.5f, 0.5f, 0.5f, .6f);
			else																	batch.setColor(0.5f, 0.5f, 0.5f, .2f);
			batch.draw(AssetMan.play, 2, 2, -2, 2);
			if (Gdx.input.isTouched() && Physic.getXClic() > Rubico.screenWidth - 4)	batch.setColor(0.5f, 0.5f, 0.5f, .6f);
			else																		batch.setColor(0.5f, 0.5f, 0.5f, .2f);
			batch.draw(AssetMan.play, Rubico.screenWidth - 2, 2, 2, 2);
		}
		batch.end();
		
		fps = Gdx.graphics.getFramesPerSecond();
		fpsMinus = fps - 10;
		perf = fps / 10;
		if (!started)
			return;
		Score.decrease();
		now += EndlessMode.delta;
		ScreenShake.act();
		deltaPlusExplosion = EndlessMode.delta + explosions;
		
//		if (lost && Gdx.input.justTouched() && now > timeLost + 0.8f) {
//			init();
//		}
	}
	
	private void alternate() {
		alternate = !alternate;
		if (++oneToFour > 4)
			oneToFour = 1;
	}

	private void stopActivated() {
		ship.draw(batch);
	}

	public static void majDeltas() {
		deltaPlusExplosion = EndlessMode.delta + explosions;
		delta15 = delta * 15;
		deltaDiv3 = delta / 3;
		deltaDiv2 = delta / 2;
		delta2 = delta * 2;
		deltaU = delta * Stats.U;
		deltaMicroU =  Stats.microU * delta;
		UnPlusDelta = 1 + delta;
		unPlusDelta2 = 1 + delta2;
		unPlusDelta3 = UnPlusDelta + delta2;
		delta25 = delta * 25;
		delta4 = delta * 4;
	}

	private void affichageEtUpdateStop() {
		timeStopBonus -= delta;
		if (timeStopBonus < 0) {
			triggerStop = false;
			transition.activate(20);
			if (--nbBonusStop > 0)
				timeStopBonus += STOP;
		}
	}

	private void bloomActive() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		if (!Rubico.profile.lights()) {
			if (triggerStop)
				bloom.setBloomIntesity(Profil.intensiteBloom + (timeStopBonus*2));
			else
				ScreenShake.bloomEffect();
			bloom.capture();
		} else {
		}
	}

	private static void mettrePause() {
		pause = true;
		Rubico.profilManager.persist();
	}
	
	private static final float top = Rubico.screenHeight * 0.85f;
	private void drawFinalScore(SpriteBatch batch) {
		timeSinceLost += Gdx.graphics.getDeltaTime();
		
		if (timeSinceLost < 0.7f)
			return;
		
		batch.setColor(AssetMan.BLACK);
		// xp dispo

		if (finished)							displayText(batch, "Congratulations !", top - Rubico.outlineFont.getBounds("X").height * 2);
		// thanks for playing
		else									displayText(batch, Strings.DEAD, top - Rubico.outlineFont.getBounds("X").height * 2);
		if (endless) {
			displayText(batch, Score.strScore, top);
			if (Rubico.profile.newHighscore)	displayText(batch, "New highscore !!!", top - Rubico.outlineFont.getBounds("X").height * 4);
			else								displayText(batch, Rubico.profile.highscoreString, top - Rubico.outlineFont.getBounds("X").height * 4);
		} else {
			displayText(batch, Rubico.profile.lvlString, top);
		}
		batch.setColor(AssetMan.WHITE);
		
		if (ui == null)
			ui = new GameFinishedUi();
		ui.draw(batch);
		
//		Buttons.drawUpgradeAndTwitter(batch);
	}
	
	private static void displayText(SpriteBatch batch, CharSequence txt, float y) {
		batch.setColor(0, 0, 0, 0.2f);
		batch.draw(AssetMan.partBloc, 0, y, Rubico.screenWidth, -Rubico.outlineFont.getBounds(txt).height);
		Rubico.outlineFont.draw(batch,txt, ((Rubico.cam.position.x-Rubico.halfWidth)) + ((Rubico.halfWidth - (Rubico.outlineFont.getBounds(txt).width)/2)), y);
//		Rubico.outlineFont.setScale( 
//				//1 + (EndlessMode.now % 1)
//				1.3f
//				);
//		Rubico.outlineFont.setColor(0, 0, 0, 1);
//		Rubico.menuFont.draw(batch,txt, ((cam.position.x-Rubico.screenHalfWidth)) + ((Rubico.screenHalfWidth - (Rubico.menuFont.getBounds(txt).width)/2)), y);
	}

	private float prevDelta;
	private void affichagePerdu() {
		prevDelta = delta;
		delta = 0;
		stopActivated();
		delta = prevDelta;
		Particles.draw(batch);
	}

	public static void effetBloom() {
		effetBloom = true;
		bloomOriginalIntensity = 50;
	}

	private void ui() {
		if (lost) {
			drawFinalScore(batch);
//			Buttons.initAndDrawButtons(batch, game);
		}
		if (pause)
			Buttons.backButton(batch, game);
		Score.draw(batch, lost);
	}

	private void affichageNonPerdu() {
		stopActivated();
		Particles.draw(batch);
//		ui();
	}

	private void update() {
		ship.move();
		if (Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.isKeyPressed(Keys.POWER) || Gdx.input.isKeyPressed(Keys.HOME) || Gdx.input.isKeyPressed(Keys.ESCAPE))
			mettrePause();
		if (lost)
			Buttons.testClick();
	}

	@Override	public void resize(int width, int height) {		Rubico.profilManager.persist();	}
	@Override	public void show() {	}
	@Override	public void hide() {		Rubico.profilManager.persist();	}
	@Override	public void pause() {	}
	@Override	public void resume() {		Rubico.assetMan.reload();	}
	@Override	public void dispose() {	}
	public static Camera getCam() {		return Rubico.cam;	}

	public static void lost() {
		if (!invicibility && !lost)
			initFinishedUi();
	}

	public static boolean aPerdu() {
		return lost;
	}
	
	public static void reset() {
		Particles.clear();
		triggerStop = false;
		pause = false;
	}

	public static void rotate(Vector2 dir, float angle) {
		if (alternate)
			dir.rotate(angle);
	}

	public static void finished() {
		ship.clear();
		for (Diamond d : Diamond.diamonds)
			d.taken();
		Rubico.profile.upLvl();
		initFinishedUi();
		finished = true;
		Builder.nextSeed();
	}

	private static void initFinishedUi() {
//		Overlay.gameFinished();
		Score.lost(true);
		timeSinceLost = 0;
		lost = true;
		
	}

	public static Game getGame() {
		return game;
	}

}