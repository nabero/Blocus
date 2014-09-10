package elements.generic.bonus;

import jeu.Rubico;
import jeu.Stats;
import assets.AssetMan;
import assets.SoundMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;

import elements.generic.Paddle;
import elements.particles.individual.PrecalculatedParticles;
import elements.particles.individual.explosions.SparklesColorOverTime;

public class BonusMagnet extends Bonus {
	
	private static final float WIDTH = Stats.UU, HALF_WIDTH = WIDTH / 2, LINE_WIDTH = Stats.u, WIDTH_MINUS_LINE = WIDTH - LINE_WIDTH, LINE_HALF_WIDTH = LINE_WIDTH / 2, WIDTH_MINUS_HALF_LINE = WIDTH - LINE_HALF_WIDTH;
	public static final Pool<BonusMagnet> POOL = new Pool<BonusMagnet>() {
		protected BonusMagnet newObject() {			return new BonusMagnet();		}
	};
	
	@Override
	void drawMe(SpriteBatch batch) {
		batch.setColor(PrecalculatedParticles.brightYellow[5]);
		batch.draw(AssetMan.debris, x, y, WIDTH, LINE_WIDTH);
		batch.draw(AssetMan.debris, x, y, LINE_WIDTH, WIDTH);
		batch.draw(AssetMan.debris, x + WIDTH_MINUS_LINE, y, LINE_WIDTH, WIDTH);
		SparklesColorOverTime.add((x + LINE_HALF_WIDTH) - SparklesColorOverTime.HALF_HEIGHT, y + WIDTH, 90 + Rubico.R.nextFloat() * 180, PrecalculatedParticles.brightYellow, 0);
		SparklesColorOverTime.add((x + WIDTH_MINUS_HALF_LINE) - SparklesColorOverTime.HALF_HEIGHT, y + WIDTH, 90 + Rubico.R.nextFloat() * 180, PrecalculatedParticles.brightYellow, 0);
	}

	@Override
	void taken() {
		SoundMan.playBruitage(SoundMan.powerup1);
		Paddle.magnet();
	}
	
	@Override
	void remove() {
		super.remove();
		POOL.free(this);
	}

	@Override	float getWidth() {			return WIDTH;							}
	@Override	float getHeight() {			return WIDTH;							}
	@Override	float[] getColors() {		return PrecalculatedParticles.brightYellow;	}
	@Override	float getHalfWidth() {		return HALF_WIDTH;						}
	@Override	float getHalfHeight() {		return HALF_WIDTH;						}


}
