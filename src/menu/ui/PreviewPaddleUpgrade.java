package menu.ui;

import menu.ui.diamond.DiamondCostDisplayer;
import jeu.Rubico;
import jeu.mode.EndlessMode;
import assets.AssetMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

import elements.generic.blocs.Bloc;
import elements.generic.blocs.MiniBloc;
import elements.generic.paddles.StraightPaddle;
import elements.generic.paddles.PaddleShot;
import elements.particles.individual.PrecalculatedParticles;

public class PreviewPaddleUpgrade extends UiElements {
	
	
	private Array<SmallBlocs> blocs = new Array<SmallBlocs>();
	private Array<SmallPaddle> paddles = new Array<SmallPaddle>();
	private boolean justStarted = true;
	private float nextIncrase = 0;
	private static final Vector2 UP = new Vector2(0, 1);
	private DiamondCostDisplayer diamondCost = new DiamondCostDisplayer();
	
	@Override
	public void draw(SpriteBatch batch) {
		time += Gdx.graphics.getDeltaTime();
		EndlessMode.majDeltas();
		if (justStarted) {
			justStarted();
		}
		
		for (SmallBlocs b : blocs) {
			b.display(batch);
		}
		for (SmallPaddle p : paddles) {
			p.draw(batch);
			p.updateLights();
			for (PaddleShot shot : p.getShots()) {
				shot.updateRectanglePos();
				
				for (SmallBlocs b : blocs) {
					if (!p.getShots().contains(shot, true))
						continue;
					if (shot.R.overlaps(b.getRectangle())) {
						shot.removeHp(-1);
						if (b.removeHp(UP, p.lvl)) {
							blocs.removeValue(b, true);
						}
						shot.checkHp(p.lvl, p);
					}
				}
			}
			for (PaddleShot shot : p.getShots())
				if (shot.isDead())
					shot.remove(p);
		}
		if (nextIncrase < time) {
			for (SmallPaddle p : paddles)
				p.increaseMiddle(-3);
			nextIncrase = time + 1;
		}
		place();
		drawSubElements(batch);
		batch.setColor(PrecalculatedParticles.uiColor[10]);
		batch.draw(AssetMan.arrow, x + width / 2.4f, y, width / 16, height / 16, width / 8, height / 8, 1, 1, 270);
		Rubico.menuFont.draw(batch, "Upgrade your paddle", 1.5f, y + height + 2.3f);
		
		diamondCost.display(batch);
		diamondCost.setX(11);
		diamondCost.setY(7);
		
	}
	
	public void reset() {
		justStarted = true;
	}

	private void justStarted() {
		remove();
		diamondCost.setX(11);
		diamondCost.setY(7);
		diamondCost.setCost(Rubico.profile.getCoutUpArme());
		initBlocs(1, x + 1);
		initPaddle(Rubico.profile.lvlPaddle, x + 1);
		initBlocs(1, x + 1  + width / 2);
		initPaddle(Rubico.profile.lvlPaddle + 1, x + 1 + width / 2);
		justStarted = false;
		EndlessMode.ship.thruster(false);
		nextIncrase = 0;
	}

	private void initPaddle(int lvl, float x) {
		SmallPaddle sp = SmallPaddle.POOL.obtain();
		sp.initialiser(Rubico.profile.getSelectedPaddleLvl());
		sp.x = x;
		sp.initialY = y + 3;
		paddles.add(sp);
	}

	private void initBlocs(int lvl, float x) {
		for (int i = 0; i < 14; i++) {
//			SmallBlocs sb = SmallBlocs.POOL.obtain();
//			sb.initAbsolute(x, i, lvl, ((y + height) - (SmallBlocs.HEIGHT * i)) - 1);
//			blocs.add(sb);
//			
			SmallBlocs sb2 = SmallBlocs.POOL.obtain();
			sb2.initAbsolute(x + sb2.getWidth(), i, lvl, ((y + height) - (SmallBlocs.HEIGHT * i)) - 1);
			blocs.add(sb2);
			
			SmallBlocs sb4 = SmallBlocs.POOL.obtain();
			sb4.initAbsolute(x + sb4.getWidth() * 2, i, lvl, ((y + height) - (SmallBlocs.HEIGHT * i)) - 1);
			blocs.add(sb4);
			
			SmallBlocs sb3 = SmallBlocs.POOL.obtain();
			sb3.initAbsolute(x + sb3.getWidth() * 3, i, lvl, ((y + height) - (SmallBlocs.HEIGHT * i)) - 1);
			blocs.add(sb3);
		}
	}
	
	@Override
	public void remove() {
		for (SmallBlocs b : blocs) {
			b.deactivate();
			SmallBlocs.POOL.free(b);
		}
		blocs.clear();
		for (SmallPaddle p : paddles) {
			p.clear();
		}
		paddles.clear();
	}

}


class SmallBlocs extends Bloc {
	
	static final Pool<SmallBlocs> POOL = new Pool<SmallBlocs>() {		protected SmallBlocs newObject() {			return new SmallBlocs();		}	};
	static final float DIV = 1.5f, WIDTH = Bloc.WIDTH / DIV, HEIGHT = Bloc.HEIGHT / DIV, HALF_WIDTH = Bloc.HALF_WIDTH / DIV, HALF_HEIGHT = Bloc.HALF_HEIGHT / DIV, TIER_WIDTH = Bloc.TIER_WIDTH / DIV, TIER_HEIGHT = Bloc.TIER_HEIGHT / DIV;
	private SmallMiniBloc[] particles = { new SmallMiniBloc(), new SmallMiniBloc(), new SmallMiniBloc(), new SmallMiniBloc(), new SmallMiniBloc(), new SmallMiniBloc(), new SmallMiniBloc(), new SmallMiniBloc(),	new SmallMiniBloc()	};
	
	public SmallBlocs() {
		create();
	}
	
	@Override
	public void display(SpriteBatch batch) {
		super.display(batch);
	}
	
	@Override
	public SmallMiniBloc[] getParticles() {
		return particles;
	}
	
	@Override	protected float getHalfHeight() {		return HALF_HEIGHT;	}
	@Override	public float getHalfWidth() {			return HALF_WIDTH;	}
	@Override	public float getTierHeight() {			return TIER_HEIGHT;	}
	@Override	public float getTierWidth() {			return TIER_WIDTH;	}
	
	public static float getWidth() {
		return WIDTH;
	}
	
	public static float getHeight() {
		return HEIGHT;
	}
	
}

class SmallMiniBloc extends MiniBloc {
	
	private static final float WIDTH = MiniBloc.WIDTH / SmallBlocs.DIV, HEIGHT = MiniBloc.HEIGHT / SmallBlocs.DIV;
	
	@Override
	protected float getHeight() {
		return HEIGHT;
	}
	
	@Override
	protected float getWidth() {
		return WIDTH;
	}
	
}

class SmallPaddle extends StraightPaddle {
	
	static final Pool<SmallPaddle> POOL = new Pool<SmallPaddle>() {		protected SmallPaddle newObject() {			return new SmallPaddle();		}	};

	public Array<PaddleShot> getShots() {
		return shots;
	}

	@Override
	protected void setBodyPos() {
		super.setBodyPos();
	}
	public void updateLights() {
		light1.setPosition(x, 					y);
		light2.setPosition(x + TIER_WIDTH, 		y);
		light3.setPosition(x + TIER_WIDTH_MUL2, y);
	}

}