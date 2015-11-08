package elements.world;

import com.badlogic.gdx.physics.box2d.Body;

public interface Collidable {

	void collideWith(Collidable b);
	Body getBody();
	int getLvl();

}
