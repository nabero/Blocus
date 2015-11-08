package menu;

import jeu.Rubico;
import jeu.Stats;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Credits {
	
	private float posX = 0;
	private final static String ADV = "                                 IF YOU LIKE THIS GAME, PLEASE RATE IT, HELP ME MAKING IT BETTER AND TALK ABOUT IT ! ";
	private final static String END = "                             THANKS FOR PLAYING, SERIOUSLY YOU ARE AWESOME, I AM REALLY HAPPY THAT YOU LIKE IT !" +
	ADV;
	private final static String CREDIT = "                DEVELOPER : JULIEN BERTOZZI,     GAME DESIGN : JULIEN BERTOZZI, THE COMMUNITY     GRAPHICS : A LOT OF PARTICLES     MUSIC : OLIVIER LAHAYE.     DONE WITH THE LIBGDX FRAMEWORK.                ";
	private final static String PATCHNOTE = 
"                                                    "
+ "What's new : More levels !   Thanks for sharing, we are almost at 100 000 downloads !! :)   " + CREDIT + 	 		END;

	public void render(SpriteBatch batch, float delta) {
		posX -= delta * Stats.UUU;
		Rubico.menuFontSmall.draw(batch, PATCHNOTE, posX, Rubico.menuFontSmall.getBounds(PATCHNOTE).height + 0.5f);
	}
}
