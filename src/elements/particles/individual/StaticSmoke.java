package elements.particles.individual;

import jeu.Rubico;
import assets.AssetMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

public class StaticSmoke implements Poolable {
	
	private float x, y, angle, width, height, angleSpeead, widthSpeed, heightSpeed, alpha;
	public static final Pool<StaticSmoke> POOL = new Pool<StaticSmoke>() {		protected StaticSmoke newObject() {			return new StaticSmoke();		}	};
	private static final Array<StaticSmoke> SMOKE = new Array<StaticSmoke>();
	
	@Override
	public void reset() {	}
	
	public static void add(float x, float y, float width) {
		SMOKE.add(POOL.obtain().init(x, y, width));
	}
	
	public static void draw(SpriteBatch batch) {
		for (StaticSmoke s : SMOKE) {
			batch.setColor(1, 1, 1, s.alpha);
			s.angle += s.angleSpeead * Gdx.graphics.getDeltaTime();
			s.width += s.widthSpeed * Gdx.graphics.getDeltaTime();
			s.height += s.heightSpeed * Gdx.graphics.getDeltaTime();
			
			s.x -= s.widthSpeed * (Gdx.graphics.getDeltaTime() / 2);
			s.y -= s.heightSpeed * (Gdx.graphics.getDeltaTime() / 2);
			s.alpha *= 0.965f;
			batch.draw(AssetMan.smoke, s.x, s.y, s.width / 2, s.height / 2, s.width, s.height, 1f, 1f, s.angle);
			if (s.alpha < 0.03f) {
				SMOKE.removeValue(s, true);
				POOL.free(s);
			}
		}
	}

	public StaticSmoke init(float x, float y, float width) {
		height = width + (Rubico.R.nextFloat() * width);
		this.y = y - height / 2;
		this.width = width + (Rubico.R.nextFloat() * width);
		this.x = x - this.width / 2;
		angle = Rubico.R.nextFloat() * 360;
		angleSpeead = (float) Rubico.R.nextGaussian() * 3;
		widthSpeed = (float) Math.abs(Rubico.R.nextGaussian()) * 5;
		heightSpeed = (float) Math.abs(Rubico.R.nextGaussian()) * 5;
		alpha = 0.2f + (Rubico.R.nextFloat() / 20f);
		return this;
	}

	public static void clear() {
		POOL.freeAll(SMOKE);
		SMOKE.clear();
	}

}
