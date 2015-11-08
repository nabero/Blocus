package menu.ui;

import jeu.Rubico;
import jeu.mode.EndlessMode;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Pool;

import elements.generic.blocs.Bloc;
import elements.generic.blocs.MiniBloc;

public class PresentationBloc extends Bloc {
	
	static final float DIV = 1;
	private PresentationMiniBloc[] particles = {new PresentationMiniBloc(), new PresentationMiniBloc(), new PresentationMiniBloc(), new PresentationMiniBloc(), new PresentationMiniBloc(), new PresentationMiniBloc(), new PresentationMiniBloc(), new PresentationMiniBloc(), new PresentationMiniBloc()};
	private static final Pool<PresentationBloc> POOL = new Pool<PresentationBloc>() {		protected PresentationBloc newObject() {			return new PresentationBloc();		}	};
	public static final float TIER_WIDTH = Bloc.TIER_WIDTH / DIV, TIER_HEIGHT = Bloc.TIER_HEIGHT / DIV, HEIGHT = Bloc.HEIGHT / DIV;
	private LevelPreview preview;
	
	public static PresentationBloc addBlock(float ancreX, int row, int lvl, float y) {
		final PresentationBloc b = POOL.obtain();
		b.init(ancreX, row, lvl);

		// top level at center preview
		b.moveY(-(Rubico.screenHeight- (y + (LevelPreview.HEIGHT / 2)) ));
		return b;
	}
	
	@Override
	public void display(SpriteBatch batch) {
		super.display(batch);
		moveY(preview.getYMovement());
		for (PresentationMiniBloc pmb : particles) {
			pmb.display = pmb.isInside(preview.x, preview.y, LevelPreview.WIDTH, LevelPreview.HEIGHT);
		}
	}
	
	private void moveY(float delta) {
		this.y += delta;
		for (PresentationMiniBloc pmb : particles)
			pmb.moveY(delta);
	}
	
	public static void clear(PresentationBloc b) {
		b.display = 0;
		b.body.setActive(false);
		b.light.setActive(false);
		POOL.free(b);
	}

	@Override	public MiniBloc[] getParticles() {		return particles;	}
	@Override	public float getTierHeight() {			return TIER_HEIGHT;	}
	@Override	public float getTierWidth() {			return TIER_WIDTH;	}

	public void setPreview(LevelPreview levelPreview) {		this.preview = levelPreview;	}
	public float getHeight() {								return HEIGHT;					}
}
