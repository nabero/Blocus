package menu.screens;

import jeu.Rubico;
import jeu.mode.EndlessMode;
import menu.ui.Arrow;
import menu.ui.LevelPreview;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;

import elements.generic.blocs.Bloc;
import elements.particles.Particles;
import elements.particles.individual.explosions.ExplodingBloc;

public class LevelChooser extends AbstractScreen {

	public final static Array<Bloc> BLOCS = new Array<Bloc>();
	private static final float Y_PADDING = Rubico.screenHeight * 0.037f,
			X_CHOOSER = (Rubico.screenWidth - LevelPreview.WIDTH) / 2,
			Y_MIN = Rubico.screenHeight * 0.1f,
			Y_TOP = ( (Rubico.screenHeight - LevelPreview.HEIGHT) - Y_PADDING * 1.5f) - Y_MIN,
			Y_MID = ( (Rubico.screenHeight - LevelPreview.HEIGHT * 2) - Y_PADDING * 2.5f) - Y_MIN,
			Y_DOWN = ( (Rubico.screenHeight - LevelPreview.HEIGHT * 3) - Y_PADDING * 3.5f) - Y_MIN,
			Y_ARROW = ( (Rubico.screenHeight - Arrow.HEIGHT * 1.2f)) - Y_MIN;
	private final LevelPreview[] previews = new LevelPreview[3];
	private boolean surprise = false;
	private final Arrow leftArrow = new Arrow(Arrow.WIDTH * 0.2f, Y_ARROW, true), rightArrow = new Arrow(Rubico.screenWidth - Arrow.WIDTH * 1.2f, Y_ARROW, false);

	public LevelChooser(Game game, boolean surprise) {
		super(game);
		this.surprise = surprise;
		Particles.initBackground();
		setUpScreenElements();
		Particles.clear();
		Rubico.profile.checkAchievementLvl();
	}
	public void setUpScreenElements() {
		Gdx.input.setCatchBackKey(true);
		int middle = Rubico.profile.lvl;
		switch (Rubico.profile.lvl % 3) {
		case 0:			middle -= 1;			break;
		case 1: 		middle += 1;			break;
		}
		setPreviews(middle);
		ajout(buttonBack);
	}
	private void setPreviews(int middle) {
		if (middle < 2)
			middle = 2;
		clear();
		previews[0] = new LevelPreview(middle - 1, X_CHOOSER, Y_TOP);
		previews[1] = new LevelPreview(middle, X_CHOOSER, Y_MID);
		previews[2] = new LevelPreview(middle + 1, X_CHOOSER, Y_DOWN);
	}

	@Override
	public void render(float delta) {
		cam.update();
		Rubico.batch.setProjectionMatrix(cam.combined);
		super.render(delta);
		Rubico.batch.begin();
		EndlessMode.delta = delta;
		EndlessMode.delta4 = delta * 4;
		EndlessMode.delta15 = delta * 15;
		Particles.draw(Rubico.batch);
		ExplodingBloc.draw(Rubico.batch, Bloc.EXPLOSIONS);
		for (LevelPreview p : previews)
			if (p.display(Rubico.batch))
				changeMenu(new EndlessMode(game, Rubico.batch, p.lvl, false, surprise));
		if (previews[1].lvl > 2 && leftArrow.draw(Rubico.batch))
			setPreviews(previews[1].lvl - 3);
		if (previews[2].lvl < Rubico.profile.lvl && rightArrow.draw(Rubico.batch))
			setPreviews(previews[1].lvl + 3);
		Rubico.batch.end();
		
	}

	@Override
	public void changeMenu(Screen s) {
		clear();
		super.changeMenu(s);
	}
	private void clear() {
		for (LevelPreview p : previews)
			if (p != null)
				p.remove();
	}
	
}
