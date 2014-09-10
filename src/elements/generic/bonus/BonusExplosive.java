package elements.generic.bonus;

import jeu.Stats;
import assets.SoundMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;

import elements.generic.Ball;
import elements.particles.Particles;
import elements.particles.individual.PrecalculatedParticles;
import elements.particles.individual.RubicoParticles;

public class BonusExplosive extends Bonus {
	
	private static final float WIDTH = Stats.UU, HALF_WIDTH = WIDTH / 2;
	private float angle = 0;
	public static final Pool<BonusExplosive> POOL = new Pool<BonusExplosive>() {
		protected BonusExplosive newObject() {			return new BonusExplosive();		}
	};

	@Override
	void drawMe(SpriteBatch batch) {
		angle++;
		Particles.RUBICO.add(RubicoParticles.POOL.obtain().init(x, y, 0, PrecalculatedParticles.blocFire, angle));
		Particles.RUBICO.add(RubicoParticles.POOL.obtain().init(x, y, 0, PrecalculatedParticles.blocFire, angle));
		Particles.RUBICO.add(RubicoParticles.POOL.obtain().init(x, y, 0, PrecalculatedParticles.blocFire, angle));
	}

	@Override
	void taken() {
		SoundMan.playBruitage(SoundMan.powerup2);
		Ball.explosiveBall();
	}
	
	@Override
	void remove() {
		super.remove();
		POOL.free(this);
	}
	@Override	float[] getColors() {		return PrecalculatedParticles.blocFire;		}
	@Override	float getHalfWidth() {		return HALF_WIDTH;							}
	@Override	float getHalfHeight() {		return HALF_WIDTH;							}
	@Override	float getWidth() {			return WIDTH;								}
	@Override	float getHeight() {			return WIDTH;								}

}
