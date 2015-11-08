package elements.particles.individual;

import jeu.Rubico;
import jeu.Stats;
import jeu.mode.EndlessMode;
import assets.AssetMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

public class PaddleParticles {
	
	private float x, y, width, height;
	private float[] colors;
	private int index;
	public static final Pool<PaddleParticles> POOL = new Pool<PaddleParticles>() {
		protected PaddleParticles newObject() {			return new PaddleParticles();		}
	};
	
	public PaddleParticles init(float x, float y, float[] colors, float width, float height) {
		this.colors = colors;
		index = Rubico.R.nextInt(colors.length);
		this.width = width;// * Rubico.R.nextFloat();
		this.height = height;
		this.x = x;
		this.y = y;
		return this;
	}

//	static int max;
	public static void act(SpriteBatch batch, Array<PaddleParticles> array) {
//		max = Math.max(max, array.size);
//		System.out.println("bump : " + max);
		if (EndlessMode.alternate) {
			for (PaddleParticles b : array) {
				batch.setColor(b.colors[b.index]);
				batch.draw(AssetMan.partBloc, b.x, b.y, b.width, b.height);
				if (++b.index >= b.colors.length) {
					POOL.free(b);
					array.removeValue(b, true);
				}
			}
		} else {
			for (PaddleParticles b : array) {
				batch.setColor(b.colors[b.index]);
				batch.draw(AssetMan.partBloc, b.x, b.y, b.width, b.height);
				b.y -= EndlessMode.delta15;
				b.x += (b.width - ((b.width * 0.95f))) / 2;
				b.width *= 0.95f;
				b.height *= 0.95f;
				if (++b.index >= b.colors.length) {
					POOL.free(b);
					array.removeValue(b, true);
				}
			}
		}
		batch.setColor(AssetMan.WHITE);
	}

	public static void clear(Array<PaddleParticles> array) {
		POOL.freeAll(array);
		array.clear();
	}
	

}
