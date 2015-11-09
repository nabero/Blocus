package menu.ui.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import assets.AssetMan;
import jeu.Physic;
import jeu.Rubico;
import jeu.Stats;
import jeu.mode.EndlessMode;
import menu.OnClick;
import menu.ui.Barre;

public class SimpleButton {
	
	public static final float PADDING_BARRE = Stats.U,
			PADDLE_WIDTH = Rubico.screenWidth * 0.3f,
			PADDLE_HEIGHT = Rubico.screenHeight * 0.18f,
			PADDLE_HORIZONTAL_OFFSET = ((Rubico.screenWidth / 2) - PADDLE_WIDTH) / 2,
			PADDING = 11,
			WIDTH = (Rubico.screenWidth / PADDING) * 9,
			HEIGHT = Rubico.screenHeight / 50, 
			HALF_WIDTH = WIDTH / 2,
			HALF_HEIGHT = HEIGHT / 2, 
			SMALL_WIDTH = (Rubico.screenWidth / PADDING) * 4,
			SMALL_HEIGHT = Rubico.screenHeight / 18, 
			MINI_WIDTH = SMALL_WIDTH / 2,
			MINI_HEIGHT = SMALL_HEIGHT / 2, yOffset = Rubico.screenHeight / 10;
	private static final Vector2 tmpTouched = new Vector2();
	
	protected float y, x;
	private float clickTime = 0, height, width;
	protected final Array<Barre> barres = new Array<Barre>();
	private long clickedTime = 0; 
	private boolean clicked = false;
	public OnClick click;
	
	public SimpleButton(float x, float y, OnClick click, float width, float height) {
		this.x = x;
		this.y = y;
		this.click = click;
		this.width = width + PADDING_BARRE * 2;
		this.height = height + PADDING_BARRE * 2;
		setBarres();
	}
	
	public SimpleButton(float x, float y, OnClick click) {
		this(x, y, click, WIDTH, HEIGHT);
	}

	protected void verticalBarre(float x, float y, float heightToCover) {
		initSurroundingSquares(x, y, heightToCover, .8f, 0, 0, 1);
	}
	
	protected void horizontalBarre(float x, float y, float widthToCover) {
		initSurroundingSquares(x, y, widthToCover, .7f, - Barre.HALF_HEIGHT, 1, 0);
	}
	
	protected void setPosition(float x, float y) {
		this.x = x - PADDING_BARRE;
		this.y = y - PADDING_BARRE;
	}
	
	public void setOnClick(OnClick onClick) {
		click = onClick;
	}
	
	private void initSurroundingSquares(float x, float y, float toCover, float mul, float xOffset, float xDisplacement, float yDisplacement) {
		final int nbrSquares = (int) (((toCover * 0.8f) / Barre.HEIGHT));
		final float length = nbrSquares * Barre.HEIGHT;
		final float totalGap = toCover - length;
		final float gap = totalGap / (nbrSquares-1);
		for (int i = 0; i < nbrSquares; i++) {
			barres.add(Barre.POOL.obtain().init(
					(x + xOffset) + (xDisplacement * i * (Barre.HEIGHT + gap)),
					y + (yDisplacement * i * (Barre.HEIGHT + gap))));
		}
	}
	
	private void setBarres() {
		barres.clear();
		// top 
		horizontalBarre(x, y + height - Barre.HEIGHT, width + Barre.HEIGHT);
		// bottom
		horizontalBarre(x, y, width + Barre.HEIGHT);
		// left
		verticalBarre(x - Barre.HALF_HEIGHT, y, height);
		verticalBarre(x + width + Barre.HEIGHT, y, height);
		Barre.nbr = 0;
		this.x -= Barre.HALF_HEIGHT;
	}

	public void draw(SpriteBatch batch) {
		drawBackground(batch);
		if (Gdx.input.justTouched() && Physic.isPointInRect(Physic.getXClic(), Physic.getYClic(), x, y, width + PADDING_BARRE, height) && canBeClicked()) {
			clickedTime = System.currentTimeMillis() + 100;
			impulse(Physic.getXClic(), Physic.getYClic());
			if (click != null)
				click.onClick();
		}
		
		insideDraw(batch);
		
		for (Barre b : barres)
			b.draw(batch);

		act();
	}

	protected void insideDraw(SpriteBatch batch) {};

	public void act() {
		if (clicked)
			clickTime += EndlessMode.delta;
		if (clickTime > .2f)
			click.onClick();
	}
	
	private void drawBackground(SpriteBatch batch) {
		batch.setColor(0, 0 , 0, 0.6f);
		batch.draw(AssetMan.debris, x, y, width + PADDING_BARRE / 2, height);
		batch.setColor(AssetMan.WHITE);
	}

	private void impulse(float x, float y) {
		tmpTouched.x = this.x + width / 2;
		tmpTouched.y = this.y + height / 2;
		for (Barre b : barres)
			b.impulse(tmpTouched);
	}
	
	private boolean canBeClicked() {
		return clickedTime < System.currentTimeMillis();
	}
	
	protected void updateWidth(float width, float newX) {
		this.width = width;
		setBarres();
	}

}
