package assets;

import jeu.Rubico;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public final class SoundMan {

	public static Sound explosion1, explosion2, bigExplosion, explosion3, explosion4, explosion5, explosion6, shotRocket, bonusTaken, sunWeapon, xp, column, powerup1, powerup2;
	private static Sound[] EXPLOSIONS = new Sound[7];
	public static Music outsideNorm;
	private static final float MIN = 0.1f;
	private static float originalVolume, changedVolume, gap;

	private SoundMan() {
	}

	public static void initExplosions() {
		EXPLOSIONS[0] = explosion1;
		EXPLOSIONS[1] = explosion2;
		EXPLOSIONS[2] = explosion3;
		EXPLOSIONS[3] = explosion4;
		EXPLOSIONS[4] = explosion5;
		EXPLOSIONS[5] = explosion6;
		EXPLOSIONS[6] = bigExplosion;
	}

	public static void playBruitage(Sound s) {
		if (Rubico.profile.effectsVolume > MIN)
			s.play(Rubico.profile.effectsVolume);
	}

	public synchronized static void playMusic() {
		if (Rubico.profile.musicVolume > MIN && outsideNorm != null) {
			try {
				outsideNorm.play();
				outsideNorm.setVolume(Rubico.profile.musicVolume);
				outsideNorm.setLooping(true);
			} catch (Exception e) {
			}
		}
	}

	public static void stopMusic() {
		if (outsideNorm != null)
			outsideNorm.stop();
	}

	public static void halfVolume() {
		try {
			if (outsideNorm != null && outsideNorm.isPlaying()) {
				originalVolume = outsideNorm.getVolume();
				outsideNorm.setVolume(originalVolume / 2);
				changedVolume = originalVolume / 3;
				gap = originalVolume - changedVolume;
			}
		} catch (Exception e) {

		}
	}

	public static void transitionUp(float f) {
		try {
			if (outsideNorm != null && outsideNorm.isPlaying()) {
				outsideNorm.setVolume(changedVolume + (gap * f));
			}
		} catch (Exception e) {

		}
	}

	public static void setOriginalVolume() {
		if (Rubico.profile.musicVolume > MIN && outsideNorm != null) {
			try {
				outsideNorm.setVolume(Rubico.profile.musicVolume);
				outsideNorm.setLooping(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void explosion(int previousHp) {
		if (previousHp < 0)
			previousHp = 0;
		else if (previousHp > EXPLOSIONS.length -1)
			previousHp = EXPLOSIONS.length - 1;
		playBruitage(EXPLOSIONS[previousHp]);
	}
	
	
}
