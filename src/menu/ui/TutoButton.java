package menu.ui;

import jeu.Physic;
import jeu.Rubico;
import jeu.Stats;
import jeu.mode.EndlessMode;
import menu.OnClick;
import assets.AssetMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class TutoButton extends AbstractButton {

	private static final float PADDING_BARRE = 0;
	private boolean clicked = false;
	public Sprite sprite;
	public OnClick click;
	private float clickTime = 0, height, width;
	private final float originalWidth, originalHeight;
	

	public TutoButton(float width, float height, float x, float y, OnClick click, Texture texture) {
		sprite = new Sprite(texture);
		sprite.setBounds(x, y, width, height);
		this.originalHeight = height;
		this.originalWidth = width;
		this.height = height;
		this.width = width;
		init(x, y);
		this.click = click;
	}

	private void init(float x, float y) {
		updateBorders(x, y, originalWidth, originalHeight);
	}

	private void updateBorders(float x, float y, float originalWidth, float originalHeight) {
		y = (y + (originalHeight/2)) - (height/2);
		x = (x + (originalWidth/2)) - (width/2);
		x -= PADDING_BARRE;
		y -= PADDING_BARRE;
		width += PADDING_BARRE * 2;
		height += PADDING_BARRE * 2;
		setBarres(x, y);
	}

	private void setBarres(float x, float y) {
		barres.clear();
		// top 
		horizontalBarre(x, y + height - Barre.HEIGHT, width + Barre.HEIGHT);
		// bottom
		horizontalBarre(x, y, width + Barre.HEIGHT);
		// left
		verticalBarre(x - Barre.HALF_HEIGHT, y, height);
		verticalBarre(x + width, y, height);
		Barre.nbr = 0;
		this.x = x - Barre.HALF_HEIGHT;
		this.y = y;
	}

	public void setClick(OnClick click) {
		this.click = click;
	}

	public void draw(SpriteBatch batch) {
		drawBackground(batch);
		
		sprite.draw(batch);
		
		for (Barre b : barres)
			b.draw(batch);
		
		if (Gdx.input.justTouched()) {
			if (Physic.isPointInRect(Physic.getXClic(), Physic.getYClic(), x, y, width + PADDING_BARRE, height)) {
				impulse(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
				if (click != null)
					click.onClick();
			}
		}
		act();
	}

	private void impulse(int x, int y) {
		tmpTouched.x = x;
		tmpTouched.y = y;
		for (Barre b : barres)
			b.impulse(tmpTouched);
	}

	private static final Vector2 tmpTouched = new Vector2();

	private void drawBackground(SpriteBatch batch) {
		batch.setColor(AssetMan.BLACK);
		batch.draw(AssetMan.debris, x, y, width + PADDING_BARRE / 2, height);
		batch.setColor(AssetMan.WHITE);
	}

	public void act() {
		if (clicked)
			clickTime += EndlessMode.delta;
		if (clickTime > .2f)
			click.onClick();
	}
	
	public void camMoveY(float y) {
		sprite.setY(sprite.getY()+y);
	}
	
	public void camMoveX(float x) {
		sprite.setY(sprite.getY()+x);
	}

	public void camZoom(float f) {
		sprite.scale(f);
	}

	public static void testClick(TutoButton b, float xOffset) {
		if (b != null && Physic.isPointInRect(Gdx.input.getX() + xOffset, Rubico.screenHeight - Gdx.input.getY(), 0, b.sprite.getY() - Stats.U, Rubico.screenWidth, b.sprite.getHeight() + Stats.UU)) {
			if (b.click != null)
				b.click.onClick();
		}
	}
}