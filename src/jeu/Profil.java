package jeu;

import assets.AssetMan;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

import elements.generic.paddles.AbstractPaddle;
import elements.generic.paddles.ShipPaddle;
import elements.generic.paddles.SpreadPaddle;
import elements.generic.paddles.StraightPaddle;

public class Profil implements Serializable {
	
	private static final String strXP = "XP", STR_VOLUME_MUSIQUE = "sjciuend", STR_VOLUME_BRUITAGES = "sjciuen", STR_RELATIVE = "sfdsfiuen", HIGHSCORE = "hsc", PADDLE = "dfzdzdze", STR_LEVEL = "csdfsddddddddd", STR_SHADER = "SHADER";
	private static final String STR_INTENSITE_BLOOM = "intensitebloom", STR_SCREENSHAKE = "screens", LIGHTS = "fdfsfdsfdsfvcvxc", ADS = "DSDFSFDSFSDFDS", STR_SELECTED_PADDLE = "fdsselectedpaddle", STR_LVL_SPREAD_PADDLE = "lvldpaddlespread", PADDLE_SHIP = "lvlaspdfdddd";
	private static final float STEP_VOL = .1f;
	public int highscore, previousHighscore, lvlPaddle, lastTutorial, lvl, lvlSpreadPaddle, lvlShipPaddle;
	public String highscoreString = "", lvlString = "", txtDiamonds;
	public float weaponVolume = 0.5f, musicVolume = 0.5f, effectsVolume = 0.5f; 
	public static final float intensiteBloom = 3.4f;
	public static final int LVL_TO_UNLOCK_SHIP = 5;
	public boolean screenshake, relativeControl, newHighscore, firstTime;
	private boolean lights, adsFree;
	public boolean promoAppOfTheDay = false;
	private int diamonds;
	public int selectedPaddle, shader;
	
	/**
	 * Valeurs par defaut si pas de profil
	 */
	public Profil() {
										// 11/19/2014									// 11/23/2014
//		if (System.currentTimeMillis() > 1419292800000l && System.currentTimeMillis() < 1416697200000l) {
//		if (System.currentTimeMillis() > 1l && System.currentTimeMillis() < 1419638399000l) {
//			diamonds = 150;
//			promoAppOfTheDay = true;
//			adsFree = true;
//		} else {
			diamonds = 4;
			adsFree = false;
//		}
		
		updateDiamonds(0);
		highscore = 0;
		effectsVolume = 1;
		weaponVolume = effectsVolume / 3;
		musicVolume = 1;
		lvlPaddle = 1;
		lvlSpreadPaddle = 1;
		lastTutorial = 0;
		screenshake = true;
		relativeControl = true;
		lights = false;
		lvl = 1;
		lvlShipPaddle = 1;
		selectedPaddle = StraightPaddle.ID;
		firstTime = true;
		highscoreString();
		majLvlString();
		shader = AssetMan.ORIGINAL_SHADER;
	}

	@Override
	public void write(Json json) {
		json.writeValue(PADDLE_SHIP, lvlShipPaddle);
		json.writeValue(PADDLE, lvlPaddle);
		json.writeValue(STR_LVL_SPREAD_PADDLE, lvlSpreadPaddle);
		json.writeValue(HIGHSCORE, highscore);
		json.writeValue(strXP, diamonds);
		json.writeValue(STR_VOLUME_BRUITAGES, effectsVolume);
		json.writeValue(STR_VOLUME_MUSIQUE, musicVolume);
		json.writeValue(STR_SCREENSHAKE, screenshake);
		json.writeValue(STR_RELATIVE, relativeControl);
		json.writeValue(STR_INTENSITE_BLOOM, intensiteBloom);
		json.writeValue(STR_LEVEL, lvl);
		json.writeValue(STR_SELECTED_PADDLE, selectedPaddle);
		json.writeValue(LIGHTS, lights);
		json.writeValue(ADS, adsFree);
		json.writeValue(STR_SHADER, shader);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		lvlPaddle = json.readValue(PADDLE, Integer.class, jsonData);
		diamonds = json.readValue(strXP, Integer.class, jsonData);
		updateDiamonds(0);
		highscore = json.readValue(HIGHSCORE, Integer.class, jsonData);
		effectsVolume = json.readValue(STR_VOLUME_BRUITAGES, Float.class, jsonData);
		musicVolume = json.readValue(STR_VOLUME_MUSIQUE, Float.class, jsonData);
		weaponVolume = effectsVolume / 3;
		screenshake = json.readValue(STR_SCREENSHAKE, Boolean.class, jsonData);
		relativeControl = json.readValue(STR_RELATIVE, Boolean.class, jsonData);
		
		if (json.readValue(STR_LEVEL, Integer.class, jsonData) != null) 	lvl = json.readValue(STR_LEVEL, Integer.class, jsonData);
		else 																lvl = 1;
		if (json.readValue(STR_SELECTED_PADDLE, Integer.class, jsonData) != null) 		selectedPaddle = json.readValue(STR_SELECTED_PADDLE, Integer.class, jsonData);
		else 																			selectedPaddle = StraightPaddle.ID;
		if (json.readValue(STR_LVL_SPREAD_PADDLE, Integer.class, jsonData) != null) 	lvlSpreadPaddle = json.readValue(STR_LVL_SPREAD_PADDLE, Integer.class, jsonData);
		else 																			lvlSpreadPaddle = 1;
		if (json.readValue(PADDLE_SHIP, Integer.class, jsonData) != null) 				lvlShipPaddle = json.readValue(PADDLE_SHIP, Integer.class, jsonData);
		else 																			lvlShipPaddle = 1;
		if (json.readValue(STR_SHADER, Integer.class, jsonData) != null) 				shader = json.readValue(STR_SHADER, Integer.class, jsonData);
		else 																			shader = AssetMan.ORIGINAL_SHADER;
		if (json.readValue(LIGHTS, Boolean.class, jsonData) != null) 		lights = json.readValue(LIGHTS, Boolean.class, jsonData);
		else 																lights = true;
		if (json.readValue(ADS, Boolean.class, jsonData) != null) 			adsFree = json.readValue(ADS, Boolean.class, jsonData);
		else 																adsFree = false;
		
		highscoreString();
		majLvlString();
		checkAchievementLvl();
		firstTime = false;
		promoAppOfTheDay = false;
	}

	public void checkAchievementLvl() {
		if (lvl >= 100)
			Rubico.talkToTheWorld.unlockAchievementGPGS(Strings.ACH_LVL100);
	}

	public void diminuerVolumeBruitage() {
		if (effectsVolume > 0) {
			effectsVolume -= STEP_VOL;
			weaponVolume = effectsVolume / 3;
		}
		Rubico.profilManager.persist();
	}
	
	public boolean isAdsFree() {
		return adsFree;
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
	
	public void updateHighscore(int score) {
		newHighscore = true;
		previousHighscore = highscore;
		highscore = score;
		highscoreString();
	}

	private void highscoreString() {			highscoreString = "Highscore : " + highscore;				}
	public String getBloomString() {			return Strings.DF.format(intensiteBloom);					}
	private void majLvlString() {				lvlString = "Completed levels : " + lvl;					}
	public void reset() {						newHighscore = false;										}

	public void upPaddle() {
		if (Rubico.profile.getCoutUpArme() <= Rubico.profile.diamonds) {
			updateDiamonds(-getCoutUpArme());
			if (selectedPaddle == StraightPaddle.ID)
				lvlPaddle++;
			else if (selectedPaddle == SpreadPaddle.ID)
				lvlSpreadPaddle++;
			else
				lvlShipPaddle++;
			Rubico.profilManager.persist();
		}
	}

	private void updateDiamonds(int i) {
		diamonds += i;
		txtDiamonds = String.valueOf(diamonds); 
	}

	public void addDiamond(int score) {
		updateDiamonds(score);
		Rubico.profilManager.persist();
	}

	public void upLvl() {
		lvl++;
		majLvlString();
	}

	public void addXpFromIAP(int xpBuy) {
		addDiamond(xpBuy);
	}

	public int getCoutUpArme() {
		int lvl = getSelectedPaddleLvl();
		return (lvl * lvl) * 4;
	}

	public boolean canUpdatePaddle() {
		return diamonds >= getCoutUpArme();
	}

	public boolean lights() {
		return lights;
	}

	public void switchLights() {
		lights = !lights;
	}

	public void enableAdsFree() {
		adsFree = true;
		Rubico.profilManager.persist();
	}

	public AbstractPaddle getSelectedPaddle() {
		if (selectedPaddle == SpreadPaddle.ID)
			return new SpreadPaddle();
		if (selectedPaddle == ShipPaddle.ID)
			return new ShipPaddle();
		return new StraightPaddle();
	}

	public void setSelectedPaddle(int id) {
		selectedPaddle = id;
		Rubico.profilManager.persist();
	}

	public int getSelectedPaddleLvl() {
		if (selectedPaddle == StraightPaddle.ID)
			return lvlPaddle;
		if (selectedPaddle == ShipPaddle.ID)
			return lvlShipPaddle;
		return lvlSpreadPaddle;
	}

	public boolean isShipUnlocked() {
		return lvlPaddle >= LVL_TO_UNLOCK_SHIP && lvlSpreadPaddle >= LVL_TO_UNLOCK_SHIP;
	}

}
