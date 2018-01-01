package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Path2D;

import asteroids.Constants;
import asteroids.Controller;
import asteroids.Participant;
import asteroids.ParticipantCountdownTimer;
import asteroids.destroyers.AlienDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.BulletDestroyer;
import asteroids.destroyers.ShipDestroyer;

/**
 * Class that creates the bullets for the alien ships
 * 
 * @author Jon Bown
 */

public class ABullet extends Participant implements ShipDestroyer, AsteroidDestroyer {

	// Shape of the bullet
	private Shape outline;

	// Controller for the game
	private Controller controller;

	public ABullet(double x, double y, double rotation, Controller controller) {

		this.controller = controller;
		setPosition(x, y);
		setDirection(rotation);
		setVelocity(Constants.BULLET_SPEED, rotation);

		Path2D.Double poly = new Path2D.Double();
		poly.moveTo(0, 1);
		poly.lineTo(1, 0);
		poly.lineTo(0, -1);
		poly.lineTo(-1, 0);
		poly.closePath();

		outline = poly;

		new ParticipantCountdownTimer(this, "delete", Constants.BULLET_DURATION);

	}

	@Override
	protected Shape getOutline() {

		return outline;
	}

	// Tells the bullet to go away and that it collided with an asteroid.
	@Override
	public void collidedWith(Participant p) {
		if (p instanceof AsteroidDestroyer) {
			// Expire the bullet from the game
			Participant.expire(this);
			// Tell the controller the ship was destroyed
			controller.bulletDestroyed();
		}
		else if(p instanceof BulletDestroyer){
			//if the bullet contacts the shield it will also expire
			Participant.expire(this);
		}
	}

	// Moves the bullet
	@Override
	public void move() {
		super.move();
	}

	// Timer that deletes the bullet after the given time expires
	@Override
	public void countdownComplete(Object payload) {
		// Give a burst of acceleration, then schedule another
		// burst for 200 msecs from now.
		if (payload.equals("delete")) {
			Participant.expire(this);

			controller.bulletDestroyed();
		}
	}



}
