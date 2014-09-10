package menu.ui;

import com.badlogic.gdx.utils.Array;

public abstract class AbstractButton {
	
	protected float y, x;
	protected final Array<Barre> barres = new Array<Barre>();
	
	protected void verticalBarre(float x, float y, float heightToCover) {
		final int nbrBarre = (int) (((heightToCover * 0.8f) / Barre.HEIGHT));
		final float distanceCouverte = nbrBarre * Barre.HEIGHT;
		final float ecartTotal = heightToCover - distanceCouverte;
		final float ecart = ecartTotal / (nbrBarre-1);
		float tmpX = 0;
		for (int i = 0; i < nbrBarre; i++) {
			barres.add(Barre.POOL.obtain().init(x, y + tmpX));
			tmpX += (Barre.HEIGHT + ecart);
		}
	}
	
	protected void horizontalBarre(float x, float y, float widthToCover) {
		final int nbrBarre = (int) (((widthToCover * 0.7f) / Barre.HEIGHT));
		final float distanceCouverte = nbrBarre * Barre.HEIGHT;
		final float ecartTotal = widthToCover - distanceCouverte;
		final float ecart = ecartTotal / (nbrBarre-1);
		float tmpX = -Barre.HALF_HEIGHT;
		for (int i = 0; i < nbrBarre; i++) {
			barres.add(Barre.POOL.obtain().init(x + tmpX, y));
			tmpX += (Barre.HEIGHT + ecart);
		}
	}

}
