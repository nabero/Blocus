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
import box2dLight.Light;
import box2dLight.PointLight;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import elements.generic.Ball;
import elements.generic.bonus.Bonus;
import elements.particles.Particles;
import elements.particles.individual.PrecalculatedParticles;
import elements.particles.individual.explosions.ExplodingBloc;
import elements.particles.individual.explosions.SparklesColorOverTime;
import elements.world.BlocWorld;

public class Bloc {
	
	public static final Array<ExplodingBloc> EXPLOSIONS = new Array<ExplodingBloc>();
	public static final float WIDTH = Rubico.screenWidth * 0.08f, HEIGHT = Rubico.screenHeight * 0.03f, PADDING = WIDTH / 40f, HALF_PADDING = PADDING / 2,  
			HALF_WIDTH = WIDTH / 2, HALF_HEIGHT = HEIGHT / 2, WIDTH_DOT = WIDTH * 0.1f, HALF_WIDTH_DOT = WIDTH_DOT / 2,
			TIER_WIDTH = (WIDTH / 3) - PADDING, TIER_HEIGHT = (HEIGHT / 3) - PADDING, TIER_WIDTH_MUL2 = TIER_WIDTH * 2, TIER_HEIGHT_MUL2 = TIER_HEIGHT * 2, 
			STEP = Stats.u / 16, QUART_WIDTH = WIDTH / 4, QUART_HEIGHT = HEIGHT / 4, WIDTH_DIV8 = WIDTH / 8, HEIGHT_DIV8 = HEIGHT / 8;
	private static int tmpInt;
	private static final int[] BLOC_TO_HP = {2, 5, 8, 0, 3, 6, 4};
	static final Rectangle R = new Rectangle(0, 0, WIDTH, HEIGHT);
	public static boolean first = true;
	private boolean colorHasToChange;
	public int hp, row;
	public static final int MAX = PrecalculatedParticles.LENGTH_DIV4_MUL3 + 6, MIN = 0;
	public float balance, place, x, y, display, nextAnim, lvlDownTimer = 0.0f;
	public float[] colors = PrecalculatedParticles.blocBlue;//, smallColors;
	public Body body; 
	public Light light;
	private MiniBloc[] particles = { new MiniBloc(), new MiniBloc(), new MiniBloc(), new MiniBloc(), new MiniBloc(), new MiniBloc(), new MiniBloc(), new MiniBloc(), new MiniBloc() };
	private static final Pool<Bloc> POOL = new Pool<Bloc>() {		protected Bloc newObject() {			return new Bloc();		}	};
	public static final TextureRegion TEXTURE = AssetMan.getTextureRegion("briq");
	private boolean firstActivation = true;
	
	public Bloc() {
		create();
	}

	protected void create() {
		body = BlocWorld.bodyFactoryBox(Vector2.Zero, getHalfWidth(), getHalfHeight(), null, BodyType.StaticBody);
		light = new PointLight(BlocWorld.rayHandler, 5);
		light.attachToBody(body, 0, 0);
		activateLight();
		light.setActive(false);
	}

	private void activateLight() {
		light.setStaticLight(true);
		light.setDistance(2.85f);
		light.setActive(true);
	}

	public static Bloc addBlock(float ancreX, int row, int lvl) {
		final Bloc b = POOL.obtain();
		b.init(ancreX, row, lvl);
		return b;
	}

	protected void init(float ancreX, int row, int lvl) {
		initAbsolute(ancreX, row, lvl, Builder.MIN_Y - (HEIGHT * row));
		if (y < -HEIGHT)
			EndlessMode.lost();
	}

	public void initAbsolute(float ancreX, int row, int lvl, float y) {
		this.y = y;
		this.x = ancreX;
		this.row = row;
		this.init(lvl);
		initPlace();
		display = 0;
		body.setTransform(x + getHalfWidth(), y + getHalfHeight(), 0);
		body.setActive(true);
		setLightColor();
		setNextAnim();
		tmpInt = (int) place;
		initParticles();
		firstActivation = true;
	}


	protected void initParticles() {
		for (int x = 0; x < 3; x++) {
			setMiniBloc(x, 0, colors[tmpInt]);
			setMiniBloc(x, 2, colors[tmpInt]);
		}
		setMiniBloc(0, 1, colors[tmpInt]);
		setMiniBloc(1, 1, colors[MAX - tmpInt]);
		setMiniBloc(2, 1, colors[tmpInt]);
	}

	private void setNextAnim() {
		nextAnim = EndlessMode.now + Rubico.R.nextFloat() * 5;
	}

	protected void setMiniBloc(int x, int y, float color) {
		getParticles()[(x * 3) + y].init(this.x + (x * getTierWidth()), this.y + (y * getTierHeight()), color, light.getColor());
	}

	public void initPlace() {
		balance = 0.4f;
		place = 25;
	}
	
	private void init(int lvl) {
		if (lvl < 1)
			lvl = 1;
		hp = lvl;
		colors = PrecalculatedParticles.getColors(lvl);
	}

	public void display(SpriteBatch batch) {
		
		place += balance;
		if (place > PrecalculatedParticles.HALF_LENGTH)		balance -= 0.007f;
		else												balance += 0.007f;
		if (place < MIN)									place = MIN;
		else if (place >= MAX)								place = MAX;

		tmpInt = (int) place;
		batch.setColor(colors[tmpInt]);
		if (display < 3.5f) {
			body.setTransform(x + HALF_WIDTH, y + HALF_HEIGHT, 0);
			light.setPosition(x + HALF_WIDTH, y + HALF_HEIGHT);
			display += EndlessMode.delta;
			if (display > 1.5f) {
				if (firstActivation) {
					for (MiniBloc b : getParticles())
						b.activatAnim();
					firstActivation = false;
				}
				activateLight();
			}
			getParticles()[0].draw(batch);
			getParticles()[1].draw(batch);
			getParticles()[2].draw(batch);
			getParticles()[3].draw(batch);
			getParticles()[5].draw(batch);
			getParticles()[6].draw(batch);
			getParticles()[7].draw(batch);
			getParticles()[8].draw(batch);
//			
			batch.setColor(colors[(colors.length) - tmpInt]);
			getParticles()[4].draw(batch);
			return;
		}  
		//batch.draw(AssetMan.partBloc, x, y, WIDTH, HEIGHT);
		
		getParticles()[0].drawSimple(batch);
		getParticles()[1].drawSimple(batch);
		getParticles()[2].drawSimple(batch);
		getParticles()[3].drawSimple(batch);
		getParticles()[5].drawSimple(batch);
		getParticles()[6].drawSimple(batch);
		getParticles()[7].drawSimple(batch);
		getParticles()[8].drawSimple(batch);
//		
		batch.setColor(colors[(colors.length) - tmpInt]);
		getParticles()[4].drawSimple(batch);
		
		// 2 5 8	0 3 6
		for (int i = 0; i < hp; i++) {
			batch.draw(AssetMan.screw, (particles[getIndex(i)].x) + WIDTH_DOT, particles[getIndex(i)].y + HALF_WIDTH_DOT, WIDTH_DOT, WIDTH_DOT); 
		}
		if (EndlessMode.now > nextAnim) {
			setNextAnim();
			particles[Rubico.R.nextInt(particles.length)].activatAnim();
		}
		if (colorHasToChange) {
			lvlDownTimer -= EndlessMode.delta;
			if (lvlDownTimer <= 0.0f) {
				changeColor();
				colorHasToChange = false;
			}
		}
		
//		batch.setColor(1, 0, 0, 1);
//		batch.draw(AssetMan.debris, x, 			y, 	0.1f, 			HEIGHT);
	}
	protected MiniBloc[] getParticles() {
		return particles;
	}

	private static int getIndex(int i) {
		if (i > BLOC_TO_HP.length -1)
			i = BLOC_TO_HP.length - 1;
		return BLOC_TO_HP[i];
	}
	
	public boolean collision(Ball ball) {
		if (display < 1f)
			return false;
		Column.blip = true;
		Column.ROW = row;
		removeHp(ball.dir, ball.lvl);
		
		// LEFT
		if (Intersector.intersectSegments(	x, 			y, 	x, 			(y + HEIGHT), ball.centerX, 	ball.centerY, 	ball.centerX - ball.dir.x, ball.centerY - ball.dir.y, null)) {
			collideLeft(ball);
		} else if (Intersector.intersectSegments(	x + WIDTH, 	y, 	x + WIDTH, 	y + HEIGHT, ball.centerX, 	ball.centerY, 	ball.centerX - ball.dir.x, ball.centerY - ball.dir.y, null)) {
			collideRight(ball);
		} else if (Intersector.intersectSegments(	x, 	y + HEIGHT, 	x + WIDTH, 	y + HEIGHT, ball.centerX, 	ball.centerY, 	ball.centerX - ball.dir.x, ball.centerY - ball.dir.y, null)) {
			collideUp(ball);
		} else if (Intersector.intersectSegments(	x, 	y, 	x + WIDTH, 	y, ball.centerX, 	ball.centerY, 	ball.centerX - ball.dir.x, ball.centerY - ball.dir.y, null)) {
			collideDown(ball);
		}
		while (getRectangle().contains(ball.centerX, ball.centerY)) {
			ball.centerX += ball.dir.x * EndlessMode.delta * 0.1f;
			ball.centerY += ball.dir.y * EndlessMode.delta * 0.1f;
		}
		return hp <= 0;
	}

	private void collideUp(Ball ball) {
		ball.dir.y = Math.abs(ball.dir.y);
		ball.hit(90, 15);
		ball.centerY = y + HEIGHT;
	}

	private void collideRight(Ball ball) {
		ball.dir.x = Math.abs(ball.dir.x);
		ball.hit(0, 15);
		ball.centerX = x + WIDTH;
	}

	private void collideDown(Ball ball) {
		ball.dir.y = -Math.abs(ball.dir.y);
		ball.hit(270, 15);
		ball.centerY = y;
	}

	private void collideLeft(Ball ball) {
		ball.dir.x = -Math.abs(ball.dir.x);
		ball.hit(180, 15);
		ball.centerX = x;
	}

	public boolean removeHp(Vector2 dir, int hp) {
		Bonus.blocTaken(this);
		Score.up(hp);
		SoundMan.explosion(this.hp);
		this.hp -= hp;
		if (this.hp <= 0) {
			dead(dir);
			Score.up(this.hp);
			return true;
		} else {
			lvlDown();
			return false;
		}
	}

	private void lvlDown() {
		lvlDownTimer = AssetMan.FLIP_DURATION / 2;
		colorHasToChange = true;
		for (MiniBloc b : particles)
			b.activateFlip();
	}

	private void changeColor() {
		setLightColor();
		colors = PrecalculatedParticles.getColors(this.hp);
	}

	private void dead(Vector2 dir) {
		body.setActive(false);
		light.setActive(false);
		SparklesColorOverTime.add(Particles.COLOR_OVER_TIME, this, colors, dir);
		ExplodingBloc.explode(this, EXPLOSIONS, dir);
		ScreenShake.screenShake(90);
	}

	public static void freeAll(Array<Bloc> blocs) {
		for (Bloc b : blocs)
			clear(b);
		blocs.clear();
	}

	public boolean overlaps(Rectangle r) {
		R.setPosition(x, y);
		return r.overlaps(R);
	}

	public static void clear() {
		first = true;
		
		ExplodingBloc.clear(EXPLOSIONS);
		
		for (Bloc b : Menu.BLOCS)
			clear(b);
		Menu.BLOCS.clear();
	}

	public static void clear(Bloc b) {
		b.display = 0;
		b.deactivate();
		POOL.free(b);
	}

	public void deactivate() {
		body.setActive(false);
		light.setActive(false);
	}

	public void free(Array<Bloc> blocs) {
		blocs.removeValue(this, true);
		POOL.free(this);
		deactivate();
	}

	public Rectangle getRectangle() {
		R.setPosition(x, y);
		return R;
	}
	public float getTierHeight() {						return TIER_HEIGHT;	}
	public float getTierWidth() {						return TIER_WIDTH;	}
	private void setLightColor() {						light.setColor(PrecalculatedParticles.getColor(hp));	}
	public float getRandomYInside() {					return y + Rubico.R.nextFloat() * HEIGHT;																}
	public float getRandomXInside() {					return x + Rubico.R.nextFloat() * WIDTH;																}
	protected float getHalfHeight() {					return HALF_HEIGHT;	}
	public float getHalfWidth() { 						return HALF_WIDTH;	}

}