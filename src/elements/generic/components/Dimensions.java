package elements.generic.components;

import jeu.Stats;

public enum Dimensions {

	FRAG_WEAPON(1,2);
	
	public float width, height, halfWidth, halfHeight, quartWidth, threeQuarterWidth, doubleWidth, quartHeight;

	private Dimensions(float width) {
		this.width = width * Stats.U;
		this.height = width * Stats.U;
		otherStats();
	}
	
	private Dimensions(float width, float height) {
		this.width = width * Stats.U;
		this.height = height * Stats.U;
		otherStats();
	}

	private Dimensions(Dimensions original, float ratio) {
		width = original.width * ratio;
		height = original.height * ratio;
		otherStats();
	}

	/**
	 * @param width
	 * @param height
	 */
	protected void otherStats() {
		halfWidth = width/2;
		halfHeight = height/2;
		quartWidth = width / 4;
		threeQuarterWidth = quartWidth * 3;
		quartHeight = height / 4;
		doubleWidth = width * 2;
	}
	
}
