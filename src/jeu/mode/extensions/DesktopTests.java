package jeu.mode.extensions;

import jeu.Rubico;
import jeu.Stats;
import jeu.levels.Builder;
import jeu.mode.EndlessMode;
import assets.SoundMan;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

import elements.generic.Ball;
import elements.generic.blocs.Bloc;

public class DesktopTests {
	
	public static void debug() {
		
		if (EndlessMode.oneToFour != 2)
			return;
		if (Gdx.input.isKeyPressed(Keys.PAGE_DOWN))	{
			EndlessMode.cam.translate(0, 0, 1);
			EndlessMode.cam();
			SoundMan.playBruitage(SoundMan.bonusTaken);
		}
		// POC
		if (Gdx.input.isKeyPressed(Keys.Z)) {
			Ball.addBall(Ball.BALLS.get(0).x, Ball.BALLS.get(0).y, -Ball.BALLS.get(0).dir.x, -Ball.BALLS.get(0).dir.y);
		}
		if (Gdx.input.isKeyPressed(Keys.E)) {
			Ball.CHRONO_EXPLOSIVE++;
		}
		if (Gdx.input.isKeyPressed(Keys.A)) {
			Rubico.tmpPos.x = Stats.U;
			Rubico.tmpPos.y = Rubico.halfHeight;
		}
		if (Gdx.input.isKeyPressed(Keys.R)) {
			Bloc.clear();
			Builder.nextSeed();
			Builder.init();
		}
		
		if (Gdx.input.isKeyPressed(Keys.F1))	EndlessMode.invicibility = true;
		if (Gdx.input.isKeyPressed(Keys.F2))	EndlessMode.invicibility = false;
		if (Gdx.input.isKeyPressed(Keys.F3))	EndlessMode.freeze = true;
		if (Gdx.input.isKeyPressed(Keys.F4))	EndlessMode.freeze = false;
		if (Gdx.input.isKeyPressed(Keys.F11))	EndlessMode.invoque = true;
		if (Gdx.input.isKeyPressed(Keys.F12))	EndlessMode.invoque = false;
		if (Gdx.input.isKeyPressed(Keys.F5)) 	Score.score++;
	}

}
