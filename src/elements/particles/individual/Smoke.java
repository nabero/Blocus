package elements.particles.individual;

import jeu.Rubico;
import jeu.levels.Builder;
import jeu.mode.EndlessMode;
import assets.AssetMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pool.Poolable;

public class Smoke implements Poolable {
	
	private float x, y, angle, width, height, angleSpeead, widthSpeed, heightSpeed;
	public static final Pool<Smoke> POOL = new Pool<Smoke>() {		protected Smoke newObject() {			return new Smoke();		}	};
	private static final Array<Smoke> SMOKE = new Array<Smoke>();
	private static final float SPEED = 6;
	
	@Override
	public void reset() {	}
	
	public static void add(float x, float y, float width) {
		SMOKE.add(POOL.obtain().init(x, y, width));
	}
	
	static float modulatedSpeed; 
	public static void draw(SpriteBatch batch) {
		batch.setColor(1, 1, 1, 0.1f);
		modulatedSpeed = (EndlessMode.delta + Gdx.graphics.getDeltaTime()) * SPEED; 
		for (Smoke s : SMOKE) {
			s.y -= modulatedSpeed;
			s.angle += s.angleSpeead * Gdx.graphics.getDeltaTime();
			s.width += s.widthSpeed * Gdx.graphics.getDeltaTime();
			s.height += s.heightSpeed * Gdx.graphics.getDeltaTime();
			
			s.x -= s.widthSpeed * (Gdx.graphics.getDeltaTime() / 2);
			s.y -= s.heightSpeed * (Gdx.graphics.getDeltaTime() / 2);
			
			batch.draw(AssetMan.smoke, s.x, s.y, s.width / 2, s.height / 2, s.width, s.height, 1f, 1f, s.angle);
			if (s.y + s.height < 0) {
				SMOKE.removeValue(s, true);
				POOL.free(s);
			}
		}
	}

	public Smoke init(float x, float y, float width) {
		this.x = x;
		height = width + (Rubico.R.nextFloat() * width);
		this.y = y;
		this.width = width + (Rubico.R.nextFloat() * width);
		angle = Rubico.R.nextFloat() * 360;
		angleSpeead = (float) Rubico.R.nextGaussian() * 3;
		widthSpeed = (float) Math.abs(Rubico.R.nextGaussian()) * 3;
		heightSpeed = (float) Math.abs(Rubico.R.nextGaussian()) * 3;
		return this;
	}

	public static void clear() {
		POOL.freeAll(SMOKE);
		SMOKE.clear();
	}

}
