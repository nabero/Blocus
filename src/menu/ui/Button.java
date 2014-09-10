package menu.ui;

import jeu.Rubico;
import jeu.Physic;
import jeu.Stats;
import jeu.mode.EndlessMode;
import menu.OnClick;
import assets.AssetMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Button extends AbstractButton {

	private static final float PADDING_BARRE = Stats.U;
	private BitmapFont font;
	private String texte;
	private boolean clicked = false;
	public Sprite sprite;
	public OnClick click;
	private float clickTime = 0, height, width;
	private final float originalWidth, originalHeight;
	

	public Button(String s, BitmapFont font, int srcWidth, int srcHeight, float srcX, float f, OnClick click) {
		sprite = new Sprite();
		sprite.setBounds(srcX, f, srcWidth, srcHeight);
		this.originalHeight = srcHeight;
		this.originalWidth = srcWidth;
		init(s, font, srcX, f);
		this.click = click;
	}

	private void init(String s, BitmapFont font, float x, float y) {
		this.font = font;
		texte = s;
		updateBorders(font, x, y, originalWidth, originalHeight);
	}

	private void updateBorders(BitmapFont font, float x, float y, float originalWidth, float originalHeight) {
		height = font.getBounds(texte).height;
		width = font.getBounds(texte).width;
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
		verticalBarre(x + width + Barre.HEIGHT, y, height);
		Barre.nbr = 0;
		this.x = x - Barre.HALF_HEIGHT;
		this.y = y;
	}

	public Button(String s, BitmapFont font, int srcWidth, int srcHeight, float srcX, int srcY) {
		sprite = new Sprite();
		sprite.setBounds(srcX, srcY, srcWidth, srcHeight);
		this.originalHeight = srcHeight;
		this.originalWidth = srcWidth;
		init(s, font, srcX, srcY);
	}

	public Button(String s, BitmapFont font, int srcWidth, int srcHeight, float srcX, int srcY, OnClick onClick) {
		sprite = new Sprite();
		sprite.setBounds(srcX, srcY, srcWidth, srcHeight);
		click = onClick;
		this.originalHeight = srcHeight;
		this.originalWidth = srcWidth;
		init(s, font, srcX, srcY);
	}

	public void setClick(OnClick click) {
		this.click = click;
	}

	public void draw(SpriteBatch batch) {
		drawBackground(batch);
		
		font.draw(batch, texte, getX(),	getY());
		
		for (Barre b : barres)
			b.draw(batch);
		
		if (Gdx.input.justTouched()) {
			if (Physic.isPointInRect(Gdx.input.getX(), Rubico.screenHeight - Gdx.input.getY(), x, y, width + PADDING_BARRE, height)) {
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
	private float getY() {
		return sprite.getY() + font.getBounds(texte).height + sprite.getHeight()/2 - font.getBounds(texte).height/2;
	}

	private float getX() {
		return (sprite.getX() + (sprite.getWidth()/2)) - font.getBounds(texte).width/2;
	}

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
	
	public void setTexte(String texte) {
		// center
		this.texte = texte;
		width = font.getBounds(texte).width;
		width += PADDING_BARRE * 2;
		setBarres(getX() - PADDING_BARRE, y);
	}
	
	
	public BitmapFont font() {
		return font;
	}

	public void camMoveY(float y) {
		sprite.setY(sprite.getY()+y);
	}
	
	public void camMoveX(float x) {
		sprite.setY(sprite.getY()+x);
	}

	public void camZoom(float f) {
		sprite.scale(f);
		font.scale(f);
	}

	public static void testClick(Button b, float xOffset) {
		if (b != null && Physic.isPointInRect(Gdx.input.getX() + xOffset, Rubico.screenHeight - Gdx.input.getY(), 0, b.sprite.getY() - Stats.U, Rubico.screenWidth, b.sprite.getHeight() + Stats.UU)) {
			if (b.click != null)
				b.click.onClick();
		}
	}
}