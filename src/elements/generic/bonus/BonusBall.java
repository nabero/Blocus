package elements.generic.bonus;

import assets.SoundMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;

import elements.generic.Ball;
import elements.particles.Particles;
import elements.particles.individual.PrecalculatedParticles;
import elements.particles.individual.RubicoParticles;

public class BonusBall extends Bonus {
	
	public static final Pool<BonusBall> POOL = new Pool<BonusBall>() {
		protected BonusBall newObject() {			return new BonusBall();		}
	};
	
	public BonusBall() {
		super();
		light.setColor(PrecalculatedParticles.COLORS[1]);
	}
	
	@Override
	void drawMe(SpriteBatch batch) {
		Particles.RUBICO.add(RubicoParticles.POOL.obtain().init(x, y, 0, PrecalculatedParticles.blocFire));
		Particles.RUBICO.add(RubicoParticles.POOL.obtain().init(x, y, 0, PrecalculatedParticles.blocFire));
//		batch.setColor(PrecalculatedParticles.blocFire[0]);
//		batch.draw(Ball.TEXTURE, x - HALF_WIDTH, y - HALF_WIDTH, HALF_WIDTH, HALF_WIDTH, WIDTH, WIDTH, 1f, 1f, 270);
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

	@Override	float[] getColors() {		return PrecalculatedParticles.blocFire;	}

}
