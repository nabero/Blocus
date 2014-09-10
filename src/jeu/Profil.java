package jeu;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

import elements.generic.Paddle;

public class Profil implements Serializable {
	
	private static final String strXP = "XP", STR_VOLUME_MUSIQUE = "sjciuend", STR_VOLUME_BRUITAGES = "sjciuen", STR_RELATIVE = "sfdsfiuen", HIGHSCORE = "hsc", PADDLE = "dfzdzdze", STR_LEVEL = "csdfsddddddddd";
	private static final String STR_INTENSITE_BLOOM = "intensitebloom", STR_SCREENSHAKE = "screens";
	private static final float STEP_VOL = .1f;
	public int highscore, previousHighscore, xpDispo, lvlPaddle, lastTutorial, lvl;
	public String highscoreString = "", lvlString = "";
	public float weaponVolume = 0.5f, musicVolume = 0.5f, effectsVolume = 0.5f, intensiteBloom = 2.1f;
	public boolean screenshake, relativeControl, newHighscore;
	
	/**
	 * Valeurs par defaut si pas de profil
	 */
	public Profil() {
		xpDispo = (int) (200 * Rubico.mulSCORE);
		highscore = 0;
		effectsVolume = 1;
		weaponVolume = effectsVolume / 3;
		musicVolume = 1;
		lvlPaddle = 1;
		lastTutorial = 0;
		intensiteBloom = 2.4f;
		screenshake = true;
		relativeControl = true;
		lvl = 0;
		highscoreString();
		majLvlString();
	}

	@Override
	public void write(Json json) {
		json.writeValue(PADDLE, lvlPaddle);
		json.writeValue(HIGHSCORE, highscore);
		json.writeValue(strXP, xpDispo);
		json.writeValue(STR_VOLUME_BRUITAGES, effectsVolume);
		json.writeValue(STR_VOLUME_MUSIQUE, musicVolume);
		json.writeValue(STR_SCREENSHAKE, screenshake);
		json.writeValue(STR_RELATIVE, relativeControl);
		json.writeValue(STR_INTENSITE_BLOOM, intensiteBloom);
		json.writeValue(STR_LEVEL, lvl);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		lvlPaddle = json.readValue(PADDLE, Integer.class, jsonData);
		xpDispo = json.readValue(strXP, Integer.class, jsonData);
		highscore = json.readValue(HIGHSCORE, Integer.class, jsonData);
		effectsVolume = json.readValue(STR_VOLUME_BRUITAGES, Float.class, jsonData);
		musicVolume = json.readValue(STR_VOLUME_MUSIQUE, Float.class, jsonData);
		weaponVolume = effectsVolume / 3;
		intensiteBloom = json.readValue(STR_INTENSITE_BLOOM, Float.class, jsonData);
		screenshake = json.readValue(STR_SCREENSHAKE, Boolean.class, jsonData);
		relativeControl = json.readValue(STR_RELATIVE, Boolean.class, jsonData);
		
		if (json.readValue(STR_LEVEL, Integer.class, jsonData) != null) 	lvl = json.readValue(STR_LEVEL, Integer.class, jsonData);
		else 																lvl = 1;
		
		highscoreString();
		majLvlString();
	}

	public void diminuerVolumeBruitage() {
		if (effectsVolume > 0) {
			effectsVolume -= STEP_VOL;
			weaponVolume = effectsVolume / 3;
		}
		Rubico.profilManager.persist();
	}
	
	public void augmenterVolumeBruitage() {
		if (effectsVolume < 1) {
			effectsVolume += STEP_VOL;
			weaponVolume = effectsVolume / 3;
		}
		Rubico.profilManager.persist();
	}

	public void augmenterVolumeMusique() {
		if (musicVolume < 1)
			musicVolume += STEP_VOL;
		Rubico.profilManager.persist();
	}
	
	public void diminuerVolumeMusique() {
		if (musicVolume > 0)
			musicVolume -= STEP_VOL;
		Rubico.profilManager.persist();
	}
	
	public void downBloom() {
		intensiteBloom -= .2f;
		if (intensiteBloom < .4f)
			intensiteBloom = .4f;
	}

	public void updateHighscore(int score) {
		newHighscore = true;
		previousHighscore = highscore;
		highscore = score;
		highscoreString();
	}

	public int getCoutUpArme(int nv) {			return (int) (((nv+nv) * nv * nv * 50) * Rubico.mulSCORE);	}
	private void highscoreString() {			highscoreString = "Highscore : " + highscore;				}
	public String getBloomString() {			return Strings.DF.format(intensiteBloom);					}
	private void majLvlString() {				lvlString = "Completed levels : " + lvl;					}
	public void upBloom() {						intensiteBloom += .2f;										}
	public void reset() {						newHighscore = false;										}

	public void upPaddle() {
		xpDispo -= getCoutUpArme(Paddle.lvl);
		lvlPaddle++;
		Rubico.profilManager.persist();
	}

	public void addXp(int score) {
		xpDispo += score;
		Rubico.profilManager.persist();
	}

	public void upLvl() {
		lvl++;
		majLvlString();
	}

}
