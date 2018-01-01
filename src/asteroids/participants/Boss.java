package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;

import asteroids.Constants;
import asteroids.Controller;
import asteroids.Participant;
import asteroids.ParticipantCountdownTimer;
import asteroids.destroyers.AlienDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.BossDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.destroyers.TokenDestroyer;

/**
 * Creates a Boss shaped like a 'U' that starts out big and gets smaller as it
 * is destroyed
 * 
 * @author Jon Bown
 */
public class Boss extends Participant implements ShipDestroyer, TokenDestroyer {

	private Shape outline;
	private Controller controller;

	private boolean changeDirection = false;
	private int size;

	public Boss(Controller controller, int size) {

		this.size = size;
		this.controller = controller;

		Path2D.Double poly = new Path2D.Double();
		poly.moveTo(-7D, 30.0D);
		poly.lineTo(-7D, 4.0D);
		poly.lineTo(-25.0D, -15.0D);
		poly.lineTo(-10D, -20.0D);
		poly.lineTo(0.0D, -4.0D);
		poly.lineTo(10.0D, -20.0D);
		poly.lineTo(25.0D, -15.0D);
		poly.lineTo(7.0D, 4.0D);
		poly.lineTo(7.0D, 30.0D);
		poly.closePath();
		outline = poly;
		double scale = size;
		poly.transform(AffineTransform.getScaleInstance(scale, scale));
		new ParticipantCountdownTimer(this, "shoot", 1000);
		new ParticipantCountdownTimer(this, "move", 1500);
		new ParticipantCountdownTimer(this, "shoot2", 1800);
		controller.addLocation(this, controller.getListNumber());
		setColor(java.awt.Color.BLUE);
	}

	@Override
	protected Shape getOutline() {

		return outline;
	}

	@Override
	public void countdownComplete(Object payload) {
		// controller.getLevel() > 7 &&
			if (payload.equals("shoot2")) {
				controller.addParticipant(new ABullet(getX(), getY(), getShootingDirectionToShip(), this.controller));
				controller.getSounds().play("fire");
				new ParticipantCountdownTimer(this, "shoot2", 1000);
			}
			if (payload.equals("shoot")) {
				controller.addParticipant(new ABullet(getX(), getY(), getShootingDirectionToShip(), this.controller));
				controller.getSounds().play("fire");
				new ParticipantCountdownTimer(this, "shoot", 1500);
			}
			 if (payload.equals("move")) {
				new ParticipantCountdownTimer(this, "move", 1000);
				changeDirection = true;
			}
		
	}

	public void fireBullet() {
		if (this != null) {
			
		}
	}

	public double getShootingDirectionToShip() {
		if (controller.getShip() != null) {
			Ship ship = this.controller.getShip();
			double deltaX = ship.getX() - getX();
			double deltaY = ship.getY() - getY();
			double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
			double direction = Math.acos(deltaX / distance);
			return deltaY > 0.0 ? direction : -direction;
		}

		else {
			return 0.0;
		}
	}

	public void remove() {
		Participant.expire(this);
		controller.getSounds().stop("boss");
		controller.getSounds().play("enhancedMusic");
		controller.alienDestroyed();
		controller.removeLocation(this);
		this.controller.getSounds().play("destroyS");
		this.controller.getSounds().stop("alien");

	}
/**
 * Uses the move method with some extra changes in direction
 */
	public void move() {
		super.move();

		if (this.changeDirection) {
			changeDirection = false;

			if (Math.cos(getDirection()) > 0.0D) {
				setDirection(Constants.RANDOM.nextInt(3) - 1);
			} else {
				setDirection(Math.PI + Constants.RANDOM.nextInt(3) - 1.0D);
			}

			new ParticipantCountdownTimer(this, "change", 1000);
		}
	}
/**
 * If it collides with a BossDestroyer it expires
 */
	@Override
	public void collidedWith(Participant p) {
		if ((p instanceof BossDestroyer)) {

			if (this.size > 1) {
				Participant.expire(this);
				this.controller.placeBoss(this.size - 1);
				this.size--;
			} else {
				remove();
				this.controller.addParticipant(new ShipDebris(getX(), getY(), 10 * (3)));
				this.controller.addParticipant(new ShipDebris(getX(), getY(), 10 * (3)));
				this.controller.addParticipant(new ShipDebris(getX(), getY(), 10 * (4)));
				this.controller.addParticipant(new ShipDebris(getX(), getY(), 10 * (5)));
				this.controller.addParticipant(new ShipDebris(getX(), getY(), 5 * (2)));
				this.controller.addParticipant(new ShipDebris(getX(), getY(), 5 * (1)));
			}
		}

	}

}
