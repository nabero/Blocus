package elements.generic.bonus;

import assets.SoundMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;

import elements.generic.Ball;
import elements.particles.Particles;
import elements.particles.individual.PrecalculatedParticles;
import elements.particles.individual.RubicoParticles;

public class BonusExplosive extends Bonus {
	
	private float angle = 0;
	public static final Pool<BonusExplosive> POOL = new Pool<BonusExplosive>() {
		protected BonusExplosive newObject() {			return new BonusExplosive();		}
	};
	
	public BonusExplosive() {
		super();
		light.setColor(PrecalculatedParticles.COLORS[4]);
	}

	@Override
	void drawMe(SpriteBatch batch) {
		angle++;
		Particles.RUBICO.add(RubicoParticles.POOL.obtain().init(x, y, 0, PrecalculatedParticles.blocPink, angle));
		Particles.RUBICO.add(RubicoParticles.POOL.obtain().init(x, y, 0, PrecalculatedParticles.blocPink, angle));
		Particles.RUBICO.add(RubicoParticles.POOL.obtain().init(x, y, 0, PrecalculatedParticles.blocPink, angle));
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
	@Override	float[] getColors() {		return PrecalculatedParticles.blocPink;		}

}
