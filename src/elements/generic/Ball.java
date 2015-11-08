package elements.generic;

import jeu.Physic;
import jeu.Rubico;
import jeu.Stats;
import jeu.Strings;
import jeu.mode.EndlessMode;
import jeu.mode.extensions.ScreenShake;
import jeu.mode.extensions.TemporaryText;
import assets.AssetMan;
import assets.SoundMan;
import box2dLight.PointLight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import elements.generic.blocs.Bloc;
import elements.generic.blocs.Column;
import elements.generic.bonus.Bonus;
import elements.generic.paddles.StraightPaddle;
import elements.particles.Particles;
import elements.particles.individual.Bump;
import elements.particles.individual.PrecalculatedParticles;
import elements.particles.individual.RubicoParticles;
import elements.particles.individual.explosions.FlashLight;
import elements.particles.individual.explosions.SparklesColorOverTime;
import elements.world.BlocWorld;

public class Ball {
	
	public static final Array<Ball> BALLS = new Array<Ball>();
	public final Vector2 dir = new Vector2(), oldDir = new Vector2();
	public float angle, centerX, centerY, oldCenterX, oldCenterY;
	public int lvl = 1, column;
	public static final float SPEED = Stats.U * 29, TOLERANCE = Rubico.screenWidth * 0.01f, MAGNET = Rubico.screenWidth * 0.2f;
	private float[] colors;
	public static int CHRONO_EXPLOSIVE = 0;
	public static final TemporaryText lvlUp = new TemporaryText(" Column clear  \nBall level up !"), newBall = new TemporaryText("New ball !"), explosiveBall = new TemporaryText("Explosive balls !");
	public static final Pool<Ball> POOL = new Pool<Ball>() {
		protected Ball newObject() {			return new Ball();		}
	};
	private final Body body;
	public final PointLight light = new PointLight(BlocWorld.rayHandler, 20);
	public static final TextureRegion TEXTURE = AssetMan.getTextureRegion("ball");
	public static final float WIDTH = Stats.UU * 0.55f, HALF_WIDTH = WIDTH / 2;
	public static final float L_WIDTH = WIDTH * 1.25f, L_HALF_WIDTH = WIDTH / 2;
	
	public Ball() {
		// First we create a body definition
		BodyDef bodyDef = new BodyDef();
		// We set our body to dynamic, for something like ground which doesn't move we would set it to StaticBody
		bodyDef.type = BodyType.KinematicBody;
		// Set our body's starting position in the world
		bodyDef.position.set(3, 3);

		// Create our body in the world using our body definition
		body = BlocWorld.world.createBody(bodyDef);

		// Create a circle shape and set its radius to 6
		CircleShape circle = new CircleShape();
		circle.setRadius(0.5f);

		// Create a fixture definition to apply our shape to
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = circle;
		fixtureDef.density = 0.5f; 
		fixtureDef.friction = 0.0f;
		fixtureDef.restitution = 1f; // Make it bounce a little bit

		// Create our fixture and attach it to the body
		body.createFixture(fixtureDef);

		// Remember to dispose of any shapes after you're done with them!
		// BodyDef and FixtureDef don't need disposing, but shapes do.
		circle.dispose();
		
	}
	
	
	public float nextLightning;
	public static void addBall(float x, float y, float dirX, float dirY) {
		Ball b = POOL.obtain();
		b.updatePos(x, y);
		b.dir.set(dirX, -dirY / 2).nor().scl(SPEED);
		b.lvl = 1;
//		b.width = Stats.UU;
		if (BALLS.size > 0)		TemporaryText.add(newBall);
		b.colors = PrecalculatedParticles.getColors(b.lvl);
		BALLS.add(b);
		if (BALLS.size >= 4)	Rubico.talkToTheWorld.unlockAchievementGPGS(Strings.ACH_4BALLS);
		if (BALLS.size >= 7)	Rubico.talkToTheWorld.unlockAchievementGPGS(Strings.ACH_7BALLS);
		
//		b.body.setTransform(b.x,  b.y, 90);
		b.body.setTransform(x, y, 90);
//		b.body.setActive(true);
//		b.body.setLinearVelocity(b.dir);
//		b.body.setType(BodyType.DynamicBody);
//		b.body.applyForceToCenter(b.dir.x, b.dir.y, true);
//		b.body.setUserData(b);
		b.light.attachToBody(b.body, 0, 0);
		b.setColor();
		b.light.setActive(true);
		b.light.setDistance(12);
		b.setNextLightning();
	}

	private void setNextLightning() {
		nextLightning = EndlessMode.now + (Rubico.R.nextFloat() * 8);
	}

	protected void setColor() {
		light.setColor(PrecalculatedParticles.COLORS[lvl-1]);
	}

	protected void updatePos(float d, float e) {
		oldCenterX = this.centerX;
		oldCenterY = this.centerY;
		this.centerX = d;
		this.centerY = e;
	}
	
	public static void draw(SpriteBatch batch) {
		for (int i = 0; i < BALLS.size; i++) {
			Ball b1 = BALLS.get(i);
			b1.act();
			displayThisBall(batch, b1);
			if (b1.nextLightning < EndlessMode.now) {
				for (float f = (EndlessMode.now - b1.nextLightning); f < 0.35f; f += 0.02f)
					Particles.RUBICO.add(RubicoParticles.POOL.obtain().init(b1));
				batch.setColor(b1.colors[0]);
				batch.draw(AssetMan.lightnings[Rubico.R.nextInt(AssetMan.lightnings.length)], b1.centerX - HALF_WIDTH, b1.centerY - HALF_WIDTH,	HALF_WIDTH, HALF_WIDTH, WIDTH, WIDTH, 
						((0.75f - (EndlessMode.now - b1.nextLightning) * 3) * 3) + Rubico.R.nextFloat() / 2, 	((0.75f - (EndlessMode.now - b1.nextLightning) * 3) * 3) + Rubico.R.nextFloat() / 2, 	b1.angle);
				if (b1.nextLightning < EndlessMode.now - 0.35f)
					b1.setNextLightning();
			}
			b1.angle += (b1.lvl) * EndlessMode.delta15 * 10;
//			batch.setColor(0, 1, 0, 1);
//			batch.draw(AssetMan.debris, b1.centerX - 0.25f, 			b1.centerY - 0.005f, 	0.5f, 			0.02f);
//			batch.draw(AssetMan.debris, b1.centerX - 0.005f, 			b1.centerY - 0.25f, 	0.02f, 			0.5f);
//			batch.setColor(0, 1, 1, 1);
//			batch.draw(AssetMan.debris, (b1.centerX - 0.25f) - b1.dir.x, 			(b1.centerY - 0.005f) - b1.dir.y, 	0.5f, 			0.02f);
//			batch.draw(AssetMan.debris, (b1.centerX - 0.005f) - b1.dir.x, 			(b1.centerY - 0.25f) - b1.dir.y, 	0.02f, 			0.5f);
		}
		batch.setColor(AssetMan.WHITE);
		
	}

	private static void displayThisBall(SpriteBatch batch, Ball b1) {
		//			if (!Rubico.profile.lights) {
//		batch.setColor(0, 0, 0, 0.95f);
		batch.setColor(b1.colors[0]);
		batch.draw(TEXTURE, b1.centerX - HALF_WIDTH, b1.centerY - HALF_WIDTH,	HALF_WIDTH				, 			HALF_WIDTH, WIDTH, WIDTH, 1.55f + b1.chronoOutline, 	1.55f + b1.chronoOutline, 	b1.angle);
		//			}
		batch.setColor(b1.colors[30]);
		batch.draw(TEXTURE, b1.centerX - HALF_WIDTH, b1.centerY - HALF_WIDTH,	HALF_WIDTH				, 			HALF_WIDTH, WIDTH, WIDTH, 1.30f + b1.chronoOutline, 	1.30f + b1.chronoOutline, 	-b1.angle);
//		batch.setColor(b1.colors[15]);
//		batch.draw(TEXTURE, b1.centerX - HALF_WIDTH, b1.centerY - HALF_WIDTH,	HALF_WIDTH				, 			HALF_WIDTH, WIDTH, WIDTH, 1.15f + b1.chronoOutline, 	1.15f + b1.chronoOutline, 	-b1.angle);
		batch.setColor(b1.colors[0]);
		batch.draw(TEXTURE, b1.centerX - HALF_WIDTH, b1.centerY - HALF_WIDTH,	HALF_WIDTH				, 			HALF_WIDTH, WIDTH, WIDTH, 1f, 	1f, 	b1.angle);
	}

	public void act() {
		if (EndlessMode.surprise && Gdx.input.isTouched()) {
			if (Physic.getXClic() < 4)
				dir.rotate(1);
			else if (Physic.getXClic() > Rubico.screenWidth - 4)
				dir.rotate(-1);
		}
		chronoOutline *= 0.9f;
		if (dir.x > 0) {
			for (int i = 0; i < Column.COLUMNS.size; i++) {
				Column c = Column.COLUMNS.get(i);
				if (getLeftX() < c.getRightX() && getRightX() > c.getLeftX()) {
					if (testCollisionWithColumn(c))
						break;
				}
			}
		} else {
			for (int i = Column.COLUMNS.size - 1; i >= 0; i--) {
				Column c = Column.COLUMNS.get(i);
				if (getLeftX() < c.getRightX() && getRightX() > c.getLeftX()) {
					if (testCollisionWithColumn(c))
						break;
				}
			}
		}
		
		updatePos(centerX + dir.x * EndlessMode.delta, centerY + dir.y * EndlessMode.delta);
		body.setTransform(centerX, centerY, angle);
		if (EndlessMode.alternate) {
			if (CHRONO_EXPLOSIVE > 0) {
				angle++;
				angle++;
				
				for (int i = 0; i <= lvl; i++)
					Particles.RUBICO.add(RubicoParticles.POOL.obtain().initFurious(this));
			} else 
				Particles.RUBICO.add(RubicoParticles.POOL.obtain().init(this));
			Particles.RUBICO.add(RubicoParticles.POOL.obtain().init(this));
			Particles.RUBICO.add(RubicoParticles.POOL.obtain().init(this));
		}
		collisions(this);
	}
	
	private boolean testCollisionWithColumn(Column c) {
		for (Bloc b : c.getBlocs()) {
//			if (Intersector.overlaps(getCircle(), b.getRectangle())) {
			if ( b.getRectangle().contains(centerX, centerY) ) {
				Column.collisionBloc(c, this, b);
				hit(this);
				return true;
			}
		}
		return false;
	}

	private static void collisions(Ball b) {
		if (b.centerX + HALF_WIDTH > Rubico.screenWidth) {
			if (Bonus.noWalls > 0) {
				b.centerX = 1;
				Bump.addLeftBall(b.centerY, b.colors);
			} else
				b.borderHit(-Math.abs(b.dir.x), Rubico.screenWidth - HALF_WIDTH, 180);
			Bump.addRightBall(b.centerY, b.colors);
			FlashLight.add(b.centerX + Stats.UU, b.centerY, b.light.getColor());
			hit(b);
		} else if (b.centerX < HALF_WIDTH) {
			if (Bonus.noWalls > 0) {
				b.centerX = (Rubico.screenWidth - Stats.U) - 1;
				Bump.addRightBall(b.centerY, b.colors);
			} else
				b.borderHit(Math.abs(b.dir.x), HALF_WIDTH, 0);
			Bump.addLeftBall(b.centerY, b.colors);
			FlashLight.add(b.centerX, b.centerY, b.light.getColor());
			hit(b);
		} else if (b.centerY + HALF_WIDTH > Rubico.screenHeight) {
			Bump.addUpBall(b.centerX, b.colors);
			b.hit(270, 60);
			b.dir.y = -Math.abs(b.dir.y);
			b.centerY = Rubico.screenHeight - HALF_WIDTH;
			hit(b);
		} else if (b.centerY - HALF_WIDTH < EndlessMode.ship.top) {
			hit(b);
			if (b.centerY + HALF_WIDTH > EndlessMode.ship.y - StraightPaddle.HEIGHT && b.centerX > EndlessMode.ship.x - TOLERANCE && b.centerX < EndlessMode.ship.right + TOLERANCE)
				EndlessMode.ship.hit(b);
			else if (b.centerY < 0)
				b.remove();
		}
//		b.angle = b.dir.angle();
	}

	float chronoOutline;
	private static void hit(Ball b) {
		b.nextLightning = EndlessMode.now;
		b.chronoOutline = 1f;
	}

	protected void borderHit(float dirX, float x, float angle) {
		dir.x = dirX;
		this.centerX = x;
		hit(angle, 60);
	}

	private void remove() {
		light.setActive(false);
		body.setActive(false);
		BALLS.removeValue(this, true);
		POOL.free(this);
		if (BALLS.size == 0)
			EndlessMode.lost();
	}

	public void hit(float sparkleAngle, float shake) {
		SparklesColorOverTime.add(centerX, centerY, sparkleAngle, colors, SPEED, 1, 30, 2);
//		width = Stats.UU;
		SoundMan.playBruitage(SoundMan.xp);
		ScreenShake.screenShake(shake);
	}

	public static void clear() {
		for (Ball b : BALLS) {
			b.light.setActive(false);
			b.body.setActive(false);
		}
		CHRONO_EXPLOSIVE = 0;
		POOL.freeAll(BALLS);
		BALLS.clear();
	}

	public float[] getColors() {
		return colors;
	}

	public void lvlUp() {
		dir.scl(1.085f);
		if (lvl >= PrecalculatedParticles.BRIGHT_YELLOW) {
			return;
		}
		lvl++;
		nextLightning = EndlessMode.now;
		if (lvl == PrecalculatedParticles.BRIGHT_YELLOW)
			Rubico.talkToTheWorld.unlockAchievementGPGS(Strings.ACH_BALL_LVL_MAX);
		colors = PrecalculatedParticles.getColors(lvl);
		light.setColor(PrecalculatedParticles.COLORS[lvl-1]);
		TemporaryText.add(lvlUp);
		setColor();
	}

	public static void explosiveBall() {
		CHRONO_EXPLOSIVE += 2;
		TemporaryText.add(explosiveBall);
	}

	public static void lvlUpOneBall() {
		for (Ball b : BALLS)
			if (b.lvl < PrecalculatedParticles.BRIGHT_YELLOW) {
				b.lvlUp();
				return;
			}
	}

	public float getXCenter() {
		return centerX;// + width / 2;
	}

	public float getYCenter() {
		return centerY;// + width / 2;
	}

	public float getLeftX() {
		return centerX - HALF_WIDTH;
	}

	public float getRightX() {
		return centerX + HALF_WIDTH;
	}

	private static final Circle circle = new Circle(0, 0, WIDTH);
	public Circle getCircle() {
		circle.setPosition(getLeftX(), getBottom());
		return circle;
	}

	private float getBottom() {
		return centerY - HALF_WIDTH;
	}

}
