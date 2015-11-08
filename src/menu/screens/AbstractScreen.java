/*******************************************************************************
 * Copyright 2012-Present, MoribitoTech
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package menu.screens;

import jeu.Rubico;
import menu.Credits;
import menu.OnClick;
import menu.ui.Button;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.Array;

import elements.particles.Particles;

public abstract class AbstractScreen implements Screen {

	protected Game game;
	protected Array<Button> buttons = new Array<Button>();
	private Credits credits;
	public static final String PLAY = "Play!", SHIP = "Weapon", OPTION = "Options", HIGHSCORE = "Highscores", EXIT = "Exit", BACK = "BACK", WEAPON_VOL = "WEAPON VOL  ", MOINS = "-", PLUS = "+", BRUITAGE_VOL = "EFFECTS VOL  ", MUSIQUE_VOL = "MUSIC VOL  ", INTENSITY = "INTENSITY : ", OTHER_WEAP = "Next weapon", TUTO = "Tutorial" , ACHIEVEMENT = "Achievements", SUPPORT_US = "Support us !";
	public final static float PADDING = 11, BUTTON_WIDTH = (Rubico.screenWidth / PADDING) * 9, BUTTON_HEIGHT = Rubico.screenHeight / 50, BUTTON_HALF_WIDTH = BUTTON_WIDTH / 2, BUTTON_HALF_HEIGHT = BUTTON_HEIGHT / 2,
			SMALL_BUTTON_WIDTH = (Rubico.screenWidth / PADDING) * 4, SMALL_BUTTON_HEIGHT = Rubico.screenHeight / 18, MINI_BOUTON_WIDTH = SMALL_BUTTON_WIDTH/2, MINI_BOUTON_HEIGHT = SMALL_BUTTON_HEIGHT/2, yOffset = Rubico.screenHeight/10;
	public static OrthographicCamera cam = new OrthographicCamera(Rubico.screenWidth, Rubico.screenHeight);
	protected Button buttonBack;
	public boolean renderBackground = true;
	
	public AbstractScreen(final Game game) {
		super();
		this.game = game;
		credits = new Credits();
		
		Rubico.initBloom();
		cam.position.set(Rubico.screenWidth /2, Rubico.screenHeight/2, 0);
		
		buttonBack = new Button(BACK, Rubico.menuFontSmall, SMALL_BUTTON_WIDTH, SMALL_BUTTON_HEIGHT, -BUTTON_HEIGHT, BUTTON_HEIGHT*3.0f,
	    		new OnClick() {
					public void onClick() {
						changeMenu(new Menu(game));
						Rubico.profilManager.persist();
					}
				});
		Rubico.reset();
	}

	public void setRenderBackground(boolean renderBackground) {
		this.renderBackground = renderBackground;
	}
	
	protected void ajout(Button bouton) {
		buttons.add(bouton);
	}
	
	@Override
	public void render(float delta) {
		Rubico.begin(delta);
		
		if (Gdx.input.isKeyPressed(Keys.BACK))
			keyBackPressed();
		Rubico.batch.begin();
		if (renderBackground)
			Particles.background(Rubico.batch);
		Particles.draw(Rubico.batch);
		for (int i = 0; i < buttons.size; i++)
			if (buttons.get(i) != null) buttons.get(i).draw(Rubico.batch);
		if (credits != null)
			credits.render(Rubico.batch, delta);
		Rubico.end();
	}

	public void changeMenu (Screen s) {
		game.setScreen(s);
	}
	
	protected int detectiopnKonamiCode(int etapeCode) {
		switch (etapeCode) {
		case 0:		case 1:
			if (Gdx.input.justTouched() && Gdx.input.getY() < Rubico.halfHeight)
				etapeCode++;
			break;
		case 2:		case 3:
			if (Gdx.input.justTouched() && Gdx.input.getY() > Rubico.halfHeight)
				etapeCode++;
			break;
		case 4:		case 6:
			if (Gdx.input.justTouched() && Gdx.input.getX() < Rubico.halfWidth)
				etapeCode++;
			break;
		case 5:		case 7:
			if (Gdx.input.justTouched() && Gdx.input.getX() > Rubico.halfWidth)
				etapeCode++;
			break;
		}
		return etapeCode;
	}
	


	public void setBackButtonActive(boolean isBackButtonActive) {		Gdx.input.setCatchBackKey(true);	}
	public void keyBackPressed() {	}
	public void setGame(Game game) {		this.game = game;	}
	@Override	public void resize(int width, int height) {		cam.position.set(width /2, height/2, 0);	}
	@Override	public void show() {    }
	@Override	public void hide() {	}
	@Override	public void resume() {		Rubico.assetMan.reload();	}
	@Override	public void pause() {	}
	@Override	public void dispose() {	}

}
