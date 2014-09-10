package elements.particles.individual;

import assets.AssetMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import elements.particles.individual.explosions.SparklesColorOverTime;

public class Ghost {

	private float x, y, angle, color;
	private int ttl;
	public static final float HEIGHT = SparklesColorOverTime.HEIGHT, HALF_HEIGHT = HEIGHT / 2, WIDTH = SparklesColorOverTime.WIDTH / 2, HALF_WIDTH = WIDTH / 2;
	public static final Pool<Ghost> POOL = new Pool<Ghost>() {
		protected Ghost newObject() {			return new Ghost();		}
	};
	
	public Ghost init(float x, float y, float color, float angle) {
		this.x = x;
		this.y = y;
		this.angle = angle;
		this.color = color;
		this.ttl = 0;
		return this;
	}

	public static void act(SpriteBatch batch, Array<Ghost> array) {
		for (Ghost b : array) {
			batch.setColor(b.color);
			batch.draw(AssetMan.dust, b.x, b.y, HALF_HEIGHT, HALF_WIDTH, HEIGHT, WIDTH, 1f, 1f, b.angle);
			
			if (++b.ttl >= 7) {
				POOL.free(b);
				array.removeValue(b, true);
			}
		}
		batch.setColor(AssetMan.WHITE);
	}

	public static void clear(Array<Ghost> array) {
		POOL.freeAll(array);
		array.clear();
	}
}
