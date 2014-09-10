package elements.generic;

import jeu.Rubico;
import jeu.mode.EndlessMode;
import assets.AssetMan;
import assets.SoundMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import elements.generic.blocs.Bloc;
import elements.generic.blocs.Column;
import elements.particles.Particles;
import elements.particles.individual.PaddleShotParticle;

public class PaddleShot {
	
	public static final Pool<PaddleShot> POOL = new Pool<PaddleShot>() {
		protected PaddleShot newObject() {			return new PaddleShot();		}
	};
	public static final Array<PaddleShot> SHOTS = new Array<PaddleShot>();
	public static final float WIDTH = Paddle.TIER_WIDTH, HEIGHT = Paddle.HEIGHT, SPEED = Rubico.screenHeight * 0.55f, HALF_WIDTH = WIDTH / 2;
	private float x, y;
	private static final Rectangle R = new Rectangle(0,0, WIDTH, HEIGHT);
	private static final Vector2 DIR = new Vector2(0, SPEED);
	private int hit;
	
	
	public static void add(float x) {
		final PaddleShot shot = POOL.obtain();
		shot.x = x;
		shot.y = Paddle.y;
		shot.hit = 0;
		SHOTS.add(shot);
		Rubico.tmpPos.set(x + HALF_WIDTH, shot.y);
		Particles.sparkles(Rubico.tmpPos, 90, Paddle.colors);
		Paddle.y -= Paddle.HEIGHT;
		SoundMan.playBruitage(SoundMan.shotRocket);
	}
	
	public static void act(SpriteBatch batch) {
		for (PaddleShot shot : SHOTS) {
			shot.y += EndlessMode.delta * SPEED;
			PaddleShotParticle.add(shot.y, shot.x);
			if (shot.y > Rubico.screenHeight)	shot.remove();
			else								shot.testCollision();
		}
		batch.setColor(AssetMan.WHITE);
	}

	private void testCollision() {
		R.setPosition(x, y);
		for (int i = 0; i < Column.COLUMNS.size; i++) {
			Column c = Column.COLUMNS.get(i);
			if (c.overlaps(x, WIDTH)) {
				for (Bloc B : c.getBlocs()) {
					if (B.overlaps(R)) {
						B.removeHp(DIR, Paddle.lvl);
						if (B.hp <= 0)
							c.removeBloc(B, null);
						if (hit >= Paddle.lvl) {
							remove();
							return;
						}
						hit++;
					}
				}
			}
			
		}
	}

	private void remove() {
		POOL.free(this);
		SHOTS.removeValue(this, true);
	}

}
