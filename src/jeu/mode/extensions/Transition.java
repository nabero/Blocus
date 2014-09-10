
package jeu.mode.extensions;

import jeu.mode.EndlessMode;
import assets.SoundMan;

public class Transition {
	
	private float facteurTransition = 20;
	private final static float stepTransition = .1f;
	private boolean movementTransition = false;
	
	private void transitionVersMouvement() {
		// On ne décremente le chrono de ralentissement que quand on est au facteur de ralentissement max
		if (facteurTransition > 1) {
			facteurTransition -= stepTransition;
			SoundMan.transitionUp(((20 - facteurTransition)/20));
		} else {
			SoundMan.setOriginalVolume();
			movementTransition = false;
		}
		EndlessMode.delta /= facteurTransition;
		EndlessMode.majDeltas();
	}

	public void activate(float length) {
		facteurTransition = length;
		movementTransition = true;
	}

	public void act() {
		if (movementTransition)
			transitionVersMouvement();
	}

	public void reset() {
		facteurTransition = 20;
		movementTransition = false;
	}

}
