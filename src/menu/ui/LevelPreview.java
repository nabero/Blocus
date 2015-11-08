package menu.ui;

import assets.AssetMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import elements.generic.blocs.Bloc;
import jeu.Physic;
import jeu.Rubico;
import jeu.levels.Builder;
import jeu.levels.Shape;
import jeu.mode.EndlessMode;

public class LevelPreview extends AbstractButton {
	
	public static final float WIDTH = Rubico.screenWidth * 0.9f, HEIGHT = Rubico.screenHeight * 0.20f, HALF_HEIGHT = HEIGHT / 2;
	final float x, y;
	public final int lvl;
	private final String text;
	private Array<PresentationBloc> blocs = new Array<PresentationBloc>();
	private Rectangle collider = new Rectangle();
	
	public LevelPreview(int lvl, float x, float y) {
		this.lvl = lvl;
		Shape shape = new Shape(lvl);
		this.x = x;
		this.y = y;
		collider.set(x, y, WIDTH, HEIGHT);
		horizontalBarre(x, y, WIDTH + Barre.HEIGHT);
		horizontalBarre(x, y + HEIGHT, WIDTH + Barre.HEIGHT);
		verticalBarre(x - Barre.HALF_HEIGHT, y, HEIGHT);
		verticalBarre(x + WIDTH, y, HEIGHT);
		
		for (int col = 0; col < Builder.COL; col++) {
			for (int row = 0; row < shape.shape.length; row++) {
				if (shape.shape[row][col] != 0) {
					PresentationBloc b = PresentationBloc.addBlock(getXColonne(col), row, shape.shape[row][col], this.y); 
					blocs.add(b);
					b.setPreview(this);
				}
			}
		}
		
		text = "Level " + lvl + " : ";
		
		color = 0.5f;
		mvtColor = 0.5f;
	}

	protected float getXColonne(int col) {
		return Builder.MIN_X + (col * Builder.MULTI_X);
//		return Rubico.halfWidth - (col * (Builder.MULTI_X / PresentationBloc.DIV));
	}
	
	static float plusBas, plusHaut;
	float centerPos, mvt, color, mvtColor;
	public boolean display(SpriteBatch batch) {
		if (isLocked())
			batch.setColor(color / 6, 0, 0, 1);
		else if (lvl == Rubico.profile.lvl)
			batch.setColor(0, 0, 0, 1);
		else
			batch.setColor(0, color/ 6, 0, 1);
		batch.draw(AssetMan.debris, x, y, WIDTH, HEIGHT);
		plusBas = 3000;
		plusHaut = -3000;
		for (PresentationBloc b : blocs) {
			b.display(batch);
			plusBas = Math.min(b.y, plusBas);
			plusHaut = Math.max(b.y + b.getHeight(), plusHaut);
		}
		
		centerPos = (plusBas + plusHaut) / 2;
		if (centerPos > y + HALF_HEIGHT) {
			mvt -= 0.005f;
		} else {
			mvt += 0.005f;
		}
		
		if (isLocked()) {
//			batch.draw(AssetMan.dust, e.x, e.y, HALF_WIDTH, HALF_HEIGHT, WIDTH, HEIGHT, 1f, 1f, e.angle);
			batch.setColor(1, 0,  0, 0.5f);
			batch.draw(AssetMan.dust, x, y + HALF_HEIGHT, WIDTH / 2, HALF_HEIGHT/10, WIDTH, HEIGHT/10, 1, 1, 20);
			batch.draw(AssetMan.dust, x, y + HALF_HEIGHT, WIDTH / 2, HALF_HEIGHT/10, WIDTH, HEIGHT/10, 1, 1, -20);
		}
		if (lvl < Rubico.profile.lvl) {
			batch.setColor(0, 1,  0, 0.5f);
			batch.draw(AssetMan.dust, x + WIDTH / 1.3f, y + HALF_HEIGHT / 2, WIDTH / 8, HALF_HEIGHT/15, WIDTH / 4, HEIGHT/15, 1, 1, 45);
			batch.draw(AssetMan.dust, x + WIDTH / 1.42f, y + HALF_HEIGHT / 3.3f, WIDTH / 16, HALF_HEIGHT / 18, WIDTH / 8, HEIGHT/18, 1, 1, -45);
		}
		
		for (Barre b : barres)
			b.draw(batch);
		batch.setColor(AssetMan.WHITE);
		
		text(batch);
		
		if (color > 0.5f)
			mvtColor -= 0.001f;
		else
			mvtColor += 0.001f;
		
		color += mvtColor;
		
		if (color > 1)
			color = 1;
		else if (color < 0)
			color = 0;
//		debug(batch);
		
		return Gdx.input.justTouched() && !isLocked() && collider.contains(Physic.getXClic(), Physic.getYClic());
	}

	private boolean isLocked() {
		return lvl > Rubico.profile.lvl;
	}

	private void text(SpriteBatch batch) {
		Rubico.menuFont.draw(batch, text, 
				Rubico.halfWidth - Rubico.menuFont.getBounds(text).width / 2, 
				y + HEIGHT + Rubico.menuFont.getBounds(text).height + 0.3f);
	}

	private void debug(SpriteBatch batch) {
		batch.setColor(Color.RED);
		batch.draw(AssetMan.debris, 0, centerPos, 100, 0.1f);
		batch.setColor(Color.BLUE);
		batch.draw(AssetMan.debris, 0, plusBas, 100, 0.1f);
		batch.setColor(Color.CYAN);
		batch.draw(AssetMan.debris, 0, plusHaut, 100, 0.1f);
	}

	public void remove() {
		for (PresentationBloc b : blocs) {
			PresentationBloc.clear(b);
		}
	}

	public float getYMovement() {
		return mvt * EndlessMode.delta;
//		return ((y + (HEIGHT / 2)) - (centerPos)) * EndlessMode.delta;
//		return ((centerPos) - (y + (HEIGHT / 2))) * EndlessMode.delta;
//		return up ? EndlessMode.delta : -EndlessMode.delta;
	}
	
	

}