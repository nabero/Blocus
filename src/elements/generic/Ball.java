package elements.generic;

import jeu.Rubico;
import jeu.Stats;
import jeu.Strings;
import jeu.mode.EndlessMode;
import jeu.mode.extensions.ScreenShake;
import jeu.mode.extensions.TemporaryText;
import assets.SoundMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import elements.generic.bonus.Bonus;
import elements.particles.Particles;
import elements.particles.individual.Bump;
import elements.particles.individual.PrecalculatedParticles;
import elements.particles.individual.RubicoParticles;

public class Ball {
	
	public static final Array<Ball> BALLS = new Array<Ball>();
	public final Vector2 dir = new Vector2(), oldDir = new Vector2();
	public float width, angle;
	public float oldX, oldY, x, y;
	public int lvl = 1, column;
	public static final int LVL_MAX = 4;
	public static final float SPEED = Stats.U * 29, TOLERANCE = Rubico.screenWidth * 0.01f, MAGNET = Rubico.screenWidth * 0.2f;
	private float[] colors;
	public static int CHRONO_EXPLOSIVE = 0;
	public static final TemporaryText lvlUp = new TemporaryText(" Column clear  \nBall level up !"), newBall = new TemporaryText("New ball !"), explosiveBall = new TemporaryText("Explosive balls !");
	public static final Pool<Ball> POOL = new Pool<Ball>() {
		protected Ball newObject() {			return new Ball();		}
	};
	
	public static void addBall(float x, float y, float dirX, float dirY) {
		Ball b = POOL.obtain();
		b.updatePos(x, y);
		b.dir.set(dirX, dirY).nor().scl(SPEED);
		b.oldDir.scl(b.dir);
		b.lvl = 1;
		b.width = Stats.UU;
		if (BALLS.size > 0)		TemporaryText.add(newBall);
		b.colors = PrecalculatedParticles.getColor(b.lvl);
		BALLS.add(b);
		if (BALLS.size >= 4)	Rubico.talkToTheWorld.unlockAchievementGPGS(Strings.ACH_4BALLS);
		if (BALLS.size >= 7)	Rubico.talkToTheWorld.unlockAchievementGPGS(Strings.ACH_7BALLS);
	}

	protected void updatePos(float d, float e) {
		oldX = this.x;
		oldY = this.y;
		this.x = d;
		this.y = e;
	}
	
	public static void draw(SpriteBatch batch) {
		for (Ball b : BALLS)
			b.act();
	}

	public void act() {
		if (Paddle.magnetActivated && dir.y < 0) {
//			updatePos(x + ((Paddle.xCenter - x) * EndlessMode.delta2), y + dir.y * EndlessMode.delta);
			if (Paddle.xCenter > x) {
				dir.rotate(EndlessMode.delta25 * 3);
			} else {
				dir.rotate(-EndlessMode.delta25 * 3);
			}
		} else {
			updatePos(x + dir.x * EndlessMode.delta, y + dir.y * EndlessMode.delta);
		}
		
		if (!Gdx.input.isTouched())
			dir.rotate(EndlessMode.delta15 * 3);
		
		width *= 0.95f;
		
		if (CHRONO_EXPLOSIVE > 0) {
			angle++;
			angle++;
			
			for (int i = -4; i < EndlessMode.fps / 4; i++)
				Particles.RUBICO.add(RubicoParticles.POOL.obtain().initFurious(this));
		} else 
			for (int i = -4; i < EndlessMode.fps / 4; i++)
				Particles.RUBICO.add(RubicoParticles.POOL.obtain().init(this));
		
		collisions(this);
	}

	private static void collisions(Ball b) {
		if (b.x + Stats.U > Rubico.screenWidth) {
			if (Bonus.noWalls > 0) {
				b.x = 1;
				Bump.addLeftBall(b.y, b.colors);
			} else
				b.borderHit(-Math.abs(b.dir.x), Rubico.screenWidth - Stats.U, 180);
			Bump.addRightBall(b.y, b.colors);
		} else if (b.x < 0) {
			if (Bonus.noWalls > 0) {
				b.x = (Rubico.screenWidth - Stats.U) - 1;
				Bump.addRightBall(b.y, b.colors);
			} else
				b.borderHit(Math.abs(b.dir.x), 0, 0);
			Bump.addLeftBall(b.y, b.colors);
		}
		
		if (b.y + Stats.U > Rubico.screenHeight) {
			Bump.addUpBall(b.x, b.colors);
			b.hit(270, 60);
			b.dir.y = -Math.abs(b.dir.y);
			b.y = Rubico.screenHeight - Stats.U;
		} else if (b.y < Paddle.top) {
			if (b.y > Paddle.y && b.x > Paddle.x - TOLERANCE && b.x < Paddle.right + TOLERANCE)
				Paddle.hit(b);
			else if (b.y < 0)
				b.remove();
		}
	}

	protected void borderHit(float dirX, float x, float angle) {
		dir.x = dirX;
		this.x = x;
		hit(angle, 60);
	}

	private void remove() {
		BALLS.removeValue(this, true);
		POOL.free(this);
		if (BALLS.size == 0)
			EndlessMode.lost();
	}

	public void hit(float sparkleAngle, float shake) {
		Particles.sparkles(x, y, sparkleAngle, colors);
		SoundMan.playBruitage(SoundMan.xp);
		width = Stats.UU;
		ScreenShake.screenShake(shake);
	}

	public static void clear() {
		CHRONO_EXPLOSIVE = 0;
		POOL.freeAll(BALLS);
		BALLS.clear();
	}

	public float[] getColors() {
		return colors;
	}

	public void lvlUp() {
		if (lvl >= PrecalculatedParticles.BRIGHT_YELLOW)
			return;
		lvl++;
		if (lvl == PrecalculatedParticles.BRIGHT_YELLOW)
			Rubico.talkToTheWorld.unlockAchievementGPGS(Strings.ACH_BALL_LVL_MAX);
		colors = PrecalculatedParticles.getColor(lvl);
		TemporaryText.add(lvlUp);
		dir.scl(1.075f);
	}

	public static void explosiveBall() {
		CHRONO_EXPLOSIVE += 2;
		TemporaryText.add(explosiveBall);
	}

	public static void lvlUpOneBall() {
		for (Ball b : BALLS)
			if (b.lvl < PrecalculatedParticles.BRIGHT_YELLOW) {
				b.lvlUp();
				return;
			}
	}

	public void updateDir() {
//		if (EndlessMode.now > 2) {
//			tmp = dir.len();
//			dir.set(oldX - x, oldY - y).scl(tmp);
//		}
	}

	public static void saveDir() {
		for (Ball b : BALLS)
			b.oldDir.set(b.dir);
	}

}
