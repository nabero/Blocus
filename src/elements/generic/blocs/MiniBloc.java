package elements.generic.blocs;

import jeu.Rubico;
import jeu.Stats;
import jeu.mode.EndlessMode;
import assets.AssetMan;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;


public class MiniBloc {

	protected static final float WIDTH = Bloc.TIER_WIDTH, HEIGHT = Bloc.TIER_HEIGHT;
	private Animation animation = AssetMan.animBlink;
	public boolean anim;
	protected float x;
	public float y;
	protected float ancreX;
	protected float ancreY;
	protected float time;
	public static final Pool<MiniBloc> POOL = new Pool<MiniBloc>() {
		protected MiniBloc newObject() {
			return new MiniBloc();
		}
	};

	public MiniBloc init(float ancreX, float ancreY, float color, Color lightColor) {
		this.ancreX = ancreX;
		this.ancreY = ancreY;
		do {
			x = (float) (ancreX + Rubico.R.nextGaussian() * Stats.U90);
		} while (x > -Bloc.TIER_WIDTH && x < Rubico.screenWidth);
		y = (float) (ancreY + Rubico.R.nextGaussian() * Stats.U90);
		return this;
	}

	public void activateFlip() {
		animation = AssetMan.animFlip;
		anim = true;
		time = 0;
	}

	public void activatAnim() {
		if (anim)
			return;
		anim = true;
		time = 0;
		animation = AssetMan.animBlink;
	}

	public void drawSimple(SpriteBatch batch) {
		if (anim) {
			time += EndlessMode.delta;
			batch.draw(animation.getKeyFrame(time), x + Bloc.PADDING, y + Bloc.PADDING, getWidth(), getHeight());
			if (time > animation.getAnimationDuration())
				anim = false;
		} else
			batch.draw(AssetMan.partBloc, x + Bloc.PADDING, y + Bloc.PADDING, getWidth(), getHeight());
	}

	protected float getHeight() {
		return HEIGHT;
	}

	protected float getWidth() {
		return WIDTH;
	}

	public void draw(SpriteBatch batch) {
		if (anim) {
			time += EndlessMode.delta;
			batch.draw(animation.getKeyFrame(time), x, y, getWidth(), getHeight());
			if (time > animation.getAnimationDuration())
				anim = false;
		} else
			batch.draw(AssetMan.partBloc, x, y, getWidth(), getHeight());
		x += (ancreX - x) * EndlessMode.delta4 * 1.5f;
		y += (ancreY - y) * EndlessMode.delta4 * 1.5f;
	}

}
