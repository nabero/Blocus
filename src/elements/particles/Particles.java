package elements.particles;

import jeu.Rubico;
import jeu.Stats;
import jeu.mode.EndlessMode;
import assets.AssetMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import elements.particles.individual.Bump;
import elements.particles.individual.PaddleShotParticle;
import elements.particles.individual.RubicoParticles;
import elements.particles.individual.background.Star;
import elements.particles.individual.explosions.FlashLight;
import elements.particles.individual.explosions.SparklesColorOverTime;

public class Particles {
	 	
	public static final int MAX_BACKGROUND = 400;
	private static final Array<Star> STAR = new Array<Star>(false, 300);
	public static final Array<Bump> BUMPS = new Array<Bump>();
	public static final Array<SparklesColorOverTime> COLOR_OVER_TIME = new Array<SparklesColorOverTime>();
	public static final Array<RubicoParticles> RUBICO = new Array<RubicoParticles>(40);
	
	public static float alphaBg = 0.85f, mv = 0.001f;
	
	public static void initBackground() {
		Star.initBackground(STAR);
	}
	
	public static void background(SpriteBatch batch) {
		if (alphaBg >= .85f)		mv -= 0.001f * Gdx.graphics.getDeltaTime();
		else						mv += 0.001f * Gdx.graphics.getDeltaTime();
		if (alphaBg > 1)			alphaBg = 1;
		else if (alphaBg < 0.5f)	alphaBg = 0.5f;
		alphaBg += mv;
		batch.setColor(alphaBg, 1, 1, 0.5f);
		batch.draw(AssetMan.background, 0, 0, Rubico.halfWidth, Rubico.halfHeight, Rubico.screenWidth, Rubico.screenHeight, 1.5f, 2.3f, (alphaBg * 4));
		Star.act(batch, STAR);
	}

	public static void draw(SpriteBatch batch) {
		SparklesColorOverTime.act(COLOR_OVER_TIME, batch);
		RubicoParticles.act(RUBICO, batch);
		Bump.act(batch, BUMPS);
	}
	
	public static void clear() {
		FlashLight.clear();
		RubicoParticles.clear(RUBICO);
		SparklesColorOverTime.clear(COLOR_OVER_TIME);
		Bump.clear(BUMPS);
	}

	public static void sparkles(float x, float y, float sparkleAngle, float[] colors) {
		SparklesColorOverTime.add(x, y, (float) (sparkleAngle + Rubico.R.nextGaussian() * 36), colors, Stats.U50 + (Stats.U90 * Rubico.R.nextFloat()), 1);
	}
	
}
