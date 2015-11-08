package menu.ui.diamond;

import jeu.Rubico;
import assets.AssetMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import elements.particles.individual.PrecalculatedParticles;

public class DiamondCostDisplayer {
	
	private static final float WIDTH = 1, HEIGHT = WIDTH;
	private float x, y;
	private int cost;
	private String str = "";
	
	public void display (SpriteBatch batch) {
		batch.setColor(PrecalculatedParticles.blocBlue[0]);
		batch.draw(AssetMan.diamond, x, y, WIDTH, HEIGHT);
		Rubico.menuFont.draw(batch, str, x + 1.4f, y + Rubico.menuFont.getBounds("x").height);
		
	}

	public void setX(float i) {
		x = i;
	}

	public void setY(float i) {
		y = i;
	}

	public void setCost(int coutUpArme) {
		cost = coutUpArme;
		str = "x " + cost;
	}
	

}
