package elements.generic.bonus;

import jeu.Rubico;
import jeu.mode.EndlessMode;
import assets.AssetMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import elements.generic.Ball;
import elements.generic.Paddle;
import elements.generic.blocs.Bloc;

public abstract class Bonus {
	
	protected float x, y, place, balance;
	public static Array<Bonus> BONUSES = new Array<Bonus>();
	public static float noWalls;
	private static final float SPEED = Rubico.screenHeight * 0.1f, Y_COLLISION_TOP = Paddle.y + Paddle.HEIGHT, Y_COLLISION_BOTTOM = Y_COLLISION_TOP - Paddle.HEIGHT;
	
	public static void draw(SpriteBatch batch) {
		if (noWalls >= 0)
			noWalls -= EndlessMode.delta;
		for (Bonus b : BONUSES) {
			b.color();
			batch.setColor(b.getColors()[(int) b.place]);
			batch.draw(AssetMan.debris, b.x - b.getHalfWidth(), b.y - b.getHalfHeight(), b.getWidth(), b.getHeight());
			b.drawMe(batch);
			b.y -= SPEED * EndlessMode.delta;
			if (b.y < Y_COLLISION_TOP && b.y > Y_COLLISION_BOTTOM && b.x + b.getWidth() > Paddle.x && b.x < Paddle.x + Paddle.WIDTH) {
				b.taken();
				b.remove();
			} else if (b.y + b.getHeight() < 0)
				b.remove();
		}
	}
	
	private void color() {
		place += balance;
		if (place > getColors().length/2)	balance -= 0.01f;
		else								balance += 0.01f;
		
		if (place < 0)						place = 0;
		else if (place >= 30)				place = 30;
	}

	abstract float[] getColors();

	public Bonus init(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}
	
	abstract float getWidth();
	abstract float getHeight();
	abstract float getHalfWidth();
	abstract float getHalfHeight();
	abstract void drawMe(SpriteBatch batch);
	abstract void taken();
	
	void remove() {
		BONUSES.removeValue(this, true);
	}

	public static void add(Bonus init) {
		BONUSES.add(init);
	}

	public static void clear() {
		for (Bonus b : BONUSES)
			b.remove();
		BONUSES.clear();
		noWalls = 0;
	}

	public static void blocTaken(Bloc b) {
		if (Ball.BALLS.size == 0)
			return;
		if (rollForBonus(15))
			Bonus.add(BonusBall.POOL.obtain().init(b.x + Bloc.HALF_WIDTH, b.y));
		if (rollForBonus(15))
			Bonus.add(BonusNoWalls.POOL.obtain().init(b.x + Bloc.HALF_WIDTH, b.y));
//		if (rollForBonus(1))
//			Bonus.add(BonusMagnet.POOL.obtain().init(x + HALF_WIDTH, y));
		if (rollForBonus(30))
			Bonus.add(BonusExplosive.POOL.obtain().init(b.x + Bloc.HALF_WIDTH, b.y));
	}

	protected static boolean rollForBonus(int chance) {
		if (Bloc.first) {
			Bloc.first = false;
			return true;
		}
		return Rubico.R.nextInt((int) (1 + (chance * (Ball.BALLS.size * 2.5f + Bonus.BONUSES.size)))) == 0;		
	}

}
