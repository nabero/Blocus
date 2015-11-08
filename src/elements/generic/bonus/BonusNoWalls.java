package elements.generic.bonus;

import jeu.Rubico;
import jeu.Stats;
import jeu.mode.extensions.TemporaryText;
import assets.AssetMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Pool;

import elements.particles.individual.PrecalculatedParticles;
import elements.particles.individual.explosions.SparklesColorOverTime;

public class BonusNoWalls extends Bonus {
	
	private static final float LINE_WIDTH = Stats.u, LINE_HALF_WIDTH = LINE_WIDTH / 2, WIDTH_MINUS_HALF_LINE = WIDTH - LINE_HALF_WIDTH;
	public static final Pool<BonusNoWalls> POOL = new Pool<BonusNoWalls>() {
		protected BonusNoWalls newObject() {			return new BonusNoWalls();		}
	};
	private static final TemporaryText TEXT = new TemporaryText("No walls !");
	
	@Override
	void drawMe(SpriteBatch batch) {
		batch.setColor(PrecalculatedParticles.brightPink[5]);
		batch.draw(AssetMan.debris, x + LINE_WIDTH, y, LINE_WIDTH, WIDTH);
//		SparklesColorOverTime.add((x + LINE_HALF_WIDTH) - SparklesColorOverTime.HALF_HEIGHT, y + WIDTH, 90 + Rubico.R.nextFloat() * 180, PrecalculatedParticles.brightPink, 0);
//		SparklesColorOverTime.add((x + WIDTH_MINUS_HALF_LINE) - SparklesColorOverTime.HALF_HEIGHT, y + WIDTH, 90 + Rubico.R.nextFloat() * 180, PrecalculatedParticles.brightPink, 0);
	}

	@Override
	void taken() {
		Bonus.noWalls += 2;
		TemporaryText.add(TEXT);
	}
	
	@Override
	void remove() {
		super.remove();
		POOL.free(this);
	}

	@Override	float[] getColors() {		return PrecalculatedParticles.brightPink;	}


}
