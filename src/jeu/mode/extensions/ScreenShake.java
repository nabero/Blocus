package jeu.mode.extensions;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;

import elements.particles.individual.PrecalculatedParticles;
import elements.world.BlocWorld;
import box2dLight.DirectionalLight;
import jeu.Profil;
import jeu.Rubico;
import jeu.Stats;
import jeu.mode.EndlessMode;

public class ScreenShake {

	// screenshake
	private static boolean shake = false, bloomSet = false;
	private static float chronoShake = 0, shakeTmpForce = 0, shakeTotalMvtX, shakeTotalMvtY;
	public static final Random R = new Random();
	private static float tmp;
	private static final float MAX_BLOOM = 10;
//	private static final DirectionalLight dirLight = new DirectionalLight(BlocWorld.rayHandler, 5, new Color(0.8f, 0.8f, 0.2f, 1), -75f);

	public static void init() {
		shake = false;
		chronoShake = 0;
	}

	private static final float SHAKE_MIN = 0.1f, SHAKE_MAX = 4;

	public static void screenShake(float valeur) {
		if (shake == false) {
			shakeTotalMvtX = 0;
			shakeTotalMvtY = 0;
		}
		shake = true;
		bloomSet = false;
		chronoShake += valeur / 100f;
		if (chronoShake > SHAKE_MAX)
			chronoShake = SHAKE_MAX;
	}

	public static void act() {
		if (shake) {
			if (EndlessMode.alternate)
//				BlocWorld.rayHandler.setAmbientLight(0.1f, 0.1f, 0.11f, Math.min(chronoShake/2, 1f));
				BlocWorld.rayHandler.setAmbientLight(PrecalculatedParticles.COLORS[Score.lvl -1].r / 5, PrecalculatedParticles.COLORS[Score.lvl -1].g / 5, PrecalculatedParticles.COLORS[Score.lvl -1].b / 5, Math.min(chronoShake/4, 1f));
//				BlocWorld.setDirLight(PrecalculatedParticles.COLORS[Score.lvl -1].r / 5, PrecalculatedParticles.COLORS[Score.lvl -1].g / 5, PrecalculatedParticles.COLORS[Score.lvl -1].b / 5, Math.min(chronoShake/2, 1f));
			if (chronoShake <= SHAKE_MIN) {
				shake = false;
				EndlessMode.getCam().position.y = Rubico.halfHeight;
				EndlessMode.getCam().position.x = Rubico.halfWidth;
//				dirLight.setColor(0.8f, 0.8f, 0.2f, 1);
			} else {
				setColor();
				chronoShake /= 1.3f + EndlessMode.delta2;
				if (Rubico.profile.screenshake == false)
					return;
				shakeTmpForce = (float) ((R.nextFloat() / 2) * (chronoShake * Stats.U));
				if (shakeTotalMvtX < 0) {
					EndlessMode.getCam().position.x += shakeTmpForce;
					shakeTotalMvtX += shakeTmpForce;
				} else {
					EndlessMode.getCam().position.x -= shakeTmpForce;
					shakeTotalMvtX -= shakeTmpForce;
				}
				shakeTmpForce = (float) ((R.nextFloat() / 2) * (chronoShake * Stats.U));
				if (shakeTotalMvtY < 0) {
					EndlessMode.getCam().position.y += shakeTmpForce;
					shakeTotalMvtY += shakeTmpForce;
				} else {
					EndlessMode.getCam().position.y -= shakeTmpForce;
					shakeTotalMvtY -= shakeTmpForce;
				}
			}
		}
	}

	/**
	 * 
	 */
	protected static void setColor() {
		float color = chronoShake/2;
		if (color > 1)
			color = 1;
		else if (color < 0.8f)
			color = 0.8f;
			
//		dirLight.setColor(chronoShake / 2, chronoShake / 2, 0.2f, 1);
	}

	public static void bloomEffect() {
		if (!shake) {
			if (!bloomSet) {
				EndlessMode.bloom.setBloomIntesity(Profil.intensiteBloom);
				bloomSet = true;
			}
		} else {
			tmp = Profil.intensiteBloom + (chronoShake * 5);
			EndlessMode.bloom.setBloomIntesity(tmp);
			if (tmp > MAX_BLOOM)
				tmp = MAX_BLOOM;
		}
	}

}
