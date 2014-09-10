package elements.generic.blocs;

import jeu.Rubico;
import jeu.Stats;
import jeu.levels.Builder;
import jeu.mode.EndlessMode;
import jeu.mode.extensions.Score;
import jeu.mode.extensions.ScreenShake;
import menu.screens.Menu;
import assets.AssetMan;
import assets.SoundMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import elements.generic.Ball;
import elements.generic.bonus.Bonus;
import elements.generic.bonus.BonusBall;
import elements.generic.bonus.BonusExplosive;
import elements.generic.bonus.BonusMagnet;
import elements.particles.Particles;
import elements.particles.individual.PrecalculatedParticles;
import elements.particles.individual.explosions.ExplodingBloc;
import elements.particles.individual.explosions.SparklesColorOverTime;
import elements.particles.individual.explosions.SparklesPopBloc;

public class Bloc {
	
	public static final Array<ExplodingBloc> EXPLOSIONS = new Array<ExplodingBloc>();
	public static final float WIDTH = Rubico.screenWidth * 0.08f, HEIGHT = Rubico.screenHeight * 0.03f, HALF_WIDTH = WIDTH / 2, HALF_HEIGHT = HEIGHT / 2, TIER_WIDTH = WIDTH / 3, TIER_HEIGHT = HEIGHT / 3, TIER_WIDTH_MUL2 = TIER_WIDTH * 2, TIER_HEIGHT_MUL2 = TIER_HEIGHT * 2, STEP = Stats.u / 16, QUART_WIDTH = WIDTH / 4, QUART_HEIGHT = HEIGHT / 4, WIDTH_DIV8 = WIDTH / 8, HEIGHT_DIV8 = HEIGHT / 8;
	private static boolean INC = true;
	private static int PLACE_CPT = 0;
	static final Rectangle R = new Rectangle(0, 0, WIDTH, HEIGHT);
	public static boolean first = true;
	public int hp, row, min, max;
	public float balance, place, x, y, display;
	public float[] colors = PrecalculatedParticles.blocBlue;
	private MiniBloc[] particles = {
			new MiniBloc(), new MiniBloc(), new MiniBloc(), new MiniBloc(), new MiniBloc(), new MiniBloc(), new MiniBloc(), new MiniBloc(),  
			new MiniBloc(), new MiniBloc(), new MiniBloc(), new MiniBloc(), new MiniBloc(), new MiniBloc(), new MiniBloc(), new MiniBloc()  
	};
//	public final Array<SparklesPopBloc> sparkles = new Array<SparklesPopBloc>();
	private static final Pool<Bloc> POOL = new Pool<Bloc>() {
		protected Bloc newObject() {			return new Bloc();		}
	};
	
	public static Bloc addBlock(float ancreX, int row, int lvl) {
		final Bloc b = POOL.obtain();
		b.x = ancreX;
		b.y = Builder.MIN_Y - (HEIGHT * row);
		b.row = row;
		b.init(lvl);
		b.initPlace();
		b.display = 0;
		tmpInt = (int) b.place;
		for (int x = 0; x < 4; x++) {
			setMiniBloc(b, x, 0, b.colors[tmpInt]);
			setMiniBloc(b, x, 3, b.colors[tmpInt]);
		}
		
		setMiniBloc(b, 0, 1, b.colors[tmpInt]);
		setMiniBloc(b, 0, 2, b.colors[tmpInt]);
		setMiniBloc(b, 3, 1, b.colors[tmpInt]);
		setMiniBloc(b, 3, 2, b.colors[tmpInt]);
		
		setMiniBloc(b, 1, 1, b.colors[(b.colors.length) - tmpInt]);
		setMiniBloc(b, 1, 2, b.colors[(b.colors.length) - tmpInt]);
		setMiniBloc(b, 2, 1, b.colors[(b.colors.length) - tmpInt]);
		setMiniBloc(b, 2, 2, b.colors[(b.colors.length) - tmpInt]);
		
		if (b.y < -HEIGHT)
			EndlessMode.lost();
		return b;
	}

	protected static void setMiniBloc(Bloc b, int x, int y, float color) {
		b.particles[(x * 4) + y].init(b.x + (x * QUART_WIDTH), b.y + (y * QUART_HEIGHT), color);
	}

	public void initPlace() {
		if (INC)		PLACE_CPT++;
		else			PLACE_CPT--;
		min = 1 + PLACE_CPT % 6;
		max = PrecalculatedParticles.LENGTH_DIV4_MUL3;
		if (PLACE_CPT >= max) {
			PLACE_CPT = max;
			INC = false;
		} else if (PLACE_CPT < min) {
			PLACE_CPT = min;
			INC = true;
		}
		balance = 0.4f;
		place = 25;
	}
	
	private void init(int lvl) {
		hp = lvl;
		colors = PrecalculatedParticles.getColor(lvl);
	}

	private static float tmp, tmp2;
	private static int tmpInt;
	public void display(SpriteBatch batch) {
		display += EndlessMode.delta;
		if (display < 2.5f) {
			for (MiniBloc b : particles)
				b.draw(batch);
//			if (sparkles.size < 16) 
//				sparkles.add(SparklesPopBloc.POOL.obtain().init(x + HALF_WIDTH, y + HALF_HEIGHT, colors));
//			SparklesPopBloc.act(sparkles, batch);
			return;
		} 
		
		place += balance;
		if (place > PrecalculatedParticles.HALF_LENGTH)		balance -= 0.01f;
		else												balance += 0.01f;
		if (place < min)									place = min;
		else if (place >= max)								place = max;
		
		tmpInt = (int) place;
		batch.setColor(colors[tmpInt]);
		batch.draw(AssetMan.debris, x, y, WIDTH, HEIGHT);
		batch.setColor(colors[(colors.length) - tmpInt]);
		tmp = (place - PrecalculatedParticles.HALF_LENGTH) * STEP;
		tmp2 = tmp * 2;
		batch.draw(AssetMan.debris, (x + TIER_WIDTH) - tmp, (y + TIER_HEIGHT) - tmp, TIER_WIDTH + tmp2, TIER_HEIGHT + tmp2);
	}

	public boolean collision(Ball ball) {
		if (display < 1.5f)
			return false;
		
		Column.blip = true;
		Column.ROW = row;
		removeHp(ball.dir, ball.lvl);
		
//		if (ball.oldX < x) {
//			ball.dir.x = -Math.abs(ball.dir.x);
//			ball.hit(180, 15);
//			ball.x = x;
//		} else if (ball.oldX > x + WIDTH) {
//			ball.dir.x = Math.abs(ball.dir.x);
//			ball.hit(0, 15);
//			ball.x = x + WIDTH;
//		} else if (ball.oldY < y) {
//			ball.dir.y = -Math.abs(ball.dir.y);
//			ball.hit(270, 15);
//			ball.y = y;
//		} else {
//			ball.dir.y = Math.abs(ball.dir.y);
//			ball.hit(90, 15);
////			ball.y = y + HEIGHT; 
//		}
		
		final float angle = ball.dir.angle();
		
		if (angle < 90) {
//			float height = ball.y - y;
//			float width = ball.x - x;
			// from right to up : can collide with left and / or down
			// collide with left
			if (ball.oldX < x) {
				ball.dir.x = -Math.abs(ball.dir.x);
				ball.hit(180, 15);
				ball.x = x;
			}
			// collide with down
			if (ball.oldY < y) {
				ball.dir.y = -Math.abs(ball.dir.y);
				ball.hit(270, 15);
				ball.y = y;
			}
		} else if (angle < 180) {
			// from up to left : can collide with right and / or down
			// collide with right
			if (ball.oldX > x + WIDTH) {
				ball.dir.x = Math.abs(ball.dir.x);
				ball.hit(0, 15);
				ball.x = x + WIDTH;
			}
			// collide with down
			if (ball.oldY < y) {
				ball.dir.y = -Math.abs(ball.dir.y);
				ball.hit(270, 15);
				ball.y = y;
			}
		} else if (angle < 270) {
			// from down to left : can collide with right and / or up
			// collide with right
			if (ball.oldX > x + WIDTH) {
				ball.dir.x = Math.abs(ball.dir.x);
				ball.hit(0, 15);
				ball.x = x + WIDTH;
			}
			// collide with up
			if (ball.oldY > y + HEIGHT) {
				ball.dir.y = Math.abs(ball.dir.y);
				ball.hit(90, 15);
				ball.y = y + HEIGHT; 
			}
		} else {
			// from down to right : can collide with left and / or up
			// collide with left
			if (ball.oldX < x) {
				ball.dir.x = -Math.abs(ball.dir.x);
				ball.hit(180, 15);
				ball.x = x;
			}
			// collide with up
			if (ball.oldY > y + HEIGHT) {
				ball.dir.y = Math.abs(ball.dir.y);
				ball.hit(90, 15);
				ball.y = y + HEIGHT; 
			}
		}
		
		return hp <= 0;
	}

	public void removeHp(Vector2 dir, int hp) {
		Score.up(hp);
		SoundMan.explosion(this.hp);
		this.hp -= hp;
		if (this.hp <= 0) {
			dead(dir);
			Score.up(this.hp);
		} else
			colors = PrecalculatedParticles.getColor(this.hp);
	}

	private void dead(Vector2 dir) {
		SparklesColorOverTime.add(Particles.COLOR_OVER_TIME, this, colors, dir);
		ExplodingBloc.explode(this, EXPLOSIONS, dir);
		ScreenShake.screenShake(90);
		Bonus.blocTaken(this);
	}

	public float getRandomYInside() {					return y + Rubico.R.nextFloat() * HEIGHT;																}
	public float getRandomXInside() {					return x + Rubico.R.nextFloat() * WIDTH;																}
	public static void freeAll(Array<Bloc> blocs) {		POOL.freeAll(blocs);																					}

	public boolean overlaps(Rectangle r) {
		R.setPosition(x, y);
		return r.overlaps(R);
	}

	public static void clear() {
		first = true;
		
		for (ExplodingBloc b : EXPLOSIONS)
			ExplodingBloc.POOL.free(b);
		EXPLOSIONS.clear();
		
		for (Bloc b : Menu.BLOCS)
			POOL.free(b);
		Menu.BLOCS.clear();
	}

	public void free(Array<Bloc> blocs) {
		blocs.removeValue(this, true);
		POOL.free(this);
	}

}

class MiniBloc {
	public final Array<SparklesPopBloc> sparkles = new Array<SparklesPopBloc>();
//	float[] colors;
	float x, y, ancreX, ancreY, color;
	public static final Pool<MiniBloc> POOL = new Pool<MiniBloc>() {
		protected MiniBloc newObject() {			return new MiniBloc();		}
	};
	
	public MiniBloc init(float ancreX, float ancreY, float color) {
		this.ancreX = ancreX;
		this.ancreY = ancreY;
		this.color = color;
		do {
			x = (float) (ancreX + Rubico.R.nextGaussian() * Stats.U90);
		} while (x > -Bloc.TIER_WIDTH && x < Rubico.screenWidth);
		y = (float) (ancreY + Rubico.R.nextGaussian() * Stats.U90);
		return this;
	}
	
	public void draw(SpriteBatch batch) {
		batch.setColor(color);
		batch.draw(AssetMan.debris, x, y, Bloc.QUART_WIDTH, Bloc.QUART_HEIGHT);
		x += (ancreX - x) * EndlessMode.delta4;
		y += (ancreY - y) * EndlessMode.delta4;
		
//		if (sparkles.size < 16) 
//			sparkles.add(SparklesPopBloc.POOL.obtain().init(x + Bloc.WIDTH_DIV8, y + Bloc.HEIGHT_DIV8, colors));
//		SparklesPopBloc.act(sparkles, batch);
	}
	
}