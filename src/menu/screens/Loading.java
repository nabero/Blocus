package menu.screens;

import jeu.Rubico;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Loading extends AbstractScreen {
	
	private int alterner = 0, cpt = 0;
	private String loading[] = {"Loading", "lOading", "loAding", "loaDing", "loadIng", "loadiNg", "loadinG", "loading"};

	public Loading(Game csg) {
		super(csg);
		Rubico.log("new loading");
		Rubico.assetMan.load();
	}

	@Override
	public void render(float delta) {
		if (!Rubico.assetMan.fini()) {
			afficherLoading();
		} else { // si il a fini le loading on change le menu
			Rubico.assetMan.loadPartie2();
			game.setScreen(new Menu(game));
		}
	}

	private void afficherLoading() {
		if (++alterner > 10) { // on bouge toutes les 10 frames pour le pas bouger trop vite
			alterner = 0;
			if (++cpt >= loading.length)
				cpt = 0;
		}
		Gdx.graphics.getGL20().glClear(GL20.GL_COLOR_BUFFER_BIT);
		Rubico.batch.begin();
		Rubico.menuFont.draw(Rubico.batch, loading[cpt], Rubico.halfWidth - Rubico.menuFont.getBounds("Loading -").width / 2, Rubico.halfHeight - Rubico.menuFontSmall.getBounds("Loading -").height / 2);
		Rubico.batch.end();
	}

	@Override	public void show() {	}
	@Override	public void hide() {	}
	@Override	public void pause() {	}
	@Override	public void resize(int w, int h) {		}
	@Override	public void resume() {	}
	@Override	public void dispose() {	}
}
