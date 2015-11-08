package elements.generic.paddles;

import jeu.Rubico;
import jeu.mode.EndlessMode;
import assets.AssetMan;
import box2dLight.ConeLight;
import box2dLight.Light;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import elements.generic.Ball;
import elements.particles.individual.PrecalculatedParticles;
import elements.particles.individual.Smoke;

public class ShipPaddle extends AbstractPaddle {

	public static final int ID = 3, INCREASE = 33;
	protected final Array<ShipShot> shots = new Array<ShipShot>();
	protected final ConeLight coneLight1 = getConeLight(), coneLight2 = getConeLight(), coneLight3 = getConeLight();
	
	@Override
	public void initialiser(int lvl) {
		super.initialiser(lvl);

		coneLight1.setColor(PrecalculatedParticles.getColor(lvl));
		coneLight1.setDirection(-90);
		coneLight2.setColor(PrecalculatedParticles.getColor(lvl));
		coneLight2.setDirection(-90);
		coneLight2.setDistance(6);
		coneLight3.setColor(PrecalculatedParticles.getColor(lvl));
		coneLight3.setDirection(-90);
		light1.setColor(PrecalculatedParticles.getColor(1));
		light2.setColor(PrecalculatedParticles.getColor(1));
		light3.setColor(PrecalculatedParticles.getColor(1));
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		for (ShipShot shot : shots)
			shot.act(batch, this);
		for (ShipShot shot : shots)
			if (shot.isDead())
				shot.remove(this);
		
		if (shot1 && nextShot1 < EndlessMode.now)			shot();
	}
	
	@Override
	protected void activateLight(Light light, float xOffset) {
		if (lvl <= 7)	light.setColor(PrecalculatedParticles.COLORS[lvl-1]);
		else			light.setColor(PrecalculatedParticles.COLORS[6]);
		light.setActive(true);
	}
	
	@Override
	protected void drawPaddle(SpriteBatch batch) {
		light1.setPosition(x + HALF_WIDTH, y);
		light2.setPosition(x, y + 1);
		light3.setPosition(x + WIDTH, y + 1);

		coneLight1.setPosition(x + 1.35f, y - 1);
		coneLight2.setPosition(x + 2.35f, y - 0.6f);
		coneLight3.setPosition(x + 3.35f, y - 1);
		
		// fire mid
		batch.setColor(colors[Rubico.R.nextInt(colors.length)]);
		batch.draw(AssetMan.dust, x + 1.96f, y - HEIGHT * 2.3f, 0.65f, 2.4f);
		batch.setColor(colors[Rubico.R.nextInt(colors.length)]);
		batch.draw(AssetMan.dust, x + 1.96f, y - HEIGHT * 1.5f, 0.65f, 0.9f);
		batch.setColor(colors[Rubico.R.nextInt(colors.length / 2)]);
		batch.draw(AssetMan.dust, x + 2.06f, y - HEIGHT * 1.43f, 0.45f, 0.75f);
		batch.setColor(colors[Rubico.R.nextInt(colors.length / 3)]);
		batch.draw(AssetMan.dust, x + 2.16f, y - HEIGHT * 2.63f, 0.25f, 2.75f);
		
		// fire left
		batch.setColor(colors[Rubico.R.nextInt(colors.length)]);
		batch.draw(AssetMan.dust, x + 1.23f, y - HEIGHT * 2.3f, 0.45f, 1.9f);
		batch.setColor(colors[Rubico.R.nextInt(colors.length)]);
		batch.draw(AssetMan.dust, x + 1.23f, y - HEIGHT * 1.8f, 0.45f, 0.9f);
		// fire right
		batch.setColor(colors[Rubico.R.nextInt(colors.length)]);
		batch.draw(AssetMan.dust, x + 2.88f, y - HEIGHT * 2.3f, 0.45f, 1.9f);
		batch.setColor(colors[Rubico.R.nextInt(colors.length)]);
		batch.draw(AssetMan.dust, x + 2.88f, y - HEIGHT * 1.8f, 0.45f, 0.9f);
		
		batch.setColor(AssetMan.WHITE);
		batch.draw(AssetMan.shipPaddle, x, y - HEIGHT * 1.25f, WIDTH, HEIGHT * 2.5f);
		batch.setColor(colors[index1]);
		
		batch.draw(AssetMan.debris, x + HALF_WIDTH - 0.1f, y + 0.26f, 0.255f, 0.1f);
		batch.draw(AssetMan.debris, x + HALF_WIDTH - 0.1f, y + 0.26f, 0.255f, 0.1f);
		batch.draw(AssetMan.dust, x + HALF_WIDTH - 0.25f, y + 0.2f, 0.55f, 0.2f);
		batch.draw(AssetMan.dust, x + HALF_WIDTH - 0.40f, y + 0.15f, 0.85f, 0.3f);
		
		Smoke.draw(batch);
		Smoke.add(x + 1.8f, y - HEIGHT * 1.8f, 0.65f);
		Smoke.add(x + 1.8f, y - HEIGHT * 1.8f, 0.65f);
		Smoke.add(x + 1.8f, y - HEIGHT * 1.8f, 0.65f);
		Smoke.add(x + 1.8f, y - HEIGHT * 1.8f, 0.65f);
		
//		Smoke.add(x + 1.2f, y - HEIGHT * 1.65f, 0.4f);
		Smoke.add(x + 1.2f, y - HEIGHT * 1.65f, 0.4f);
		Smoke.add(x + 1.2f, y - HEIGHT * 1.65f, 0.4f);
		
		Smoke.add(x + 2.8f, y - HEIGHT * 1.65f, 0.4f);
		Smoke.add(x + 2.8f, y - HEIGHT * 1.65f, 0.4f);
//		Smoke.add(x + 2.8f, y - HEIGHT * 1.65f, 0.4f);
	}
	
	@Override
	protected void paddleParticle(SpriteBatch batch) {
	}
	
	@Override
	protected void thrusterAct(SpriteBatch batch) {
		super.thrusterAct(batch);
	}
	
	@Override
	public void thruster(SpriteBatch batch) {
	}
	
	protected void paddleShot() {
		
	}

	private void shot() {
		nextShot1 = EndlessMode.now + 0.5f;
		ShipShot.add(x, this, y);
		index1 += INCREASE;
	}

	public void addShot(ShipShot shot) {
		shots.contains(shot, true);
		shots.add(shot);
	}

	public void removeShot(ShipShot paddleShot) {
		shots.removeValue(paddleShot, true);
	}
	
	@Override
	protected void increaseLeft(Ball b) {
		index1 -= getIncreaseShot();
	}
	@Override
	public void increaseMiddle(int lvl) {
		index1 -= getIncreaseShot();
	}
	@Override
	protected void increaseRight(Ball b) {
		index1 -= getIncreaseShot();
	}
	
	@Override
	public void clear() {
		super.clear();
		Smoke.clear();
		ShipShot.clear(shots);
	}
	
	@Override
	protected int getIncreaseShot() {
		return 10;
	}
}