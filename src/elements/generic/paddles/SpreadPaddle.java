package elements.generic.paddles;

import jeu.mode.EndlessMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class SpreadPaddle extends AbstractPaddle {

	public static final int ID = 2, INCREASE = 33;
	public static final int STRAIGHT_PADDLE = 1, SPREAD_PADDLE = 2;
	protected final Array<SpreadShot> shots = new Array<SpreadShot>();
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		for (SpreadShot shot : shots)
			shot.act(batch, this);
		for (SpreadShot shot : shots)
			if (shot.isDead())
				shot.remove(this);
		
		paddleShot();
	}
	
	protected void paddleShot() {
		if (shot1 && nextShot1 < EndlessMode.now)			shotRight();
		if (shot2 && nextShot2 < EndlessMode.now) 			shotMiddle();
		if (shot3 && nextShot3 < EndlessMode.now) 			shotLeft();
	}

	private void shotLeft() {
		nextShot3 = EndlessMode.now + 0.5f;
		SpreadShot.add(x + TIER_WIDTH_MUL2, this);
		index3 += INCREASE;
	}

	private void shotRight() {
		nextShot1 = EndlessMode.now + 0.5f;
		SpreadShot.add(x, this);
		index1 += INCREASE;
	}

	public void shotMiddle() {
		nextShot2 = EndlessMode.now + 0.5f;
		SpreadShot.add(x + getTierWidth(), this);
		index2 += INCREASE;
	}
	
	public void addShot(SpreadShot shot) {
		shots.contains(shot, true);
		shots.add(shot);
	}

	public void removeShot(SpreadShot paddleShot) {
		shots.removeValue(paddleShot, true);
	}
	
	@Override
	public void clear() {
		super.clear();
		SpreadShot.clear(shots);
	}
	
	@Override
	protected int getIncreaseShot() {
		return 8;
	}
}