package menu.ui.diamond;

import menu.ui.Barre;
import menu.ui.UiElements;
import jeu.Rubico;
import assets.AssetMan;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DiamondDisplayer extends UiElements {
	
	@Override
	public void draw(SpriteBatch batch) {
//		batch.draw(AssetMan.buttonBackground, x - 0.5f, y - Rubico.menuFont.getBounds(Rubico.profile.txtDiamonds).height * 2, width + 1f, (height + Rubico.menuFont.getBounds(Rubico.profile.txtDiamonds).height * 2) + 0.25f);
		batch.setColor(colors[(int) place]);
		place();
		batch.draw(AssetMan.diamond, x, y, width, height);
		Rubico.menuFont.draw(batch, Rubico.profile.txtDiamonds, (x - Rubico.menuFont.getBounds(Rubico.profile.txtDiamonds).width / 2) + width / 2, y - 0.4f);
//		for (Barre b : barres)
//			b.draw(batch);
	}

}
