package elements.world;

import jeu.Rubico;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Wall implements Collidable {
	
//	public static final Wall LEFT = new Wall(	new Vector2(-1, 1), 								BlocWorld.world, 	1,					Rubico.screenHeight);
	public static final Wall LEFT = new Wall(	new Vector2(2, 2), 								BlocWorld.world, 	1,					Rubico.screenHeight);
	public static final Wall RIGHT = new Wall(	new Vector2(Rubico.screenWidth + 1, 1), 			BlocWorld.world, 	1,					Rubico.screenHeight);
	public static final Wall UP = new Wall(	new Vector2(-1, Rubico.screenHeight + 1), 				BlocWorld.world, 	Rubico.screenWidth * 2,					1);
//	public static final Wall UP = new Wall(		new Vector2(0, Rubico.screenHeight/2), 	BlocWorld.world,	Rubico.screenWidth/2,	10);
	private Body body;
	
	public Wall(Vector2 position, World world, float halfWidth, float halfHeight) {
		body = BlocWorld.bodyFactoryBox(position, halfWidth, halfHeight, this, BodyType.StaticBody);
		body.setActive(true);
	}

	@Override
	public void collideWith(Collidable b) {
		System.out.println("Wall collide with : " + b);
	}

	@Override
	public Body getBody() {
		return body;
	}

	@Override
	public int getLvl() {
		return -1;
	}

}
