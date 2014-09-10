package elements.generic.bonus;

import jeu.Stats;
import assets.SoundMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;

import elements.generic.Ball;
import elements.particles.Particles;
import elements.particles.individual.PrecalculatedParticles;
import elements.particles.individual.RubicoParticles;

public class BonusBall extends Bonus {
	
	private static final float WIDTH = Stats.UU, HALF_WIDTH = WIDTH / 2;
	public static final Pool<BonusBall> POOL = new Pool<BonusBall>() {
		protected BonusBall newObject() {			return new BonusBall();		}
	};
	
	@Override
	void drawMe(SpriteBatch batch) {
		Particles.RUBICO.add(RubicoParticles.POOL.obtain().init(x, y, 0, PrecalculatedParticles.blocBlue));
		Particles.RUBICO.add(RubicoParticles.POOL.obtain().init(x, y, 0, PrecalculatedParticles.blocBlue));
		Particles.RUBICO.add(RubicoParticles.POOL.obtain().init(x, y, 0, PrecalculatedParticles.blocBlue));
	}

	@Override
	void taken() {
		SoundMan.playBruitage(SoundMan.powerup1);
		Ball.addBall(x, y, 1, 1.2f);
	}
	
	@Override
	void remove() {
		super.remove();
		POOL.free(this);
	}

	@Override	float getWidth() {			return WIDTH;							}
	@Override	float getHeight() {			return WIDTH;							}
	@Override	float[] getColors() {		return PrecalculatedParticles.blocBlue;	}
	@Override	float getHalfWidth() {		return HALF_WIDTH;						}
	@Override	float getHalfHeight() {		return HALF_WIDTH;						}


}
