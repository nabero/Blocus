package elements.generic.paddles;

import jeu.Physic;
import jeu.Rubico;
import jeu.mode.EndlessMode;
import assets.AssetMan;
import box2dLight.ConeLight;
import box2dLight.Light;
import box2dLight.PointLight;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;

import elements.generic.Ball;
import elements.particles.BorderParticlesGenerator;
import elements.particles.individual.PaddleParticles;
import elements.particles.individual.PrecalculatedParticles;
import elements.world.BlocWorld;

public abstract class AbstractPaddle {
	
	public static final float WIDTH = Rubico.screenWidth * 0.25f, HALF_WIDTH = WIDTH/2;
	public static final float TIER_WIDTH = WIDTH / 3, TIER_WIDTH_MUL2 = TIER_WIDTH * 2, INC = 0.3f, HEIGHT = Rubico.screenHeight * 0.03f, HALF_HEIGHT = HEIGHT / 2, TIER_WIDTH_DIV2 = TIER_WIDTH / 2,
			 BALL_IMPACT = Rubico.screenHeight * 0.04f, RECOVERY = Rubico.screenHeight * 0.005f, WIDTH_DIV6 = WIDTH/6;
	public static final TextureRegion SIDE = AssetMan.getTextureRegion("paddleside");
	public static final TextureRegion MID = AssetMan.getTextureRegion("paddlemiddle");
	private static final Array<PaddleParticles> particles = new Array<PaddleParticles>();
	public int lvl;
	public float initialY = (int) (Rubico.screenHeight * 0.17f), y = initialY, xCenter = 0, yCenter = 0, x, MIN = (Rubico.screenWidth * 0.02f), top, right;
	protected float nextShot1, nextShot2, nextShot3, decrease;
	protected boolean shot1, shot2, shot3, thruster = true;
	protected int index1, index2, index3;
	public float[] colors;
	private Body body; 
	protected final Light light1 = getLight(), light2 = getLight(), light3 = getLight();
	private BorderParticlesGenerator middleGenerator = new BorderParticlesGenerator(TIER_WIDTH, HEIGHT);
	private BorderParticlesGenerator rightGenerator = new BorderParticlesGenerator(TIER_WIDTH, HEIGHT);
	private BorderParticlesGenerator leftGenerator = new BorderParticlesGenerator(TIER_WIDTH, HEIGHT);

	protected ConeLight getConeLight(float angle) {
		ConeLight tmp = new ConeLight(BlocWorld.rayHandler, 20, Color.BLUE, 12, x, initialY, angle, 10);
		tmp.setActive(false);
		return tmp;
	}

	protected static ConeLight getConeLight() {
		ConeLight light = new ConeLight(BlocWorld.rayHandler, 6, Color.WHITE, 4, 0, 0, 0, 30);  
		return light;
	}
	
	protected static PointLight getLight() {
		PointLight light = new PointLight(BlocWorld.rayHandler, 6);
		light.setDistance(3);
		light.setActive(false);
		return light;
	}
	
	public void initialiser(int lvl) {
		thruster = true;
		this.lvl = lvl;
		xCenter = Rubico.screenWidth * 0.5f;
		yCenter = Rubico.screenHeight * 0.1f;
		x = xCenter - HALF_WIDTH;
		y = 0;
		initialY = (int) (Rubico.screenHeight * 0.17f);
		shot1 = false;
		shot2 = false;
		shot3 = false;
		colors = PrecalculatedParticles.getColors(lvl);
		index1 = colors.length - 1;
		index2 = colors.length - 1;
		index3 = colors.length - 1;
		nextShot1 = 0;
		nextShot2 = 0;
		nextShot3 = 0;
		decrease = 0;
		if (body == null)
			initBody();
		activateLight(light1, -getTierWidth());//, coneLight1);
		activateLight(light2, 0);//, coneLight2);
		activateLight(light3, +getTierWidth());//, coneLight3);
//		setBodyPos();
		PaddleParticles.clear(particles);
		
		middleGenerator.setColor(colors);
		middleGenerator.setPos(x + getTierWidth(), y);
		
		leftGenerator.setColor(colors);
		leftGenerator.setPos(x, y);
		
		rightGenerator.setColor(colors);
		rightGenerator.setPos(x + TIER_WIDTH_MUL2, y);
	}
	
	public void thruster(SpriteBatch batch) {
		if (EndlessMode.oneToFour == 3)
			particles.add(PaddleParticles.POOL.obtain().init(x + Rubico.R.nextFloat() * TIER_WIDTH_MUL2, y - (HEIGHT * 0.95f + (Rubico.R.nextFloat()/10)), colors, getTierWidth(), HEIGHT));
		middleGenerator.act(batch);
		middleGenerator.updatePos(x + getTierWidth(), y);
		rightGenerator.act(batch);
		rightGenerator.updatePos(x + TIER_WIDTH_MUL2, y);
		leftGenerator.act(batch);
		leftGenerator.updatePos(x, y);
		middleGenerator.setSpeed(index2);
		leftGenerator.setSpeed(index1);
		rightGenerator.setSpeed(index3);
	}
	
	private void initBody() {
		BodyDef groundBodyDef = new BodyDef();  
		groundBodyDef.position.x = -1000;
		body = BlocWorld.world.createBody(groundBodyDef);
		PolygonShape groundBox = new PolygonShape();
		groundBox.setAsBox(HALF_WIDTH, HALF_HEIGHT);
		Fixture fixture = body.createFixture(groundBox, 0.0f);
		fixture.setRestitution(1);
		groundBox.dispose();
		body.setType(BodyType.KinematicBody);
	}

	protected void activateLight(Light light, float xOffset) { //, ConeLight coneLight) {
		if (lvl <= 7)	light.setColor(PrecalculatedParticles.COLORS[lvl-1]);
		else			light.setColor(PrecalculatedParticles.COLORS[6]);
		light.attachToBody(body, xOffset, 0);
		light.setActive(true);
	}
	
	public void move() {
		if (EndlessMode.surprise)
			return;
		x = Physic.getXClic() - HALF_WIDTH;
		xCenter = x + HALF_WIDTH;
	}
	
	public void draw(SpriteBatch batch) {
		if (index1 >= colors.length) {
			index1 = colors.length - 1;
			shot1 = false;
		} else if (index1 <= 0) {
			shot1 = true;
			index1 = 0;
		}
		if (index2 >= colors.length) {
			index2 = colors.length - 1;
			shot2 = false;
		} else if (index2 <= 0) {
			shot2 = true;
			index2 = 0;
		}
		if (index3 >= colors.length) {
			index3 = colors.length - 1;
			shot3 = false;
		} else if (index3 <= 0) {
			shot3 = true;
			index3 = 0;
		}

		paddleParticle(batch);
			// left
		drawPaddle(batch);
		
		setBodyPos();
		thrusterAct(batch);
		
		if (decrease < EndlessMode.now) {
			index1++;
			index2++;
			index3++;
			decrease = EndlessMode.now + 0.5f;
		}
	}

	protected void thrusterAct(SpriteBatch batch) {
		if (y < initialY)	{
			light1.setActive(true);
			light2.setActive(true);
			light3.setActive(true);
			y += RECOVERY;
			for (int i = 0; i < lvl; i++)
				particles.add(PaddleParticles.POOL.obtain().init(x + Rubico.R.nextFloat() * TIER_WIDTH_MUL2, y - (HEIGHT * 0.95f + (Rubico.R.nextFloat()/10)), colors, getTierWidth(), HEIGHT));
		} else {
			y = initialY + 0.01f;
		}
		top = y + HEIGHT;
		right = x + WIDTH;
		
		if (thruster)
			thruster(batch);
	}

	protected void paddleParticle(SpriteBatch batch) {
		PaddleParticles.act(batch, particles);
	}

	protected void drawPaddle(SpriteBatch batch) {
		batch.setColor(colors[index1]);
		batch.draw(SIDE, x, y, getTierWidth(), HEIGHT);
		batch.draw(SIDE, x, y, getTierWidth(), HEIGHT);
			// mid
		batch.setColor(colors[index2]);
		batch.draw(MID, x + getTierWidth(), y, getTierWidth(), HEIGHT);
		batch.draw(MID, x + getTierWidth(), y, getTierWidth(), HEIGHT);
			// right
		batch.setColor(colors[index3]);
		batch.draw(SIDE, x + TIER_WIDTH_MUL2 + getTierWidth(), y, -getTierWidth(), HEIGHT);
		batch.draw(SIDE, x + TIER_WIDTH_MUL2 + getTierWidth(), y, -getTierWidth(), HEIGHT);
	}
	
	private static float angle;//, oldAngle;
	public void hit(Ball b) {
		particles.add(PaddleParticles.POOL.obtain().init(b.centerX, y - HEIGHT * 0.6f, colors, getTierWidth(), HEIGHT));
		particles.add(PaddleParticles.POOL.obtain().init(b.centerX, y - HEIGHT * 0.7f, colors, getTierWidth(), HEIGHT));
		particles.add(PaddleParticles.POOL.obtain().init(b.centerX, y - HEIGHT * 0.8f, colors, getTierWidth(), HEIGHT));
		particles.add(PaddleParticles.POOL.obtain().init(b.centerX, y - HEIGHT * 0.9f, colors, getTierWidth(), HEIGHT));
		particles.add(PaddleParticles.POOL.obtain().init(b.centerX, y - HEIGHT, colors, getTierWidth(), HEIGHT));
		particles.add(PaddleParticles.POOL.obtain().init(b.centerX, y - HEIGHT * 1.05f, colors, getTierWidth(), HEIGHT));
		particles.add(PaddleParticles.POOL.obtain().init(b.centerX, y - HEIGHT * 1.1f, colors, getTierWidth(), HEIGHT));
		particles.add(PaddleParticles.POOL.obtain().init(b.centerX, y - HEIGHT * 1.15f, colors, getTierWidth(), HEIGHT));
		particles.add(PaddleParticles.POOL.obtain().init(b.centerX, y - HEIGHT * 1.20f, colors, getTierWidth(), HEIGHT));
		particles.add(PaddleParticles.POOL.obtain().init(b.centerX, y - HEIGHT * 1.25f, colors, getTierWidth(), HEIGHT));
		
		b.centerY = y + HEIGHT + Ball.HALF_WIDTH;
		b.nextLightning = EndlessMode.now;
		y -= BALL_IMPACT;
		b.hit(90, 60);
		
		if (b.centerX < x + getTierWidth())			increaseLeft(b);
		else if (b.centerX < x + TIER_WIDTH_MUL2)	increaseMiddle(b.lvl);
		else 										increaseRight(b);
		
		angle = (((x + WIDTH) - b.centerX ) / WIDTH)	*	180;
		
		if (angle > 160)			angle = 160;
		else if (angle < 20)		angle = 20;
		
		b.dir.setAngle(angle);
	}
	
	
	public void clear() {
		light1.setActive(false);
		light2.setActive(false);
		light3.setActive(false);
	}
	
	protected void increaseRight(Ball b) {		index3 -= getIncreaseShot();						}
	protected void increaseLeft(Ball b) {		index1 -= getIncreaseShot();						}
	public void increaseMiddle(int lvl) {		index2 -= getIncreaseShot();						}
	protected int getIncreaseShot() {			return 10;											}	
	protected void setBodyPos() {				body.setTransform(xCenter, y + HALF_HEIGHT, 0);		}
	public float getRandomXInside() {			return x + (Rubico.R.nextFloat() * WIDTH);			}
	protected float getInnerDiv(int index) {	return Math.max(index/5f, 1);						}
	public float getRandomYInside() {			return y + HALF_HEIGHT;								}
	public float getTop() {						return y + HEIGHT;									}
	public float getTierWidth() {				return TIER_WIDTH;									}
	public float getHeight() {					return HEIGHT;										}
	public void thruster(boolean b) {			thruster = b;										}

}
