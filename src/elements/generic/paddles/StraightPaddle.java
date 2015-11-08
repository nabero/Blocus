package elements.generic.paddles;

import jeu.mode.EndlessMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

public class StraightPaddle extends AbstractPaddle {

	public static final int ID = 1;
	protected final Array<PaddleShot> shots = new Array<PaddleShot>();
	
	@Override
	public void draw(SpriteBatch batch) {
		super.draw(batch);
		for (PaddleShot shot : shots)
			shot.act(batch, this);
		for (PaddleShot shot : shots)
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
		PaddleShot.add(x + TIER_WIDTH_MUL2, this);
		index3 += 13;
	}

	private void shotRight() {
		nextShot1 = EndlessMode.now + 0.5f;
		PaddleShot.add(x, this);
		index1 += 13;
	}

	public void shotMiddle() {
		nextShot2 = EndlessMode.now + 0.5f;
		PaddleShot.add(x + getTierWidth(), this);
		index2 += 13;
	}
	
	public void addShot(PaddleShot shot) {
		shots.contains(shot, true);
		shots.add(shot);
	}

	public void removeShot(PaddleShot paddleShot) {
		shots.removeValue(paddleShot, true);
	}
	
	@Override
	public void clear() {
		super.clear();
		PaddleShot.clear(shots);
	}

}