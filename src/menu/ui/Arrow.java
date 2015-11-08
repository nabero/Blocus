package menu.ui;

import jeu.Physic;
import jeu.Rubico;
import assets.AssetMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import elements.generic.blocs.Bloc;
import elements.particles.individual.PrecalculatedParticles;

public class Arrow extends AbstractButton {
	
	public static final float HEIGHT = Rubico.screenHeight * 0.04f, WIDTH = Rubico.screenWidth * 0.2f, HALF_WIDHT = WIDTH / 2, HALF_HEIGHT = HEIGHT / 2, TIER_WIDTH = WIDTH / 3, TIER_HEIGHT = HEIGHT / 3,
			BLOC_WIDTH = TIER_WIDTH, BLOC_HEIGHT = TIER_HEIGHT, BLOC_H_W = BLOC_WIDTH / 2, BLOC_H_H = BLOC_HEIGHT / 2;
	private boolean left;
	public float balance = 0, place = 0;
	private final Rectangle collider = new Rectangle();
	
	public Arrow(float x, float y, boolean left) {
		this.x = x;
		this.y = y;
		this.left = left;
		collider.set(x, y, WIDTH, HEIGHT);
		horizontalBarre(x, y, WIDTH + Barre.HEIGHT);
		horizontalBarre(x, y + HEIGHT, WIDTH + Barre.HEIGHT);
		verticalBarre(x - Barre.HALF_HEIGHT, y, HEIGHT);
		verticalBarre(x + WIDTH, y, HEIGHT);
	}

	public boolean draw(SpriteBatch batch) {
		place += balance;
		if (place > PrecalculatedParticles.HALF_LENGTH)		balance -= 0.007f;
		else												balance += 0.007f;
		if (place < Bloc.MIN)								place = Bloc.MIN;
		else if (place >= Bloc.MAX)							place = Bloc.MAX;
		
		batch.setColor(AssetMan.BLACK);
		batch.draw(AssetMan.debris, x, y, WIDTH, HEIGHT);
		
		batch.setColor(PrecalculatedParticles.getColors(4)[(int) place]);
		if (left) {
			batch.draw(AssetMan.partBloc, x + HALF_WIDHT + BLOC_WIDTH / 2.3f, (y + HALF_HEIGHT) - (BLOC_HEIGHT / 3), TIER_WIDTH, BLOC_HEIGHT );
			batch.draw(AssetMan.partBloc, (x + HALF_WIDHT + BLOC_WIDTH / 2.3f) - BLOC_WIDTH, (y + HALF_HEIGHT) - (BLOC_HEIGHT / 3), TIER_WIDTH, BLOC_HEIGHT );
			batch.draw(AssetMan.partBloc, 
					(x + HALF_WIDHT + BLOC_WIDTH / 2.3f) - BLOC_WIDTH * 1.8f,					(y + HALF_HEIGHT), 
					BLOC_H_W, 		BLOC_H_H, 
					BLOC_WIDTH, 	BLOC_HEIGHT, 
					1f, 1f, 25);
			batch.draw(AssetMan.partBloc, 
					(x + HALF_WIDHT + BLOC_WIDTH / 2.3f) - BLOC_WIDTH * 1.8f,					(y + HALF_HEIGHT) - BLOC_HEIGHT * 0.80f, 
					BLOC_H_W, 		BLOC_H_H, 
					BLOC_WIDTH, 	BLOC_HEIGHT, 
					1f, 1f, -25);
		} else {
			batch.draw(AssetMan.partBloc, x + WIDTH * 0.07f, (y + HALF_HEIGHT) - (BLOC_HEIGHT / 3), TIER_WIDTH, BLOC_HEIGHT );
			batch.draw(AssetMan.partBloc, (x + HALF_WIDHT + BLOC_WIDTH / 1.4f) - BLOC_WIDTH, (y + HALF_HEIGHT) - (BLOC_HEIGHT / 3), TIER_WIDTH, BLOC_HEIGHT );
			batch.draw(AssetMan.partBloc, 
					(x + HALF_WIDHT + BLOC_WIDTH / 2.3f) - BLOC_WIDTH * 1.8f + WIDTH * 0.60f,					(y + HALF_HEIGHT), 
					BLOC_H_W, 		BLOC_H_H, 
					BLOC_WIDTH, 	BLOC_HEIGHT, 
					1f, 1f, -25);
			batch.draw(AssetMan.partBloc, 
					(x + HALF_WIDHT + BLOC_WIDTH / 2.3f) - BLOC_WIDTH * 1.8f + WIDTH * 0.60f,					(y + HALF_HEIGHT) - BLOC_HEIGHT * 0.80f, 
					BLOC_H_W, 		BLOC_H_H, 
					BLOC_WIDTH, 	BLOC_HEIGHT, 
					1f, 1f, 25);
		}
		
		for (Barre b : barres)
			b.draw(batch);
		
		return Gdx.input.justTouched() && collider.contains(Physic.getXClic(), Physic.getYClic());
	}

}
