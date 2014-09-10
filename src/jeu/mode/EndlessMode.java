package jeu.mode;

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

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

import elements.generic.Ball;
import elements.generic.Paddle;
import elements.generic.PaddleShot;
import elements.generic.blocs.Column;
import elements.generic.bonus.Bonus;
import elements.particles.Particles;

/**
 * Classe principale gerant le mode infini du jeu
 * @author Julien
 */
public class EndlessMode implements Screen {
	
	private Game game;
	private GL20 gl;
	private static SpriteBatch batch = Rubico.batch;
	public static Bloom bloom = Rubico.bloom;
	public static boolean alternate = true, pause = false, scoreSent = false, triggerStop = false, lost = false;
	public static Paddle ship;
	public static float now = 0;
	
	public static final int X_CHRONO = Rubico.widthDiv10/2 - Rubico.halfWidth;
	public static final int FONT_HEIGHT = Rubico.heightDiv10/3;
	// *************************  D  P  A  D  ****************************

	public static final OrthographicCamera cam = new OrthographicCamera(Rubico.screenWidth, Rubico.screenHeight);
	public static int difficulty, nbBonusStop = 0, nbBombes = 0;
	public static float bloomOriginalIntensity = 1, delta = 0, timeStopBonus = 0, delta15 = 0, deltaDiv3, deltaDiv2, delta2, deltaU, deltaMicroU, UnPlusDelta, unPlusDelta2, unPlusDelta3, deltaPlusExplosion, delta25, delta4;
	public static boolean effetBloom = false, xpAjout = false;
	private static Matrix4 tmpCombined; 
	public static float originalDelta = 0, timeSinceLost = 0;
	public static int fps, perf = 3, explosions = 0;
	private static boolean started = false;
	private static float timeLost = 0;
	public static final float STOP = 3;
	public static boolean invicibility = false, freeze = false, frameByFrame = false, invoque = true;
	public static Transition transition = new Transition();
	public static int oneToFour = 1;
	public static boolean endless, finished;

	public EndlessMode(Game game, SpriteBatch batch, int level, boolean endless) {
		super();
		Gdx.input.setCatchBackKey(true);
		EndlessMode.batch = batch;
		this.game = game;
		ship = new Paddle();
		difficulty = level;
		EndlessMode.endless = endless;
		init();
		ship.initialiser();
		gl = Gdx.graphics.getGL20();
		gl.glViewport(0, 0, Rubico.screenWidth, Rubico.screenHeight);
		bloom = Rubico.bloom;
	}

	public static void init() {
		Rubico.reset();
		bloom.setBloomIntesity(Rubico.profile.intensiteBloom);
		ship.initialiser(); // Pour remettre les positions mais garder shield et adds
		if (Gdx.app.getVersion() != 0)
			Rubico.talkToTheWorld.showAds(false); // desactiver adds. A VIRER POUR LA RELEASE
		// ** DEPLACEMENT ZONE DE JEU
		cam.position.set(Rubico.screenWidth/2, Rubico.screenHeight / 2, 0);
		
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
		cam.position.z = 1;
		Rubico.profilManager.persist();
		transition.reset();
		started = false;
		Ball.clear();
		Ball.addBall(Rubico.tierWidth, Rubico.screenHeight * 0.4f, 1, -1.2f);
		Builder.init();
		Bonus.clear();
		finished = false;
	}

	@Override
	public void render(float delta) {
//		delta = Math.min(delta, 0.066f);
		if (!started) {
			delta = 0;
			if (Gdx.input.justTouched()) {
				started = true;
			}
		}
		alternate();
		originalDelta = delta;
		if (Gdx.app.getVersion() == 0)
			DesktopTests.debug();
		cam();
		
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
						Rubico.talkToTheWorld.submitScore(Strings.LEADERBOARD_KEY, Score.score );
						scoreSent = true;
					}
				}
				affichagePerdu();
				if (!triggerStop && lost) {
					drawFinalScore(batch);
					Column.explode(1);
				}
			}
			if (started && !lost)
				update();
		} else { // D O N C   E N   P A U S E
			EndlessMode.delta = 0;
			majDeltas();
			affichagePerdu();
			Buttons.backButton(batch, game);
			if (Gdx.input.justTouched())
				pause = false;
		}
		if (triggerStop)
			stopActivated();
		PaddleShot.act(batch);
		Column.draw(batch);
		if (!lost) {
			Ball.draw(batch);
			Bonus.draw(batch);
		}
		TemporaryText.draw(batch);
		batch.end();
		bloom.render();
		
		fps = Gdx.graphics.getFramesPerSecond();
		perf = fps / 10;
		if (!started)
			return;
		Score.decrease();
		now += EndlessMode.delta;
		ScreenShake.act();
		deltaPlusExplosion = EndlessMode.delta + explosions;
		
		if (lost && Gdx.input.justTouched() && now > timeLost + 0.8f) {
			init();
		}
	}

	private void alternate() {
		alternate = !alternate;
		if (++oneToFour > 4)
			oneToFour = 1;
	}

	private void stopActivated() {
		ship.draw(batch);
	}

	public static void cam() {
		cam.update();
		tmpCombined = cam.combined;
		if (tmpCombined != null) {
			batch.setProjectionMatrix(tmpCombined);
//			CSG.rayHandler.setCombinedMatrix(cam.combined);
		}
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
		if (triggerStop)
			bloom.setBloomIntesity(Rubico.profile.intensiteBloom + (timeStopBonus*2));
		else
			ScreenShake.bloomEffect();
		bloom.capture();
	}

	private static void mettrePause() {
		pause = true;
		Rubico.profilManager.persist();
	}
	
	private void drawFinalScore(SpriteBatch batch) {
		timeSinceLost += Gdx.graphics.getDeltaTime();
		
		if (timeSinceLost < 0.7f)
			return;
		if (Gdx.app.getVersion() != 0)
			Rubico.talkToTheWorld.showAds(true);
		
		batch.setColor(AssetMan.BLACK);
		// xp dispo

		displayText(batch, Score.xpDispo, Rubico.screenHeight * 0.76f);
		displayText(batch, Score.xpNeeded, Rubico.screenHeight * 0.70f);
		if (finished)							displayText(batch, "Congratulations !", Rubico.screenHeight * 0.64f);
		else									displayText(batch, Strings.DEAD, Rubico.screenHeight * 0.64f);
		displayText(batch, Score.strScore, Rubico.screenHeight * 0.58f);
		if (endless) {
			if (Rubico.profile.newHighscore)	displayText(batch, "New highscore !!!", Rubico.screenHeight * 0.52f);
			else								displayText(batch, Rubico.profile.highscoreString, Rubico.screenHeight * 0.52f);
		} else {
			displayText(batch, Rubico.profile.lvlString, Rubico.screenHeight * 0.52f);
		}
		batch.setColor(AssetMan.WHITE);
		
		Buttons.drawUpgradeAndTwitter(batch);
	}
	
	private static void displayText(SpriteBatch batch, CharSequence txt, float y) {
		Rubico.outlineFont.draw(batch,txt, ((cam.position.x-Rubico.halfWidth)) + ((Rubico.halfWidth - (Rubico.outlineFont.getBounds(txt).width)/2)), y);
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
		ui();
	}

	public static void effetBloom() {
		effetBloom = true;
		bloomOriginalIntensity = 50;
	}

	private void ui() {
		if (lost)
			Buttons.initAndDrawButtons(batch);
		Score.draw(batch, lost);
	}

	private void affichageNonPerdu() {
		stopActivated();
		Particles.draw(batch);
		ui();
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
	public static Camera getCam() {		return cam;	}

	public static void lost() {
		if (!invicibility && !lost) {
			timeLost = EndlessMode.now;
			Score.lost(true);
			timeSinceLost = 0;
			lost = true;
		}
	}

	public static boolean aPerdu() {
		return lost;
	}
	
	public static void reset() {
		Particles.clear();
		ship = new Paddle();
		triggerStop = false;
		pause = false;
	}

	public static void rotate(Vector2 dir, float angle) {
		if (alternate)
			dir.rotate(angle);
	}

	public static void finished() {
		Rubico.profile.upLvl();
		lost = true;
		finished = true;
		timeLost = EndlessMode.now;
		Score.lost(true);
		timeSinceLost = 0;
		Builder.nextSeed();
	}

}