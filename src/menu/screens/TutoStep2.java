package menu.screens;

import jeu.Rubico;
import jeu.levels.Builder;
import jeu.mode.EndlessMode;
import jeu.mode.extensions.Buttons;
import jeu.mode.extensions.FlickerText;
import jeu.mode.extensions.ScreenShake;
import jeu.mode.extensions.TemporaryText;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import elements.generic.Ball;
import elements.generic.blocs.Column;
import elements.generic.paddles.StraightPaddle;
import elements.generic.paddles.PaddleShot;
import elements.particles.Particles;

public class TutoStep2 extends AbstractScreen {
	
	private final StraightPaddle paddle = new StraightPaddle();
	private static final FlickerText MOVE_TT = new FlickerText(" Clear a column \n to upgrade \n the ball");
	private boolean ok = false, pause = false;

	public TutoStep2(Game game) {
		super(game);
//		paddle.initialiser();
		FlickerText.clear();
		FlickerText.add(MOVE_TT);
		Builder.init();
		Gdx.input.setCatchBackKey(true);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);
			
		if (Ball.BALLS.size <= 0)					
			Ball.addBall(Rubico.screenWidth * Rubico.R.nextFloat(), Rubico.screenHeight * 0.8f, Rubico.R.nextFloat() - 0.5f, -0.5f);
		
		for (Column c : Column.COLUMNS) {
			c.nextPop++;
			if (c.lvl > 1) {
				ok = true;
				FlickerText.remove(MOVE_TT);
				FlickerText.congrats();
			}
		}
		if (pause)
			delta = 0;
		EndlessMode.delta = delta;
		EndlessMode.majDeltas();
		EndlessMode.now += EndlessMode.delta;
		EndlessMode.fps = Gdx.graphics.getFramesPerSecond();
		EndlessMode.alternate = !EndlessMode.alternate;
		Rubico.begin(Gdx.graphics.getDeltaTime());
		Rubico.batch.begin();
		Particles.background(Rubico.batch);
		Column.draw(Rubico.batch);
		Ball.draw(Rubico.batch);
		Particles.draw(Rubico.batch);
		paddle.draw(Rubico.batch);
		paddle.move();
		ScreenShake.act();
		if (ok && (Gdx.input.isKeyPressed(Keys.BACK) || Gdx.input.justTouched()))
			game.setScreen(new Menu(game));
		if (Gdx.input.isKeyPressed(Keys.BACK)) {
			pause = true;
		}
		if (pause)
			Buttons.backButton(Rubico.batch, game);
		Rubico.end();
		Rubico.batch.begin();
		FlickerText.draw(Rubico.batch);
		Rubico.batch.end();
	}

}
